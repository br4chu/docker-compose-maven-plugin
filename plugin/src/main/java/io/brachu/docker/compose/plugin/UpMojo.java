package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.JohannException;
import io.brachu.johann.exception.JohannTimeoutException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "up", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public final class UpMojo extends AbstractDockerComposeMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = getConfig();
        if (config.shouldExecute()) {
            DockerCompose compose = dockerCompose(config);
            up(compose, config);
            fillProperties(compose);
        } else {
            getLog().info("Skipping.");
        }
    }

    private void up(DockerCompose compose, Config config) throws MojoExecutionException {
        WaitConfig wait = config.getWait();
        try {
            compose.up();
            compose.waitForCluster(wait.getValue(), wait.getUnit());
        } catch (JohannTimeoutException ex) {
            throw new MojoExecutionException(ExceptionMessages.clusterTimeout(ex));
        } catch (JohannException ex) {
            throw new MojoExecutionException("Docker-compose cluster failed to start", ex);
        }
    }

}
