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

/**
 * <p>Description: </p>
 *
 * @author yanglx
 * @version 1.0.0
 * @email "mailto:dev_ylx@163.com"
 * @date 2021.02.20 15:57
 * @since 1.0.0
 */
public class JsonEditor extends NonOpaquePanel {
    /** serialVersionUID */
    private static final long serialVersionUID = -871105152589937225L;
    /** Psi file */
    private final PsiFile psiFile;
    /** Project */
    private final Project project;

    /**
     * Json editor
     *
     * @param project project
     * @since 1.0.0
     */
    public JsonEditor(Project project) {
        this.project = project;
        this.psiFile = this.createPsiFile();
        VirtualFile virtualFile = this.psiFile.getVirtualFile();
        FileEditor fileEditor = this.createFileEditor(virtualFile);
        this.add(fileEditor.getComponent(), "Center");
    }

    /**
     * Create file editor
     *
     * @param virtualFile virtual file
     * @return the file editor
     * @since 1.0.0
     */
    private FileEditor createFileEditor(VirtualFile virtualFile) {
        return PsiAwareTextEditorProvider.getInstance().createEditor(this.project, virtualFile);
    }

    /**
     * Get document text
     *
     * @return the string
     * @since 1.0.0
     */
    public String getDocumentText() {
        Document document = this.getDocument();
        return document.getText();
    }

    /**
     * Get document
     *
     * @return the document
     * @since 1.0.0
     */
    public Document getDocument() {
        PsiDocumentManager instance = PsiDocumentManager.getInstance(this.project);
        return instance.getDocument(this.psiFile);
    }

    /**
     * Create psi file
     *
     * @return the psi file
     * @since 1.0.0
     */
    private PsiFile createPsiFile() {
        JsonFileType fileType = JsonFileType.INSTANCE;
        PsiFile psiFile = PsiFileFactory.getInstance(this.project)
            .createFileFromText("tmp." + fileType.getDefaultExtension()
                , fileType.getLanguage()
                , "{}"
                , true
                , false);
        psiFile.putUserData(Key.create("JSON_HELPER"), "TEST");
        return psiFile;
    }
}
