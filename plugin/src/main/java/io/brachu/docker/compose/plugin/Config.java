package io.brachu.docker.compose.plugin;

import java.util.Map;

import javax.annotation.Nullable;

class Config {

    private final String executablePath;

    private final FileConfig file;

    private final String projectName;

    private final Map<String, String> env;

    private final WaitConfig wait;

    public Config(@Nullable String executablePath, @Nullable FileConfig file, @Nullable String projectName,
                  @Nullable Map<String, String> env, @Nullable WaitConfig wait) {

        this.executablePath = executablePath;
        this.file = file;
        this.projectName = projectName;
        this.env = env;
        this.wait = wait;
    }

    public String getExecutablePath() {
        return executablePath;
    }

    public FileConfig getFile() {
        return file;
    }

    public String getProjectName() {
        return projectName;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public WaitConfig getWait() {
        if (wait != null) {
            return wait;
        } else {
            return new WaitConfig();
        }
    }

}
