package com.github.denghuichao.chatcode;

import com.github.denghuichao.chatcode.message.ChatCodeBundle;
import com.github.denghuichao.chatcode.ui.MainPanel;
import com.github.denghuichao.chatcode.ui.action.GitHubAction;
import com.github.denghuichao.chatcode.ui.action.HelpAction;
import com.github.denghuichao.chatcode.ui.action.SettingAction;
import com.github.denghuichao.chatcode.ui.action.SupportAction;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Denghuichao
 */
public class ChatCodeToolWindowFactory implements ToolWindowFactory {

    public static final Key ACTIVE_CONTENT = Key.create("ActiveContent");
    public static final String GPT35_TRUBO_CONTENT_NAME = "ChatCode";

    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        MainPanel mainPanel = new MainPanel(project);
        Content gpt35Turbo = contentFactory.createContent(mainPanel.init(), GPT35_TRUBO_CONTENT_NAME, false);
        gpt35Turbo.setCloseable(false);

        toolWindow.getContentManager().addContent(gpt35Turbo);
        project.putUserData(ACTIVE_CONTENT, mainPanel);

        List<AnAction> actionList = new ArrayList<>();
        actionList.add(new HelpAction());
        actionList.add(new SettingAction(ChatCodeBundle.message("action.settings")));
        actionList.add(new GitHubAction());
        actionList.add(new SupportAction());
        toolWindow.setTitleActions(actionList);
    }
}
