package com.pxc.zk.learn.javaapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2018/12/28
 * @Time 16:42
 */
public class AclDemo {

    private static final String CONNECTION_STRING = "192.168.11.117:2181,192.168.11.119:2181,192.168.11.121:2181,192.168.11.122:2181";
    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final Logger logger = LoggerFactory.getLogger(AclDemo.class);

    public static void main(String[] args) throws Exception {
        zooKeeper = new ZooKeeper(CONNECTION_STRING, 50000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
                System.out.println(watchedEvent.getState());
            }
        });

        countDownLatch.await();
      /*  ACL acl1 = new ACL(ZooDefs.Perms.CREATE,new Id("ip","192.168.11.161")); //允许该Ip创建子节点
        //ACL acl2 = new ACL(ZooDefs.Perms.READ,new Id("digest","root:root"));
        List<ACL> acls = new ArrayList<>();
        acls.add(acl1);*/

        zooKeeper.addAuthInfo("digest", "root:123456".getBytes());

        String result = zooKeeper.create("/auth-test1", "asd".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        System.out.println(result);
        TimeUnit.SECONDS.sleep(10);

    }

    @Test
    public void testAcl1() throws Exception {
        zooKeeper = new ZooKeeper(CONNECTION_STRING, 50000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
                System.out.println(watchedEvent.getState());
            }
        });
        countDownLatch.await();

        zooKeeper.addAuthInfo("digest", "root:123456".getBytes());
        String result = zooKeeper.create("/auth-test1/test1", "asd".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(result);
        TimeUnit.SECONDS.sleep(10);
    }

}
