# Introduction

**docker-compose-maven-plugin** is a Maven plugin that talks to docker-compose command-line interface.

Currently implemented docker-compose commands (exposed as Maven goals) are:
* up & down
* start & stop

# Requirements

* JDK8+
* Maven 3+

This project also depends on [Johann](https://github.com/br4chu/johann) library and inherits its requirements:

* Docker Engine 17.06+
* Docker Compose 1.18+

# Goals

docker-compose-maven-plugin currently implements 4 goals:
* docker-compose:up (bound to pre-integration-test phase by default)
* docker-compose:down (bound to post-integration-test phase by default)
* docker-compose:start (bound to pre-integration-test phase by default)
* docker-compose:stop (bound to post-integration-test phase by default)

# Example usage

## Integration tests

1. Put your *docker-compose.yml* file in your project's *src/test/resources* directory.

2. Add docker-compose-maven-plugin to your build descriptor as follows:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.brachu</groupId>
            <artifactId>docker-compose-maven-plugin</artifactId>
            <version>0.9.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>up</goal>
                        <goal>down</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

3. Run your integration tests with `mvn verify` command. You should then see your docker-compose cluster starting up before tests and shutting down after the
tests are done.

Above configuration is minimalistic but gets the job done. See "Configuration" section for more details on how to better configure these goals according to your
needs.

## Integration tests (start & stop goals)

You can use "start" and "stop" goals as an alternative to standard "up & down" flow. Some projects may configure integration tests to be only
run on certain dedicated machines where constantly creating and destroying containers may be too time-consuming. In such cases it may be better to
leave certain services up between Maven builds and only restart services that require it.
You can also mix "start" and "stop" goals with "up" and "down" goals for more complex build flows by leveraging Maven's profiles and multiple executions.

Below is an example configuration which starts and stops `postgresql` and `rabbitmq` services. It also assumes that compose file is located in your project's
*src/test/resources* directory.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.brachu</groupId>
            <artifactId>docker-compose-maven-plugin</artifactId>
            <version>0.9.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>start</goal>
                        <goal>stop</goal>
                    </goals>
                    <configuration>
                        <projectName>ci</projectName>
                        <startServices>
                            <service>postgresql</service>
                            <service>rabbitmq</service>
                        </startServices>
                        <stopServices>
                            <service>rabbitmq</service>
                            <service>postgresql</service>
                        </stopServices>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

# Configuration

All goals have following properties:

| Property | Description | Default value |
| --- | --- | --- |
| env | A map of custom environment variables that will be passed to docker-compose CLI. | Empty map |
| executablePath | Path to docker-compose executable file. If left blank, it will be assumed that docker-compose is accessible from operating system's PATH. | `""` |
| workDir | Path to a directory that will be set as a working directory of docker-compose process. All relative paths defined in docker-compose.yml file will be resolved against this directory. If set to relative path, it will be resolved against `${project.basedir}`. If set to blank, working directory will be the same as working directory of JVM process that started this plugin which is usually a directory from which Maven was run. | `${project.basedir}` |
| file | Path to docker-compose.yml file. If relative, it will be appended to Maven's `${project.basedir}`. | `"src/test/resources/docker-compose.yml"` |
| projectName | Name of docker-compose project. It will be passed directly to docker-compose CLI. | Random 8-letter string |
| wait | Specifies how long should this plugin wait for a cluster/service to be healthy (or running if they do not implement a health check). Timeouts result in a build failure. This property is used only by "up" and "start" goals. | 1 minute |
| skip | Decides if an execution of a goal should be skipped entirely. You can also set `dockerCompose.skip` Maven property to `true` to achieve similar effect. Be advised that skipping execution of an `up` goal without skipping execution of a `down` goal may result in an unexpected behaviour if a `projectName` property is not provided explicitly. | `false` |

Properties are added to plugin's `<configuration>` descriptor. For example:

```xml
<plugin>
    <groupId>io.brachu</groupId>
    <artifactId>docker-compose-maven-plugin</artifactId>
    <version>0.9.0</version>
    <configuration>
        <projectName>myProject</projectName>
        <file>src/test/resources/custom.docker-compose.yml</file>
        <workDir>src/tests/resources</workDir>
        <env>
            <EXTERNAL_PGSQL_PORT>5432</EXTERNAL_PGSQL_PORT>
        </env>
        <wait>
            <value>15</value>
            <unit>SECONDS</unit>
        </wait>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>up</goal>
                <goal>down</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Properties of `up` goal

`up` goal can also be parametrized with following properties:

| Property | Description | Default value |
| --- | --- | --- |
| forceBuild | When set to `true`, forces docker-compose to build images for services which have their `build:` section specified in docker-compose.yml file. Behaviour is the same as passing `--build` flag to `docker-compose up` command | `false` |
| followLogs | Boolean flag that triggers the reading of docker-compose logs. The effect of setting this parameter to `true` would be the same as running `docker-compose logs -f` command in a different terminal window after this goal finishes executing, but logs will instead be redirected to `System.out` (for standard output) and `System.err` (for standard error) of JVM process that runs this goal. | `false` |

## Properties of `down` goal

`down` goal can also be parametrized with following properties:

| Property | Description | Default value |
| --- | --- | --- |
| removeVolumes | If set to `true`, removes named volumes declared in the `volumes` section of the Compose file and anonymous volumes attached to containers. | `true` |
| removeOrphans | If set to `true`, removes containers for services not defined in the Compose file. | `false` |
| downTimeoutSeconds | Specifies how long in seconds should docker-compose wait for cluster shutdown. | `10` |
| killBeforeDown | Should 'down' goal kill all containers in the cluster before removing the cluster? | `true` |

## Properties of `start` goal

`start` goal can also be parametrized with following properties:

| Property | Description | Default value |
| --- | --- | --- |
| startServices | Names of services (as specified in a compose file) that will be started during execution of this goal. Services will be started in the same order as defined by this list. Empty list indicates that all services will be started. Starting an already started service has no effect. | Empty list |

## Properties of `stop` goal

`stop` goal can also be parametrized with following properties:

| Property | Description | Default value |
| --- | --- | --- |
| stopServices | Names of services (as specified in a compose file) that will be stopped during execution of this goal. Services will be stopped in the same order as defined by this list. Empty list indicates that all services will be stopped. Stopping an already stopped service has no effect. | Empty list |
