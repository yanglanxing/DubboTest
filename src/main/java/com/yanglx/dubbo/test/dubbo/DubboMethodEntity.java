package com.yanglx.dubbo.test.dubbo;

import lombok.Data;

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
@Data
public class DubboMethodEntity {

    private String id;
    /** Interface name */
    private String interfaceName;
    /** Method name */
    private String methodName;
    /** Version */
    private String version;
    /** Group */
    private String group;
    /** Method type */
    private String[] methodType;
    /** Param obj */
    private Object[] paramObj;
    /** Address */
    private String address;

}
