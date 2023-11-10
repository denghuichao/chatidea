package com.github.denghuichao.chatcode;

import com.github.denghuichao.chatcode.openai.OpenAiServices;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.github.denghuichao.chatcode.ui.MainPanel;
import com.github.denghuichao.chatcode.ui.MessageComponent;
import com.github.denghuichao.chatcode.ui.MessageGroupComponent;
import com.google.common.collect.Lists;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import groovy.util.logging.Slf4j;

import java.util.List;

@Slf4j
public class GPT35TurboHandler {
    public void handle(MainPanel mainPanel, MessageComponent component, String question) {
        MessageGroupComponent contentPanel = mainPanel.getContentPanel();

        OpenAISettingsState instance = OpenAISettingsState.getInstance();
        OpenAiService openAiService = OpenAiServices.get();
        ChatCompletionRequest.ChatCompletionRequestBuilder requestBuilder = ChatCompletionRequest.builder()
                .stream(false)
                .model(instance.model);

        List<ChatMessage> messages = Lists.newArrayList();

        // Define the default system role
        String role = contentPanel.getSystemRole();
        messages.add(new ChatMessage("system", role));
        ChatMessage currentMessage = new ChatMessage("user", question);
        mainPanel.getContentPanel().getMessages().add(currentMessage);

        if (instance.enableContext) {
            messages.addAll(mainPanel.getContentPanel().getMessages());
        } else {
            messages.add(currentMessage);
        }

        requestBuilder.messages(messages);

        try {
            ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(requestBuilder.build());
            String response = chatCompletionResult.getChoices().get(0).getMessage().getContent();
            //String response = question;
            mainPanel.getContentPanel().getMessages().add(new ChatMessage("assistant", response));
            component.setSourceContent(response);
            component.setContent(response);
            mainPanel.aroundRequest(false);
            component.scrollToBottom();
        } catch (Exception e) {
            component.setSourceContent(e.getMessage());
            component.setContent(e.getMessage());
            mainPanel.aroundRequest(false);
            e.printStackTrace();
        } finally {
            mainPanel.getExecutorService().shutdown();
        }
    }
}
