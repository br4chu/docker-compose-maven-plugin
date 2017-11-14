# Introduction

docker-compose-maven-plugin is a simple plugin which starts a docker-compose cluster before running project's integration tests and shuts it down after
integration test are finished.

## Goals

This plugin implements 2 goals:
* docker-compose:up (bound to pre-integration-test phase by default)
* docker-compose:down (bound to post-integration-test phase by default)

# Getting started

This project is still a work in progress and therefore cannot be used yet.

# Requirements

This project is dependant on [Johann](https://github.com/br4chu/johann) library and therefore inherits its requirements:

* Docker Engine 1.12+ (because of health checks)
* Docker Compose 1.14+ (because of `-f -` option)

# Usage

TODO
