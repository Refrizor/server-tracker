package me.devin.api;

import me.devin.exceptions.ApiClientException;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.CompletableFuture;

public class RetrofitFutureAdapter {
    public static <T> CompletableFuture<T> callAsync(Call<T> call) {
        String requestName = getCallingMethodName();
        CompletableFuture<T> future = new CompletableFuture<>();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<T> call, @NotNull Response<T> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        future.complete(response.body());
                    } else {
                        // Handle Void responses
                        future.complete(null);
                    }
                } else {
                    String errorMessage = String.format(
                            "Request failed for %s: %d - %s (Headers: %s, Body: %s)",
                            requestName,
                            response.code(),
                            response.message(),
                            response.headers(),
                            response.errorBody() != null ? response.errorBody().toString() : "null"
                    );
                    future.completeExceptionally(new ApiClientException(errorMessage, null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<T> call, @NotNull Throwable throwable) {
                future.completeExceptionally(new ApiClientException(
                        String.format("Network error (%s)", requestName), throwable));
            }
        });
        return future;
    }

    public static <T, R> CompletableFuture<R> callAsyncWithApiResponse(
            Call<ResponseWrapper<T>> call,
            Class<R> fieldClass,
            String fieldName
    ) {
        String requestName = getCallingMethodName();
        CompletableFuture<R> future = new CompletableFuture<>();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<ResponseWrapper<T>> call, @NotNull Response<ResponseWrapper<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseWrapper<T> apiResponse = response.body();

                    if (apiResponse.isSuccess()) {
                        try {
                            // Extract the desired field using the field name or pattern
                            R result = apiResponse.getFieldWithPattern(fieldName, fieldClass);
                            if (result != null) {
                                future.complete(result);
                            } else {
                                future.completeExceptionally(new ApiClientException(
                                        String.format("Field '%s' not found in the response for %s", fieldName, requestName), null));
                            }
                        } catch (Exception e) {
                            future.completeExceptionally(new ApiClientException(
                                    String.format("Failed to parse field '%s' for %s: %s", fieldName, requestName, e.getMessage()), e));
                        }
                    } else {
                        future.completeExceptionally(new ApiClientException(
                                String.format("API response indicates failure for %s: %s", requestName, apiResponse.getError()), null));
                    }
                } else {
                    String errorMessage = String.format(
                            "Request failed for %s: %d - %s (Headers: %s, Body: %s)",
                            requestName,
                            response.code(),
                            response.message(),
                            response.headers(),
                            response.errorBody() != null ? response.errorBody().toString() : "null"
                    );
                    future.completeExceptionally(new ApiClientException(errorMessage, null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseWrapper<T>> call, @NotNull Throwable throwable) {
                future.completeExceptionally(new ApiClientException(
                        String.format("Network error (%s)", requestName), throwable));
            }
        });
        return future;
    }

    private static String getCallingMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // stackTrace[2] gives the direct caller of this method
        // Adjust the index if needed depending on the stack depth
        return stackTrace[3].getMethodName();
    }
}