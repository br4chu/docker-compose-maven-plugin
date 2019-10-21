package io.brachu.docker.compose.plugin;

import java.util.Map;

import io.brachu.johann.DockerCompose;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractDockerComposeMojo extends AbstractMojo {

    static final String PROJECT_NAME_PROPERTY = "maven.dockerCompose.project";
    static final String FAILSAFE_ARGLINE_PROPERTY = "argLine";

    /**
     * Read-only property. Injects a MavenProject object into the plugin.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    protected MavenProject project;

    /**
     * Read-only property. Injects Maven's "basedir" property into the plugin.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${basedir}")
    private String basedir;

    /**
     * Decides if current execution of this plugin should be skipped.
     */
    @Parameter(property = "maven.dockerCompose.skip", defaultValue = "false")
    private boolean skip;

    /**
     * Path to docker-compose executable file. If left blank, it will be assumed that docker-compose is accessible from operating system's PATH.
     */
    @Parameter
    private String executablePath;

    /**
     * Path to docker-compose.yml file. If relative, it will be appended to Maven's "basedir". Default value is "src/test/resources/docker-compose.yml".
     */
    @Parameter(required = true, defaultValue = "src/test/resources/docker-compose.yml")
    private String file;

    /**
     * Name of docker-compose project. It will be passed directly to docker-compose CLI. If not specified, a random 8-letter string will be used instead.
     * <p>
     * Be advised that "up" goal of this plugin will override global "maven.dockerCompose.project" property with generated project name. This is
     * necessary because "down" goal needs to know which docker-compose cluster to shut down.
     */
    @Parameter(defaultValue = "${" + PROJECT_NAME_PROPERTY + "}")
    private String projectName;

    /**
     * A map of environment variables that will be passed to docker-compose CLI.
     */
    @Parameter
    private Map<String, String> env;

    private DockerComposeFactory dockerComposeFactory;

    AbstractDockerComposeMojo() {
        dockerComposeFactory = new DockerComposeFactory();
    }

    DockerCompose dockerCompose(UpConfig upConfig) throws MojoFailureException {
        return dockerCompose(upConfig.getCommonConfig());
    }

    DockerCompose dockerCompose(CommonConfig config) throws MojoFailureException {
        try {
            return dockerComposeFactory.create(config);
        } catch (Exception ex) {
            throw new MojoFailureException("Cannot build DockerCompose object", ex);
        }
    }

    CommonConfig getCommonConfig() throws MojoFailureException {
        CommonConfig config = new CommonConfig(executablePath, basedir, file, projectName, env, skip);
        CommonConfigValidator.validate(config);
        return config;
    }

    String constructFailsafeArgLine(DockerCompose compose) {
        return "-D" + PROJECT_NAME_PROPERTY + "=" + compose.getProjectName();
    }

}
