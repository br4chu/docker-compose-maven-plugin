package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import io.brachu.johann.DockerCompose;
import org.junit.Test;

public class DefaultsIT {

    @Test
    public void shouldHaveSystemProperty() {
        assertThat(System.getProperty("maven.dockerCompose.project")).isNotBlank();
    }

    @Test
    public void clusterShouldBeRunning() {
        DockerCompose dockerCompose = DockerCompose.builder().classpath().build();
        assertThat(dockerCompose.ps().size()).isOne();
    }

}
