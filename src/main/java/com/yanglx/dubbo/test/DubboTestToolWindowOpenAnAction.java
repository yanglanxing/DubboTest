package com.yanglx.dubbo.test;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.ui.content.Content;
import com.yanglx.dubbo.test.dubbo.DubboMethodEntity;
import com.yanglx.dubbo.test.ui.DubboPanel;
import com.yanglx.dubbo.test.utils.ParamUtil;

import java.util.Objects;

import static com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR;

/**
 * Copyright ©2014-2019 Youzan.com All rights reserved
 * me.wbean.plugin.dubbo.invoker com.intellij.openapi.actionSystem.AnAction
 */
public class DubboTestToolWindowOpenAnAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(EDITOR);

        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        if (!(psiElement instanceof PsiMethod)) {
            Messages.showMessageDialog("only apply on method", "warn", null);
            return;
        }

        PsiMethod psiMethod = (PsiMethod) psiElement;
        PsiParameterList parameterList = psiMethod.getParameterList();

        PsiJavaFile javaFile = (PsiJavaFile) psiMethod.getContainingFile();
        PsiClass psiClass = (PsiClass) psiElement.getParent();

        //服务名称
        String interfaceName = String.format("%s.%s", javaFile.getPackageName(), psiClass.getName());
        //入参类型
        String[] methodType = new String[parameterList.getParameters().length];
        for (int i = 0; i < parameterList.getParameters().length; i++) {
            String canonicalText = parameterList.getParameters()[i].getType().getCanonicalText();
            methodType[i] = canonicalText;
        }
        //入参
        Object[] initParamArray = ParamUtil.getInitParamArray(psiMethod.getParameterList(), psiMethod.getDocComment());
        //接口名称
        String methodName = psiMethod.getName();

        ToolWindow toolWindow = ToolWindowManager
                .getInstance(Objects.requireNonNull(e.getProject()))
                .getToolWindow("DubboTest");
        if (toolWindow != null) {
            // 无论当前状态为关闭/打开，进行强制打开ToolWindow
            toolWindow.show(() -> {

            });
            Content[] contents = toolWindow.getContentManager().getContents();
            if (contents[0].getComponent() instanceof DubboPanel) {
                DubboPanel dubboPanel1 = (DubboPanel) contents[0].getComponent() ;
                DubboMethodEntity dubboMethodEntity = new DubboMethodEntity();
                dubboMethodEntity.setInterfaceName(interfaceName);
                dubboMethodEntity.setParamObj(initParamArray);
                dubboMethodEntity.setMethodType(methodType);
                dubboMethodEntity.setMethodName(methodName);
                DubboPanel.refreshUI(dubboPanel1, dubboMethodEntity);
            }
        }
    }
}
