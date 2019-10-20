package io.brachu.docker.compose.plugin;

import java.util.concurrent.TimeUnit;

public final class WaitConfig {

    private long value;

    private TimeUnit unit;

    public WaitConfig() {
        value = 1L;
        unit = TimeUnit.MINUTES;
    }

    public long getValue() {
        return value;
    }

    public TimeUnit getUnit() {
        return unit;
    }

}
