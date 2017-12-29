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
     * "unit" property accepts any value from TimeUnit enum that's greater than or equal to SECONDS.
     * <p>
     * By default plugin will wait 1 minute for cluster to be up and running.
     */
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
            throw new MojoFailureException("Cannot build DockerCompose object", ex);
        }
    }

    Config getConfig() throws MojoFailureException {
        Config config = new Config(executablePath, basedir, file, projectName, env, wait);
        ConfigValidator.validate(config);
        return config;
    }

    String constructFailsafeArgLine(DockerCompose compose) {
        return "-D" + PROJECT_NAME_PROPERTY + "=" + compose.getProjectName();
    }

}
