package io.brachu.docker.compose.plugin;

import java.io.File;
import java.util.Map;

import io.brachu.johann.DockerCompose;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

public abstract class AbstractDockerComposeMojo extends AbstractMojo {

    private static final String PROJECT_NAME_PROPERTY = "maven.dockerCompose.project";
    private static final String FAILSAFE_ARGLINE_PROPERTY = "argLine";

    /**
     * Read-only property. Injects a MavenProject object into the plugin.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${project}")
    protected MavenProject project;

    /**
     * Read-only property. Injects Maven's "project.basedir" property into the plugin.
     */
    @Parameter(required = true, readonly = true, defaultValue = "${project.basedir}")
    private File basedir;

    /**
     * Decides if current execution of this plugin should be skipped.
     */
    @Parameter(property = "dockerCompose.skip", defaultValue = "false")
    private boolean skip;

    /**
     * Path to docker-compose executable file. If left blank, it will be assumed that docker-compose is accessible from operating system's PATH.
     */
    @Parameter
    private String executablePath;

    /**
     * Path to a directory that will be set as a working directory of docker-compose process. All relative paths defined in docker-compose.yml
     * file will be resolved against this directory. If set to relative path, it will be resolved against "${project.basedir}". If set to blank,
     * working directory will be the same as working directory of JVM process that started this plugin which is a directory from which Maven was run
     * in most cases. Default value is "${project.basedir}".
     */
    @Parameter(defaultValue = "${project.basedir}")
    private File workDir;

    /**
     * Path to docker-compose.yml file. If relative, it will be appended to Maven's "project.basedir". Default value is
     * "src/test/resources/docker-compose.yml".
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
     * "unit" property accepts any value from <code>java.util.concurrent.TimeUnit</code> enum that's greater than or equal to SECONDS.
     * <p>
     * By default plugin will wait 1 minute for cluster to be up and running.
     */
    @Parameter
    private WaitConfig wait;

    private DockerComposeFactory dockerComposeFactory;

    AbstractDockerComposeMojo() {
        dockerComposeFactory = new DockerComposeFactory();
    }

    DockerCompose dockerCompose(Config config) throws MojoFailureException {
        try {
            return dockerComposeFactory.create(config);
        } catch (Exception ex) {
            throw new MojoFailureException("Cannot build DockerCompose object", ex);
        }
    }

    Config getConfig() throws MojoFailureException {
        Config config = new Config(executablePath, workDir, basedir, file, projectName, env, wait, skip);
        ConfigValidator.validate(config);
        return config;
    }

    void fillProperties(DockerCompose compose) {
        fillProjectProperties(compose);
        fillFailsafeArgLine(compose);
        fillSystemProperties(compose);
    }

    void clearProperties(DockerCompose compose) {
        clearProjectProperties();
        clearFailsafeArgLine(compose);
        clearSystemProperties();
    }

    private void fillProjectProperties(DockerCompose compose) {
        project.getProperties().setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

    private void clearProjectProperties() {
        project.getProperties().remove(PROJECT_NAME_PROPERTY);
    }

    private void fillFailsafeArgLine(DockerCompose compose) {
        String failsafeArgLine = project.getProperties().getProperty(FAILSAFE_ARGLINE_PROPERTY);
        String argLineParam = constructFailsafeArgLine(compose);

        if (failsafeArgLine != null && !failsafeArgLine.contains(argLineParam)) {
            failsafeArgLine = argLineParam + " " + failsafeArgLine;
        } else {
            failsafeArgLine = argLineParam;
        }

        project.getProperties().setProperty(FAILSAFE_ARGLINE_PROPERTY, failsafeArgLine);
    }

    private void clearFailsafeArgLine(DockerCompose compose) {
        String failsafeArgLine = project.getProperties().getProperty(FAILSAFE_ARGLINE_PROPERTY);
        if (failsafeArgLine != null) {
            failsafeArgLine = StringUtils.trimToNull(failsafeArgLine.replace(constructFailsafeArgLine(compose), ""));
            if (failsafeArgLine != null) {
                project.getProperties().setProperty(FAILSAFE_ARGLINE_PROPERTY, failsafeArgLine);
            } else {
                project.getProperties().remove(FAILSAFE_ARGLINE_PROPERTY);
            }
        }
    }

    private String constructFailsafeArgLine(DockerCompose compose) {
        return "-D" + PROJECT_NAME_PROPERTY + "=" + compose.getProjectName();
    }

    private void fillSystemProperties(DockerCompose compose) {
        System.setProperty(PROJECT_NAME_PROPERTY, compose.getProjectName());
    }

    private void clearSystemProperties() {
        System.clearProperty(PROJECT_NAME_PROPERTY);
    }

}
