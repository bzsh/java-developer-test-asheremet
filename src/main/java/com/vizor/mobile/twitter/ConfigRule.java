package com.vizor.mobile.twitter;

import java.util.Optional;

public class ConfigRule implements Rule
{
    private String value;
    private String tag;

    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public String getTag()
    {
        return tag;
    }

    @Override
    public Optional<String> getId()
    {
        return Optional.empty();
    }
}
