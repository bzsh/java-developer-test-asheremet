package com.vizor.mobile.twitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;


class TwitterFilteredStreamConnectorTest {
    private static List<Rule> rulesFromFile;
    String apiKey = "zL55lyOjjpRVkJNzXYvbPuNsS";
    String secretKey = "K5ADN3YAgnVOZKXb7HZkYdtBZadMUXmAiiI5ND1e80Znwoq6cs";
    String bearerToken = "AAAAAAAAAAAAAAAAAAAAAOskVAEAAAAALQ948IiuroOpdocf7fKsAYt04Kg%3DQ5VaeaEtwWvJVECIwp4I2UQjieqnTR5a3q8vUnR15WcwEAdVaD";
    TwitterFilteredStreamConnector connector = new TwitterFilteredStreamConnector(apiKey, secretKey);

    @BeforeAll
    static void getRulesFromFile() {
        List<Rule> rules;
        try (InputStream rulesStream = Main.class.getResourceAsStream("/rules.json")) {
            Type type = new TypeToken<List<ConfigRule>>() {
            }.getType();
            InputStreamReader reader = new InputStreamReader(rulesStream);
            rules = new Gson().fromJson(reader, type);
            rulesFromFile = rules;
        } catch (IOException e) {
            System.exit(2);
        }
    }

    @Test
    void getToken() {
        String token = connector.getBearerToken(apiKey, secretKey);
    }

    @Test
    void getRules() {
        List<Rule> ruleList = connector.getRules(bearerToken);
    }

    @Test
    void deleteRules() {
        List<Rule> ruleList = connector.getRules(bearerToken);
        connector.deleteRules(bearerToken, ruleList);
    }

    @Test
    void createRules() {
        connector.createRules(bearerToken, rulesFromFile);
    }

    @Test
    void connectToStream() {
        connector.connectStream(bearerToken);
    }
}