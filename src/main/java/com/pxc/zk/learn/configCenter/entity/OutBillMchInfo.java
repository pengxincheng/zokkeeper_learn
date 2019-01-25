package com.pxc.zk.learn.configCenter.entity;

import java.util.Date;

public class OutBillMchInfo extends OutBillMchInfoKey {
    private String mchId;

    private String mchName;

    private Date createTime;

    private Date updateTime;

    private Integer templeteId;

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTempleteId() {
        return templeteId;
    }

    public void setTempleteId(Integer templeteId) {
        this.templeteId = templeteId;
    }
}