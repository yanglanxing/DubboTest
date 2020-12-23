package com.yanglx.dubbo.test;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.yanglx.dubbo.test.ui.DubboPanel;
import org.jetbrains.annotations.NotNull;

public class DubboPanelToolWindow implements ToolWindowFactory {



    @Override
    public void createToolWindowContent(@NotNull Project project,
                                        @NotNull ToolWindow toolWindow) {
        DubboPanel dubboPanel = new DubboPanel(project,toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(dubboPanel, null, false);
        toolWindow.getContentManager().addContent(content);
    }
}
