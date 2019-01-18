package com.pxc.zk.learn.zkClient;

import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.TimeUnit;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2019/1/11
 * @Time 15:23
 */
public class ZkClientDemo {

    private static final String CONNECTION_STRING = "192.168.11.117:2181,192.168.11.119:2181,192.168.11.121:2181,192.168.11.122:2181";

    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient(CONNECTION_STRING, 60000);
        System.out.println("连接成功！");

        //创建临时节点
        zkClient.createEphemeral("/zklearn");
        log("/zklearn");

        zkClient.createEphemeral("/zklearn1", 123);
        log("/zklearn1");

        //临时有序
        zkClient.createEphemeralSequential("/zklearn2", 456);
        log("/zklearn2");

        //创建持久节点
        zkClient.createPersistent("/test1/test1-1", "qwer");
        log("/test1/test1-1");

        //创建持久节点(递归创建)
        zkClient.createPersistent("/test1/test1-1/test2-1",true);
        log("/test1/test1-1/test2-1");
        String result =zkClient.createPersistentSequential("/test1/test1-2","asd");
        System.out.println(result);
        log("/test1/test1-2");

        //获取节点值
        String s = zkClient.readData("/test1/test1-20000000012");
        System.out.println(s);

        TimeUnit.MINUTES.sleep(2);

    }

    private static void log(String path) {
        System.out.println("path=" + path + "节点创建成功！");
    }
}
