package com.yanglx.dubbo.test.ui;

import com.yanglx.dubbo.test.utils.StrUtils;

import java.util.UUID;

public class MyConfigurableDubboSettings {

    private final UUID id;

    private String name;

    private String address;

    private String ip;

    private String protocol;

    private String port;

    private String version;

    private String group;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public MyConfigurableDubboSettings() {
        this(UUID.randomUUID());
    }

    public MyConfigurableDubboSettings(UUID id) {
        this.id = id;
    }

    public void setConfig(String name, String address, String version, String group) {
        String protocol = address.substring(0, address.indexOf("://"));
        String ip = address.substring(address.indexOf("://") + 3, address.lastIndexOf(":"));
        String port = address.substring(address.lastIndexOf(":") + 1);
        this.ip = ip;
        this.protocol = protocol;
        this.port = port;
        this.version = version;
        this.group = group;
        this.name = name;
    }

    public String getProcessedAddress() {
        if (StrUtils.isNotBlank(this.protocol) && StrUtils.isNotBlank(this.ip) && StrUtils.isNotBlank(this.port)) {
            return this.protocol + "://" + this.ip + ":" + this.port;
        } else {
            return null;
        }
    }
}
