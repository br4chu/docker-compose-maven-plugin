package io.brachu.docker.compose.plugin;

import java.util.concurrent.TimeUnit;

class WaitConfig {

    private long value;

    private TimeUnit unit;

    WaitConfig() {
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
