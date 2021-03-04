package com.yanglx.dubbo.test.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.yanglx.dubbo.test.ui.TabBar;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

public class RenameTabAction extends AbstractTabEditorAction{

    private TabBar tabBar;

    public RenameTabAction(TabBar tabBar) {
        super("Rename", "Renames a tab", PluginIcons.DUBBO);
        this.tabBar = tabBar;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }
}
