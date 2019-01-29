package com.pxc.zk.learn.configCenter;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Properties;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2019/1/25
 * @Time 10:56
 */
public class ZookeeperConfigCenter {

    private Logger logger = LoggerFactory.getLogger(ZookeeperPlaceholderConfigurer.class);

    //curator客户端
    private CuratorFramework zkClient;

    //curator事件监听
    private TreeCache treeCache;

    //zookeeper的ip和端口
    private String zkServers;

    //zookeeper上的/Jdbc路径
    private String zkPath;

    //超时设置
    private int sessionTimeout;

    //读取zookeeper上的数据库配置文件放到这里
    private Properties props;

    public ZookeeperConfigCenter(String zkServers, String zkPath, int sessionTimeout) {
        this.zkServers = zkServers;
        this.zkPath = zkPath;
        this.sessionTimeout = sessionTimeout;
        this.props = new Properties();

        //初始化curator客户端
        initZkClient();
        //从zookeeper的Jdbc节点下获取数据库配置存入props
        getConfigData();
        //对zookeeper上的数据库配置文件所在节点进行监听，如果有改变就动态刷新props
        addZkListener();
    }

    //初始化curator客户端
    private void initZkClient() {
        zkClient = CuratorFrameworkFactory.builder().connectString(zkServers).sessionTimeoutMs(sessionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        zkClient.start();
    }

    //从zookeeper的Jdbc节点下获取数据库配置存入props
    private void getConfigData() {
        try {
            List<String> list = zkClient.getChildren().forPath(zkPath);
            for (String key : list) {
                String value = new String(zkClient.getData().forPath(zkPath + "/" + key));
                if (value != null && value.length() > 0) {
                    props.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //对zookeeper上的数据库配置文件所在节点进行监听，如果有改变就动态刷新props
    private void addZkListener() {
        TreeCacheListener listener = (client, event) -> {
            if (event.getType() == TreeCacheEvent.Type.NODE_UPDATED) {
                ZookeeperConfigCenter.this.getConfigData();
                WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
                DruidDataSource dataSource = (DruidDataSource) ctx.getBean("dataSource");

                dataSource.setUrl(props.getProperty("url"));
                dataSource.setUsername(props.getProperty("username"));
                dataSource.setPassword(props.getProperty("password "));
            }
        };

        treeCache = new TreeCache(zkClient, zkPath);
        try {
            treeCache.start();
            treeCache.getListenable().addListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Properties getProps() {
        return props;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
}
