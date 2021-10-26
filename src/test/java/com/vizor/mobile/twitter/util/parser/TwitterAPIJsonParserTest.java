package com.vizor.mobile.twitter.util.parser;

import com.vizor.mobile.twitter.Rule;
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
        System.out.println(rules.isEmpty());
    }
}