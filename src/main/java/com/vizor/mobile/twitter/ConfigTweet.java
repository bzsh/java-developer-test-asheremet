package com.vizor.mobile.twitter;

import java.util.List;
import java.util.Objects;

public class ConfigTweet implements Tweet{
    private String text;
    private List<Rule> matchingRules;
    @Override
    public String getText() {
        return null;
    }

    @Override
    public List<Rule> getMatchingRules() {
        return null;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMatchingRules(List<Rule> matchingRules) {
        this.matchingRules = matchingRules;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigTweet that = (ConfigTweet) o;
        return Objects.equals(text, that.text) && Objects.equals(matchingRules, that.matchingRules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, matchingRules);
    }

    @Override
    public String toString() {
        return "ConfigTweet{" +
                "text='" + text + '\'' +
                ", matchingRules=" + matchingRules +
                '}';
    }
}
