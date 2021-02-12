package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.UpConfig;
import io.brachu.johann.exception.JohannException;
import io.brachu.johann.exception.JohannTimeoutException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "up", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public final class UpMojo extends AbstractDockerComposeMojo {

    /**
     * Forces docker-compose to build images for all services that have their "build:" section supplied in docker-compose.yml. Build will be invoked
     * even if an image already exists. This is equivalent of passing "--build" switch to docker-compose CLI.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean forceBuild;

    /**
     * Boolean flag that triggers the reading of docker-compose logs. The effect of setting this parameter to "true" would be the same as running
     * "docker-compose logs -f" command in a different terminal window after this goal has finished executing. Logs will be redirected to System.out
     * (for standard output) and System.err (for standard error) of JVM process that runs this goal.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean followLogs;

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
            compose.up(config());
            if (followLogs) {
                compose.followLogs();
            }
            compose.waitForCluster(wait.getValue(), wait.getUnit());
        } catch (JohannTimeoutException ex) {
            throw new MojoExecutionException(ExceptionMessages.clusterTimeout(ex));
        } catch (JohannException ex) {
            throw new MojoExecutionException("Docker-compose cluster failed to start", ex);
        }
    }

    private UpConfig config() {
        return UpConfig.defaults()
                .withForceBuild(forceBuild);
    }

}
