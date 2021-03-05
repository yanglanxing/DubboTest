package com.yanglx.dubbo.test.ui;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class Tab extends JPanel {

    private String id;

    private DubboPanel dubboPanel;

    public Tab(@NotNull Project project, @NotNull String id, @NotNull TreePanel leftTree) {
        dubboPanel = new DubboPanel(project, leftTree);
        this.setLayout(new BorderLayout());
        this.add(dubboPanel, BorderLayout.CENTER, 0);
        this.id = id;
    }

    public final String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DubboPanel getDubboPanel() {
        return dubboPanel;
    }

    public void setDubboPanel(DubboPanel dubboPanel) {
        this.dubboPanel = dubboPanel;
    }
}
