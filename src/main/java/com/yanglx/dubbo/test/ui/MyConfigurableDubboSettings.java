package com.yanglx.dubbo.test.ui;

import com.yanglx.dubbo.test.utils.StringUtils;
import lombok.Data;

import java.util.UUID;

@Data
public class MyConfigurableDubboSettings {

    private final UUID id;

    private String name;

    private String address;

    private String ip;

    private String protocol;

    private String port;

    private String version;

    private String group;

    public MyConfigurableDubboSettings(){
        this(UUID.randomUUID());
    }

    public MyConfigurableDubboSettings(UUID id){
        this.id = id;
    }

    public void setConfig(String name,String address,String version,String group){
        String protocol = address.substring(0, address.indexOf("://"));
        String ip = address.substring(address.indexOf("://")+3, address.lastIndexOf(":"));
        String port = address.substring(address.lastIndexOf(":")+1);
        this.ip = ip;
        this.protocol = protocol;
        this.port = port;
        this.version = version;
        this.group = group;
        this.name = name;
    }

    public String getProcessedAddress() {
        if (StringUtils.isNotBlank(this.protocol) && StringUtils.isNotBlank(this.ip) && StringUtils.isNotBlank(this.port)) {
            return this.protocol + "://"+ this.ip + ":" + this.port;
        }else {
            return null;
        }
    }
}
