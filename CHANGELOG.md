# 0.4.0

Updated Johann dependency to version 0.7.0.

Parametrized "down" goal with "killBeforeDown" property (defaults to `true`).

Refer to javadocs of DownMojo for more information.

# 0.3.0

Updated Johann dependency to version 0.5.0.

Parametrized "down" goal with properties:
* removeVolumes (defaults to `true`)
* removeOrphans (default to `false`)
* downTimeoutSeconds (defaults to 10)

Refer to javadocs of DownMojo for more information.

# 0.2.0

Better integration with maven-failsafe-plugin. There is now no need to disable forking in maven-failsafe-plugin or proxying "maven.dockerCompose.project"
property to forked process.

# 0.1.0

Initial release. Working "up" and "down" goals.
