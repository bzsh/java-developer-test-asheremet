package com.vizor.mobile.aggregator;

public class TextUtils {
    private static final String wordDelimiter = "(?U)\\W+";                            //todo
     static int wordCount(String text) {
        return text.split(wordDelimiter).length;
    }
}
