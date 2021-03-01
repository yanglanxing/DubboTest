package com.yanglx.dubbo.test.ui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.DubboTestBundle;
import com.yanglx.dubbo.test.dubbo.DubboApiLocator;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import com.yanglx.dubbo.test.utils.PluginUtils;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
@Getter
@Setter
public class DubboPanel extends JPanel {

    private static final long serialVersionUID = -8541227582365214834L;
    /** Main panel */
    private JPanel mainPanel;
    /** Button 1 */
    private JButton button1;
    /** Interface name text field */
    private JTextField interfaceNameTextField;
    /** Req pane */
    private JPanel reqPane;
    /** Resp pane */
    private JPanel respPane;
    /** Tip */
    private JLabel tip;
    /** Save btn */
    private JButton saveBtn;
    /** Del btn */
    private JButton delBtn;
    /** Address box */
    private JComboBox<String> addressBox;
    /** Method name text field */
    private JTextField methodNameTextField;
    /** Version text field */
    private JTextField versionTextField;
    /** group text field */
    private JTextField groupTextField;
    /** Json editor req */
    private JsonEditor jsonEditorReq;
    /** Json editor resp */
    private JsonEditor jsonEditorResp;
    /** My project */
    private Project project;

    /** Dubbo method entity */
    private final DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();

    /**
     * Dubbo panel
     *
     * @param project    project
     * @param toolWindow tool window
     * @since 1.0.0
     */
    public DubboPanel(Project project, ToolWindow toolWindow) {
        this.project = project;
        this.setLayout(new BorderLayout());
        this.add(this.mainPanel, "Center", 0);

        this.jsonEditorReq = new JsonEditor(project);
        this.reqPane.add(this.jsonEditorReq, "Center", 0);

        this.jsonEditorResp = new JsonEditor(project);
        this.respPane.add(this.jsonEditorResp, "Center", 0);

        DubboSetingState instance = DubboSetingState.getInstance();
        for (String address : instance.getAddress()) {
            this.addressBox.addItem(address);
        }

        this.saveBtn.addActionListener(e -> {
            String selectedItem = (String) this.addressBox.getSelectedItem();
            instance.getAddress().add(selectedItem);
            this.addressBox.addItem(selectedItem);
        });

        this.delBtn.addActionListener(e -> {
            String selectedItem = (String) this.addressBox.getSelectedItem();
            instance.getAddress().remove(selectedItem);
            this.addressBox.removeItem(selectedItem);
        });

        this.button1.addActionListener(e -> {
            this.refreshDubboMethodEntity();
            PluginUtils.writeDocument(this.project, this.jsonEditorResp.getDocument(), "");
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Object> submit = executorService.submit(() -> {
                try {
                    this.tip.setText(DubboTestBundle.message("dubbo-test.invokeing.tootip"));
                    this.tip.updateUI();
                    long start = System.currentTimeMillis();
                    DubboApiLocator dubboApiLocator = new DubboApiLocator();
                    Object invoke = dubboApiLocator.invoke(this.dubboMethodEntity);
                    PluginUtils.writeDocument(this.project,
                                              this.jsonEditorResp.getDocument(),
                                              JSON.toJSONString(invoke, SerializerFeature.PrettyFormat));
                    long end = System.currentTimeMillis();
                    this.tip.setText("耗时:" + (end - start));
                    this.tip.updateUI();
                    return invoke;
                } catch (Exception e1) {
                    this.tip.setText("错误:" + e1.getMessage());
                    this.tip.updateUI();
                }
                return new Object();
            });
            executorService.execute(() -> {
                try {
                    submit.get(4000, TimeUnit.MILLISECONDS);
                } catch (Exception ignored) {
                    DubboPanel.this.tip.setText(DubboTestBundle.message("dubbo-test.invoke.timeout.tootip"));
                    DubboPanel.this.tip.updateUI();
                }
            });

        });
    }

    /**
     * Refresh dubbo method entity
     *
     * @since 1.0.0
     */
    public void refreshDubboMethodEntity() {
        JTextField interfaceName = this.getInterfaceNameTextField();
        JTextField methodName = this.getMethodNameTextField();
        JTextField versionTextField = this.getVersionTextField();
        JTextField groupTextField = this.getGroupTextField();
        JComboBox<String> address = this.getAddressBox();
        JsonEditor jsonEditorReq = this.getJsonEditorReq();
        this.dubboMethodEntity.setMethodName(methodName.getText());
        this.dubboMethodEntity.setInterfaceName(interfaceName.getText());
        this.dubboMethodEntity.setAddress((String) address.getSelectedItem());
        this.dubboMethodEntity.setVersion(versionTextField.getText());
        this.dubboMethodEntity.setGroup(groupTextField.getText());
        if (jsonEditorReq.getDocumentText() != null
            && jsonEditorReq.getDocumentText().length() > 0) {
            JSONObject jsonObject = JSON.parseObject(jsonEditorReq.getDocumentText());
            JSONArray methodTypeArray = jsonObject.getJSONArray("methodType");
            if (methodTypeArray != null) {
                List<String> strings = methodTypeArray.toJavaList(String.class);
                this.dubboMethodEntity.setMethodType(strings.toArray(new String[0]));
            } else {
                this.dubboMethodEntity.setMethodType(new String[] {});
            }
            JSONArray paramArray = jsonObject.getJSONArray("param");
            if (paramArray != null) {
                this.dubboMethodEntity.setParamObj(paramArray.toArray());
            } else {
                this.dubboMethodEntity.setParamObj(new Object[] {});
            }
        } else {
            this.dubboMethodEntity.setParamObj(new Object[] {});
            this.dubboMethodEntity.setMethodType(new String[] {});
        }
    }

    /**
     * 刷新UI
     *
     * @param dubboPanel        dubbo panel
     * @param dubboMethodEntity dubbo method entity
     * @since 1.0.0
     */
    public static void refreshUI(DubboPanel dubboPanel, DubboMethodEntity dubboMethodEntity) {
        JTextField textField1 = dubboPanel.getInterfaceNameTextField();
        JTextField textField2 = dubboPanel.getMethodNameTextField();
        JsonEditor jsonEditorReq = dubboPanel.getJsonEditorReq();
        textField1.setText(dubboMethodEntity.getInterfaceName());
        textField2.setText(dubboMethodEntity.getMethodName());

        Map<String, Object> map = new HashMap<>();
        map.put("param", dubboMethodEntity.getParamObj());
        map.put("methodType", dubboMethodEntity.getMethodType());

        PluginUtils.writeDocument(dubboPanel.getProject(),
                                  jsonEditorReq.getDocument(),
                                  JSON.toJSONString(map, SerializerFeature.PrettyFormat));
        dubboPanel.updateUI();
    }

}
