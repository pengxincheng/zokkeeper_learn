package com.pxc.zk.learn.lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2019/1/18
 * @Time 15:18
 * zookeeper分布式锁的应用示例
 */
public class LockDemo {

    private static final String CONNECTION_STRING = "192.168.11.117:2181,192.168.11.119:2181,192.168.11.121:2181,192.168.11.122:2181";
    @Test
    public void client1() throws Exception {
        for (Integer i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    testLock(Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        TimeUnit.MINUTES.sleep(5);
    }

    private void testLock(String clientId) throws Exception {
        //创建zookeeper的客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client = CuratorFrameworkFactory.newClient(CONNECTION_STRING, retryPolicy);

        client.start();

        //创建分布式锁, 锁空间的根节点路径为/curator/lock
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");

        mutex.acquire();

        //获得了锁, 进行业务流程
        System.out.println(clientId + "获取到锁，开始执行逻辑。。。");
        TimeUnit.SECONDS.sleep(10);

        //完成业务流程, 释放锁
        mutex.release();
        //关闭客户端

        client.close();
    }


}
