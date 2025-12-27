package com.coordinate.viewer.config;

import com.coordinate.viewer.util.ConfigLoader;

public final class AppConfig {
    private final String appName;
    private final String version;
    private final String author;
    private final String authorEmail;
    private final String metaData;

    private final String dataSourceUrl;
    private final int retryMaxAttempts;
    private final long retryBaseDelayMillis;
    private final long timeoutIntervalSeconds;
    private final int autoReloadInterval;

    public AppConfig() {
        this.appName = ConfigLoader.get("app.name");
        this.version = ConfigLoader.get("app.version");
        this.author = ConfigLoader.get("app.author");
        this.authorEmail = ConfigLoader.get("app.email");
        this.metaData = ConfigLoader.get("app.copyright");

        this.autoReloadInterval = ConfigLoader.getInt("auto.reload.interval.ms", 30000);
        this.dataSourceUrl = ConfigLoader.get("data.source.url");
        this.retryMaxAttempts = ConfigLoader.getInt("retry.maxAttempts", 3);
        this.retryBaseDelayMillis = ConfigLoader.getLong("retry.baseDelayMillis", 500);
        this.timeoutIntervalSeconds = ConfigLoader.getLong("timeout.intervalSeconds", 10);
    }

    public String getDataSourceUrl() {
        return dataSourceUrl;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public long getRetryBaseDelayMillis() {
        return retryBaseDelayMillis;
    }
    public long getTimeoutIntervalSeconds() {
        return timeoutIntervalSeconds;
    }

    public String getAppName() {
        return appName;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getMetaData() {
        return metaData;
    }

    public int getAutoReloadInterval() {
        return autoReloadInterval;
    }

}