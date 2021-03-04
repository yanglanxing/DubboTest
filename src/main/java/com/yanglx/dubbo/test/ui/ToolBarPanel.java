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

import javax.swing.*;

public class ToolBarPanel extends SimpleToolWindowPanel implements Disposable {

    private ActionManager actionManager;

    private TabBar tabBar;

    private MyDefaultMutableTreeNode leftTree;

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

    public MyDefaultMutableTreeNode getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(MyDefaultMutableTreeNode leftTree) {
        this.leftTree = leftTree;
    }

    public ToolBarPanel(Project project, ToolWindow toolWindow) {
        super(false,true);

        actionManager = ActionManager.getInstance();

        //分割线
        JBSplitter mContentSplitter = new JBSplitter();
        mContentSplitter.setProportion(0.1f);



        //左树结构,默认为收藏
        leftTree = new MyDefaultMutableTreeNode();
        leftTree.createTree(MyDefaultMutableTreeNode.TreeNodeTypeEnum.COLLECTIONS);
        mContentSplitter.setFirstComponent(leftTree);
        //tabBar
        tabBar = new TabBar(project,leftTree);
        mContentSplitter.setSecondComponent(tabBar);

        this.setToolbar(createToolbar());
        this.setContent(mContentSplitter);
    }


    private JComponent createToolbar(){
        AddTabAction addTabAction = new AddTabAction(tabBar);

        HistoryAbstractTabEditorAction abstractTabEditorAction = new HistoryAbstractTabEditorAction(leftTree);
        CollectionsAbstractTabEditorAction collectionsAbstractTabEditorAction = new CollectionsAbstractTabEditorAction(leftTree);

        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(addTabAction);
        actionGroup.add(collectionsAbstractTabEditorAction);
        actionGroup.add(abstractTabEditorAction);
        ActionToolbar actionToolbar = actionManager.createActionToolbar("toolbar", actionGroup, false);
        return actionToolbar.getComponent();
    }

    @Override
    public void dispose() {

    }
}
