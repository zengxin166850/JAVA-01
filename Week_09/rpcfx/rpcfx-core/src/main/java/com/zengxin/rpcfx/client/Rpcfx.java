package com.zengxin.rpcfx.client;

import com.alibaba.fastjson.parser.ParserConfig;
import com.zengxin.rpcfx.api.Filter;
import com.zengxin.rpcfx.api.LoadBalancer;
import com.zengxin.rpcfx.api.Router;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public final class Rpcfx {
    private static final String ROOT = "/";
    private static final StubFactory stubFactory = new CglibProxyStub();
    /**
     * 服务名 --> url映射
     */
    private static final Map<String, List<String>> serviceUrlMapping = new HashMap<>();
    /**
     * 服务类 --> 代理类映射
     */
    private static final ConcurrentMap<Class<?>, Object> serviceProxyMapping = new ConcurrentHashMap<>();


    private Rpcfx() {
    }

    static {
        ParserConfig.getGlobalInstance().addAccept("com.zengxin");
    }

    public static <T> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {

        // 加filte之一,例如判断url、serviceclass不合法？
        // 如果已经有代理类，直接返回
        if (serviceProxyMapping.containsKey(serviceClass)) {
            return (T) serviceProxyMapping.get(serviceClass);
        }
        List<String> invokers = new ArrayList<>();
        try {
            // 1. 简单：从zk拿到服务提供的列表
            CuratorFramework client = CuratorClient.getClient(zkUrl);
            invokers = client.getChildren().forPath(ROOT + serviceClass.getName());
            serviceUrlMapping.put(serviceClass.getSimpleName(), invokers);
            // 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）
            startServiceListener(client, serviceClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //router
        List<String> urls = router.route(invokers);

        String url = loadBalance.select(urls); // router, loadbalance
        String[] ipPort = url.split("_");
        url = "http://" + ipPort[0] + ":" + ipPort[1];
        T proxy = stubFactory.createStub(serviceClass, url);
        serviceProxyMapping.putIfAbsent(serviceClass, proxy);
        return proxy;

    }

    /**
     * 注册监听器
     *
     * @param client       zk客户端
     * @param serviceClass 服务
     * @param <T>          T
     */
    private static <T> void startServiceListener(CuratorFramework client, Class<T> serviceClass) {
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
    }

    /**
     * 更新注册表中的服务
     */
    private static <T> void updateServices(CuratorFramework listenClient, Class<T> serviceClass) throws Exception {
        List<String> newServices = listenClient.getChildren().forPath("/");
        List<String> oldServices = serviceUrlMapping.get(serviceClass.getSimpleName());
        oldServices.clear();
        oldServices.addAll(newServices);
    }


}