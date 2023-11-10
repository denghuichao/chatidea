package com.github.denghuichao.chatcode.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Denghuichao
 * Supports storing the application settings in a persistent way.
 * The {@link com.intellij.openapi.components.State} and {@link com.intellij.openapi.components.Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.github.denghuichao.chatcode.settings.OpenAISettingsState",
        storages = @Storage("ChatCodeSettingsPlugin.xml")
)
public class OpenAISettingsState implements PersistentStateComponent<OpenAISettingsState> {
    public long timeout = 50000L;

    public String apiKey = "NOT SET";
    public String apiHost = "api.openai.com";
    public String model = "gpt-3.5-turbo";

    public boolean enableContext = true;

    public boolean enableAvatar = true;

    public Boolean enableLineWarp = true;
    public String roleText = "You are a helpful coding assistant, you should respond to all messages in the style of a programming expert. All message you response must be formatted as markdown. Also, if there is any code in the message, please put it in a markdown code block format like this: \n\n" +
            "```\nthe code here\n```";

    public String findBugActionName = "Find Bug";
    public String findBugPrompt = "Find bugs in the code below:";
    public String optimizeCodeActionName = "Optimize Code";
    public String optimizeCodePrompt = "Optimize the following code:";
    public String codeReviewActionName = "Code Review";
    public String codeReviewPrompt = "Review following code and point out areas where the code could be improved:";
    public String utActionName = "Add Test Case";
    public String utPrompt = "Add test case for following code:";
    public String addCommentActionName = "Add Comment";
    public String addCommentPrompt = "Add comment for following code:";

    @Tag("customPrompts")
    public Map<String, String> customPrompts = new HashMap<>();

    public static OpenAISettingsState getInstance() {
        return ApplicationManager.getApplication().getService(OpenAISettingsState.class);
    }

    @Nullable
    @Override
    public OpenAISettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull OpenAISettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void reload() {
        loadState(this);
    }
}
