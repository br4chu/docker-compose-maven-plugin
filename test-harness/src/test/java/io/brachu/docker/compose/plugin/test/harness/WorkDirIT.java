package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collections;

import io.brachu.johann.DockerCompose;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Test;

public class WorkDirIT {

    @Test
    public void clusterShouldBeUpWhenUsingCustomWorkDirThatPointsToCorrectDirectory() {
        // given
        DockerCompose dockerCompose = DockerCompose.builder()
                .classpath("/10.docker-compose.yml")
                .workDir(System.getProperty("user.dir") + "/src/test/resources")
                .build();

        // expect
        assertThat(dockerCompose.isUp()).isTrue();
    }

    @Test
    public void shouldFailOnInvalidworkDir() throws MavenInvocationException {
        StringBuffer buffer = new StringBuffer();
        InvocationRequest request = new DefaultInvocationRequest();

        request.setBatchMode(true);
        request.setOutputHandler(line -> buffer.append(line).append(System.lineSeparator()));
        request.setPomFile(new File("10.workdir.invalid.pom.xml"));
        request.setGoals(Collections.singletonList("docker-compose:up"));

        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);

        String output = buffer.toString();
        assertThat(output).contains("nonexisting/dir' does not point to an existing directory.");
    }

}
