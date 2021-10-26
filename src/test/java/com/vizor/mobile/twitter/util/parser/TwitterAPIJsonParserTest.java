package com.vizor.mobile.twitter.util.parser;

import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.Tweet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwitterAPIJsonParserTest {

    @Test
    void parseResponseWithToken() {
        String json = "{\"token_type\":\"bearer\",\"access_token\":\"TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTO\"}\n";
        String expected = "TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTO";
        String actual = TwitterAPIJsonParser.parseJsonWithToken(json);
        assertEquals(expected, actual);
    }

    @Test
    void parseJsonWithRules() {
        String json = "{\"data\": [{\"value\": \"cat has:images\",\"tag\": \"cats with images\",\"id\": \"1273026480692322304\"}],\"meta\": {\"sent\": \"2020-06-16T22:55:39.356Z\",\"summary\": {\"created\": 1,\"not_created\": 0,\"valid\": 1,\"invalid\": 0}}}";
        List<Rule> rules = TwitterAPIJsonParser.parseJsonWithRules(json);
        assertNotNull(rules);
    }

    @Test
    void parseJsonWithDeleteRules() {
        String json = "{\"data\":[{\"id\":\"1452949583244431375\",\"value\":\"cat\",\"tag\":\"cats\"}],\"meta\":{\"sent\":\"2021-10-26T10:49:33.519Z\",\"result_count\":1}}";
        String expected = "{\"delete\": { \"ids\": [\"1452949583244431375\"]}}";
        List<Rule> rules = TwitterAPIJsonParser.parseJsonWithRules(json);
        String actual = TwitterAPIJsonParser.getDeleteRulesJson(rules);
        assertEquals(expected, actual);

    }

    @Test
    void parseJsonWithAddRules() {
        String json = "{\"data\":[{\"id\":\"1452949583244431375\",\"value\":\"cat\",\"tag\":\"cats\"}],\"meta\":{\"sent\":\"2021-10-26T10:49:33.519Z\",\"result_count\":1}}";
        String expected = "{\"add\": [{\"value\": \"cat\", \"tag\": \"cats\"}]}";
        List<Rule> rules = TwitterAPIJsonParser.parseJsonWithRules(json);
        String actual = TwitterAPIJsonParser.getAddRulesJson(rules);
        assertEquals(expected, actual);
    }

    @Test
    void parseJsonWithTweet() {
        String json = "{\"data\":{\"id\":\"1452990823231102979\",\"text\":\"RT @kunsnem: https://t.co/1GHdoPpQ66\"},\"matching_rules\":[{\"id\":\"1452968013381226500\",\"tag\":\"cats\"}]}";
        Tweet actual = TwitterAPIJsonParser.parseJsonWithTweet(json);
        assertNotNull(actual);
    }
}