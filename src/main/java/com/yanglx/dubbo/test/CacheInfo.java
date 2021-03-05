package com.yanglx.dubbo.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    /**
     * 将CacheInfo转换DubboMethodEntity并返回
     * @return
     */
    public DubboMethodEntity getDubboMethodEntity(){
        DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();
        dubboMethodEntity.setId(this.getId());
        dubboMethodEntity.setInterfaceName(this.getInterfaceName());
        dubboMethodEntity.setMethodName(this.getMethodName());
        dubboMethodEntity.setVersion(this.getVersion());
        dubboMethodEntity.setGroup(this.getGroup());
        List<String> stringList = JSON.parseArray(this.getMethodTypeJson(), String.class);
        String[] methodTypes = new String[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            methodTypes[i] = stringList.get(i);
        }
        dubboMethodEntity.setMethodType(methodTypes);
        JSONArray objects = JSON.parseArray(this.getParamObjJson());
        dubboMethodEntity.setParamObj(objects.toArray());
        dubboMethodEntity.setAddress(this.getAddress());
        return dubboMethodEntity;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CacheInfo)) return false;
        CacheInfo cacheInfo = (CacheInfo) o;
        return id.equals(cacheInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
