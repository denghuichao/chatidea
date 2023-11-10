package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class AddCommentAction extends AbstractEditorAction {

    public AddCommentAction() {
        super(() -> OpenAISettingsState.getInstance().addCommentActionName);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        key = state.addCommentPrompt;
        super.actionPerformed(e);
    }

}
