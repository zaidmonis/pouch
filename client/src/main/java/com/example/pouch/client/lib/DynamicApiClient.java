package com.example.pouch.client.lib;

import com.example.pouch.client.request.GetUserRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DynamicApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public DynamicApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T send(GetUserRequest request, Class<T> expectedResponseModelClass)
            throws IOException, InterruptedException {
        List<String> fields = resolveFields(expectedResponseModelClass);
        String fieldQuery = fields.stream()
                .map(value -> URLEncoder.encode(value, StandardCharsets.UTF_8))
                .collect(Collectors.joining(","));

        String url = "http://localhost:8080/users/" + request.userId() + "?fields=" + fieldQuery;
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Role", Objects.requireNonNullElse(request.role(), "USER"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new ApiException(response.statusCode(), response.body());
        }
        return objectMapper.readValue(response.body(), expectedResponseModelClass);
    }

    private List<String> resolveFields(Class<?> modelClass) {
        if (modelClass.isRecord()) {
            List<String> fields = new ArrayList<>();
            for (RecordComponent component : modelClass.getRecordComponents()) {
                ApiField apiField = component.getAnnotation(ApiField.class);
                fields.add(apiField != null ? apiField.value() : component.getName());
            }
            return fields;
        }

        List<String> fields = new ArrayList<>();
        for (Field field : modelClass.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
                continue;
            }
            ApiField apiField = field.getAnnotation(ApiField.class);
            fields.add(apiField != null ? apiField.value() : field.getName());
        }
        return fields;
    }
}
