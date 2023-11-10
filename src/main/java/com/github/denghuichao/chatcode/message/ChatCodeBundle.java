package com.github.denghuichao.chatcode.message;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;


public class ChatCodeBundle extends DynamicBundle {

    @NonNls
    private static final String BUNDLE = "messages.ChatCodeBundle";
    private static final ChatCodeBundle INSTANCE = new ChatCodeBundle();

    private ChatCodeBundle() {
        super(BUNDLE);
    }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                      Object @NotNull ... params) {
        return INSTANCE.getMessage(key, params);
    }

    @NotNull
    public static Supplier<@Nls String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                                       Object @NotNull ... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
