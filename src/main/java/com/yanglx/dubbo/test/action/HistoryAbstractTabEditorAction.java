package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TreePanel;
import org.jetbrains.annotations.NotNull;

/**
 * 历史
 */
public class HistoryAbstractTabEditorAction extends AnAction {

    private TreePanel component;

    public HistoryAbstractTabEditorAction(TreePanel component) {
        super("History", "Switch to History", AllIcons.Vcs.History);
        this.component = component;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        component.createTree(TreePanel.TreeNodeTypeEnum.HISTORY);
    }
}
