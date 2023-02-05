# 1.0.0

Update Johann dependency to version 2.0.0. Project is now compiled against JDK 11 because Johann dependency requires it.

# 0.11.0

Update Johann dependency to version 1.5.0.

`down` goal will no longer fail if `killBeforeDown` property is set to `true` and cluster is already down. Such action will be logged instead.

# 0.10.0

Update Johann dependency to version 1.4.0. This should fix any potential issues when running against Docker Compose v2.x.

# 0.9.0

Updated Johann dependency to version 1.3.0.

Added "forceBuild" property to `up` goal. See README.md for more information about it.

# 0.8.0

Updated Johann dependency to version 1.2.0.

Added "workDir" property to all goals. See README.md for more information about it.

Added "followLogs" property to `up` goal. See README.md for more information about it.

# 0.7.0

Updated Johann dependency to version 1.1.0.

Added "skip" property to all goals. Plugin executions can now be skipped based on conditional criteria provided by the end user.
Users can also provide `dockerCompose.skip` Maven property to skip all plugin executions at once.

Improved error message when a timeout occurs. Previous message printed entire stacktrace of an exception which confused users into thinking it may be
a bug in the plugin which was usually not the case.

Added new Maven goals:

* start (bound to pre-integration-test phase by default)
* stop (bound to post-integration-test phase by default)

Both new goals are equivalent to "start" and "stop" commands that can be passed to docker-compose CLI.

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
