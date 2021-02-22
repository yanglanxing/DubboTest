package com.yanglx.dubbo.test.dubbo;

import com.alibaba.fastjson.JSON;

import junit.framework.TestCase;

/**
 * <p>Description:  </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2021.02.20 17:40
 * @since 1.1.0
 */
public class DubboApiLocatorTest extends TestCase {

    /**
     * Test
     *
     * @since 1.1.0
     */
    public void test() {
        DubboApiLocator dubboApiLocator = new DubboApiLocator();
        DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();
        dubboMethodEntity.setParamObj(new Object[] {"YHQBgI8UmB"});
        dubboMethodEntity.setMethodType(new String[] {"java.lang.String"});
        dubboMethodEntity.setInterfaceName("com.test.service.HelloService");
        dubboMethodEntity.setMethodName("sayHelloStr");
        dubboMethodEntity.setAddress("zookeeper://127.0.0.1:2181");
        Object invoke = dubboApiLocator.invoke(dubboMethodEntity);
        System.out.print(JSON.toJSONString(invoke));
    }
}
