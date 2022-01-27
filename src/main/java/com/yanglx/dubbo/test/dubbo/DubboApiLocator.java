package com.yanglx.dubbo.test.dubbo;


import com.google.common.collect.Lists;

import com.intellij.openapi.diagnostic.Logger;
import com.yanglx.dubbo.test.PluginConstants;
import com.yanglx.dubbo.test.common.AddressTypeEnum;
import com.yanglx.dubbo.test.ui.DubboPanel;
import com.yanglx.dubbo.test.utils.JsonUtils;
import com.yanglx.dubbo.test.utils.StrUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.config.utils.ReferenceConfigCache.KeyGenerator;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * application
     */
    private static final ApplicationConfig application = new ApplicationConfig();
    private static final Logger LOGGER = Logger.getInstance(DubboPanel.class);
    private static final String CACHE_NAME = PluginConstants.PLUGIN_NAME;

    /**
     * change key when dubboTest config changed
     */
    private static final KeyGenerator generator = (referenceConfig) -> {
        // interfaceName
        String interfaceName = referenceConfig.getInterface();
        if (StringUtils.isBlank(interfaceName)) {
            Class<?> clazz = referenceConfig.getInterfaceClass();
            interfaceName = clazz.getName();
        }
        if (StringUtils.isBlank(interfaceName)) {
            throw new IllegalArgumentException("No interface info in ReferenceConfig" + referenceConfig);
        }

        // get other unique param
        String group = StringUtils.defaultString(referenceConfig.getGroup());
        String version = StringUtils.defaultString(referenceConfig.getVersion());
        String url = StringUtils.defaultString(referenceConfig.getUrl());
        String registries = StringUtils.defaultString(
                JsonUtils.toJSONString(referenceConfig.getRegistries()));

        List<String> uniqueParams = Lists.newArrayList(interfaceName, group, version, url, registries);
        return String.join("_", uniqueParams);
    };

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
        LOGGER.debug("invoke method ", JsonUtils.toJSONString(dubboMethodEntity));
        if (dubboMethodEntity == null
                || StrUtils.isBlank(dubboMethodEntity.getAddress())
                || StrUtils.isBlank(dubboMethodEntity.getMethodName())
                || StrUtils.isBlank(dubboMethodEntity.getInterfaceName())) {
            return "";
        }
        Thread.currentThread().setContextClassLoader(DubboMethodEntity.class.getClassLoader());

        ReferenceConfig<GenericService> referenceConfig = this.getReferenceConfig(dubboMethodEntity);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache(CACHE_NAME, generator);
        GenericService genericService = cache.get(referenceConfig);
        try {
            return genericService.$invoke(dubboMethodEntity.getMethodName(),
                    dubboMethodEntity.getMethodType(),
                    dubboMethodEntity.getParam());
        } catch (Exception e) {
            referenceConfig.destroy();
            cache.destroy(referenceConfig);
            throw e;
        }
    }

    /**
     * Get reference config
     *
     * @param dubboMethodEntity dubbo method entity
     * @return the reference config
     * @since 1.0.0
     */
    private ReferenceConfig<GenericService> getReferenceConfig(DubboMethodEntity dubboMethodEntity) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setInterface(dubboMethodEntity.getInterfaceName());
        reference.setCheck(false);
        reference.setGeneric("true");
        reference.setRetries(0);
        reference.setTimeout(10 * 1000);
        if (dubboMethodEntity.getAddress().startsWith(AddressTypeEnum.dubbo.name())) {
            reference.setUrl(dubboMethodEntity.getAddress());
        } else {
            RegistryConfig registryConfig = this.getRegistryConfig(dubboMethodEntity);
            reference.setRegistry(registryConfig);
        }
        if (StrUtils.isNotBlank(dubboMethodEntity.getVersion())) {
            reference.setVersion(dubboMethodEntity.getVersion());
        }
        if (StrUtils.isNotBlank(dubboMethodEntity.getGroup())) {
            reference.setGroup(dubboMethodEntity.getGroup());
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
    private RegistryConfig getRegistryConfig(DubboMethodEntity dubboMethodEntity) {
        String address = dubboMethodEntity.getAddress();
        RegistryConfig registryConfig = new RegistryConfig();
        Map<String,String> param = new HashMap<>();
        param.put("dubbo.application.service-discovery.migration","APPLICATION_FIRST");
        registryConfig.setParameters(param);
        registryConfig.setAddress(address);
        return registryConfig;
    }

}
