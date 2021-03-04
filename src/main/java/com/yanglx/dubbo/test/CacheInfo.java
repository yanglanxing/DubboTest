package com.yanglx.dubbo.test;

import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;

import java.io.Serializable;
import java.util.Date;

public class CacheInfo implements Serializable {

    private DubboMethodEntity dubboMethodEntity;

    private String name;

    private String id;

    private Date date;

    public static CacheInfo of(String id,String name,DubboMethodEntity dubboMethodEntity){
        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setId(id);
        cacheInfo.setName(name);
        cacheInfo.setDubboMethodEntity(dubboMethodEntity);
        cacheInfo.setDate(new Date());
        return cacheInfo;
    }

    public DubboMethodEntity getDubboMethodEntity() {
        return dubboMethodEntity;
    }

    public void setDubboMethodEntity(DubboMethodEntity dubboMethodEntity) {
        this.dubboMethodEntity = dubboMethodEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return name;
    }
}
