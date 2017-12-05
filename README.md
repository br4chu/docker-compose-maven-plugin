# Introduction

**docker-compose-maven-plugin** starts a docker-compose cluster before running project's integration tests and shuts it down after integration test are finished.

# Getting started

1. Put your *docker-compose.yml* file in your project's *src/test/resources* directory.

2. Add docker-compose-maven-plugin to your build descriptor as follows:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.brachu</groupId>
            <artifactId>docker-compose-maven-plugin</artifactId>
            <version>0.1.0-SNAPSHOT</version>
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

3. Run your integration tests with `mvn verify` command and you should see your docker-compose cluser turning on before tests and shutting down after tests are
done.

# Requirements

* Maven 3+

This project also depends on [Johann](https://github.com/br4chu/johann) library and inherits its requirements:

* Docker Engine 1.12+ (because of health checks)
* Docker Compose 1.14+ (because of `-f -` option)

# Usage

## Goals

docker-compose-maven-plugin implements 2 goals:
* docker-compose:up (bound to pre-integration-test phase by default)
* docker-compose:down (bound to post-integration-test phase by default)

## Parameters

docker-compose-maven-plugin has following parameters:

| Parameter | Description |
| --- | --- |
| env | A map of environment variables that will be passed to docker-compose CLI. |
| executablePath | Path to docker-compose executable file. If left blank, it will be assumed that docker-compose is accessible from operating system's PATH. |
| file | Path to docker-compose.yml file. If relative, it will be appended to Maven's "basedir". Default value is "src/test/resources/docker-compose.yml". |
| projectName | Name of docker-compose project. It will be passed directly to docker-compose CLI. If not specified, a random 8-letter string will be used instead. |
| wait | Specifies how long should this plugin wait for all containers within a cluster to be healthy or running (if they do not implement a health check). Timeouts result in build failure. By default plugin will wait 1 minute for cluster to be up and running. |

Parameters are added to plugin's `<configuration>` descriptor. For example:

```xml
<plugin>
    <groupId>io.brachu</groupId>
    <artifactId>docker-compose-maven-plugin</artifactId>
    <version>0.1.0-SNAPSHOT</version>
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
