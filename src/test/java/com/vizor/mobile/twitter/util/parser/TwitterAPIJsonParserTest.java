package com.vizor.mobile.twitter.util.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwitterAPIJsonParserTest {

    @Test
    void parseResponseWithToken() {
        String json = "{\"token_type\":\"bearer\",\"access_token\":\"TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTO\"}\n";
        String expected = "TOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTOKENTO";
        String actual = TwitterAPIJsonParser.parseJsonWithToken(json);
        assertEquals(expected, actual);
    }
}