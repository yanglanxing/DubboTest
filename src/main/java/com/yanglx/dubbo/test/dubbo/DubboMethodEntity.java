package com.yanglx.dubbo.test.dubbo;

public class DubboMethodEntity {

    private String interfaceName;

    private String methodName;

    private String version;

    private String methodType[];

    private Object paramObj[];

    private String address;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object[] getParamObj() {
        return paramObj;
    }

    public void setParamObj(Object[] paramObj) {
        this.paramObj = paramObj;
    }

    public String[] getMethodType() {
        return methodType;
    }

    public void setMethodType(String[] methodType) {
        this.methodType = methodType;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
