package com.yanglx.dubbo.test.ui;

import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.table.TableModelEditor;
import com.yanglx.dubbo.test.ui.setting.MyDialogItemEditor;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.List;

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
        browsersTable = new JPanel();
        MyDialogItemEditor myDialogItemEditor = new MyDialogItemEditor(this);

        tableModelEditor = new TableModelEditor<>(COLUMNS,
                myDialogItemEditor,
                "No dubbo configured");
        browsersTable.setLayout(new BorderLayout());
        browsersTable.add(tableModelEditor.createComponent(), BorderLayout.CENTER);
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

    public JComponent getPanel() {
        return browsersTable;
    }
}
