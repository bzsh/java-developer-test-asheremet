package com.vizor.mobile.twitter.util.parser;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.vizor.mobile.twitter.ConfigRule;
import com.vizor.mobile.twitter.ConfigTweet;
import com.vizor.mobile.twitter.Rule;
import com.vizor.mobile.twitter.Tweet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TwitterAPIJsonParser {
    public static String parseJsonWithToken(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    public static List<Rule> parseJsonWithRules(String json) {
        List<Rule> rules = new ArrayList<>();
        if (null != json) {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray arr = jsonObject.getAsJsonArray("data");
            Type type = new TypeToken<List<ConfigRule>>() {
            }.getType();
            rules = new Gson().fromJson(arr, type);
        }
        return rules;
    }

    public static Tweet parseJsonWithTweet(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        JsonArray rulesObject = jsonObject.getAsJsonArray("matching_rules");
        Type type = new TypeToken<List<ConfigRule>>() {
        }.getType();
        List<Rule> rules = new Gson().fromJson(rulesObject, type);
        ConfigTweet tweet = new Gson().fromJson(dataObject, ConfigTweet.class);
        tweet.setMatchingRules(rules);
        return tweet;
    }

    public static String getAddRulesJson(List<Rule> rules) {
        String format = "{\"add\": [%s]}";
        if (rules.size() == 1) {
            String value = rules.get(0).getValue();
            String tag = rules.get(0).getTag();
            return String.format(format, "{\"value\": \"" + value + "\", \"tag\": \"" + tag + "\"}");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Rule rule : rules) {
                String value = rule.getValue();
                String tag = rule.getTag();
                sb.append("{\"value\": \"").append(value).append("\", \"tag\": \"").append(tag).append("\"}").append(",");
            }
            String result = sb.toString();
            return String.format(format, result.substring(0, result.length() - 1));
        }
    }

    public static String getDeleteRulesJson(List<Rule> rules) {
        String format = "{\"delete\": { \"ids\": [%s]}}";
        if (rules.size() == 1) {
            Optional<String> value = rules.get(0).getId();
            String id = value.orElse("");
            return String.format(format, "\"" + id + "\"");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Rule rule : rules) {
                Optional<String> value = rule.getId();
                String id = value.orElse("");
                sb.append("\"").append(id).append("\"").append(",");
            }
            String result = sb.toString();
            return String.format(format, result.substring(0, result.length() - 1));
        }
    }
}
