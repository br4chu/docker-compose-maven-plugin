package io.brachu.docker.compose.plugin;

import org.apache.maven.plugin.MojoFailureException;

final class CommonConfigValidator {

    static void validate(CommonConfig config) throws MojoFailureException {
        validateFile(config);
    }

    private static void validateFile(CommonConfig config) throws MojoFailureException {
        if (config.getFile() == null) {
            throw new MojoFailureException("Path to docker-compose.yml file must be set.");
        }
    }

}
