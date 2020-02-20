# Basic system config

secret
jenkins.yaml
change in docker-compose

We have made a small jenkins yaml with a hello-world message in.
Need to mount in the file to docker, and set ENV variable.

https://github.com/jenkinsci/configuration-as-code-plugin/blob/master/README.md#getting-started

difference between folder and file
File is only one
Folder takes all yaml files there, making it more modular (shareable between jenkins instances).

### tasks

compose file should be changed

* Mount in the folder in volume section
* Added env variable

start jenkins without --build

look at the main page: Jenkins says hello!.

Manage Jenkins -> Configure system ->  # of executors = 3

* change system message
* Manage Jenkins -> Configuration as Code

```text
Configuration loaded from :

    /var/jenkins_config
```
Click "reload existing configuration"

* Observe the system message changed

## Dealing with misconfiguration

!!!talk about misconfig. JCasC does a dry run, will not make any changes if the YAML is misconfigured

### Tasks

Change systemMessage to systeMessage,
Click reload
Observe:
io.jenkins.plugins.casc.ConfiguratorException: Invalid configuration elements for type class jenkins.model.Jenkins : systeMessage.

Correct the change again and click reload

## Adding a user to the system

Adding

!!! Take info from here:

https://github.com/ewelinawilkosz/praqma-jenkins-casc/tree/hands_on/hands-on#how-to

Show the "secret" env file

Say that normally you would not have your env file with password inside your git repo.

Accessing documentation

* Manage Jenkins -> Configuration as Code -> Documentation

### Tasks

Update docker-compose adding the env
docker-compose down and up
update jenkins yaml to add the user
reload the config
login with the new user

try to change the variable, and see that it does NOT work; docker-compose sets the env variable at startup. Nothing to do with CasC.

## Clean up

docker-compose down
