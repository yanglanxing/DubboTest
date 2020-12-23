package com.yanglx.dubbo.test.utils;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;

public class PluginUtils {

    /**
     * 写入文本
     * @param project
     * @param document
     * @param text
     */
    public static void writeDocument(Project project, Document document, String text) {
        WriteCommandAction.runWriteCommandAction(project, (Runnable) new Runnable() {
            public final void run() {
                document.setText(text);
            }
        });
    }

}
