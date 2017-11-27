package io.brachu.docker.compose.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

class Config {

    private final String executablePath;

    private final String file;

    private final String projectName;

    private final Map<String, String> env;

    private final WaitConfig wait;

    private final boolean clusterAlreadyUp;

    Config(@Nullable String executablePath, String projectBasedir, String file, @Nullable String projectName, @Nullable Map<String, String> env,
           @Nullable WaitConfig wait, boolean clusterAlreadyUp) {

        this.executablePath = StringUtils.trimToNull(executablePath);
        this.file = prepareFilePath(projectBasedir, file);
        this.projectName = StringUtils.trimToNull(projectName);
        this.env = env;
        this.wait = wait;
        this.clusterAlreadyUp = clusterAlreadyUp;
    }

    String getExecutablePath() {
        return executablePath;
    }

    String getFile() {
        return file;
    }

    String getProjectName() {
        return projectName;
    }

    Map<String, String> getEnv() {
        return env;
    }

    WaitConfig getWait() {
        if (wait != null) {
            return wait;
        } else {
            return new WaitConfig();
        }
    }

    boolean isClusterAlreadyUp() {
        return clusterAlreadyUp;
    }

    private String prepareFilePath(String projectBasedir, String file) {
        Path basedirPath = Paths.get(projectBasedir);
        Path filePath = Paths.get(file);
        return basedirPath.resolve(filePath).toString();
    }

}
