package com.yanglx.dubbo.test;

import com.intellij.AbstractBundle;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;


/**
 * <p>Company: 成都返空汇网络技术有限公司 </p>
 * <p>Description: </p>
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2021.02.20 16:49
 * @since 1.1.0
 */
public class DubboTestBundle {

    /** BUNDLE */
    @NonNls
    private static final String BUNDLE = "messages.DubboTestBundle";
    /** Our bundle */
    private static Reference<ResourceBundle> ourBundle;

    /**
     * Mik bundle
     *
     * @since 1.1.0
     */
    private DubboTestBundle() {
    }

    /**
     * Visibility presentation
     *
     * @param modifier modifier
     * @return the string
     * @since 1.1.0
     */
    @NotNull
    public static String visibilityPresentation(@NotNull String modifier) {
        return message(modifier + ".visibility.presentation");
    }

    /**
     * Message
     *
     * @param key    key
     * @param params params
     * @return the string
     * @since 1.1.0
     */
    @NotNull
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
//        return AbstractBundle.message(getBundle(), key, params);
        return null;
    }

    /**
     * Gets bundle *
     *
     * @return the bundle
     * @since 1.1.0
     */
    private static ResourceBundle getBundle() {
        ResourceBundle bundle = com.intellij.reference.SoftReference.dereference(ourBundle);
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE);
            ourBundle = new SoftReference<>(bundle);
        }
        return bundle;
    }
}
