package com.yanglx.dubbo.test.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DubboApiLocator {

    private static ApplicationConfig application = new ApplicationConfig();

    private static Map<String, ReferenceConfig<GenericService>> cacheReferenceMap = new ConcurrentHashMap<>();

    private static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

    static {
        application.setName("DubboTest");
    }

    public Object invoke(DubboMethodEntity dubboMethodEntity) {
        System.out.println(JSON.toJSONString(dubboMethodEntity));
        if (dubboMethodEntity == null
                || StringUtils.isBlank(dubboMethodEntity.getAddress())
                || StringUtils.isBlank(dubboMethodEntity.getMethodName())
                || StringUtils.isBlank(dubboMethodEntity.getInterfaceName())) {
            return "";
        }
        Thread.currentThread().setContextClassLoader(DubboMethodEntity.class.getClassLoader());

        AddressTypeEnum addressType = getAddressType(dubboMethodEntity.getAddress());
        if (addressType.equals(AddressTypeEnum.unknown)) {
            return "无效地址";
        }

        ReferenceConfig<GenericService> referenceConfig = getReferenceConfig(dubboMethodEntity);
        if (referenceConfig == null) {
            return null;
        }
        GenericService genericService = referenceConfig.get();
        if (genericService == null) {
            return null;
        }
        try {
            Object invoke = genericService.$invoke(dubboMethodEntity.getMethodName(),
                    dubboMethodEntity.getMethodType(),
                    dubboMethodEntity.getParamObj());
            return invoke;
        } catch (Exception e) {
            referenceConfig.destroy();
            String key = addressType.name()+"-"+dubboMethodEntity.getInterfaceName();
            cacheReferenceMap.remove(key);
            return e.getLocalizedMessage();
        }
    }

    private AddressTypeEnum getAddressType(String address) {
        if (address.startsWith("zookeeper")) {
            return AddressTypeEnum.zookeeper;
        } else if (address.startsWith("dubbo")) {
            return AddressTypeEnum.dubbo;
        } else {
            return AddressTypeEnum.unknown;
        }
    }

    enum AddressTypeEnum {
        dubbo, zookeeper,unknown
    }

    private ReferenceConfig<GenericService> getReferenceConfig(DubboMethodEntity dubboMethodEntity){
        AddressTypeEnum addressType = getAddressType(dubboMethodEntity.getAddress());
        String key = addressType.name()+"-"+dubboMethodEntity.getInterfaceName();
        ReferenceConfig<GenericService> reference = cacheReferenceMap.get(key);
        if(null == reference){
            reference = new ReferenceConfig<>();
            reference.setApplication(application);
            reference.setInterface(dubboMethodEntity.getInterfaceName());
            reference.setCheck(false);
            reference.setGeneric(true);
            reference.setRetries(0);
            reference.setTimeout(10*1000);
            if (addressType.equals(AddressTypeEnum.zookeeper)) {
                RegistryConfig registryConfig = getRegistryConfig(dubboMethodEntity.getAddress(),
                        dubboMethodEntity.getVersion());
                reference.setRegistry(registryConfig);
            } else if (addressType.equals(AddressTypeEnum.dubbo)) {
                reference.setUrl(dubboMethodEntity.getAddress());
            }
            if (StringUtils.isNotBlank(dubboMethodEntity.getVersion())) {
                reference.setVersion(dubboMethodEntity.getVersion());
            }
            cacheReferenceMap.put(key,reference);
        }
        return reference;
    }

    private static RegistryConfig getRegistryConfig(String address, String version) {
        String key = address + "-" + version;
        RegistryConfig registryConfig = registryConfigCache.get(key);
        if (null == registryConfig) {
            registryConfig = new RegistryConfig();
            if (StringUtils.isNotBlank(address)) {
                registryConfig.setAddress(address);
            }
            if (StringUtils.isNotBlank(version)) {
                registryConfig.setVersion(version);
            }
            registryConfigCache.put(key, registryConfig);
        }
        return registryConfig;
    }

    public static void main(String[] args) {
        DubboApiLocator dubboApiLocator = new DubboApiLocator();
        DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();
        dubboMethodEntity.setParamObj(new Object[]{"YHQBgI8UmB"});
        dubboMethodEntity.setMethodType(new String[]{"java.lang.String"});
        dubboMethodEntity.setInterfaceName("com.test.service.HelloService");
        dubboMethodEntity.setMethodName("sayHelloStr");
        dubboMethodEntity.setAddress("zookeeper://127.0.0.1:2181");
        Object invoke = dubboApiLocator.invoke(dubboMethodEntity);
        System.out.printf(JSON.toJSONString(invoke));
    }
}
