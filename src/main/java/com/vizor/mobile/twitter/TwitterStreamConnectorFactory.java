package com.vizor.mobile.twitter;

public class TwitterStreamConnectorFactory {


    public TwitterStreamConnector createConnector(String apiKey, String secretKey) {
        return new TwitterFilteredStreamConnector(apiKey, secretKey);
    }
}
