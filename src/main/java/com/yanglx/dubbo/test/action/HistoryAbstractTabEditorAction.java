package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TreePanel;
import org.jetbrains.annotations.NotNull;

public class HistoryAbstractTabEditorAction extends AbstractTabEditorAction {

    private TreePanel component;

    public HistoryAbstractTabEditorAction(TreePanel component) {
        super("历史记录", "点击显示历史记录", AllIcons.Vcs.History);
        this.component = component;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        component.createTree(TreePanel.TreeNodeTypeEnum.HISTORY);
    }
}
