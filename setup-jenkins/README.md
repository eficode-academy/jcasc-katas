# Setup Jenkins

Setting up Jenkins is a complex process, as both Jenkins and its plugins require some tuning and configuration, with dozens of parameters to set within the web UI, manage Jenkins section.

Many of the experienced Jenkins users rely heavily on groovy init scripts to customize jenkins and enforce a desired state. Those scripts directly invoke Jenkins' API and as such can do everything (at your own risk). But they also require you to know Jenkins and its plugins' internals, and to be confident in writing groovy scripts on top of Jenkins' API.

Configuration as Code plugin (JCasC) has been designed as an opinionated way to configure Jenkins, based on human-readable declarative configuration files. Writing such a file should be feasible without being a Jenkins expert, just translating into code the configuration one would otherwise make through the web UI.

## First boot

We'll start by learning how to startup your own Jenkins master using docker. There are two files in this folder, that we'll use to do it:

* [Dockerfile](Dockerfile)
* [docker-compose.yml](docker-compose.yml)

[Dockerfile](Dockerfile) consists of only a few lines. First we select the base docker image we'll build on top of. We set JENKINS_HOME environment variable to point to a folder we'll later map to a volume on our host machine. And we end with disabling Jenkins Setup Wizard using JAVA_OPTS environment variable.

In [docker-compose.yml](docker-compose.yml), for a time being, we have a very simple configuration, where we asked docker to build the image from Dockerfile, forward needed ports and create a volume that will be used to keep some Jenkins data persistent, even if the container goes down. (In a *Clean up* step we remove the volume, but for following exercises we may want the volume to stay)

### Start up

From within the `setup-jenkins`-folder, run the following command to start a container in the foreground:

```shell
docker-compose up
```

Once the logs print **Jenkins is fully up and running** go to the browser and try to access Jenkins by providing a valid address:

* If you run the katas on your machine use `localhost:8080`
* If you run the katas on a remote machine use `[remote machine IP]:8080`
* If you run the katas in the Google Cloud Shell - click `Web Preview` -> `Preview on port 8080`

> NB: to stop the container, press `ctrl + c`

## Clean up

To remove the container including the volumes, use the command:

```shell
docker-compose rm -fv
```

## Next step

[Installing plugins](../installing-plugins/README.md)
