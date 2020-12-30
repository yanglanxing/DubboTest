package com.yanglx.dubbo.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.ProjectAndLibrariesScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamUtil {
    /**
     * 初始化参数
     */
    public static Object[] getInitParamArray(PsiParameterList psiParameterList,PsiDocComment psiDocComment) {
        Map<String, String> docParams = new HashMap<>();
        List<Object> paramList = new ArrayList<>();
        for (PsiParameter psiParameter : psiParameterList.getParameters()) {
            SupportType supportType = SupportType.touch(psiParameter);
            Object value = supportType.getValue(psiParameter, docParams);
            paramList.add(value);
        }
        return paramList.toArray();
    }

    public static Object getValue(SupportType type,PsiParameter psiVariable){
        JavaPsiFacade instance = JavaPsiFacade
                .getInstance(psiVariable.getProject());
        switch (type){
            case OTHER:
                PsiClass psiClass = instance
                        .findClass(psiVariable.getType().getCanonicalText(),
                                new ProjectAndLibrariesScope(psiVariable.getProject()));
                return obj2Map(psiClass);
            case LIST:
                String canonicalText = psiVariable.getType().getCanonicalText();
                if (canonicalText.indexOf("<") > 0) {
                    canonicalText = canonicalText.substring(canonicalText.indexOf("<") + 1, canonicalText.length() - 1);
                }
                PsiClass psiClass1 = instance
                        .findClass(canonicalText,
                                new ProjectAndLibrariesScope(psiVariable.getProject()));
                if (psiClass1 == null) {
                    return new ArrayList<>();
                }else {
                    SupportType touch = SupportType.touch(psiClass1.getQualifiedName());
                    ArrayList arrayList = new ArrayList<>();
                    arrayList.add(obj2Map(psiClass1));
                    return arrayList;
                }
            default:
                return "";
        }
    }

    private static JSONObject obj2Map(PsiClass psiClass) {
        PsiField[] allField = PsiClassImplUtil.getAllFields(psiClass);
        JSONObject result = new JSONObject(allField.length);

        for (PsiField psiField : allField) {
            if (psiField.getModifierList().hasModifierProperty("static") || psiField.getModifierList().hasModifierProperty("final")) {
                continue;
            }
            if ("com.xqxc.biz.bean.BaseRequest".equals(psiField.getContainingClass().getQualifiedName())) {
                continue;
            }
            SupportType supportType = SupportType.touch(psiField);
            if (supportType == SupportType.OTHER) {
                PsiClass subPsiClass = JavaPsiFacade
                        .getInstance(psiClass.getProject())
                        .findClass(psiField.getType().getCanonicalText(),
                                new ProjectAndLibrariesScope(psiClass.getProject()));
                result.put(psiField.getName(), obj2Map(subPsiClass));
            } else {
                result.put(psiField.getName(), "");
            }

        }
        result.put("class", psiClass.getQualifiedName());
        return result;
    }
}
