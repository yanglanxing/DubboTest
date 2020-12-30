package com.yanglx.dubbo.test.ui;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.dubbo.DubboApiLocator;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import com.yanglx.dubbo.test.utils.PluginUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DubboPanel extends JPanel {

    private JPanel mainPanel;

    private JButton button1;
    private JTextField interfaceNameTextField;
    private JPanel reqPane;
    private JPanel respPane;
    private JLabel tip;
    private JButton saveBtn;
    private JButton delBtn;
    private JComboBox<String> addressBox;
    private JTextField methodNameTextField;
    private JTextField versionTextField;
    private JsonEditor jsonEditorReq;
    private JsonEditor jsonEditorResp;
    private Project myProject;

    private DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();

    public DubboPanel(Project project, ToolWindow toolWindow) {
        this.myProject = project;
        this.setLayout(new BorderLayout());
        this.add(mainPanel, "Center", 0);

        jsonEditorReq = new JsonEditor(project);
        reqPane.add(jsonEditorReq, "Center", 0);

        jsonEditorResp = new JsonEditor(project);
        respPane.add(jsonEditorResp, "Center", 0);

        DubboSetingState instance = DubboSetingState.getInstance();
        for (String address : instance.getAddress()) {
            addressBox.addItem(address);
        }

        saveBtn.addActionListener(e -> {
            String selectedItem = (String) addressBox.getSelectedItem();
            instance.getAddress().add(selectedItem);
            addressBox.addItem(selectedItem);
        });

        delBtn.addActionListener(e -> {
            String selectedItem = (String) addressBox.getSelectedItem();
            instance.getAddress().remove(selectedItem);
            addressBox.removeItem(selectedItem);
        });

        button1.addActionListener(e -> {
            refreshDubboMethodEntity();
            PluginUtils.writeDocument(myProject, jsonEditorResp.getDocument(), "");
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Object> submit = executorService.submit(() -> {
                try {
                    tip.setText("正在请求...");
                    tip.updateUI();
                    long start = System.currentTimeMillis();
                    DubboApiLocator dubboApiLocator = new DubboApiLocator();
                    Object invoke = dubboApiLocator.invoke(dubboMethodEntity);
                    PluginUtils.writeDocument(myProject,
                            jsonEditorResp.getDocument(),
                            JSON.toJSONString(invoke, SerializerFeature.PrettyFormat));
                    long end = System.currentTimeMillis();
                    tip.setText("耗时:" + (end - start));
                    tip.updateUI();
                    return invoke;
                } catch (Exception e1) {
                    tip.setText("错误:" + e1.getMessage());
                    tip.updateUI();
                }
                return new Object();
            });
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        submit.get(4000, TimeUnit.MILLISECONDS);
                    } catch (Exception ignored) {
                        tip.setText("请求超时...");
                        tip.updateUI();
                    }
                }
            });

        });
    }

    public void refreshDubboMethodEntity() {
        JTextField interfaceName = getInterfaceNameTextField();
        JTextField methodName = getMethodNameTextField();
        JTextField versionTextField = getVersionTextField();
        JComboBox address = getAddressBox();
        JsonEditor jsonEditorReq = getJsonEditorReq();
        dubboMethodEntity.setMethodName(methodName.getText());
        dubboMethodEntity.setInterfaceName(interfaceName.getText());
        dubboMethodEntity.setAddress((String) address.getSelectedItem());
        dubboMethodEntity.setVersion(versionTextField.getText());
        if (jsonEditorReq.getDocumentText() != null
                && jsonEditorReq.getDocumentText().length() > 0) {
            JSONObject jsonObject = JSON.parseObject(jsonEditorReq.getDocumentText());
            JSONArray methodTypeArray = jsonObject.getJSONArray("methodType");
            if (methodTypeArray != null) {
                List<String> strings = methodTypeArray.toJavaList(String.class);
                dubboMethodEntity.setMethodType(strings.toArray(new String[strings.size()]));
            } else {
                dubboMethodEntity.setMethodType(new String[]{});
            }
            JSONArray paramArray = jsonObject.getJSONArray("param");
            if (paramArray != null) {
                dubboMethodEntity.setParamObj(paramArray.toArray());
            } else {
                dubboMethodEntity.setParamObj(new Object[]{});
            }
        } else {
            dubboMethodEntity.setParamObj(new Object[]{});
            dubboMethodEntity.setMethodType(new String[]{});
        }
    }

    /**
     * 刷新UI
     *
     * @param dubboPanel
     * @param dubboMethodEntity
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

    public JTextField getVersionTextField() {
        return versionTextField;
    }

    public void setVersionTextField(JTextField versionTextField) {
        this.versionTextField = versionTextField;
    }

    public JComboBox getAddressBox() {
        return addressBox;
    }

    public void setAddressBox(JComboBox addressBox) {
        this.addressBox = addressBox;
    }

    public Project getProject() {
        return myProject;
    }

    public void setProject(Project project) {
        this.myProject = project;
    }

    public JsonEditor getJsonEditorReq() {
        return jsonEditorReq;
    }

    public void setJsonEditorReq(JsonEditor jsonEditorReq) {
        this.jsonEditorReq = jsonEditorReq;
    }

    public JsonEditor getJsonEditorResp() {
        return jsonEditorResp;
    }

    public void setJsonEditorResp(JsonEditor jsonEditorResp) {
        this.jsonEditorResp = jsonEditorResp;
    }

    public JTextField getInterfaceNameTextField() {
        return interfaceNameTextField;
    }

    public void setInterfaceNameTextField(JTextField interfaceNameTextField) {
        this.interfaceNameTextField = interfaceNameTextField;
    }

    public JTextField getMethodNameTextField() {
        return methodNameTextField;
    }

    public void setMethodNameTextField(JTextField methodNameTextField) {
        this.methodNameTextField = methodNameTextField;
    }

    public JPanel getReqPane() {
        return reqPane;
    }

    public void setReqPane(JPanel reqPane) {
        this.reqPane = reqPane;
    }

    public JPanel getRespPane() {
        return respPane;
    }

    public void setRespPane(JPanel respPane) {
        this.respPane = respPane;
    }


}
