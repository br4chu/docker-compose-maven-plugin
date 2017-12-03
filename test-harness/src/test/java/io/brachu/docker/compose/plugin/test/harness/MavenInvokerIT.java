package io.brachu.docker.compose.plugin.test.harness;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.junit.Test;

/**
 * A test suite which invokes maven build for all POM files we want to test.
 */
public class MavenInvokerIT {

    @Test
    public void runDefaults() throws MavenInvocationException, CommandLineException {
        InvocationResult result = invoke("01.defaults.pom.xml");
        assertThat(result.getExitCode()).isZero();
    }

    @Test
    public void runSpecifiedName() throws MavenInvocationException, CommandLineException {
        InvocationResult result = invoke("02.specified-name.pom.xml");
        assertThat(result.getExitCode()).isZero();
    }

    @Test
    public void runEnv() throws MavenInvocationException, CommandLineException {
        InvocationResult result = invoke("03.env.pom.xml");
        assertThat(result.getExitCode()).isZero();
    }

    @Test
    public void runWait() throws MavenInvocationException, CommandLineException {
        InvocationResult result = invoke("04.wait.pom.xml");
        assertThat(result.getExitCode()).isNotZero();
    }

    private InvocationResult invoke(String pomFile) throws MavenInvocationException, CommandLineException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBatchMode(true);
        request.setPomFile(new File(pomFile));
        request.setGoals(Collections.singletonList("verify"));

        Invoker invoker = new DefaultInvoker();
        InvocationResult result = invoker.execute(request);

        if (result.getExecutionException() != null) {
            throw result.getExecutionException();
        }

        return result;
    }

}
