package com.yanglx.dubbo.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class AbstractTabEditorAction extends AnAction {

    public AbstractTabEditorAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

}
