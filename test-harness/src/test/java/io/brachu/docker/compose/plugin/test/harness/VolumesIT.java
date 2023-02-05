package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.brachu.johann.DockerCompose;
import org.junit.Test;

public class VolumesIT {

    @Test
    public void namedVolumeShouldBeCreatedAndRemoved() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();

        try (
                DockerCompose dockerCompose = DockerCompose.builder().classpath("/05.docker-compose.yml").build();
                DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient)
        ) {
            String projectName = dockerCompose.getProjectName();
            assertThat(dockerCompose.isUp()).isTrue();
            InspectVolumeResponse response = dockerClient.inspectVolumeCmd(projectName + "_rabbitmq_logs").exec();
            assertThat(response.getName()).isNotNull();

            dockerCompose.down();

            try {
                dockerClient.inspectVolumeCmd(projectName + "_rabbitmq_logs").exec();
                fail("Volume should not exist");
            } catch (NotFoundException ignored) {
            }

            dockerCompose.up();
            dockerCompose.waitForCluster(1, TimeUnit.MINUTES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
