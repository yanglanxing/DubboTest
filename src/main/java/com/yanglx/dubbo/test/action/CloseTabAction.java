package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.yanglx.dubbo.test.ui.TabBar;
import org.jetbrains.annotations.NotNull;

public class CloseTabAction extends AbstractTabEditorAction {

    private TabBar tabBar;
    private String tabId;

    public CloseTabAction(TabBar tabBar, String id) {
        super("Close tab", "Closes a tab", AllIcons.Actions.Close);
        this.tabBar = tabBar;
        this.tabId = id;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ActionManager actionManager = e.getActionManager();
        System.out.println("actionManager = " + actionManager);
        e.getProject();
        tabBar.closeTab(tabId);
    }

    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setIcon(AllIcons.Actions.Close);

        //鼠标悬停图标
        Presentation presentation1 = e.getPresentation();
        presentation1.setHoveredIcon(AllIcons.Actions.CloseHovered);
    }
}
