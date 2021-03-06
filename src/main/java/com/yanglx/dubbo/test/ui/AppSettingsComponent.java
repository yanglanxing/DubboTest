package com.yanglx.dubbo.test.ui;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class AppSettingsComponent {
    private final JPanel myMainPanel;
    private final JBTextField addressField = new JBTextField();
    private final JBTextField versionField = new JBTextField();
    private final JBTextField groupField = new JBTextField();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Address: "), addressField, 1, false)
                .addLabeledComponent(new JBLabel("Version: "), versionField, 1, false)
                .addLabeledComponent(new JBLabel("Group: "), groupField, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return addressField;
    }

    public String getAddressFieldText() {
        return addressField.getText();
    }

    public String getVersionFieldText() {
        return versionField.getText();
    }

    public String getGroupFieldText() {
        return groupField.getText();
    }

    public void setAddressField(@NotNull String str){
        this.addressField.setText(str);
    }
    public void setVersionField(String str){
        this.versionField.setText(str);
    }
    public void setGroupField(String str){
        this.groupField.setText(str);
    }
}
