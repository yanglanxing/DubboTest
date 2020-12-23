package com.yanglx.dubbo.test.ui;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.components.panels.NonOpaquePanel;

import java.awt.*;

public class JsonEditor extends NonOpaquePanel {
    private final PsiFile psiFile;
    private final FileEditor fileEditor;
    private final Project project;

    public JsonEditor(Project project) {
        this.project = project;
        this.psiFile = createPsiFile();
        VirtualFile virtualFile = this.psiFile.getVirtualFile();
        this.fileEditor = createFileEditor(virtualFile);
        add((Component) this.fileEditor.getComponent(), "Center");
    }

    private FileEditor createFileEditor(VirtualFile virtualFile) {
        return PsiAwareTextEditorProvider.getInstance().createEditor(this.project, virtualFile);
    }

    public String getDocumentText(){
        Document document = getDocument();
        return document.getText();
    }

    public Document getDocument(){
        PsiDocumentManager instance = PsiDocumentManager.getInstance(this.project);
        Document document = instance.getDocument(this.psiFile);
        return document;
    }

    private PsiFile createPsiFile() {
        JsonFileType fileType = JsonFileType.INSTANCE;
        PsiFile psiFile = PsiFileFactory.getInstance(this.project)
                .createFileFromText("tmp." + fileType.getDefaultExtension()
                        , fileType.getLanguage()
                        , (CharSequence) ""
                        , true
                        , false);
        psiFile.putUserData(Key.create("JSON_HELPER"), "TEST");
        return psiFile;
    }
}
