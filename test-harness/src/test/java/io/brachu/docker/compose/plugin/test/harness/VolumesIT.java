package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.concurrent.TimeUnit;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.exceptions.VolumeNotFoundException;
import io.brachu.johann.DockerCompose;
import org.junit.Test;

public class VolumesIT {

    @Test
    public void namedVolumeShouldBeCreatedAndRemoved() throws DockerCertificateException, DockerException, InterruptedException {
        DockerCompose dockerCompose = DockerCompose.builder().classpath("/05.docker-compose.yml").build();
        DockerClient dockerClient = DefaultDockerClient.fromEnv().build();
        String projectName = dockerCompose.getProjectName();

        assertThat(dockerCompose.isUp()).isTrue();
        assertThat(dockerClient.inspectVolume(projectName + "_rabbitmq_logs")).isNotNull();

        dockerCompose.down();

        try {
            dockerClient.inspectVolume(projectName + "_rabbitmq_logs");
            fail("Volume should not exist");
        } catch (VolumeNotFoundException ignored) {
        }

        dockerCompose.up();
        dockerCompose.waitForCluster(1, TimeUnit.MINUTES);
    }

}
