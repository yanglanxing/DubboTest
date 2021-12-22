package com.yanglx.dubbo.test.dubbo;


import com.alibaba.fastjson.JSON;
import com.yanglx.dubbo.test.PluginConstants;
import com.yanglx.dubbo.test.utils.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

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

        ReferenceConfig<GenericService> referenceConfig = this.getReferenceConfig(dubboMethodEntity);
        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        GenericService genericService = cache.get(referenceConfig);
        try {
            return genericService.$invoke(dubboMethodEntity.getMethodName(),
                    dubboMethodEntity.getMethodType(),
                    dubboMethodEntity.getParamObj());
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
        reference.setGeneric(true);
        reference.setRetries(0);
        reference.setTimeout(10 * 1000);
        if (dubboMethodEntity.getMethodName().startsWith("dubbo")) {
            reference.setUrl(dubboMethodEntity.getAddress());
        } else {
            RegistryConfig registryConfig = this.getRegistryConfig(dubboMethodEntity);
            reference.setRegistry(registryConfig);
        }
        if (StringUtils.isNotBlank(dubboMethodEntity.getVersion())) {
            reference.setVersion(dubboMethodEntity.getVersion());
        }
        if (StringUtils.isNotBlank(dubboMethodEntity.getGroup())) {
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
        registryConfig.setAddress(address);
        return registryConfig;
    }

}
