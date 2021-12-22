package com.yanglx.dubbo.test.ui;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.Function;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.table.TableModelEditor;
import com.yanglx.dubbo.test.common.AddressTypeEnum;
import com.yanglx.dubbo.test.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.List;
import java.util.UUID;

import static com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo;

public class AppSettingsComponent {

    private JComponent browsersTable;

    private TableModelEditor<MyConfigurableDubboSettings> tableModelEditor;

    /**
     * 表格
     */
    private static final ColumnInfo[] COLUMNS = {
            new EditableColumnInfo<MyConfigurableDubboSettings, String>("name") {
                @Override
                public String valueOf(MyConfigurableDubboSettings item) {
                    return item.getName();
                }

                @Override
                public void setValue(MyConfigurableDubboSettings item, String value) {
                    item.setName(value);
                }

                @Override
                public boolean isCellEditable(MyConfigurableDubboSettings myConfigurableDubboSettings) {
                    return false;
                }
            },
            new EditableColumnInfo<MyConfigurableDubboSettings, String>("Address") {
                @Override
                public String valueOf(MyConfigurableDubboSettings item) {
                    return item.getProcessedAddress();
                }

                @Override
                public void setValue(MyConfigurableDubboSettings item, String value) {
                    item.setAddress(value);
                }

                @Override
                public boolean isCellEditable(MyConfigurableDubboSettings myConfigurableDubboSettings) {
                    return false;
                }
            },
            new EditableColumnInfo<MyConfigurableDubboSettings, String>("Version") {
                @Override
                public String valueOf(MyConfigurableDubboSettings item) {
                    return item.getVersion();
                }

                @Override
                public void setValue(MyConfigurableDubboSettings item, String value) {
                    item.setVersion(value);
                }

                @Override
                public boolean isCellEditable(MyConfigurableDubboSettings myConfigurableDubboSettings) {
                    return false;
                }
            },
            new EditableColumnInfo<MyConfigurableDubboSettings, String>("Group") {
                @Override
                public String valueOf(MyConfigurableDubboSettings item) {
                    return item.getGroup();
                }

                @Override
                public void setValue(MyConfigurableDubboSettings item, String value) {
                    item.setGroup(value);
                }

                @Override
                public boolean isCellEditable(MyConfigurableDubboSettings myConfigurableDubboSettings) {
                    return false;
                }
            }};

    public AppSettingsComponent() {
        TableModelEditor.DialogItemEditor<MyConfigurableDubboSettings> itemEditor = new TableModelEditor.DialogItemEditor<MyConfigurableDubboSettings>() {
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

            @Override
            public void edit(@NotNull MyConfigurableDubboSettings item, @NotNull Function<MyConfigurableDubboSettings, MyConfigurableDubboSettings> mutator, boolean isAdd) {
                //对应工具栏得添加或者编辑按钮事件
                MyConfigurableDubboSettings settings = openDialog(item);
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
        };
        tableModelEditor = new TableModelEditor<>(COLUMNS,
                itemEditor,
                "No dubbo configured");

        browsersTable = tableModelEditor.createComponent();
    }

    /**
     * 配置是否有变更
     *
     * @return
     */
    public boolean isModified() {
        return this.tableModelEditor.isModified();
    }

    /**
     * 传入配置数据集用于重置配置
     *
     * @param list
     */
    public void reset(List<MyConfigurableDubboSettings> list) {
        this.tableModelEditor.reset(list);
    }

    /**
     * 返回配置数据集
     *
     * @return
     */
    public List<MyConfigurableDubboSettings> getSettings() {
        return tableModelEditor.apply();
    }

    /**
     * 创建一个弹窗
     * @param browser
     * @return
     */
    private MyConfigurableDubboSettings openDialog(MyConfigurableDubboSettings browser) {
        JComboBox<String> comboBox = new ComboBox<>();
        AddressTypeEnum[] values = AddressTypeEnum.values();
        for (AddressTypeEnum value : values) {
            comboBox.addItem(value.name());
        }
        if (StringUtils.isNotBlank(browser.getProtocol())) {
            comboBox.setSelectedItem(browser.getProtocol());
        } else {
            comboBox.setSelectedItem(AddressTypeEnum.zookeeper.name());
        }
        JBTextField nameField = new JBTextField(browser.getName());
        JBTextField ipField = new JBTextField(browser.getIp());
        JBTextField portField = new JBTextField(browser.getPort());
        JBTextField versionField = new JBTextField(browser.getVersion());
        JBTextField groupField = new JBTextField(browser.getGroup());
        JPanel panel = FormBuilder.createFormBuilder()
                .addLabeledComponent("Name", nameField)
                .addLabeledComponent("Type", comboBox)
                .addLabeledComponent("IP", ipField)
                .addLabeledComponent("Port", portField)
                .addLabeledComponent("Version", versionField)
                .addLabeledComponent("Group", groupField)
                .getPanel();
        final DialogBuilder dialogBuilder = new DialogBuilder(browsersTable)
                .title("Dubbo Setting").centerPanel(panel);
//                        dialogBuilder.setPreferredFocusComponent(pluginChooser);
        if (dialogBuilder.show() == DialogWrapper.OK_EXIT_CODE) {

            String selectedItem = (String) comboBox.getSelectedItem();
            String port = portField.getText();
            String version = versionField.getText();
            String group = groupField.getText();
            String ip = ipField.getText();

            //校验重复
            MyConfigurableDubboSettings configurableDubboSettings = new MyConfigurableDubboSettings();
            configurableDubboSettings.setProtocol(selectedItem);
            configurableDubboSettings.setIp(ip);
            configurableDubboSettings.setPort(port);
            configurableDubboSettings.setVersion(version);
            configurableDubboSettings.setGroup(group);
            if (isExist(configurableDubboSettings)) {
                JLabel jLabel = new JLabel("Data duplication");
                DialogBuilder msgDialog = new DialogBuilder(browsersTable)
                        .title("Dubbo Setting").centerPanel(jLabel);
                msgDialog.show();
                return null;
            }

            browser.setProtocol(selectedItem);
            browser.setPort(port);
            browser.setIp(ip);
            browser.setVersion(version);
            browser.setGroup(group);
            browser.setName(nameField.getText());
            if (StringUtils.isNotBlank(browser.getProcessedAddress())) {
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
        List<MyConfigurableDubboSettings> settings1 = getSettings();
        for (MyConfigurableDubboSettings dubboSettings : settings1) {
            String item = dubboSettings.getProcessedAddress() + dubboSettings.getVersion() + dubboSettings.getGroup();
            String item2 = settings.getProcessedAddress() + settings.getVersion() + settings.getGroup();
            if (item.equals(item2)) {
                return true;
            }
        }
        return false;
    }

    public JComponent getPanel() {
        return browsersTable;
    }
}
