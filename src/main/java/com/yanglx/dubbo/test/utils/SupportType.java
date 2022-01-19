package com.yanglx.dubbo.test.utils;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.search.ProjectAndLibrariesScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author wbean, yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
public enum SupportType {
    /**
     * Boolean
     */
    BOOLEAN {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return false;
        }
    },
    /**
     * Char
     */
    CHAR {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return "";
        }
    },
    /**
     * Integer
     */
    INTEGER {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return 0;
        }
    },
    /**
     * Float
     */
    FLOAT {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return 0;
        }
    },
    /**
     * String
     */
    STRING {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return "";
        }
    },
    /**
     * LIST
     */
    LIST {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            String canonicalText = psiVariable.getType().getCanonicalText();
            if (canonicalText.indexOf("<") > 0) {
                canonicalText = canonicalText.substring(canonicalText.indexOf("<") + 1, canonicalText.length() - 1);
            }
            PsiClass psiClass = JavaPsiFacade.getInstance(psiVariable.getProject())
                    .findClass(canonicalText, new ProjectAndLibrariesScope(psiVariable.getProject()));
            //基础类型直接返回
            if (psiClass == null || isBaseType(psiClass.getQualifiedName())) {
                return Collections.emptyList();
            } else {
                if (isNotExistAndSet(psiClass.getName(),map)) {
                    Object value = SupportType.getValueByPsiClass(psiClass,map);
                    List<Object> arrayList = new ArrayList<>(1);
                    arrayList.add(value);
                    return arrayList;
                }else {
                    return Collections.emptyList();
                }
            }
        }
    },
    /**
     * Map
     */
    MAP {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return new Object();
        }
    },
    /**
     * Date
     */
    DATE {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return "";
        }
    },

    /**
     * OTHER
     */
    OTHER {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            String className =StrUtils.trimClassName(psiVariable.getType().getCanonicalText());
            PsiClass psiClass = JavaPsiFacade.getInstance(psiVariable.getProject()).findClass(className,
                    new ProjectAndLibrariesScope(psiVariable.getProject()));
            if (psiClass != null && isNotExistAndSet(psiClass.getName(),map)) {
                return SupportType.getValueByPsiClass(psiClass,map);
            }else {
                return new Object();
            }

        }
    },

    /**
     * Enum
     */
    ENUM {
        @Override
        public Object getValue(PsiVariable psiVariable,Map<String,String> map) {
            return null;
        }
    };

    /**
     * Gets value *
     *
     * @param psiVariable psi variable
     * @return the value
     * @since 1.0.0
     */
    public abstract Object getValue(PsiVariable psiVariable,Map<String,String> map);


    public static Object getValueByPsiClass(PsiClass psiClass,Map<String,String> map) {
        PsiField[] allField = PsiClassImplUtil.getAllFields(psiClass);
        Map<String, Object> result = new HashMap<>();
        for (PsiField psiField : allField) {
            SupportType touch = touch(psiField);
            result.put(psiField.getName(), touch.getValue(psiField,map));
        }
        result.put("class", psiClass.getQualifiedName());
        return result;
    }

    public boolean isNotExistAndSet(String str,Map<String,String> map){
        if (!map.containsKey(str)) {
            map.put(str,str);
            return true;
        }else {
            return false;
        }
    }

    /**
     * Touch
     *
     * @param parameter parameter
     * @return the support type
     * @since 1.0.0
     */
    public static SupportType touch(PsiVariable parameter) {
        PsiType type = parameter.getType();

        if (PsiType.BOOLEAN.equals(type)) {
            return SupportType.BOOLEAN;
        }

        if (PsiType.CHAR.equals(type)) {
            return SupportType.CHAR;
        }

        if (PsiType.BYTE.equals(type)
                || PsiType.INT.equals(type)
                || PsiType.LONG.equals(type)
                || PsiType.SHORT.equals(type)
                || type.equalsToText(Integer.class.getCanonicalName())
                || type.equalsToText(Long.class.getCanonicalName())
                || type.equalsToText(Short.class.getCanonicalName())
                || type.equalsToText(Byte.class.getCanonicalName())) {
            return SupportType.INTEGER;
        }

        if (PsiType.DOUBLE.equals(type)
                || PsiType.FLOAT.equals(type)
                || type.equalsToText(Double.class.getCanonicalName())
                || type.equalsToText(Float.class.getCanonicalName())) {
            return SupportType.FLOAT;
        }

        if (type.equalsToText(String.class.getCanonicalName())) {
            return SupportType.STRING;
        }

        if (type.equalsToText(Date.class.getCanonicalName())) {
            return SupportType.DATE;
        }

        if (type.getCanonicalText().startsWith(List.class.getCanonicalName())
                || type instanceof PsiArrayType) {
            return SupportType.LIST;
        }

        if (type.getCanonicalText().startsWith(Map.class.getCanonicalName())) {
            return SupportType.MAP;
        }

        if (type.getSuperTypes().length > 0 && type.getSuperTypes()[0].getCanonicalText().contains(Enum.class.getCanonicalName())) {
            return SupportType.ENUM;
        }

        return SupportType.OTHER;
    }

    /**
     * Is base type
     *
     * @param typeStr type str
     * @return the boolean
     * @since 1.0.0
     */
    public static boolean isBaseType(String typeStr) {
        String[] types = {String.class.getCanonicalName()
                , Long.class.getCanonicalName()
                , Integer.class.getCanonicalName()
                , Float.class.getCanonicalName()
                , Byte.class.getCanonicalName()
                , BigDecimal.class.getCanonicalName()
                , Double.class.getCanonicalName()};
        for (String type : types) {
            if (typeStr.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
