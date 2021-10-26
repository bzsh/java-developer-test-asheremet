package com.vizor.mobile.twitter;

import java.util.Objects;
import java.util.Optional;

public class ConfigRule implements Rule {
    private String id;
    private String value;
    private String tag;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public Optional<String> getId() {
        return Optional.of(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigRule that = (ConfigRule) o;
        return Objects.equals(id, that.id) && Objects.equals(value, that.value) && Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, tag);
    }

    @Override
    public String toString() {
        return "ConfigRule{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
