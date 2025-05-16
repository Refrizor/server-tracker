package me.devin.api;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Object> additionalFields = new HashMap<>();
    private boolean success;
    private String error;

    // Capture all additional fields dynamically
    @JsonAnySetter
    public void setAdditionalField(String key, Object value) {
        additionalFields.put(key, value);
    }

    public Map<String, Object> getAdditionalFields() {
        return additionalFields;
    }

    public <T> T getFieldWithPattern(String baseKey, Class<T> type) {
        String[] possibleKeys = {baseKey, baseKey + "s"};

        for (String key : possibleKeys) {
            if (additionalFields.containsKey(key)) {
                Object field = additionalFields.get(key);

                // Use ObjectMapper to convert to the expected type
                if (field instanceof Map) {
                    return objectMapper.convertValue(field, type);
                }

                // Ensure proper casting to the desired type
                if (type.isInstance(field)) {
                    return type.cast(field);
                }
            }
        }
        throw new IllegalArgumentException("Field not found for key patterns: " + baseKey + " or " + baseKey + "s");
    }

    // Dynamic Field Matching with Generic Support
    public <T> T getFieldWithPattern(String baseKey, TypeReference<T> typeReference) {
        String[] possibleKeys = {baseKey, baseKey + "s"};

        for (String key : possibleKeys) {
            if (additionalFields.containsKey(key)) {
                Object field = additionalFields.get(key);

                // Use ObjectMapper to deserialize into the desired type
                return objectMapper.convertValue(field, typeReference);
            }
        }

        throw new IllegalArgumentException("Field not found for key patterns: " + baseKey + " or " + baseKey + "s");
    }

    // Getters and setters for success and error
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}