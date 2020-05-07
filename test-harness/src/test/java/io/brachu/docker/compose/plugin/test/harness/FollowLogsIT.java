package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Test;

public class FollowLogsIT {

    @Test
    public void shouldCaptureLogsFromCluster() throws MavenInvocationException {
        StringBuffer buffer = new StringBuffer();
        InvocationRequest request = new DefaultInvocationRequest();

        request.setBatchMode(true);
        request.setOutputHandler(line -> buffer.append(line).append(System.lineSeparator()));
        request.setPomFile(new File("11.followlogs.aux.pom.xml"));
        request.setGoals(Collections.singletonList("verify"));

        Invoker invoker = new DefaultInvoker();
        invoker.execute(request);

        String output = buffer.toString();
        assertThat(output).contains("/var/lib/postgresql");
    }

}
