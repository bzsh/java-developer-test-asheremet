package com.vizor.mobile.twitter.util.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vizor.mobile.twitter.ConfigRule;
import com.vizor.mobile.twitter.Rule;

import java.util.ArrayList;
import java.util.List;

public class TwitterAPIJsonParser {
    public static String parseJsonWithToken(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return jsonObject.get("access_token").getAsString();
    }

    public static List<Rule> parseJsonWithRules(String json) {
        List<Rule> rules = new ArrayList<>();
        if (null != json) {
            JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
            JsonArray array = jsonObject.getAsJsonArray("data");
            if (array != null) {
                for (JsonElement element : array) {
                    JsonObject object = element.getAsJsonObject();
                    ConfigRule configRule = new ConfigRule();
                    configRule.setId(object.get("id").getAsString());
                    configRule.setTag(object.get("tag").getAsString());
                    configRule.setValue(object.get("value").getAsString());
                    rules.add(configRule);
                }
            }
        }
        return rules;
    }

    public static String getAddRulesJson(List<Rule> rules) {
        String add = "{\"add\": [%s]}";
        return getResultJsonString(add, rules);
    }

    public static String getDeleteRulesJson(List<Rule> rules) {
        String delete = "{ \"delete\": { \"ids\": [%s]}}";
        return getResultJsonString(delete, rules);
    }

    private static String getResultJsonString(String format, List<Rule> rules) {
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
}
