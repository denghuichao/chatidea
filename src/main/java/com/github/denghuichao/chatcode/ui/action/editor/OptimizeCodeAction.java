package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class OptimizeCodeAction extends AbstractEditorAction {

    public OptimizeCodeAction() {
        super(() -> OpenAISettingsState.getInstance().optimizeCodeActionName);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        key = state.optimizeCodePrompt;
        super.actionPerformed(e);
    }

}
