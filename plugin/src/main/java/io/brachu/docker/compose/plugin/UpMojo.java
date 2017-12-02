package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.JohannException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "up", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class UpMojo extends AbstractDockerComposeMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Config config = getConfig();
        DockerCompose compose = dockerCompose(config);

        up(compose, config);
        fillProperties(compose);
    }

    private void up(DockerCompose compose, Config config) throws MojoExecutionException {
        WaitConfig wait = config.getWait();
        try {
            compose.up();
            compose.waitForCluster(wait.getValue(), wait.getUnit());
        } catch (JohannException ex) {
            throw new MojoExecutionException("Docker-compose cluster failed to start.", ex);
        }
    }

    private void fillProperties(DockerCompose compose) {
        fillProjectProperties(compose);
        fillSystemProperties(compose);
    }

    private void fillProjectProperties(DockerCompose compose) {
        project.getProperties().setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

    private void fillSystemProperties(DockerCompose compose) {
        System.setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

}
