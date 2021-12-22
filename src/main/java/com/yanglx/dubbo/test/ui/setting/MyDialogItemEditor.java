package com.yanglx.dubbo.test.ui.setting;

import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.util.Function;
import com.intellij.util.ui.table.TableModelEditor;
import com.yanglx.dubbo.test.ui.AppSettingsComponent;
import com.yanglx.dubbo.test.ui.MyConfigurableDubboSettings;
import com.yanglx.dubbo.test.utils.StrUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.UUID;

public class MyDialogItemEditor implements TableModelEditor.DialogItemEditor<MyConfigurableDubboSettings> {

    private AppSettingsComponent appSettingsComponent;

    public MyDialogItemEditor(AppSettingsComponent appSettingsComponent) {
        this.appSettingsComponent = appSettingsComponent;
    }


    @NotNull
    @Override
    public Class<MyConfigurableDubboSettings> getItemClass() {
        return MyConfigurableDubboSettings.class;
    }

    @Override
    public MyConfigurableDubboSettings clone(@NotNull MyConfigurableDubboSettings item, boolean forInPlaceEditing) {
        //对应工具栏得复制
        MyConfigurableDubboSettings myConfigurableDubboSettings = new MyConfigurableDubboSettings(forInPlaceEditing ? item.getId() : UUID.randomUUID());
        myConfigurableDubboSettings.setConfig(item.getName(), item.getProcessedAddress(), item.getVersion(), item.getGroup());
        return myConfigurableDubboSettings;
    }

//    @Override
//    public void edit(@NotNull MyConfigurableDubboSettings item, @NotNull Function<? super MyConfigurableDubboSettings, ? extends MyConfigurableDubboSettings> mutator, boolean isAdd) {
//        //对应工具栏得添加或者编辑按钮事件
//        MyConfigurableDubboSettings settings = this.openDialog(item);
//        if (settings != null) {
//            mutator.fun(item).setConfig(settings.getName(), settings.getProcessedAddress(), settings.getVersion(), settings.getGroup());
//        }
//    }

    @Override
    public void edit(@NotNull MyConfigurableDubboSettings item, @NotNull Function<? super MyConfigurableDubboSettings, ? extends MyConfigurableDubboSettings> mutator, boolean isAdd) {
        //对应工具栏得添加或者编辑按钮事件
        MyConfigurableDubboSettings settings = this.openDialog(item);
        if (settings != null) {
            mutator.fun(item).setConfig(settings.getName(), settings.getProcessedAddress(), settings.getVersion(), settings.getGroup());
        }
    }

    @Override
    public void applyEdited(@NotNull MyConfigurableDubboSettings oldItem, @NotNull MyConfigurableDubboSettings newItem) {
        System.out.println("=====================applyEdited=====================");

    }

    @Override
    public boolean isUseDialogToAdd() {
        //设置为弹窗进行添加
        return true;
    }

    private MyConfigurableDubboSettings openDialog(MyConfigurableDubboSettings browser) {
        SettingDialog settingDialog = new SettingDialog(browser);
        final DialogBuilder dialogBuilder = new DialogBuilder(appSettingsComponent.getPanel())
                .title("Dubbo Setting").centerPanel(settingDialog.getPanel());
        if (dialogBuilder.show() == DialogWrapper.OK_EXIT_CODE) {
            MyConfigurableDubboSettings myConfigurableDubboSettings = settingDialog.getMyConfigurableDubboSettings();
            if (isExist(myConfigurableDubboSettings)) {
                JLabel jLabel = new JLabel("Data duplication");
                DialogBuilder msgDialog = new DialogBuilder(appSettingsComponent.getPanel())
                        .title("Dubbo Setting").centerPanel(jLabel);
                msgDialog.show();
                return null;
            }
            browser.setProtocol(myConfigurableDubboSettings.getProtocol());
            browser.setIp(myConfigurableDubboSettings.getIp());
            browser.setPort(myConfigurableDubboSettings.getPort());
            browser.setVersion(myConfigurableDubboSettings.getVersion());
            browser.setGroup(myConfigurableDubboSettings.getGroup());
            browser.setName(myConfigurableDubboSettings.getName());
            if (StrUtils.isNotBlank(browser.getProcessedAddress())) {
                return browser;
            }
        }
        return null;
    }

    /**
     * 校验重复
     *
     * @param settings
     * @return
     */
    private boolean isExist(MyConfigurableDubboSettings settings) {
        List<MyConfigurableDubboSettings> settings1 = appSettingsComponent.getSettings();
        for (MyConfigurableDubboSettings dubboSettings : settings1) {
            String item = dubboSettings.getName() + dubboSettings.getProcessedAddress() + dubboSettings.getVersion() + dubboSettings.getGroup();
            String item2 = settings.getName() + settings.getProcessedAddress() + settings.getVersion() + settings.getGroup();
            if (item.equals(item2)) {
                return true;
            }
        }
        return false;
    }
}
