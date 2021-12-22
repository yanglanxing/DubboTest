package com.yanglx.dubbo.test.utils;

import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.javadoc.PsiDocComment;

import java.util.ArrayList;
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
public class ParamUtil {
    /**
     * 初始化参数
     *
     * @param psiParameterList psi parameter list
     * @param psiDocComment    psi doc comment
     * @return the object [ ]
     * @since 1.0.0
     */
    public static Object[] getInitParamArray(PsiParameterList psiParameterList, PsiDocComment psiDocComment) {
        List<Object> paramList = new ArrayList<>();
        for (PsiParameter psiParameter : psiParameterList.getParameters()) {
            //map防止嵌套引用问题
            Map<String, String> map = new HashMap<>();
            SupportType supportType = SupportType.touch(psiParameter);
            Object value = supportType.getValue(psiParameter, map);
            paramList.add(value);
        }
        return paramList.toArray();
    }
}
