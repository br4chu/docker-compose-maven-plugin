package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import io.brachu.johann.ContainerPort;
import io.brachu.johann.DockerCompose;
import org.junit.Test;

public class EnvIT {

    @Test
    public void specifiedExternalPortShouldBeExposed() {
        DockerCompose dockerCompose = DockerCompose.builder().classpath().build();

        ContainerPort containerPort = dockerCompose.port("postgresql", 5432);
        assertThat(containerPort.getPort()).isEqualTo(5433);
    }

}
