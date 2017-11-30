package io.brachu.docker.compose.plugin;

import io.brachu.johann.DockerCompose;
import io.brachu.johann.exception.JohannException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "down", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class DownMojo extends AbstractDockerComposeMojo {

    public DownMojo() {
        super(true);
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        DockerCompose compose = dockerCompose();

        clearProperties();
        down(compose);
    }

    private void down(DockerCompose compose) throws MojoFailureException, MojoExecutionException {
        try {
            compose.down();
        } catch (JohannException ex) {
            throw new MojoExecutionException("Unexpected error while shutting down docker-compose cluster", ex);
        }
    }

    private void clearProperties() {
        clearProjectProperties();
        clearSystemProperties();
    }

    private void clearProjectProperties() {
        project.getProperties().remove(PROJECT_NAME_PROPERTY);
    }

    private void clearSystemProperties() {
        System.clearProperty(PROJECT_NAME_PROPERTY);
    }

}
