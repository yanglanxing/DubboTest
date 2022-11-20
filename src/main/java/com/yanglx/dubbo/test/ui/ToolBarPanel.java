package com.yanglx.dubbo.test.ui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.yanglx.dubbo.test.action.AddTabAction;
import com.yanglx.dubbo.test.action.CollectionsAbstractTabEditorAction;
import com.yanglx.dubbo.test.action.HistoryAbstractTabEditorAction;
import com.yanglx.dubbo.test.action.SettingAction;

import javax.swing.JComponent;

public class ToolBarPanel extends SimpleToolWindowPanel implements Disposable {

    private ActionManager actionManager;

    private TabBar tabBar;

    private TreePanel leftTree;

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public TabBar getTabBar() {
        return tabBar;
    }

    public void setTabBar(TabBar tabBar) {
        this.tabBar = tabBar;
    }

    public TreePanel getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(TreePanel leftTree) {
        this.leftTree = leftTree;
    }

    public ToolBarPanel(Project project, ToolWindow toolWindow) {
        super(false, true);

        this.actionManager = ActionManager.getInstance();

        //分割线
        JBSplitter mContentSplitter = new JBSplitter();
        mContentSplitter.setProportion(0.1f);

        //左树结构,默认为收藏
        this.leftTree = new TreePanel(TreePanel.TreeNodeTypeEnum.COLLECTIONS);
        this.leftTree.refresh();
        mContentSplitter.setFirstComponent(this.leftTree);
        //tabBar
        this.tabBar = new TabBar(project,this.leftTree);
        mContentSplitter.setSecondComponent(this.tabBar);

        this.leftTree.setTabBar(tabBar);
        this.setToolbar(createToolbar());
        this.setContent(mContentSplitter);
    }


    private JComponent createToolbar() {
        AddTabAction addTabAction = new AddTabAction(this.tabBar);

        HistoryAbstractTabEditorAction abstractTabEditorAction = new HistoryAbstractTabEditorAction(this.leftTree);
        CollectionsAbstractTabEditorAction collectionsAbstractTabEditorAction = new CollectionsAbstractTabEditorAction(this.leftTree);
        SettingAction settingAction = new SettingAction();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(addTabAction);
        actionGroup.add(collectionsAbstractTabEditorAction);
        actionGroup.add(abstractTabEditorAction);
        actionGroup.add(settingAction);
        ActionToolbar actionToolbar = this.actionManager.createActionToolbar("toolbar", actionGroup, false);
        actionToolbar.setTargetComponent(this.tabBar);
        return actionToolbar.getComponent();
    }

    @Override
    public void dispose() {

    }
}
