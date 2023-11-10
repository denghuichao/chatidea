package com.github.denghuichao.chatcode.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denghuichao.chatcode.settings.OpenAISettingsState;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;
import java.util.Objects;

/**
 * @author huichaodeng
 * @version 1.0.0
 * @since 2023/05/03 01:21
 */
public class OpenAiServices {
    private static OpenAiService openAiService;

    public static OpenAiService get() {
        if (openAiService == null) {
            synchronized (OpenAiServices.class) {
                if (openAiService == null) {
                    OpenAISettingsState instance = OpenAISettingsState.getInstance();
                    openAiService = new OpenAiService(buildApi(instance.apiHost, instance.apiKey, Duration.ofMillis(instance.timeout)));
                }
            }
        }
        return openAiService;
    }

    public static OpenAiApi buildApi(String apiHost, String apiKey, Duration timeout) {
        Objects.requireNonNull(apiKey, "OpenAI API Key required");
        Objects.requireNonNull(apiHost, "API Host required");
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(apiKey, timeout)
                .newBuilder()
                .build();

        if (!apiHost.startsWith("http")) {
            apiHost = "https://" + apiHost;
        }
        if (!apiHost.endsWith("/")) {
            apiHost = apiHost + "/";
        }
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(apiHost)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return (OpenAiApi) retrofit.create(OpenAiApi.class);
    }
}
