package com.github.denghuichao.chatcode.ui.action.editor;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;


public class FindBugAction extends AbstractEditorAction {

    public FindBugAction() {
        super(() -> OpenAISettingsState.getInstance().findBugActionName);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        OpenAISettingsState state = OpenAISettingsState.getInstance();
        key = state.findBugPrompt;
        super.actionPerformed(e);
    }
}
