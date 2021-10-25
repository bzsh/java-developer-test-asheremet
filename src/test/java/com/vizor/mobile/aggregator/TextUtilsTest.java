package com.vizor.mobile.aggregator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextUtilsTest
{

    @Test
    void wordCount()
    {
        assertEquals(1, TextUtils.wordCount("word"));
        assertEquals(2, TextUtils.wordCount("word word"));
        assertEquals(2, TextUtils.wordCount("word  word"));
        assertEquals(2, TextUtils.wordCount("word , word"));
        assertEquals(2, TextUtils.wordCount("Word\nWord."));
        assertEquals(2, TextUtils.wordCount("Word - Word"));
        assertEquals(3, TextUtils.wordCount("Word123 123 Word"));
        assertEquals(3, TextUtils.wordCount("Word123 123 Word "));
        assertEquals(1, TextUtils.wordCount("three-part-word"));
    }
}