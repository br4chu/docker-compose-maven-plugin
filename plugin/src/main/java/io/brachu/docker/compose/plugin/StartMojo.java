package io.brachu.docker.compose.plugin;

import java.util.List;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.JohannException;
import io.brachu.johann.exception.JohannTimeoutException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "start", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public final class StartMojo extends AbstractDockerComposeMojo {

    /**
     * Names of services (as defined in docker-compose file) that will be started during execution of "start" goal of this plugin.
     * <p>
     * Starting a service means starting all of its containers. Containers need to exist beforehand or the execution of this goal will fail. Starting an already
     * started service has no effect.
     * <p>
     * Services will be started in the same order as provided in the list.
     * <p>
     * Empty or unprovided list means that all services defined in docker-compose file will be started - this is the default behaviour of docker-compose "start"
     * command.
     */
    @Parameter
    private List<String> startServices;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = getConfig();
        if (config.shouldExecute()) {
            DockerCompose compose = dockerCompose(config);
            start(compose, config);
            fillProperties(compose);
        } else {
            getLog().info("Skipping.");
        }
    }

    private void start(DockerCompose compose, Config config) throws MojoExecutionException {
        try {
            startInternal(compose, config);
        } catch (JohannException ex) {
            throw new MojoExecutionException("Unexpected exception during starting of docker-compose services.", ex);
        }
    }

    private void startInternal(DockerCompose compose, Config config) throws MojoExecutionException {
        if (startServices != null && !startServices.isEmpty()) {
            String[] serviceNames = startServices.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
            compose.start(serviceNames);
            waitForServices(serviceNames, compose, config.getWait());
        } else {
            compose.startAll();
            waitForAll(compose, config.getWait());
        }
    }

    private void waitForServices(String[] serviceNames, DockerCompose compose, WaitConfig wait) throws MojoExecutionException {
        for (String serviceName : serviceNames) {
            try {
                compose.waitForService(serviceName, wait.getValue(), wait.getUnit());
            } catch (JohannTimeoutException ex) {
                throw new MojoExecutionException(ExceptionMessages.serviceTimeout(serviceName, ex));
            }
        }
    }

    private void waitForAll(DockerCompose compose, WaitConfig wait) throws MojoExecutionException {
        try {
            compose.waitForCluster(wait.getValue(), wait.getUnit());
        } catch (JohannTimeoutException ex) {
            throw new MojoExecutionException(ExceptionMessages.clusterTimeout(ex));
        }
    }

}
