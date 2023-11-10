package com.github.denghuichao.chatcode.ui.action;

import com.github.denghuichao.chatcode.GPT35TurboHandler;
import com.github.denghuichao.chatcode.message.ChatCodeBundle;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.github.denghuichao.chatcode.ui.MainPanel;
import com.github.denghuichao.chatcode.ui.MessageComponent;
import com.github.denghuichao.chatcode.ui.MessageGroupComponent;
import com.github.denghuichao.chatcode.util.StringUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

import static com.github.denghuichao.chatcode.ChatCodeToolWindowFactory.ACTIVE_CONTENT;


public class SendAction extends AnAction {

    private static final Logger LOG = LoggerFactory.getLogger(SendAction.class);

    private String data;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Object mainPanel = project.getUserData(ACTIVE_CONTENT);
        doActionPerformed((MainPanel) mainPanel, data, "");
    }

    private boolean presetCheck() {
        OpenAISettingsState instance = OpenAISettingsState.getInstance();
        if (StringUtil.isEmpty(instance.apiKey)) {
            Notifications.Bus.notify(
                    new Notification(ChatCodeBundle.message("group.id"),
                            "Wrong setting",
                            "Please configure a API Key first.",
                            NotificationType.ERROR));
            return false;
        }
        if (StringUtil.isEmpty(instance.apiHost)) {
            Notifications.Bus.notify(
                    new Notification(ChatCodeBundle.message("group.id"),
                            "Wrong setting",
                            "Please configure API Host first.",
                            NotificationType.ERROR));
            return false;
        }

        return true;
    }

    public void doActionPerformed(MainPanel mainPanel, String data, String fileName) {
        // Filter the empty text
        if (StringUtils.isEmpty(data)) {
            return;
        }

        // Check the configuration first
        if (!presetCheck()) {
            return;
        }

        // Reset the question container
        mainPanel.getSearchTextArea().getTextArea().setText("");
        mainPanel.aroundRequest(true);
        Project project = mainPanel.getProject();
        MessageGroupComponent contentPanel = mainPanel.getContentPanel();

        // Add the message component to container
        MessageComponent question = new MessageComponent(data, true, fileName);
        MessageComponent answer = new MessageComponent("Waiting for response...", false, fileName);
        contentPanel.add(question);
        contentPanel.add(answer);

        try {
            ExecutorService executorService = mainPanel.getExecutorService();
            // Request the server.

            GPT35TurboHandler gpt35TurboHandler = project.getService(GPT35TurboHandler.class);
            executorService.submit(() -> {
                gpt35TurboHandler.handle(mainPanel, answer, data);
                contentPanel.updateLayout();
                contentPanel.scrollToBottom();
            });
        } catch (Exception e) {
            answer.setSourceContent(e.getMessage());
            answer.setContent(e.getMessage());
            mainPanel.aroundRequest(false);
            LOG.error("ChatGPT: Request failed, error={}", e.getMessage());
        }
    }
}
