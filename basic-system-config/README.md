# Basic system config

Having Jenkins up and running with required plugins installed is just a first step.

Configuring it in a way that will let you proceed with specific tasks is what comes next.

In this exercise, we will use several files and folders to configure Jenkins:

* `secret/admin.txt` text file for the admin password.
* `casc-config/jenkins.yaml` file for JCasC configuration of Jenkins.
* `docker-compose.yml` file for controlling our Jenkins docker instance.

We have made a small jenkins yaml with a hello-world message in as a starter.

Now we need to wire in our configurations to the Jenkins instance.
We are using three diffrent types in Docker-compose:

* A `volume` mounting in the config file
* An `environment variable` to point JCasC towards the right folder
* A `secret` to pass in the admin password as an environment variable.

!!! https://github.com/jenkinsci/configuration-as-code-plugin/blob/master/README.md#getting-started

## starting JCasC

For the first part, we will deal with getting JCasC to read the config from a file.

### tasks

In the docker compose file:

* Add another volume mount with the jenkins configuration: `- ./casc-config:/var/jenkins_config`

* Add an environment variable to let JCasC know where the configuration folder is.

```yml
    environment:
      - CASC_JENKINS_CONFIG=/var/jenkins_config
```

* start jenkins without --build
* look at the main page: Jenkins says hello!.
* Manage Jenkins -> Configure system ->  # of executors = 3
* Try to change system message to something else
* Go to Manage Jenkins -> Configuration as Code and verify that the path for config is read from jenkins. It should display something like the following:

```text
Configuration loaded from :

    /var/jenkins_config
```

* Click "reload existing configuration"
* Observe the system message changed on the front page

Congratulations! Jenkins core is now configured via JCasC!

Now, lets head on to the next part!

## Dealing with misconfiguration

As with all configuration and coding, we all make typos, and mistakes.

And with something as important as your build servers ability to work, it can be daunting to even try to fix something, for what if it breaks?!?!

Luckily, JCasC does a dry run of your configuration before applying it. It will not make any changes if the YAML is misconfigured, but rather display an error so you can fix your mistake.

In the next exercise, you are going to misconfigure the yaml file and observe what happens.

### Tasks

* In the `jenkins.yaml`, Change systemMessage to systeMessage
* Under the Jenkins as code, Click reload
* Observe that you get the following error from Jenkins

```log
io.jenkins.plugins.casc.ConfiguratorException: Invalid configuration elements for type class jenkins.model.Jenkins : systeMessage.
```

* Correct the change again and click reload

Everything should be back to the original state agin.

## Adding a user to the system

As it is right now, everyone can go in and use our instance, so the first thing we need to adjust is to enable security, so non-authenticated users cannot change everything.

We will need to enable security in the JCasC configuration file.

First we'll create a user (under jenkins root element)

> Note: as said before, all the objects that we are creating can be found in the documentation reference for JCasC. To see it, go to: Manage Jenkins -> Configuration as Code -> Documentation

!!! Take info from here:
https://github.com/ewelinawilkosz/praqma-jenkins-casc/tree/hands_on/hands-on#how-to

### Tasks

docker-compose down and up
update jenkins yaml to add the user

```yaml
  securityRealm:
    local:
      allowsSignup: false
      users:
      - id: admin
        password: password
```

* Reload configuration `Manage Jenkins -> Configuration as Code -> Reload existing configuration`
* login with the new admin user

## Extra: Hardening the user

While we made sure that annon

> Normally you would not have your env file with password inside your git repo. Instead you would either have it ignored by git, or some other external place. For secrets handling on a larger scale, look at services like [Vault](https://www.vaultproject.io/).

We will need to both edit the docker-compose file to pass in the password file, and enabling security in the JCasC configuration file.

Show the "secret" env file

Say that 
Update docker-compose adding the env

> Extra: try to change the variable, and see that it does NOT work. **Why?**
>
> docker-compose sets the env variable at startup. Nothing to do with CasC.

## Clean up

docker-compose down

### Extra

difference between folder and file
File is only one
Folder takes all yaml files there, making it more modular (shareable between jenkins instances).


