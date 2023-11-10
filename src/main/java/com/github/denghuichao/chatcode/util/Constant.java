package com.github.denghuichao.chatcode.util;

import com.github.denghuichao.chatcode.settings.OpenAISettingsState;

public class Constant {

    public static final String CHAT_GPT_INFO_CONTENT =
            "### Important Tips:  \n" +
                    "- This chat is powered by the %s model  \n" +
                    "- Please follow the user's manual to setup this plugin before you start. If you have done, just ignore this message.  \n" +
                    "- [User's manual](https://github.com/denghuichao/chatcode/blob/main/README.md/)";

    public static String getChatGptInfoContent() {
        return String.format(CHAT_GPT_INFO_CONTENT, OpenAISettingsState.getInstance().model);
    }
}
