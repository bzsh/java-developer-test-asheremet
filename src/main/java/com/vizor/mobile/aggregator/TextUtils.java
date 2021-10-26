package com.vizor.mobile.aggregator;

public class TextUtils {
    private static final String wordDelimiter = "\\s\\W\\s|\\s+";

    static int wordCount(String text) {
        return text.split(wordDelimiter).length;
    }

    static int charsCount(String text) {
        return text.length();
    }
}
