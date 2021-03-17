package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TreePanel;
import org.jetbrains.annotations.NotNull;

/**
 * 收藏
 */
public class CollectionsAbstractTabEditorAction extends AnAction {
    private TreePanel component;

    public CollectionsAbstractTabEditorAction(TreePanel component) {
        super("Collections", "Switch to collections", AllIcons.Nodes.Folder);
        this.component = component;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.component.refresh(TreePanel.TreeNodeTypeEnum.COLLECTIONS);
    }
}
