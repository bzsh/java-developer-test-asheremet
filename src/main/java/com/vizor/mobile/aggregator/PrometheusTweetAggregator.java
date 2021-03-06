package com.vizor.mobile.aggregator;

import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.Tweet;
import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;


/**
 * Собирает информацию о полученных твитах в Prometheus метрики.
 * <p>
 * Часть метрик маркируется тэгом, ассоциированным с совпавшим с твитом {@link Rule#getTag()}. Для таких метрик считаем,
 * что если твит подпадает под несколько правил сразу, то обновляем метрики на каждое совпавшее правило.
 * <p>
 * Класс создает и настраивает несколько стандартных метрик:
 * <ul>
 *     <li>twitter_stream_tweet_count_total -- количество обработанных твитов. Счетчик, подсчитываем вне зависимости
 *     от тэга </li>
 *     <li>twitter_stream_tweet_length_characters -- количество символов в твите. Гистограмма, подсчитываем в
 *     рамках тэга</li>
 *     <li>twitter_stream_tweet_length_words -- количество слов в твите. Гистограмма, подсчитываем в рамках тэга</li>
 * </ul>
 * <p>
 * Для метрики twitter_stream_tweet_count_total значение должно совпадать с количеством вызовов метода
 * {@link PrometheusTweetAggregator#aggregateTweet(Tweet)}.
 */
public class PrometheusTweetAggregator implements TweetAggregator {
    /**
     * Метрика, показывающая количество обработанных {@link Tweet}
     */
    private final Counter tweetsTotal;
    /**
     * Метрика, подсчитывающая распределение длинны текста в Tweet в рамках одного {@link Rule#getTag()}
     */
    private final Histogram tweetsLengthCharacters;
    /**
     * Метрика, подсчитывающая распределение количества слов в тексте Tweet в рамках одного {@link Rule#getTag()}. Для
     * подсчета используется функция {@link TextUtils#wordCount(String)}.
     */
    private final Histogram tweetsLengthWords;

    PrometheusTweetAggregator() {
        tweetsTotal = Counter.build()
                .help("Total tweets processed by topic")
                .namespace("twitter_stream")
                .name("tweet_count_total")
                .create().register();

        tweetsLengthCharacters = Histogram.build()
                .help("Total tweets in characters by topic")
                .namespace("twitter_stream")
                .name("tweet_length_characters")
                .labelNames("tag")
                .create().register();

        tweetsLengthWords = Histogram.build()
                .help("Tweet length in words by topic")
                .namespace("twitter_stream")
                .name("tweet_length_words")
                .labelNames("tag")
                .create().register();
    }

    @Override
    public void aggregateTweet(Tweet tweet) {
        countTweets();
        for (Rule rule : tweet.getMatchingRules()) {
            calculateTweetsLengthCharacters(tweet.getText(), rule.getTag());
            calculateTweetsLengthWords(tweet.getText(), rule.getTag());
        }
    }

    private void countTweets() {
        tweetsTotal.inc();
    }

    private void calculateTweetsLengthWords(String text, String tag) {
        Histogram.Timer requestTimer = tweetsLengthWords.labels(tag).startTimer();
        try {
            tweetsLengthWords.labels(tag).observe(TextUtils.wordCount(text));
        } finally {
            requestTimer.observeDuration();
        }
    }

    private void calculateTweetsLengthCharacters(String text, String tag) {
        Histogram.Timer requestTimer = tweetsLengthCharacters.labels(tag).startTimer();
        try {
            tweetsLengthCharacters.labels(tag).observe(TextUtils.charsCount(text));
        } finally {
            requestTimer.observeDuration();
        }
    }
}
