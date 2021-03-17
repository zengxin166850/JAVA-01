package com.zengxin.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.zengxin.rpcfx.api.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Rpcfx {
    private static final String ROOT = "/";
    /**
     * 服务名-->url映射
     */
    private static final Map<String, List<String>> serviceUrlMapping = new HashMap<>();

    static {
        ParserConfig.getGlobalInstance().addAccept("com.zengxin");
    }

    public static <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {

        // 加filte之一,例如判断url、serviceclass不合法？

        // curator Provider list from zk
        List<String> invokers = new ArrayList<>();
        // 1. 简单：从zk拿到服务提供的列表
        try {
            CuratorFramework client = CuratorClient.getClient(zkUrl);
            invokers = client.getChildren().forPath(ROOT + serviceClass.getName());
            serviceUrlMapping.put(serviceClass.getSimpleName(), invokers);
            CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache(ROOT + serviceClass.getName(), client, ((listenClient, event) -> {
                switch (event.getType()) {
                    case CHILD_ADDED:
                    case CHILD_REMOVED:
                    case CHILD_UPDATED:
                        updateServices(listenClient, serviceClass);
                        break;
                    default:
                        break;
                }
            })).build();
            CuratorCache cache = CuratorCache.builder(client, ROOT).build();
            cache.listenable().addListener(listener);
            cache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）

        List<String> urls = router.route(invokers);

        String url = loadBalance.select(urls); // router, loadbalance
        String[] ipPort = url.split("_");
        url = "http://" + ipPort[0] + ":" + ipPort[1];
        return (T) create(serviceClass, url, filter);

    }

    private static <T> void updateServices(CuratorFramework listenClient, Class<T> serviceClass) throws Exception {
        List<String> newServices = listenClient.getChildren().forPath("/");
        List<String> oldServices = serviceUrlMapping.get(serviceClass.getSimpleName());
        oldServices.clear();
        oldServices.addAll(newServices);
    }

    public static <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {
        //切换springAop？不需要invoke，在aop内执行http远程调用
        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url, filters));

    }

    public static class RpcfxInvocationHandler implements InvocationHandler {

        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;
        private final Filter[] filters;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url, Filter... filters) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.filters = filters;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            //获取到最新的url个数
            List<String> urls = serviceUrlMapping.get(this.serviceClass.getSimpleName());
            System.err.println("获取到" + serviceClass.getSimpleName() + "的服务个数：" + urls.size());
            // 加filter地方之二
            // mock == true, new Student("hubao");

            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

            if (null != filters) {
                for (Filter filter : filters) {
                    if (!filter.filter(request)) {
                        return null;
                    }
                }
            }

            RpcfxResponse response = post(request, url);

            // 加filter地方之三
            // Student.setTeacher("cuijing");

            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException

            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: " + reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();
            System.out.println("resp json: " + respJson);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }
    }
}