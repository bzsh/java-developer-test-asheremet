package com.vizor.mobile.twitter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

public class TwitterStreamConnectorFactory {
    private static final String HOST = "https://api.twitter.com";
    private static final String STREAM_ENDPOINT = "/2/tweets/search/stream";
    private static final String RULES_ENDPOINT = "/2/tweets/search/stream/rules";
    private static final String TOKEN_ENDPOINT = "/oauth2/token?grant_type=client_credentials";
    private static final String AUTH_HEADER = "Authorization";
    private static final String PREFIX_BASIC = "Basic ";
    private static final String PREFIX_BEARER = "Bearer ";
    private String tokenResponse = "";
    private String bearerToken;

    public TwitterStreamConnector createConnector(String apiKey, String secretKey) {
        return null;
    }

    public String getBearerToken(String apiKey, String secretKey) {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                        .uri(URI.create(HOST + TOKEN_ENDPOINT))
                .setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .setHeader(AUTH_HEADER, PREFIX_BASIC + getEncodedCredentials(apiKey, secretKey));
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            tokenResponse = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return tokenResponse;
    }


    private String getEncodedCredentials(String apiKey, String secretKey) {
        String credentials = apiKey + ":" + secretKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
