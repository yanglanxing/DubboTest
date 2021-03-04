package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.treeStructure.Tree;
import com.yanglx.dubbo.test.ui.MyDefaultMutableTreeNode;
import org.jetbrains.annotations.NotNull;

public class CollectionsAbstractTabEditorAction extends AbstractTabEditorAction {
    private MyDefaultMutableTreeNode component;

    public CollectionsAbstractTabEditorAction(MyDefaultMutableTreeNode component) {
        super("收藏", "Parses and formats the JSON", AllIcons.Nodes.Folder);
        this.component = component;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        component.createTree(MyDefaultMutableTreeNode.TreeNodeTypeEnum.COLLECTIONS);
    }
}
