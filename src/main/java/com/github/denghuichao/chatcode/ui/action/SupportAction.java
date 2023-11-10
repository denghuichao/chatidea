package com.github.denghuichao.chatcode.ui.action;

import com.github.denghuichao.chatcode.ui.SupportDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import static com.github.denghuichao.chatcode.icons.ChatCodeIcons.SUPPORT;

/**
 * @author huichaodeng
 * @version 1.0.0
 * @since 2023/11/09 18:45
 */
public class SupportAction extends DumbAwareAction {

    public SupportAction() {
        super(() -> "Support this plugin", SUPPORT);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new SupportDialog(null).show();
    }
}
