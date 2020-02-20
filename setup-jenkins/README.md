# Setup Jenkins 

Setting up Jenkins is a complex process, as both Jenkins and its plugins require some tuning and configuration, with dozens of parameters to set within the web UI manage section.

Many of the experienced Jenkins users rely on groovy init scripts to customize jenkins and enforce desired state. Those scripts directly invoke Jenkins API and as such can do everything (at your own risk). But they also require you to know Jenkins and its plugins' internals, and to be confident in writing groovy scripts on top of Jenkins API.

Configuration-as-Code plugin has been designed as an opinionated way to configure Jenkins based on human-readable declarative configuration files. Writing such a file should be feasible without being a Jenkins expert, just translating into code a configuration process one is used to executing in the web UI.

## First boot

We'll start by learning how to startup your own Jenkins master using docker. There are two files in this folder, that we'll use to do it:
* [Dockerfile](Dockerfile)
* [docker-compose.yml](docker-compose.yml)

## Clean up

Remove the jenkins you just made, including the volumes

`docker-compose down -v`

## Next step

[Installing plugins](../installing-plugins/README.md)