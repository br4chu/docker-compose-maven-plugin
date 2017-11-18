package io.brachu.docker.compose.plugin;

class FileConfig {

    private String classpath;

    private String absolute;

    public FileConfig() {
        classpath = absolute = null;
    }

    String getClasspath() {
        return classpath;
    }

    String getAbsolute() {
        return absolute;
    }

}
