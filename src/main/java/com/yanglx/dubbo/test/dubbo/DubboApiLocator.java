package com.yanglx.dubbo.test.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.yanglx.dubbo.test.PluginConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
public class DubboApiLocator {

    /** application */
    private static final ApplicationConfig application = new ApplicationConfig();

    /** cacheReferenceMap */
    private static final Map<String, ReferenceConfig<GenericService>> cacheReferenceMap = new ConcurrentHashMap<>();

    /** registryConfigCache */
    private static final Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();

    static {
        application.setName(PluginConstants.PLUGIN_NAME);
    }

    /**
     * Invoke
     *
     * @param dubboMethodEntity dubbo method entity
     * @return the object
     * @since 1.0.0
     */
    public Object invoke(DubboMethodEntity dubboMethodEntity) {
        System.out.println(JSON.toJSONString(dubboMethodEntity));
        if (dubboMethodEntity == null
            || StringUtils.isBlank(dubboMethodEntity.getAddress())
            || StringUtils.isBlank(dubboMethodEntity.getMethodName())
            || StringUtils.isBlank(dubboMethodEntity.getInterfaceName())) {
            return "";
        }
        Thread.currentThread().setContextClassLoader(DubboMethodEntity.class.getClassLoader());

        AddressTypeEnum addressType = this.getAddressType(dubboMethodEntity.getAddress());
        if (addressType.equals(AddressTypeEnum.unknown)) {
            return "无效地址";
        }

        ReferenceConfig<GenericService> referenceConfig = this.getReferenceConfig(dubboMethodEntity);
        if (referenceConfig == null) {
            return null;
        }
        GenericService genericService = referenceConfig.get();
        if (genericService == null) {
            return null;
        }
        try {
            return genericService.$invoke(dubboMethodEntity.getMethodName(),
                                          dubboMethodEntity.getMethodType(),
                                          dubboMethodEntity.getParamObj());
        } catch (Exception e) {
            referenceConfig.destroy();
            String key = addressType.name() + "-" + dubboMethodEntity.getInterfaceName();
            cacheReferenceMap.remove(key);
            return e.getLocalizedMessage();
        }
    }

    /**
     * Gets address type *
     *
     * @param address address
     * @return the address type
     * @since 1.0.0
     */
    private AddressTypeEnum getAddressType(String address) {
        if (address.startsWith("zookeeper")) {
            return AddressTypeEnum.zookeeper;
        } else if (address.startsWith("dubbo")) {
            return AddressTypeEnum.dubbo;
        } else {
            return AddressTypeEnum.unknown;
        }
    }

    /**
     * <p>Description: </p>
     *
     * @author yanglx
     * @version 1.0.0
     * @email "mailto:dev_ylx@163.com"
     * @date 2021.02.20 15:57
     * @since 1.0.0
     */
    enum AddressTypeEnum {
        /** Dubbo address type enum */
        dubbo,
        /** Zookeeper address type enum */
        zookeeper,
        /** Unknown address type enum */
        unknown
    }

    /**
     * Get reference config
     *
     * @param dubboMethodEntity dubbo method entity
     * @return the reference config
     * @since 1.0.0
     */
    private ReferenceConfig<GenericService> getReferenceConfig(DubboMethodEntity dubboMethodEntity) {
        AddressTypeEnum addressType = this.getAddressType(dubboMethodEntity.getAddress());
        String key = addressType.name() + "-" + dubboMethodEntity.getInterfaceName();
        ReferenceConfig<GenericService> reference = cacheReferenceMap.get(key);
        if (null == reference) {
            reference = new ReferenceConfig<>();
            reference.setApplication(application);
            reference.setInterface(dubboMethodEntity.getInterfaceName());
            reference.setCheck(false);
            reference.setGeneric(true);
            reference.setRetries(0);
            reference.setTimeout(10 * 1000);
            if (addressType.equals(AddressTypeEnum.zookeeper)) {
                RegistryConfig registryConfig = getRegistryConfig(dubboMethodEntity);
                reference.setRegistry(registryConfig);
            } else if (addressType.equals(AddressTypeEnum.dubbo)) {
                reference.setUrl(dubboMethodEntity.getAddress());
            }
            if (StringUtils.isNotBlank(dubboMethodEntity.getVersion())) {
                reference.setVersion(dubboMethodEntity.getVersion());
            }
            if (StringUtils.isNotBlank(dubboMethodEntity.getGroup())) {
                reference.setGroup(dubboMethodEntity.getGroup());
            }
            cacheReferenceMap.put(key, reference);
        }
        return reference;
    }

    /**
     * Gets registry config *
     *
     * @param dubboMethodEntity dubboMethodEntity
     * @return the registry config
     * @since 1.0.0
     */
    private static RegistryConfig getRegistryConfig(DubboMethodEntity dubboMethodEntity) {
        String address = dubboMethodEntity.getAddress();
        String version = dubboMethodEntity.getVersion();
        String group = dubboMethodEntity.getGroup();
        String key = address + "-" + version + "-" + group;
        RegistryConfig registryConfig = registryConfigCache.get(key);
        if (null == registryConfig) {
            registryConfig = new RegistryConfig();
            if (StringUtils.isNotBlank(address)) {
                registryConfig.setAddress(address);
            }
            if (StringUtils.isNotBlank(version)) {
                registryConfig.setVersion(version);
            }
            if (StringUtils.isNotBlank(group)) {
                registryConfig.setGroup(group);
            }
            registryConfig.setRegister(false);
            registryConfigCache.put(key, registryConfig);
        }
        return registryConfig;
    }

}
