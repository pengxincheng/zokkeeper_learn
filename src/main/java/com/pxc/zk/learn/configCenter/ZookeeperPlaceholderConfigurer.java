package com.pxc.zk.learn.configCenter;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2019/1/25
 * @Time 11:25
 * <p>
 * 加载props里面的数据库配置,这个类等价于以前在xml文件里面的配置：
 * * <context:property-placeholder location="classpath:config/jdbc_conf.properties"/>
 */
public class ZookeeperPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private Logger logger = LoggerFactory.getLogger(ZookeeperPlaceholderConfigurer.class);

    private ZookeeperConfigCenter zookeeperConfigCenter;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        logger.debug("加载到的配置为{}", JSON.toJSONString(zookeeperConfigCenter.getProps()));

        super.processProperties(beanFactoryToProcess, zookeeperConfigCenter.getProps());
    }

    public void setZookeeperConfigCenter(ZookeeperConfigCenter zookeeperConfigCenter) {
        this.zookeeperConfigCenter = zookeeperConfigCenter;
    }
}
