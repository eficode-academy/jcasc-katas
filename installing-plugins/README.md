# Plugins

Jenkins itself provide a basic functionality that let's you automate a number of things but in most cases a handful of additionally installed plugins is required in order to run a meaningful build.

Traditionally plugins can be installed manually, through UI, during Jenkins setup process or any time later. But since we're using docker we can automate the process using the script provided within the official image.

Check [preinstalling plugins](https://github.com/jenkinsci/docker#preinstalling-plugins) for detailed descirption.
Section "Script usage" is directly mentioning the method used.


In order to automate plugin installation we need to have a file containing all plugins that needs to be installed:

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
configuration-as-code:latest
ssh-agent:latest
bouncycastle-api:latest
```

It will still be possible to install plugins manually afterwards, they will persist in a volume but if you use the same file to recreate Jenkins e.g. on another machines, manually installed plugins will be missing from the installation.

## Task

Start up your Jenkins the way you did in previous exercise ([Setup Jenkins](../setup-jenkins/README.md)):

`docker-compose up -d`

Look around: go to **Manage jenkins** -> **Plugin manager** and check what are the **Installed** plugins.
For instance check if Configuration as Code plugin is there

Take your Jenkins down

`docker-compose down`

In the docker file - for example at the end of it - add a COPY command and then a RUN command

```DOCKERFILE
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
```

Save the file and rerun docker-compose command adding `--build` option to force the rebuild of the image

`docker-compose up --build -d`

Look around again:**Manage jenkins** -> **Plugin manager** and notice the **Installed** plugins list contains Configuration as Code plugin, among other newly installed plugins.

For instance check if Configuration as Code plugin is there

Plugin installation will take time, and in a normal setup, you would build the Jenkins image before running it, decreasing the downtime of the running server.

## Maintaining plugin list

!!! Talk about how you can export the list of plugins from your jenkins instance
Under script usage there is a script for extracting the list of plugins.

## Are we there yet?

Manage jenkins -> Configuration as Code page

Master has no configuration as code file set.

We need to work on that now.

Go to [Basic system config](basic-system-config/README.md)

## Clean up

`docker-compose down` not deleting the volumes we just made with installing the plugins