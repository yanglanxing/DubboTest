package com.yanglx.dubbo.test.action;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.yanglx.dubbo.test.PluginConstants;
import com.yanglx.dubbo.test.utils.PluginUtils;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Description: alt + enter, 支持接口, 实现类方法, 方法引用 </p>
 * todo-dong4j : (2021.02.20 20:29) [判断是否为 dubbo 接口]
 *
 * @author dong4j
 * @version 1.0.0
 * @email "mailto:dong4j@fkhwl.com"
 * @date 2021.02.20 17:57
 * @since 1.1.0
 */
public class DubboTestIntentionAction extends PsiElementBaseIntentionAction {
    /**
     * Invokes intention action for the element under caret.
     *
     * @param project the project in which the file is opened.
     * @param editor  the editor for the file.
     * @param element the element under cursor.
     * @throws IncorrectOperationException incorrect operation exception
     * @since 1.1.0
     */
    @Override
    public void invoke(@NotNull Project project,
                       Editor editor,
                       @NotNull PsiElement element) throws IncorrectOperationException {
        if (project.isDisposed()) {
            return;
        }
        
        PluginUtils.openToolWindow(project, element);
    }

    /**
     * 检查此意图在文件中的插入符号偏移量是否可用。如果此方法返回true，则显示用于此意图的一个提示.
     *
     * @param project the project in which the availability is checked.
     * @param editor  the editor in which the intention will be invoked.
     * @param element the element under caret.
     * @return true if the intention is available, false otherwise.
     * @since 1.1.0
     */
    @Override
    public boolean isAvailable(@NotNull Project project,
                               Editor editor,
                               @NotNull PsiElement element) {
        return PluginUtils.isAvailable(project, editor, element);
    }

    /**
     * Gets text *
     *
     * @return the text
     * @since 1.1.0
     */
    @Nls
    @NotNull
    @Override
    public String getText() {
        return PluginConstants.PLUGIN_NAME;
    }

    /**
     * Returns the name of the family of intentions. It is used to externalize
     * "auto-show" state of intentions. When the user clicks on a light bulb in intention list,
     * all intentions with the same family name get enabled/disabled.
     * The name is also shown in settings tree.
     *
     * @return the intention family name.
     * @since 1.1.0
     */
    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return PluginConstants.PLUGIN_NAME;
    }
}
