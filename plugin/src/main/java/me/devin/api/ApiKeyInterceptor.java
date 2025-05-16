package me.devin.api;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ApiKeyInterceptor implements Interceptor {

    private final String apiKey;

    @Inject
    public ApiKeyInterceptor(@Named("ApiKey") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request requestWithApiKey = original.newBuilder()
                .header("x-api-key", apiKey)
                .build();
        return chain.proceed(requestWithApiKey);
    }
}
