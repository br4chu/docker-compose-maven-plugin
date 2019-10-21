package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.DownConfig;
import io.brachu.johann.exception.JohannException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "down", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public final class DownMojo extends AbstractDockerComposeMojo {

    /**
     * Should 'down' goal remove named volumes declared in the `volumes` section of the Compose file and anonymous volumes attached to containers?
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean removeVolumes;

    /**
     * Should 'down' goal remove containers for services not defined in the Compose file?
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean removeOrphans;

    /**
     * Specifies a shutdown timeout in seconds for 'down' goal.
     */
    @Parameter(required = true, defaultValue = "10")
    private int downTimeoutSeconds;

    /**
     * Should 'down' goal kill all containers in the cluster before removing the cluster?
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean killBeforeDown;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = getConfig();
        if (config.shouldExecute()) {
            DockerCompose compose = dockerCompose(config);
            clearProperties(compose);
            down(compose);
        } else {
            getLog().info("Skipping.");
        }
    }

    private void down(DockerCompose compose) throws MojoExecutionException {
        try {
            if (killBeforeDown) {
                compose.kill();
            }
            compose.down(config());
        } catch (JohannException ex) {
            throw new MojoExecutionException("Docker-compose cluster failed to shut down", ex);
        }
    }

    private DownConfig config() {
        return DownConfig.defaults()
                .withRemoveVolumes(removeVolumes)
                .withRemoveOrphans(removeOrphans)
                .withTimeoutSeconds(downTimeoutSeconds);
    }

}
