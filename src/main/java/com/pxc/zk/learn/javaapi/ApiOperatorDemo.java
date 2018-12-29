package com.pxc.zk.learn.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper基础api操作示例
 * 节点增删改查
 *
 * @author pengxincheng@ipaynow.cn
 * @Date: 2018/12/24
 * @Time 18:47
 */
public class ApiOperatorDemo implements Watcher {

    private static final String CONNECTION_STRING = "192.168.11.117:2181,192.168.11.119:2181,192.168.11.121:2181,192.168.11.122:2181";
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final Logger logger = LoggerFactory.getLogger(ApiOperatorDemo.class);

    public static void main(String[] args) {
        try {
            zooKeeper = new ZooKeeper(CONNECTION_STRING, 50000, new ApiOperatorDemo());
            countDownLatch.await();
            //创建节点(持久化节点)
            String path1 = "/test4";          //节点操作示例，
            String path2 = "/test1/test1-1";  //子节点操作示例

            if (null == zooKeeper.exists(path2, true)) {
                String result = zooKeeper.create(path2, "789".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                System.out.println(result);
                TimeUnit.SECONDS.sleep(2);
            }
            //修改节点数据
            if (null != zooKeeper.exists(path2, true)) {
                zooKeeper.setData(path2, "pxc".getBytes(), -1);
                TimeUnit.SECONDS.sleep(2);
            }
            if (null != zooKeeper.exists(path2, true)) {
                //删除节点
                zooKeeper.delete(path2, -1);
                TimeUnit.SECONDS.sleep(2);
            }
            TimeUnit.SECONDS.sleep(10);

            //创建临时节点,服务断开后自动删除
            logger.info("开始创建临时节点。。。");
            if (null == zooKeeper.exists("/temp", true)) {
                String result = zooKeeper.create("/temp", "qwe".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                TimeUnit.SECONDS.sleep(2);
                System.out.println("节点创建完成" + result);
            }
            //创建临时有序节点，节点路径后有数字/temp10000000015
            logger.info("开始创建临时有序节点。。。");
            if (null == zooKeeper.exists("/temp2", true)) {
                String result2 = zooKeeper.create("/temp1", "youxu".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
                System.out.println("建临时有序节点创建完成" + result2);
            }

            //临时节点不能创建子节点
            logger.info("开始测试临时节点是否可以创建子节点");
            if (null != zooKeeper.exists("/temp", true)) {
                String result3 = zooKeeper.create("/temp/temp1-1", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                System.out.println(result3);
            }

            //获取父节点下所有的子节点
            logger.info("获取父节点下所有的子节点");
            if (null != zooKeeper.exists("/test1", true)) {
                zooKeeper.create("/test1/test1-2","测试".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
                zooKeeper.create("/test1/test1-3","测试".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
                zooKeeper.create("/test1/test1-4","测试".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
                TimeUnit.SECONDS.sleep(5);

                List<String> childrens = zooKeeper.getChildren("/test1",true);
                childrens.forEach(s -> System.out.println(s));
            }
            TimeUnit.SECONDS.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            //如果当前的连接状态是连接成功的，那么通过计数器去控制
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                switch (watchedEvent.getType()) {
                    case None:
                        countDownLatch.countDown();
                        System.out.println(watchedEvent.getState() + "-->" + watchedEvent.getType());
                        break;
                    case NodeCreated:
                        //watcher的通知是一次性，一旦触发一次通知后，该watcher就失效。监听执行完需要再次注册监听,此处在getData再次注册
                        logger.info("创建节点path={},data={}", watchedEvent.getPath(), new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
                        break;

                    case NodeDataChanged:
                        logger.info("节点path={}值发生改变，改变后data={}", watchedEvent.getPath(), new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
                        break;

                    case NodeChildrenChanged:
                        logger.info("子节点path={}值发生改变，改变后data={}", watchedEvent.getPath(), new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));

                    case NodeDeleted:
                        logger.info("节点被删除path={}", watchedEvent.getPath());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
