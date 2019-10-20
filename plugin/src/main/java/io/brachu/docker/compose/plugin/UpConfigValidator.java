package io.brachu.docker.compose.plugin;

import java.util.concurrent.TimeUnit;

import org.apache.maven.plugin.MojoFailureException;

final class UpConfigValidator {

    static void validate(UpConfig config) throws MojoFailureException {
        validateWait(config);
    }

    private static void validateWait(UpConfig config) throws MojoFailureException {
        WaitConfig wait = config.getWait();
        if (wait != null) {
            if (wait.getValue() < 1) {
                throw new MojoFailureException("Wait time value must be greater than zero.");
            }

            if (wait.getUnit() == null) {
                throw new MojoFailureException("Wait time unit must not be null.");
            } else if (wait.getUnit().ordinal() < TimeUnit.SECONDS.ordinal()) {
                throw new MojoFailureException("Wait time unit cannot be smaller than seconds.");
            }
        }
    }

}
