package com.yanglx.dubbo.test.config;

import com.intellij.openapi.options.Configurable;
import com.intellij.ui.tabs.TabInfo;
import com.yanglx.dubbo.test.CacheInfo;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.ui.AppSettingsComponent;
import com.yanglx.dubbo.test.ui.MyConfigurableDubboSettings;
import com.yanglx.dubbo.test.ui.Tab;
import com.yanglx.dubbo.test.ui.TabBar;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认设置
 */
 public class AppSettingsConfigurable implements Configurable  {

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "DubboTest";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        return mySettingsComponent.isModified();
    }

    @Override
    public void apply() {
        DubboSetingState settings = DubboSetingState.getInstance();
        List<MyConfigurableDubboSettings> settings1 = mySettingsComponent.getSettings();
        List<CacheInfo> collect = settings1.stream().map(config -> {
            CacheInfo cacheInfo = new CacheInfo();
            cacheInfo.setId(config.getId().toString());
            cacheInfo.setName(config.getName());
            cacheInfo.setAddress(config.getProcessedAddress());
            cacheInfo.setGroup(config.getGroup());
            cacheInfo.setVersion(config.getVersion());
            return cacheInfo;
        }).collect(Collectors.toList());
        settings.setDubboConfigs(collect);

        //刷新下拉
        TabInfo selectedInfo = TabBar.getSelectionTabInfo();
        Tab component = (Tab)selectedInfo.getComponent();
        component.getDubboPanel().reset();
    }

    @Override
    public void reset() {
        DubboSetingState settings = DubboSetingState.getInstance();
        List<CacheInfo> dubboConfigs = settings.getDubboConfigs();
        List<MyConfigurableDubboSettings> collect = dubboConfigs.stream().map(cacheInfo -> {
            MyConfigurableDubboSettings config = new MyConfigurableDubboSettings();
            config.setConfig(cacheInfo.getName(),cacheInfo.getAddress(), cacheInfo.getVersion(), cacheInfo.getGroup());
            return config;
        }).collect(Collectors.toList());
        mySettingsComponent.reset(collect);
    }
}
