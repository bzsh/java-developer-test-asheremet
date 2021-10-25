package com.vizor.mobile.twitter;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class TwitterStreamConnectorImpl implements TwitterStreamConnector{
    @Override
    public void listenStream(List<Rule> ruleList, Consumer<Tweet> streamConsumer) throws IOException, InterruptedException {

    }
}
