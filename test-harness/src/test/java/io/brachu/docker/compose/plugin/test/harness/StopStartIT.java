package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import io.brachu.johann.DockerCompose;
import org.junit.Test;

public class StopStartIT {

    @Test
    public void stoppedAndStartedServiceShouldBeUp() {
        DockerCompose dockerCompose = DockerCompose.builder().classpath().build();
        assertThat(dockerCompose.isUp()).isTrue();
        assertThat(dockerCompose.port("postgresql", 5432)).isNotNull();
    }

}
