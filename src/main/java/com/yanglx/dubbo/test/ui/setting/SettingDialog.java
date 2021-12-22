package com.yanglx.dubbo.test.ui.setting;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.yanglx.dubbo.test.common.AddressTypeEnum;
import com.yanglx.dubbo.test.ui.MyConfigurableDubboSettings;
import com.yanglx.dubbo.test.utils.StringUtils;

import javax.swing.*;

public class SettingDialog {

    private JComboBox<String> comboBox;
    private JBTextField nameField;
    private JBTextField ipField;
    private JBTextField portField;
    private JBTextField versionField;
    private JBTextField groupField;

    private JPanel panel;

    public SettingDialog(MyConfigurableDubboSettings browser) {
        this.comboBox = new ComboBox<>();
        AddressTypeEnum[] values = AddressTypeEnum.values();
        for (AddressTypeEnum value : values) {
            this.comboBox.addItem(value.name());
        }
        if (StringUtils.isNotBlank(browser.getProtocol())) {
            this.comboBox.setSelectedItem(browser.getProtocol());
        } else {
            this.comboBox.setSelectedItem(AddressTypeEnum.zookeeper.name());
        }
        this.nameField = new JBTextField(browser.getName());
        this.ipField = new JBTextField(browser.getIp());
        this.portField = new JBTextField(browser.getPort());
        this.versionField = new JBTextField(browser.getVersion());
        this.groupField = new JBTextField(browser.getGroup());

        this.panel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Name", nameField)
                .addLabeledComponent("Type", comboBox)
                .addLabeledComponent("IP", ipField)
                .addLabeledComponent("Port", portField)
                .addLabeledComponent("Version", versionField)
                .addLabeledComponent("Group", groupField)
                .getPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

    public MyConfigurableDubboSettings getMyConfigurableDubboSettings() {
        String selectedItem = (String) this.comboBox.getSelectedItem();
        String port = this.portField.getText();
        String version = this.versionField.getText();
        String group = this.groupField.getText();
        String ip = this.ipField.getText();
        String nameField = this.nameField.getText();
        //校验重复
        MyConfigurableDubboSettings configurableDubboSettings = new MyConfigurableDubboSettings();
        configurableDubboSettings.setName(nameField);
        configurableDubboSettings.setProtocol(selectedItem);
        configurableDubboSettings.setIp(ip);
        configurableDubboSettings.setPort(port);
        configurableDubboSettings.setVersion(version);
        configurableDubboSettings.setGroup(group);
        return configurableDubboSettings;
    }

}
