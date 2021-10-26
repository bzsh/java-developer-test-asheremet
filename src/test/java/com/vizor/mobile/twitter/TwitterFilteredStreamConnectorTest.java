package com.vizor.mobile.twitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.Main;
import com.vizor.mobile.aggregator.PrometheusTweetAggregator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TwitterFilteredStreamConnectorTest {
    String apiKey = "zL55lyOjjpRVkJNzXYvbPuNsS";
    String secretKey = "K5ADN3YAgnVOZKXb7HZkYdtBZadMUXmAiiI5ND1e80Znwoq6cs";

    TwitterFilteredStreamConnector connector = new TwitterFilteredStreamConnector(apiKey, secretKey);

    @Test
    void printKeys(){
        System.out.println(apiKey);
        System.out.println(secretKey);
    }


    @Test
    void getRules() {
       List<Rule> ruleList =  connector.getRules("AAAAAAAAAAAAAAAAAAAAAOskVAEAAAAALQ948IiuroOpdocf7fKsAYt04Kg%3DQ5VaeaEtwWvJVECIwp4I2UQjieqnTR5a3q8vUnR15WcwEAdVaD");
        System.out.println("List with rules length is : " + ruleList.size());
        System.out.println("Is list is empty ? : " + ruleList.isEmpty());
        for (Rule r : ruleList) {
            System.out.println(r.getId());
            System.out.println(r.getTag());
            System.out.println(r.getValue());
        }
    }

    @Test
    void getToken() {
       String token =  connector.getBearerToken(apiKey, secretKey);
        System.out.println(token);
    }

    @Test
    void createRules() {
        List<Rule> rules = new ArrayList<>();
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAAOskVAEAAAAALQ948IiuroOpdocf7fKsAYt04Kg%3DQ5VaeaEtwWvJVECIwp4I2UQjieqnTR5a3q8vUnR15WcwEAdVaD";
        try(InputStream rulesStream = Main.class.getResourceAsStream("/rules.json"))
        {
            Type type = new TypeToken<List<ConfigRule>>(){}.getType();
            InputStreamReader reader = new InputStreamReader(rulesStream);
            rules = new Gson().fromJson(reader, type);
        }
        catch (IOException e)
        {
            System.exit(2);
        }
        connector.createRules(bearerToken, rules);
    }
}