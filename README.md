# Introduction

**docker-compose-maven-plugin** starts a docker-compose cluster before running project's integration tests and shuts it down after integration tests are
finished.

# Getting started

1. Put your *docker-compose.yml* file in your project's *src/test/resources* directory.

2. Add docker-compose-maven-plugin to your build descriptor as follows:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.brachu</groupId>
            <artifactId>docker-compose-maven-plugin</artifactId>
            <version>0.6.0</version>
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

# Requirements

* JDK8+
* Maven 3+

This project also depends on [Johann](https://github.com/br4chu/johann) library and inherits its requirements:

* Docker Engine 17.06+
* Docker Compose 1.18+

# Usage

## Goals

docker-compose-maven-plugin implements 2 goals:
* docker-compose:up (bound to pre-integration-test phase by default)
* docker-compose:down (bound to post-integration-test phase by default)

## Parameters

Both `up` and `down` goals have following parameters:

| Parameter | Description | Default value |
| --- | --- | --- |
| env | A map of custom environment variables that will be passed to docker-compose CLI. | Empty map |
| executablePath | Path to docker-compose executable file. If left blank, it will be assumed that docker-compose is accessible from operating system's PATH. | `""` |
| file | Path to docker-compose.yml file. If relative, it will be appended to Maven's "basedir". | `"src/test/resources/docker-compose.yml"` |
| projectName | Name of docker-compose project. It will be passed directly to docker-compose CLI. | Random 8-letter string |
| wait | Specifies how long should this plugin wait for all containers within a cluster to be healthy (or running if they do not implement a health check). Timeouts result in build failure. | 1 minute |

Parameters are added to plugin's `<configuration>` descriptor. For example:

```xml
<plugin>
    <groupId>io.brachu</groupId>
    <artifactId>docker-compose-maven-plugin</artifactId>
    <version>0.6.0</version>
    <configuration>
        <projectName>myProject</projectName>
        <file>src/test/resources/custom.docker-compose.yml</file>
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

## Parameters of `down` goal

`down` goal can also be parametrized with following properties:

| Parameter | Description | Default value |
| --- | --- | --- |
| removeVolumes | If set to `true`, removes named volumes declared in the `volumes` section of the Compose file and anonymous volumes attached to containers. | `true` |
| removeOrphans | If set to `true`, removes containers for services not defined in the Compose file. | `false` |
| downTimeoutSeconds | Specifies how long in seconds should docker-compose wait for cluster shutdown. | `10` |
| killBeforeDown | Should 'down' goal kill all containers in the cluster before removing the cluster? | `true` |
