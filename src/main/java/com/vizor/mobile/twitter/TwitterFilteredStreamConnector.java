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

public class TwitterFilteredStreamConnector implements TwitterStreamConnector{

    private static final String HOST = "https://api.twitter.com";
    private static final String STREAM_ENDPOINT = "/2/tweets/search/stream";
    private static final String RULES_ENDPOINT = "/2/tweets/search/stream/rules";
    private static final String TOKEN_ENDPOINT = "/oauth2/token?grant_type=client_credentials";
    private static final String AUTH_HEADER = "Authorization";
    private static final String CONTENT_HEADER = "Content-Type";
    private static final String PREFIX_BASIC = "Basic ";
    private static final String PREFIX_BEARER = "Bearer ";
    private final String apiKey;
    private final String secretKey;

     TwitterFilteredStreamConnector(String apiKey, String secretKey){
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }
    @Override
    public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) {
       String bearerToken = getBearerToken(apiKey, secretKey);
        setupRules(bearerToken, ruleList);
        connectStream(bearerToken);

    }

    public String getBearerToken(String apiKey, String secretKey) {     //todo
        String responseBody = "";
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(HOST + TOKEN_ENDPOINT))
                .setHeader(CONTENT_HEADER, "application/x-www-form-urlencoded;charset=UTF-8")
                .setHeader(AUTH_HEADER, PREFIX_BASIC + getEncodedCredentials(apiKey, secretKey));
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            responseBody = TwitterAPIJsonParser.parseJsonWithToken(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return responseBody;
    }

    private String getEncodedCredentials(String apiKey, String secretKey) {
        String credentials = apiKey + ":" + secretKey;
        return Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }


    private void setupRules(String bearerToken, List<Rule> rules) {
        List<String> existingRules = getRules(bearerToken);
        if (existingRules.size() > 0) {
            deleteRules(bearerToken, existingRules);
        }
        createRules(bearerToken, rules);
    }

    private void deleteRules(String bearerToken, List<String> existingRules){
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(HOST + TOKEN_ENDPOINT))
                .setHeader(CONTENT_HEADER, "application/x-www-form-urlencoded;charset=UTF-8")
                .setHeader(AUTH_HEADER, PREFIX_BASIC + getEncodedCredentials(apiKey, secretKey));
        try {
            client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createRules(String bearerToken, List<Rule> rules){
         String json = TwitterAPIJsonParser.getAddRulesJson(rules);
        System.out.println("This is a body for POST request : " + json);      //todo
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(HOST + RULES_ENDPOINT))
                .setHeader(CONTENT_HEADER, "application/json")
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
            System.out.println("This is a body : " + response.body());   //todo
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRules(String bearerToken){
        List<String> rules = new ArrayList<>();
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
            System.out.println("This is the body : " + body);            //todo
//           rules = TwitterAPIJsonParser.parseJsonWithRules(body, rules);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rules;
    }

    private void connectStream(String bearerToken){                     // todo ??????????????????????????
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(HOST + STREAM_ENDPOINT))
                .setHeader(CONTENT_HEADER, "application/json")
                .setHeader(AUTH_HEADER, PREFIX_BEARER + bearerToken);
        try {
            HttpResponse<InputStream> responseToStream = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseToStream.body()));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
