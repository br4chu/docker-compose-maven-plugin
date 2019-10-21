package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
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
     * Specifies how long should this plugin wait for all containers within a cluster to be healthy (or running if they do not implement a health check).
     * Timeouts result in build failure.
     * <p>
     * Example that will wait 5 seconds:
     * <pre>
     *     &lt;wait&gt;
     *         &lt;value&gt;5&lt;/value&gt;
     *         &lt;unit&gt;SECONDS&lt;/unit&gt;
     *     &lt;/wait&gt;
     * </pre>
     * "unit" property accepts any value from <code>java.util.concurrent.TimeUnit</code> enum that's greater than or equal to SECONDS.
     * <p>
     * By default plugin will wait 1 minute for cluster to be up and running.
     */
    @Parameter
    private WaitConfig wait;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        UpConfig config = getConfig();
        if (config.shouldExecute()) {
            DockerCompose compose = dockerCompose(config);
            up(compose, config);
            fillProperties(compose);
        } else {
            getLog().info("Skipping.");
        }
    }

    private UpConfig getConfig() throws MojoFailureException {
        CommonConfig commonConfig = getCommonConfig();
        return new UpConfig(commonConfig, wait);
    }

    private void up(DockerCompose compose, UpConfig config) throws MojoExecutionException {
        WaitConfig wait = config.getWait();
        try {
            compose.up();
            compose.waitForCluster(wait.getValue(), wait.getUnit());
        } catch (JohannTimeoutException ex) {
            throw new MojoExecutionException("Timed out while waiting for cluster to be healthy."
                    + " Either at least one of containers in the cluster failed to start properly or the value of 'wait' property is too short."
                    + " Current 'wait' property value is " + ex.getTime() + " " + ex.getUnit() + ".");
        } catch (JohannException ex) {
            throw new MojoExecutionException("Docker-compose cluster failed to start", ex);
        }
    }

    private void fillProperties(DockerCompose compose) {
        fillProjectProperties(compose);
        fillFailsafeArgLine(compose);
        fillSystemProperties(compose);
    }

    private void fillProjectProperties(DockerCompose compose) {
        project.getProperties().setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

    private void fillFailsafeArgLine(DockerCompose compose) {
        String failsafeArgLine = project.getProperties().getProperty(FAILSAFE_ARGLINE_PROPERTY);
        String argLineParam = constructFailsafeArgLine(compose);

        if (failsafeArgLine != null) {
            failsafeArgLine = argLineParam + " " + failsafeArgLine;
        } else {
            failsafeArgLine = argLineParam;
        }

        project.getProperties().setProperty(FAILSAFE_ARGLINE_PROPERTY, failsafeArgLine);
    }

    private void fillSystemProperties(DockerCompose compose) {
        System.setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

}
