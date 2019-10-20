package io.brachu.docker.compose.plugin;

import java.util.Optional;

import javax.annotation.Nullable;

final class UpConfig {

    private final CommonConfig commonConfig;

    private final WaitConfig wait;

    UpConfig(CommonConfig commonConfig, @Nullable WaitConfig wait) {
        this.commonConfig = commonConfig;
        this.wait = Optional.ofNullable(wait).orElseGet(WaitConfig::new);
    }

    CommonConfig getCommonConfig() {
        return commonConfig;
    }

    WaitConfig getWait() {
        return wait;
    }

    boolean shouldExecute() {
        return commonConfig.shouldExecute();
    }

}
