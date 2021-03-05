package com.yanglx.dubbo.test.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class NameDialogue extends DialogWrapper {

    private JTextField editor;

    public NameDialogue(@NotNull Project project) {
        super(project);
        this.init();
        this.setTitle("Save to...");
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel dialoguePanel = new JPanel((LayoutManager)new BorderLayout());
        this.editor = new JTextField();
        dialoguePanel.add(this.editor, BorderLayout.CENTER);
        return dialoguePanel;
    }

    public String getText() {
        return this.editor.getText();
    }
}
