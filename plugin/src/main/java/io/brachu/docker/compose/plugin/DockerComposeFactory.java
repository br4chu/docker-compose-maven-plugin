package io.brachu.docker.compose.plugin;

import java.util.Map;

import io.brachu.johann.DockerCompose;

final class DockerComposeFactory {

    DockerCompose create(Config config) {
        DockerCompose.OngoingBuild.ComposeFile file = setupBuilder(config);
        DockerCompose.OngoingBuild.Project project = setupFile(file, config);
        DockerCompose.OngoingBuild.WorkDir workDir = setupProject(project, config);
        DockerCompose.OngoingBuild.Env env = setupWorkDir(workDir, config);
        DockerCompose.OngoingBuild.Finish finish = setupEnv(env, config);
        return finish.build();
    }

    private DockerCompose.OngoingBuild.ComposeFile setupBuilder(Config config) {
        String executablePath = config.getExecutablePath();

        if (executablePath != null) {
            return DockerCompose.builder(executablePath);
        } else {
            return DockerCompose.builder();
        }
    }

    private DockerCompose.OngoingBuild.Project setupFile(DockerCompose.OngoingBuild.ComposeFile file, Config config) {
        return file.absolute(config.getFile());
    }

    private DockerCompose.OngoingBuild.WorkDir setupProject(DockerCompose.OngoingBuild.Project project, Config config) {
        String projectName = config.getProjectName();

        if (projectName != null) {
            return project.projectName(projectName);
        } else {
            return project;
        }
    }

    private DockerCompose.OngoingBuild.Env setupWorkDir(DockerCompose.OngoingBuild.WorkDir workDir, Config config) {
        return workDir.workDir(config.getWorkDir());
    }

    private DockerCompose.OngoingBuild.Finish setupEnv(DockerCompose.OngoingBuild.Env env, Config config) {
        Map<String, String> configEnv = config.getEnv();

        if (configEnv != null) {
            return env.env(configEnv);
        } else {
            return env;
        }
    }

}
