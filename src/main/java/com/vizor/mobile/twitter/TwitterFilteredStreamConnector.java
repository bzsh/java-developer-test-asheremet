package com.vizor.mobile.twitter;

import com.vizor.mobile.twitter.util.parser.TwitterAPIJsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

public class TwitterFilteredStreamConnector implements TwitterStreamConnector {

    private static final String HOST = "https://api.twitter.com";
    private static final String STREAM_ENDPOINT = "/2/tweets/search/stream";
    private static final String RULES_ENDPOINT = "/2/tweets/search/stream/rules";
    private static final String TOKEN_ENDPOINT = "/oauth2/token?grant_type=client_credentials";
    private static final String AUTH_HEADER = "Authorization";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String CONTENT_HEADER_VALUE = "application/json";
    private static final String PREFIX_BASIC = "Basic ";
    private static final String PREFIX_BEARER = "Bearer ";
    private final String apiKey;
    private final String secretKey;

    TwitterFilteredStreamConnector(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Override
    public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) {
        String bearerToken = getBearerToken(apiKey, secretKey);
        setupRules(bearerToken, ruleList);
        connectStream(bearerToken);
    }

    public String getBearerToken(String apiKey, String secretKey) {
        String token = "";
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(HOST + TOKEN_ENDPOINT))
                .setHeader(CONTENT_HEADER, CONTENT_HEADER_VALUE)
                .setHeader(AUTH_HEADER, PREFIX_BASIC + getEncodedCredentials(apiKey, secretKey));
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            token = TwitterAPIJsonParser.parseJsonWithToken(response.body());
            System.out.println("This is response body, getBearerToken() method: " + response.body());                         //todo
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return token;
    }

    private String getEncodedCredentials(String apiKey, String secretKey) {
        String credentials = apiKey + ":" + secretKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }


    public void setupRules(String bearerToken, List<Rule> rules) {
        List<Rule> existingRules = getRules(bearerToken);
        if (!existingRules.isEmpty()) {
            deleteRules(bearerToken, existingRules);
        }
        createRules(bearerToken, rules);
    }

    public void deleteRules(String bearerToken, List<Rule> rules) {
        String json = TwitterAPIJsonParser.getDeleteRulesJson(rules);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(HOST + RULES_ENDPOINT))
                .setHeader(CONTENT_HEADER, CONTENT_HEADER_VALUE)
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            System.out.println("This is response body, deleteRules() method: " + response.body());                         //todo
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createRules(String bearerToken, List<Rule> rules) {
        String json = TwitterAPIJsonParser.getAddRulesJson(rules);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(HOST + RULES_ENDPOINT))
                .setHeader(CONTENT_HEADER, CONTENT_HEADER_VALUE)
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            System.out.println("This is response body, createRules() method: " + response.body());                         //todo
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Rule> getRules(String bearerToken) {
        List<Rule> rules = new ArrayList<>();
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST + RULES_ENDPOINT))
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            System.out.println("This is response body, getRules() method: " + body);                                       //todo
            rules = TwitterAPIJsonParser.parseJsonWithRules(body);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public void connectStream(String bearerToken) {              // todo ??????????????????????????
        int lineCounter = 1;
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST + STREAM_ENDPOINT))
                .setHeader(CONTENT_HEADER, CONTENT_HEADER_VALUE)
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<InputStream> responseToStream = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseToStream.body()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);                                          //todo
                if (lineCounter == 20) {
                    return;
                }
                lineCounter++;
                line = reader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
