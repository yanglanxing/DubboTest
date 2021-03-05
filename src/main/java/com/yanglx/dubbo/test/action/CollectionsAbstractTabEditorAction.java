package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TreePanel;
import org.jetbrains.annotations.NotNull;

public class CollectionsAbstractTabEditorAction extends AbstractTabEditorAction {
    private TreePanel component;

    public CollectionsAbstractTabEditorAction(TreePanel component) {
        super("收藏", "Parses and formats the JSON", AllIcons.Nodes.Folder);
        this.component = component;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        component.createTree(TreePanel.TreeNodeTypeEnum.COLLECTIONS);
    }
}
