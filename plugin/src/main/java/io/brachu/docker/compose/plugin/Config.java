package io.brachu.docker.compose.plugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

final class Config {

    private final String executablePath;
    private final File workDir;
    private final String file;
    private final String projectName;
    private final Map<String, String> env;
    private final WaitConfig wait;
    private final boolean skip;

    Config(@Nullable String executablePath,
           @Nullable File workDir,
           File projectBasedir,
           String file,
           @Nullable String projectName,
           @Nullable Map<String, String> env,
           @Nullable WaitConfig wait, boolean skip) {

        this.executablePath = StringUtils.trimToNull(executablePath);
        this.workDir = prepareWorkDirPath(projectBasedir, workDir);
        this.file = prepareFilePath(projectBasedir, file);
        this.projectName = StringUtils.trimToNull(projectName);
        this.env = env;
        this.wait = Optional.ofNullable(wait).orElseGet(WaitConfig::new);
        this.skip = skip;
    }

    String getExecutablePath() {
        return executablePath;
    }

    File getWorkDir() {
        return workDir;
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
        return wait;
    }

    boolean shouldExecute() {
        return !skip;
    }

    private File prepareWorkDirPath(File projectBasedir, @Nullable File workDir) {
        if (workDir != null) {
            Path basedirPath = projectBasedir.toPath();
            Path workDirPath = workDir.toPath();
            return basedirPath.resolve(workDirPath).toFile();
        } else {
            return null;
        }
    }

    private String prepareFilePath(File projectBasedir, String file) {
        Path basedirPath = projectBasedir.toPath();
        Path filePath = Paths.get(file);
        return basedirPath.resolve(filePath).toString();
    }

}
