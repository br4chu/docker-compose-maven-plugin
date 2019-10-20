# 0.7.0 (in progress)

Added "skip" property to all goals. Plugin executions can now be skipped based on conditional criteria provided by the end user.

# 0.6.0

Updated Johann dependency to version 1.0.0.

# 0.5.0

Updated Johann dependency to version 0.8.0. This should result in better support of JDK9+ projects.

# 0.4.0

Updated Johann dependency to version 0.7.0.

Parametrized "down" goal with "killBeforeDown" property (defaults to `true`).

Refer to README or javadocs of DownMojo for more information.

# 0.3.0

Updated Johann dependency to version 0.5.0.

Parametrized "down" goal with properties:
* removeVolumes (defaults to `true`)
* removeOrphans (default to `false`)
* downTimeoutSeconds (defaults to 10)

Refer to README or javadocs of DownMojo for more information.

# 0.2.0

Better integration with maven-failsafe-plugin. There is now no need to disable forking in maven-failsafe-plugin or proxying "maven.dockerCompose.project"
property to forked process.

# 0.1.0

Initial release. Working "up" and "down" goals.
