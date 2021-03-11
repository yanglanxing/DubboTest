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
        dubboMethodEntity.setParamObj(new Object[] {"你好啊"});
        dubboMethodEntity.setMethodType(new String[] {"java.lang.String"});
        dubboMethodEntity.setInterfaceName("org.apache.dubbo.admin.api.GreetingService");
        dubboMethodEntity.setMethodName("sayHello");
        dubboMethodEntity.setVersion("1.0.0");
//        dubboMethodEntity.setAddress("zookeeper://127.0.0.1:2181");
        dubboMethodEntity.setAddress("dubbo://127.0.0.1:20880");
        Object invoke = dubboApiLocator.invoke(dubboMethodEntity);
        System.out.println("返回报文："+JSON.toJSONString(invoke));
    }
}
