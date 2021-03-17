package com.yanglx.dubbo.test.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiArrayType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.impl.PsiClassImplUtil;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.ProjectAndLibrariesScope;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    /** Boolean */
    BOOLEAN {
        @Override
        public Boolean getRandomValue(PsiVariable psiVariable) {
            return RandomUtils.nextBoolean();
        }

        @Override
        public Boolean getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                return Boolean.valueOf(defaultValue);
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** Char */
    CHAR {
        @Override
        public Character getRandomValue(PsiVariable psiVariable) {
            return RandomStringUtils.randomAlphabetic(1).charAt(0);
        }

        @Override
        public Character getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                return defaultValue.charAt(0);
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** Integer */
    INTEGER {
        @Override
        public Integer getRandomValue(PsiVariable psiVariable) {
            return RandomUtils.nextInt(10000);
        }

        @Override
        public Integer getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (StringUtils.isNumeric(defaultValue)) {
                return Integer.valueOf(defaultValue);
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** Float */
    FLOAT {
        @Override
        public Float getRandomValue(PsiVariable psiVariable) {
            return RandomUtils.nextFloat();
        }

        @Override
        public Float getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (StringUtils.isNumeric(defaultValue)) {
                return Float.valueOf(defaultValue);
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** String */
    STRING {
        @Override
        public String getRandomValue(PsiVariable psiVariable) {
            return RandomStringUtils.randomAlphanumeric(10);
        }

        @Override
        public String getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                return defaultValue;
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** LIST */
    LIST {
        @Override
        public List<Map> getRandomValue(PsiVariable psiVariable) {
            String canonicalText = psiVariable.getType().getCanonicalText();
            if (canonicalText.indexOf("<") > 0) {
                canonicalText = canonicalText.substring(canonicalText.indexOf("<") + 1, canonicalText.length() - 1);
            }
            PsiClass psiClass = JavaPsiFacade
                .getInstance(psiVariable.getProject())
                .findClass(canonicalText, new ProjectAndLibrariesScope(psiVariable.getProject()));
            //基础类型直接返回
            if (psiClass == null || isBaseType(psiClass.getQualifiedName())) {
                return new ArrayList(0);
            } else {
                SupportType touch = SupportType.touch(psiClass.getQualifiedName());
                if (SupportType.OTHER.equals(touch)) {
                    List arrayList = new ArrayList(1);
                    arrayList.add(this.obj2Map2(psiClass));
                    return arrayList;
                } else {
                    return new ArrayList(0);
                }
            }
        }

        @Override
        public List<Map> getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                try {
                    return JSON.parseArray(defaultValue, Map.class);
                } catch (Exception ignored) {
                }
            }
            return this.getRandomValue(psiVariable);
        }

        private JSONObject obj2Map2(PsiClass psiClass) {
            PsiField[] allField = PsiClassImplUtil.getAllFields(psiClass);
            JSONObject result = new JSONObject(allField.length);

            for (PsiField psiField : allField) {
                if (psiField.getModifierList().hasModifierProperty("static") || psiField.getModifierList().hasModifierProperty("final")) {
                    continue;
                }
                SupportType supportType = SupportType.touch(psiField);

                if (supportType == SupportType.OTHER) {
                    PsiClass subPsiClass =
                        JavaPsiFacade.getInstance(psiClass.getProject()).findClass(psiField.getType().getCanonicalText(),
                                                                                   new ProjectAndLibrariesScope(psiClass.getProject()));
                    result.put(psiField.getName(), this.obj2Map2(subPsiClass));
                } else {
                    result.put(psiField.getName(), supportType.getRandomValue(psiField));
                }
            }
            result.put("class", psiClass.getQualifiedName());
            return result;
        }
    },
    /** Map */
    MAP {
        @Override
        public Map getRandomValue(PsiVariable psiVariable) {
            return new HashMap(0);
        }

        @Override
        public Map getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                try {
                    return JSON.parseObject(defaultValue);
                } catch (Exception ignored) {
                }
            }
            return this.getRandomValue(psiVariable);
        }
    },
    /** Date */
    DATE {
        @Override
        public Date getRandomValue(PsiVariable psiVariable) {
            return new Date();
        }

        @Override
        public Date getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return simpleDateFormat.parse(defaultValue);
                } catch (Exception e) {
                }
            }
            return this.getRandomValue(psiVariable);
        }
    },

    /** OTHER */
    OTHER {
        @Override
        public JSONObject getRandomValue(PsiVariable psiVariable) {
            PsiClass psiClass = JavaPsiFacade.getInstance(psiVariable.getProject()).findClass(psiVariable.getType().getCanonicalText(),
                                                                                              new ProjectAndLibrariesScope(psiVariable.getProject()));
            return this.obj2Map(psiClass);
        }

        @Override
        public Map getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            JSONObject docValue = null;
            if (defaultValue != null) {
                try {
                    docValue = JSON.parseObject(defaultValue);
                } catch (Exception ignored) {
                }
            }

            JSONObject randomValue = this.getRandomValue(psiVariable);
            return this.mergeJson(randomValue, docValue).getInnerMap();
        }

        public JSONObject mergeJson(JSONObject object1, JSONObject object2) {
            if (object1 == null && object2 == null) {
                return null;
            }
            if (object1 == null) {
                return object2;
            }
            if (object2 == null) {
                return object1;
            }
            for (String key : object2.keySet()) {
                Object value2 = object2.get(key);
                if (object1.containsKey(key)) {
                    Object value1 = object1.get(key);

                    if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                        object1.put(key, this.mergeJson((JSONObject) value1, (JSONObject) value2));
                    } else {
                        object1.put(key, value2);
                    }
                } else {
                    object1.put(key, value2);
                }
            }
            return object1;
        }

        private JSONObject obj2Map(PsiClass psiClass) {
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
                    PsiClass subPsiClass =
                        JavaPsiFacade.getInstance(psiClass.getProject()).findClass(psiField.getType().getCanonicalText(),
                                                                                   new ProjectAndLibrariesScope(psiClass.getProject()));
                    if ("".equals(subPsiClass.getQualifiedName())) {

                    }
                    result.put(psiField.getName(), this.obj2Map(subPsiClass));
                } else {
                    result.put(psiField.getName(), supportType.getRandomValue(psiField));
                }

            }
            result.put("class", psiClass.getQualifiedName());
            return result;
        }
    },

    /** Enum */
    ENUM {
        @Override
        public String getRandomValue(PsiVariable psiVariable) {
            return ((PsiClassReferenceType) psiVariable.getType()).rawType().resolve().getFields()[0].getName();
        }

        @Override
        public String getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap) {
            String defaultValue = defaultValueMap.get(psiVariable.getName());
            if (defaultValue != null) {
                return defaultValue;
            }
            return this.getRandomValue(psiVariable);
        }
    };

    /**
     * Gets random value *
     *
     * @param psiVariable psi variable
     * @return the random value
     * @since 1.0.0
     */
    public abstract Object getRandomValue(PsiVariable psiVariable);

    /**
     * Gets value *
     *
     * @param psiVariable     psi variable
     * @param defaultValueMap default value map
     * @return the value
     * @since 1.0.0
     */
    public abstract Object getValue(PsiVariable psiVariable, Map<String, String> defaultValueMap);

    /**
     * Touch
     *
     * @param type type
     * @return the support type
     * @since 1.0.0
     */
    public static SupportType touch(String type) {
        if (Long.class.getCanonicalName().equals(type) ||
            Integer.class.getCanonicalName().equals(type)) {
            return SupportType.INTEGER;
        }
        return SupportType.OTHER;
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
