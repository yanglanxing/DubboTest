package com.yanglx.dubbo.test.config;

import com.intellij.openapi.options.Configurable;
import com.yanglx.dubbo.test.CacheInfo;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.ui.AppSettingsComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;

/**
 * 默认设置
 */
public class AppSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "DubboTest";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        DubboSetingState settings = DubboSetingState.getInstance();
        CacheInfo defaultSetting = settings.getDefaultSetting();
        boolean modified = !mySettingsComponent.getAddressFieldText().equals(defaultSetting.getAddress())
                || !mySettingsComponent.getVersionFieldText().equals(defaultSetting.getVersion())
                || !mySettingsComponent.getGroupFieldText().equals(defaultSetting.getGroup());
        return modified;
    }

    @Override
    public void apply() {
        DubboSetingState settings = DubboSetingState.getInstance();
        CacheInfo defaultSetting = new CacheInfo();
        defaultSetting.setVersion(mySettingsComponent.getVersionFieldText());
        defaultSetting.setAddress(mySettingsComponent.getAddressFieldText());
        defaultSetting.setGroup(mySettingsComponent.getGroupFieldText());
        settings.setDefaultSetting(defaultSetting);
    }

    @Override
    public void reset() {
        DubboSetingState settings = DubboSetingState.getInstance();
        CacheInfo defaultSetting = settings.getDefaultSetting();
        mySettingsComponent.setVersionField(defaultSetting.getVersion());
        mySettingsComponent.setAddressField(defaultSetting.getAddress());
        mySettingsComponent.setGroupField(defaultSetting.getGroup());
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}
