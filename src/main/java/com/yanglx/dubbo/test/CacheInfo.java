package com.yanglx.dubbo.test;

import com.alibaba.fastjson.JSON;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CacheInfo implements Serializable {

    /** Interface name */
    private String interfaceName;
    /** Method name */
    private String methodName;
    /** Version */
    private String version;
    /** Group */
    private String group;
    /** Method type */
    private String methodTypeJson;
    /** Param obj */
    private String paramObjJson;
    /** Address */
    private String address;

    private String name;

    private String id;

    private Date date;

    public static CacheInfo of(String id,String name,DubboMethodEntity dubboMethodEntity){
        CacheInfo cacheInfo = new CacheInfo();
        cacheInfo.setId(id);
        cacheInfo.setName(name);
        cacheInfo.setInterfaceName(dubboMethodEntity.getInterfaceName());
        cacheInfo.setMethodName(dubboMethodEntity.getMethodName());
        cacheInfo.setVersion(dubboMethodEntity.getVersion());
        cacheInfo.setGroup(dubboMethodEntity.getGroup());
        cacheInfo.setMethodTypeJson(JSON.toJSONString(dubboMethodEntity.getMethodType()));
        cacheInfo.setParamObjJson(JSON.toJSONString(dubboMethodEntity.getParamObj()));
        cacheInfo.setAddress(dubboMethodEntity.getAddress());
        cacheInfo.setDate(new Date());
        return cacheInfo;
    }

    @Override
    public String toString() {
        return name;
    }
}
