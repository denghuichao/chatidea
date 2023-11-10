package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class AddUtAction extends AbstractEditorAction {

    public AddUtAction() {
        super(() -> OpenAISettingsState.getInstance().utActionName);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        key = state.utPrompt;
        super.actionPerformed(e);
    }

}
