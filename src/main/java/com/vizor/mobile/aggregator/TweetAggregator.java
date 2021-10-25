package com.vizor.mobile.aggregator;

import com.vizor.mobile.twitter.Tweet;

/**
 * Базовый интерфейс для обработки твитов.
 *
 * Используется для того чтобы подписаться на новые твиты в {@link com.vizor.mobile.twitter.TwitterStreamConnector}.
 */
public interface TweetAggregator
{
    void aggregateTweet(Tweet tweet);
}
