package com.yanglx.dubbo.test.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.yanglx.dubbo.test.PluginConstants;
import org.jetbrains.annotations.NotNull;

/**
 * 设置
 */
public class SettingAction extends AnAction {
    public SettingAction() {
        super("Default Settings", "Open the setting view", AllIcons.General.Settings);
    }
    @Override
   public void actionPerformed(@NotNull AnActionEvent e) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(),PluginConstants.PLUGIN_NAME);
    }
}
