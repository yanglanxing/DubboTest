package com.yanglx.dubbo.test.ui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBPanel;
import com.yanglx.dubbo.test.CacheInfo;
import com.yanglx.dubbo.test.DubboSetingState;
import com.yanglx.dubbo.test.dubbo.DubboApiLocator;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import com.yanglx.dubbo.test.utils.JsonUtils;
import com.yanglx.dubbo.test.utils.PluginUtils;
import com.yanglx.dubbo.test.utils.StrUtils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
public class DubboPanel extends JBPanel {

    private static final long serialVersionUID = -8541227582365214834L;
    /**
     * Main panel
     */
    private JPanel mainPanel;
    /**
     * Button 1
     */
    private JButton invokeBtn;
    /**
     * Interface name text field
     */
    private JTextField interfaceNameTextField;
    /** Req pane */
//    private JPanel reqPane;
    /** Resp pane */
//    private JPanel respPane;
    /**
     * Tip
     */
    private JLabel tip;
    /**
     * Save btn
     */
    private JButton saveAsBtn;
    /**
     * Address box
     */
    private JComboBox<CacheInfo> addressBox;
    /**
     * Method name text field
     */
    private JTextField methodNameTextField;
    /**
     * Version text field
     */
    private JTextField versionTextField;
    /**
     * group text field
     */
    private JTextField groupTextField;
    private JButton saveBtn;
    private JPanel editorPane;
    /**
     * Json editor req
     */
    private JsonEditor jsonEditorReq;
    /**
     * Json editor resp
     */
    private JsonEditor jsonEditorResp;
    /**
     * My project
     */
    private Project project;

    /**
     * Dubbo method entity
     */
    private final DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JButton getInvokeBtn() {
        return invokeBtn;
    }

    public void setInvokeBtn(JButton invokeBtn) {
        this.invokeBtn = invokeBtn;
    }

    public JTextField getInterfaceNameTextField() {
        return interfaceNameTextField;
    }

    public void setInterfaceNameTextField(JTextField interfaceNameTextField) {
        this.interfaceNameTextField = interfaceNameTextField;
    }

    public JLabel getTip() {
        return tip;
    }

    public void setTip(JLabel tip) {
        this.tip = tip;
    }

    public JButton getSaveAsBtn() {
        return saveAsBtn;
    }

    public void setSaveAsBtn(JButton saveAsBtn) {
        this.saveAsBtn = saveAsBtn;
    }

    public JComboBox<CacheInfo> getAddressBox() {
        return addressBox;
    }

    public void setAddressBox(JComboBox<CacheInfo> addressBox) {
        this.addressBox = addressBox;
    }

    public JTextField getMethodNameTextField() {
        return methodNameTextField;
    }

    public void setMethodNameTextField(JTextField methodNameTextField) {
        this.methodNameTextField = methodNameTextField;
    }

    public JTextField getVersionTextField() {
        return versionTextField;
    }

    public void setVersionTextField(JTextField versionTextField) {
        this.versionTextField = versionTextField;
    }

    public JTextField getGroupTextField() {
        return groupTextField;
    }

    public void setGroupTextField(JTextField groupTextField) {
        this.groupTextField = groupTextField;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public void setSaveBtn(JButton saveBtn) {
        this.saveBtn = saveBtn;
    }

    public JPanel getEditorPane() {
        return editorPane;
    }

    public void setEditorPane(JPanel editorPane) {
        this.editorPane = editorPane;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DubboMethodEntity getDubboMethodEntity() {
        return dubboMethodEntity;
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    /**
     * Dubbo panel
     *
     * @param project project
     * @since 1.0.0
     */
    public DubboPanel(Project project, TreePanel leftTree) {

        this.project = project;
        this.setLayout(new BorderLayout());
        this.add(this.mainPanel, BorderLayout.CENTER);

        //分割线
        JBSplitter mContentSplitter = new JBSplitter();
        mContentSplitter.setProportion(0.5f);
        this.jsonEditorReq = new JsonEditor(project);
        this.jsonEditorReq.setBorder(BorderFactory.createTitledBorder("Params"));
        this.jsonEditorResp = new JsonEditor(project);
        this.jsonEditorResp.setBorder(BorderFactory.createTitledBorder("Response"));
        mContentSplitter.setFirstComponent(jsonEditorReq);
        mContentSplitter.setSecondComponent(jsonEditorResp);
        this.editorPane.add(mContentSplitter, BorderLayout.CENTER);

        //初始化数据
        this.reset();

        //给定一个收藏名称进行收藏
        this.saveAsBtn.addActionListener(e -> {
            DubboSetingState instance = DubboSetingState.getInstance();
            this.refreshDubboMethodEntity();
            if (isBlankEntity()) {
                return;
            }
            NameDialogue dialogue = new NameDialogue(project);
            dialogue.show();
            if (dialogue.isOK()) {
                String name = dialogue.getText();
                if (StrUtils.isBlank(name)) {
                    name = this.dubboMethodEntity.getInterfaceName() + "#" + this.dubboMethodEntity.getMethodName();
                }
                String id = StrUtils.isBlank(this.dubboMethodEntity.getId()) ? UUID.randomUUID().toString() : this.dubboMethodEntity.getId();
                CacheInfo of = CacheInfo.of(id, name, this.dubboMethodEntity);
                instance.add(of, DubboSetingState.CacheType.COLLECTIONS);
                //刷新左边树结构
                leftTree.refresh();
            }
        });
        //默认使用接口名和方法名作为收藏名进行收藏
        saveBtn.addActionListener(e -> {
            DubboSetingState instance = DubboSetingState.getInstance();
            this.refreshDubboMethodEntity();
            if (isBlankEntity()) {
                return;
            }
            String name = this.dubboMethodEntity.getMethodName() + "#" + this.dubboMethodEntity.getInterfaceName();
            String id = StrUtils.isBlank(this.dubboMethodEntity.getId()) ? UUID.randomUUID().toString() : this.dubboMethodEntity.getId();
            CacheInfo of = CacheInfo.of(id, name, this.dubboMethodEntity);
            instance.add(of, DubboSetingState.CacheType.COLLECTIONS);
            //刷新左边树结构
            leftTree.refresh();
        });

        //执行dubbo请求
        this.invokeBtn.addActionListener(e -> {
            this.refreshDubboMethodEntity();
            if (isBlankEntity()) {
                return;
            }
            //添加历史
            DubboSetingState instance = DubboSetingState.getInstance();
            String id = UUID.randomUUID().toString();
            String name = this.dubboMethodEntity.getMethodName() + "#" + this.dubboMethodEntity.getInterfaceName();
            CacheInfo of = CacheInfo.of(id, name, this.dubboMethodEntity);
            instance.add(of, DubboSetingState.CacheType.HISTORY);
            //刷新左边树结构
            leftTree.refresh();
            //清空数据
            PluginUtils.writeDocument(this.project, this.jsonEditorResp.getDocument(), "");
            //异步处理
            Future<Object> submit = executorService.submit(() -> {
                DubboApiLocator dubboApiLocator = new DubboApiLocator();
                Object invoke = dubboApiLocator.invoke(dubboMethodEntity);
                return invoke;
            });
            //异步获取返回值，设置超时时间
            executorService.submit(() -> {
                tip.setText("Requesting...");
                tip.updateUI();
                long start = System.currentTimeMillis();
                try {
                    Object invoke = submit.get(10, TimeUnit.SECONDS);

                    PluginUtils.writeDocument(this.project,
                            this.jsonEditorResp.getDocument(),
                            JsonUtils.toJSONString(invoke));

                    long end = System.currentTimeMillis();
                    this.tip.setText("time:" + (end - start));
                    this.tip.updateUI();
                } catch (TimeoutException timeoutException) {
//                    DubboPanel.this.tip.setText(DubboTestBundle.message("dubbo-test.invoke.timeout.tootip"));
                    tip.setText("Timeout...");
                    tip.updateUI();
                    submit.cancel(true);
                } catch (ExecutionException | InterruptedException exception) {
                    String replaceAll = exception.getLocalizedMessage().replaceAll("\r\n", "\n");
                    PluginUtils.writeDocument(this.project,
                            this.jsonEditorResp.getDocument(),
                            replaceAll);
                }
            });


        });

        //下拉
        addressBox.addItemListener(e -> {
            CacheInfo item = (CacheInfo) e.getItem();
            versionTextField.setText(item.getVersion());
            groupTextField.setText(item.getGroup());
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
        JComboBox<CacheInfo> addressBox = this.getAddressBox();
        JsonEditor jsonEditorReq = this.getJsonEditorReq();
        this.dubboMethodEntity.setMethodName(methodName.getText());
        this.dubboMethodEntity.setInterfaceName(interfaceName.getText());
        CacheInfo selectedItem = (CacheInfo) addressBox.getSelectedItem();
        this.dubboMethodEntity.setAddress(selectedItem.getAddress());
        this.dubboMethodEntity.setVersion(versionTextField.getText());
        this.dubboMethodEntity.setGroup(groupTextField.getText());
        if (jsonEditorReq.getDocumentText() != null
                && jsonEditorReq.getDocumentText().length() > 0) {
            DubboMethodEntity dubboMethodEntity = JsonUtils.toJava(jsonEditorReq.getDocumentText(), DubboMethodEntity.class);
            this.dubboMethodEntity.setMethodType(dubboMethodEntity.getMethodType());
            this.dubboMethodEntity.setParam(dubboMethodEntity.getParam());
        } else {
            this.dubboMethodEntity.setParam(new Object[]{});
            this.dubboMethodEntity.setMethodType(new String[]{});
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
        dubboPanel.dubboMethodEntity.setId(dubboMethodEntity.getId());
        JTextField textField1 = dubboPanel.getInterfaceNameTextField();
        JTextField textField2 = dubboPanel.getMethodNameTextField();
        JsonEditor jsonEditorReq = dubboPanel.getJsonEditorReq();
        textField1.setText(dubboMethodEntity.getInterfaceName());
        textField2.setText(dubboMethodEntity.getMethodName());

        JComboBox<CacheInfo> textField3 = dubboPanel.getAddressBox();
        for (int i = 0; i < textField3.getItemCount(); i++) {
            CacheInfo itemAt = textField3.getItemAt(i);
            String item = itemAt.getAddress() + itemAt.getVersion() + itemAt.getGroup();
            String item1 = dubboMethodEntity.getAddress() + dubboMethodEntity.getVersion() + dubboMethodEntity.getGroup();
            if (item.equals(item1)) {
                textField3.setSelectedIndex(i);
            }
        }

        JTextField textField4 = dubboPanel.getGroupTextField();
        textField4.setText(dubboMethodEntity.getGroup());
        JTextField textField5 = dubboPanel.getVersionTextField();
        textField5.setText(dubboMethodEntity.getVersion());

        Map<String, Object> map = new HashMap<>();
        map.put("param", dubboMethodEntity.getParam());
        map.put("methodType", dubboMethodEntity.getMethodType());

        PluginUtils.writeDocument(dubboPanel.getProject(),
                jsonEditorReq.getDocument(),
                JsonUtils.toPrettyJSONString(map));
        dubboPanel.updateUI();
    }

    public void reset() {
        DubboSetingState settings = DubboSetingState.getInstance();
        List<CacheInfo> dubboConfigs = settings.getDubboConfigs();
        this.addressBox.removeAllItems();
        for (CacheInfo dubboConfig : dubboConfigs) {
            this.addressBox.addItem(dubboConfig);
        }
    }

    private boolean isBlankEntity() {
        return StrUtils.isBlank(dubboMethodEntity.getMethodName())
                || StrUtils.isBlank(this.dubboMethodEntity.getInterfaceName());
    }
}
