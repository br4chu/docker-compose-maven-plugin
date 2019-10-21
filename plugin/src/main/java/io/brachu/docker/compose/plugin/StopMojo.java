package io.brachu.docker.compose.plugin;

import java.util.List;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.JohannException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "stop", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public final class StopMojo extends AbstractDockerComposeMojo {

    /**
     * Names of services (as defined in docker-compose file) that will be stopped during execution of "stop" goal of this plugin.
     * <p>
     * Stopping a service means stopping all of its containers without removing them. Stopping an already stopped service has no effect.
     * <p>
     * Services will be stopped in the same order as provided in the list.
     * <p>
     * Empty or unprovided list means that all services defined in docker-compose file will be stopped - this is the default behaviour of docker-compose "stop"
     * command.
     */
    @Parameter
    private List<String> stopServices;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = getConfig();
        if (config.shouldExecute()) {
            DockerCompose compose = dockerCompose(config);
            stop(compose);
        } else {
            getLog().info("Skipping.");
        }
    }

    private void stop(DockerCompose compose) throws MojoExecutionException {
        try {
            stopInternal(compose);
        } catch (JohannException ex) {
            throw new MojoExecutionException("Unexpected exception during stopping of docker-compose services.", ex);
        }
    }

    private void stopInternal(DockerCompose compose) {
        if (stopServices != null && !stopServices.isEmpty()) {
            String[] serviceNames = stopServices.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
            compose.stop(serviceNames);
        } else {
            compose.stopAll();
        }
    }

}
