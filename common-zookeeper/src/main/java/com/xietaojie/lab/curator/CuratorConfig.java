package com.xietaojie.lab.curator;

import lombok.Data;

/**
 * @author xietaojie1992
 */
@Data
public class CuratorConfig {

    private String  namespace;
    private String  connectString;
    private int     sessionTimeoutMs    = 5000;
    private int     connectionTimeoutMs = 15000;
    private String  retryType           = "retryNTimes";
    private boolean enableListener      = false;

    private ExponentialBackoffRetryConfig        exponentialBackoffRetry;
    private BoundedExponentialBackoffRetryConfig boundedExponentialBackoffRetry;
    private RetryNTimesConfig                    retryNTimes;
    private RetryForeverConfig                   retryForever;
    private RetryUntilElapsedConfig              retryUntilElapsed;

    @Data
    public static class ExponentialBackoffRetryConfig {
        private int baseSleepTimeMs = 2000;
        private int maxRetries      = 10;
    }

    @Data
    public static class BoundedExponentialBackoffRetryConfig {
        private int baseSleepTimeMs = 2000;
        private int maxSleepTimeMs  = 60000;
        private int maxRetries      = 10;
    }

    @Data
    public static class RetryNTimesConfig {
        private int count                 = 10;
        private int sleepMsBetweenRetries = 2000;
    }

    @Data
    public static class RetryForeverConfig {
        private int retryIntervalMs = 1000;
    }

    @Data
    public static class RetryUntilElapsedConfig {
        private int maxElapsedTimeMs      = 6000;
        private int sleepMsBetweenRetries = 2000;
    }

}
