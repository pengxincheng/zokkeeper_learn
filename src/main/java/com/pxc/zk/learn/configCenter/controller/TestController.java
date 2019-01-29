package com.pxc.zk.learn.configCenter.controller;

import com.pxc.zk.learn.configCenter.dao.OutBillMchInfoMapper;
import com.pxc.zk.learn.configCenter.entity.OutBillMchInfo;
import com.pxc.zk.learn.configCenter.entity.OutBillMchInfoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengxincheng@ipaynow.cn
 * @Date: 2019/1/25
 * @Time 15:04
 */
@Controller
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private OutBillMchInfoMapper outBillMchInfoMapper;

    @RequestMapping("/test")
    @ResponseBody
    public List<OutBillMchInfo> test(){

        logger.info("test..............");
        OutBillMchInfoExample example = new OutBillMchInfoExample();
        return outBillMchInfoMapper.selectByExample(example);

    }

}
