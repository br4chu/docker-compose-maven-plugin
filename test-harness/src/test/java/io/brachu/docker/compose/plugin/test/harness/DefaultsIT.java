package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class DefaultsIT {

    @Test
    public void shouldRunOnDefaults() {
        assertThat(System.getProperty("maven.dockerCompose.project")).isNotNull();
    }

}
