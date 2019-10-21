package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.DockerComposeException;
import org.junit.Test;

public class StopIT {

    @Test
    @SuppressWarnings({ "ResultOfMethodCallIgnored", "CatchMayIgnoreException" })
    public void stoppedServiceShouldBeDown() {
        DockerCompose dockerCompose = DockerCompose.builder().classpath().build();
        assertThat(dockerCompose.isUp()).isTrue();
        try {
            dockerCompose.port("postgresql", 5432);
            fail("Postgresql service should be turned off but it isn't");
        } catch (DockerComposeException ex) {
        }
    }

}
