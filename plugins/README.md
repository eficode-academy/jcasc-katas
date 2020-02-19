# Plugins

!!!Intro to installing plugins

We have a file containing all plugins that needs to be installed:

https://github.com/jenkinsci/docker#preinstalling-plugins

Section "Script usage" is directly mentioning the method used.

```txt
credentials:latest
git:latest
ssh-slaves:latest
warnings:latest
matrix-auth:latest
job-dsl:latest
workflow-aggregator:latest
jdk-tool:latest
command-launcher:latest
```

Note: all admin can install plugins afterwards manually, they will persist in the volume, but not if recreated from scratch.

## Task

Before adding plugin file
Manage jenkins -> plugin manager -> installed plugins

In the docker file add a COPY command and then a run command

```DOCKERFILE
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
```

`docker-compose up --build`

Manage jenkins -> plugin manager -> installed plugins -> Configuration as code

!!! This will take time, and in a normal setup, you would build the Jenkins image before running it, decreasing the downtime of the running server.

## Maintaining plugin list

!!! Talk about how you can export the list of plugins from your jenkins instance
Under script usage there is a script for extracting the list of plugins.

## Are we there yet?

Manage jenkins -> Configuration as Code page

Master has no configuration as code file set.

We need to work on that now.

Go to [Basic system config](basic-system-config/README.md)
