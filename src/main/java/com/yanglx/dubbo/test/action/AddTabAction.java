package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TabBar;
import org.jetbrains.annotations.NotNull;

/**
 * 添加tab
 */
public class AddTabAction extends AnAction {

    private TabBar tabBar;

    public AddTabAction(TabBar tabBar) {
        super("Add Tab", "Adds a tab", AllIcons.General.Add);
        this.tabBar = tabBar;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        this.tabBar.addTab();
    }
}
