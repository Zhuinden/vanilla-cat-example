package com.zhuinden.vanillacatexample.domain.object;

import com.google.auto.value.AutoValue;

/**
 * Created by Owner on 2017. 04. 29..
 */

@AutoValue
public abstract class Cat {
    public abstract String id();

    public abstract String url();

    public abstract String sourceUrl();

    public static Cat create(String id, String url, String sourceUrl) {
        return new AutoValue_Cat.Builder().setId(id).setUrl(url).setSourceUrl(sourceUrl).build();
    }

    public Builder toBuilder() {
        return new AutoValue_Cat.Builder(this);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);

        public abstract Builder setUrl(String url);

        public abstract Builder setSourceUrl(String sourceUrl);

        public abstract Cat build();
    }
}
