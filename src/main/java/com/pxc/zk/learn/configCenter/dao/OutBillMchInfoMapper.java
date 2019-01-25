package com.pxc.zk.learn.configCenter.dao;


import com.pxc.zk.learn.configCenter.entity.OutBillMchInfo;
import com.pxc.zk.learn.configCenter.entity.OutBillMchInfoExample;
import com.pxc.zk.learn.configCenter.entity.OutBillMchInfoKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutBillMchInfoMapper {
    int countByExample(OutBillMchInfoExample example);

    int deleteByExample(OutBillMchInfoExample example);

    int deleteByPrimaryKey(OutBillMchInfoKey key);

    int insert(OutBillMchInfo record);

    int insertSelective(OutBillMchInfo record);

    List<OutBillMchInfo> selectByExample(OutBillMchInfoExample example);

    OutBillMchInfo selectByPrimaryKey(OutBillMchInfoKey key);

    int updateByExampleSelective(@Param("record") OutBillMchInfo record, @Param("example") OutBillMchInfoExample example);

    int updateByExample(@Param("record") OutBillMchInfo record, @Param("example") OutBillMchInfoExample example);

    int updateByPrimaryKeySelective(OutBillMchInfo record);

    int updateByPrimaryKey(OutBillMchInfo record);
}