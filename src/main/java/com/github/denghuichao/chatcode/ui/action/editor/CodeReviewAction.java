package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class CodeReviewAction extends AbstractEditorAction {

    public CodeReviewAction() {
        super(() -> OpenAISettingsState.getInstance().codeReviewActionName);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        key = state.codeReviewPrompt;
        super.actionPerformed(e);
    }

}
