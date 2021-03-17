package com.zengxin.rpcfx.client;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 获取curator-->zk客户端
 */
public class CuratorClient {
    private static final String RPCNAMESPACE = "rpcfx";

    private CuratorClient() {
    }

    public static CuratorFramework getClient(String zkUrl, String namespace) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zkUrl)
                .namespace(namespace).retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }

    public static CuratorFramework getClient(String zkUrl) {
        return getClient(zkUrl, RPCNAMESPACE);
    }

}
