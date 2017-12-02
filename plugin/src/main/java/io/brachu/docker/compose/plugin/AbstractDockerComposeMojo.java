package io.brachu.docker.compose.plugin;

import java.util.Map;

import io.brachu.johann.DockerCompose;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractDockerComposeMojo extends AbstractMojo {

    static final String PROJECT_NAME_PROPERTY = "maven.dockerCompose.project";

    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    protected MavenProject project;
    @Parameter(required = true, readonly = true, defaultValue = "${basedir}")
    private String basedir;

    @Parameter
    private String executablePath;
    @Parameter(required = true, defaultValue = "src/test/resources/docker-compose.yml")
    private String file;
    @Parameter(defaultValue = "${" + PROJECT_NAME_PROPERTY + "}")
    private String projectName;
    @Parameter
    private Map<String, String> env;
    @Parameter
    private WaitConfig wait;

    private DockerComposeFactory dockerComposeFactory;

    AbstractDockerComposeMojo() {
        dockerComposeFactory = new DockerComposeFactory();
    }

    DockerCompose dockerCompose() throws MojoFailureException {
        return dockerCompose(getConfig());
    }

    DockerCompose dockerCompose(Config config) throws MojoFailureException {
        try {
            return dockerComposeFactory.create(config);
        } catch (Exception ex) {
            throw new MojoFailureException("Unexpected error while creating DockerCompose object", ex);
        }
    }

    Config getConfig() throws MojoFailureException {
        Config config = new Config(executablePath, basedir, file, projectName, env, wait);
        ConfigValidator.validate(config);
        return config;
    }

}
