# Connecting agents

JCasC can also be used to configure the external agents, and how they're
connected. In this kata, we'll spin up a couple of extra containers, and let
the Jenkins master node connect to them.

Build agents are quite useful for delegating jobs to other servers, and
ensuring a proper load, so you have a fast response time on your jobs.

In this exercise, we will use several files and folders to configure Jenkins:

- `casc-config/jenkins.yml` file for JCasC configuration of Jenkins.
- `docker-compose.yml` file for controlling our Jenkins docker instances.

Please note that the `docker-compose.yml` contains two containers that are
specified using a image that contains Java, in addition to the instance of our
custom image. This is because the Jenkins master will configure the agents, and
communicate the configuration using SSH. If you haven't done the [SSH
katas](../configuring-ssh/README.md), it's a good idea if you aren't so
familiar with the concepts.

## First steps

The Jenkins master will use the SSH protocol and matching keys to transfer the
Java packages to configure the agents.

We will create a public private keypair for the master and agents to talk to
each other.
Docker will mount the keypair from your host into the master container when
running docker-compose up.
The same key will be used in the agents for allowing SSH to connect.

After this step we'll gradually add the configuration to configure the nodes.

### Tasks

- Run the `setup.sh` script which will generate a set of SSH keys.
- Start the containers `docker-compose up --build -d`
- Go into `<yourIP>:8080` to check that Jenkins starts up right

### Import the keys to jenkins

Jenkins needs to know about the key as well, which is stored as a credential.
You will use the same approach as in
[configuring plugins section](../configuring-plugins/README.md).

You are going to use this template and paste it into `casc-config/jenkins.yml`,
at the bottom of the file, replacing the `# paste the private key` part with your
key.

```yaml
credentials:
  system:
    domainCredentials:
      - credentials:
          - basicSSHUserPrivateKey:
              scope: SYSTEM
              id: ssh_with_passphrase_provided
              username: jenkins
              description: "SSH passphrase with private key file."
              privateKeySource:
                directEntry:
                  privateKey: |
                    # paste the private key
```

### Tasks

- Paste the example above into the JCasC config YAML.
- Replace the comment with your private key found in the `keys` folder
- Reload JCasC config to make sure the syntax is correct.
- Observethat you under `manage jenkins -> credentials` now have a key called
  ssh_with_passphrase_provided

> NOTE: If you're in doubt about the syntax of YAML, you can see [this
> guide](https://yaml-multiline.info/) which lets you input various
> options for the line breaking.

### Adding agents

Next, we need to connect the two agents. JCasC provides different options for the
agents. You'll note that in one of the agents, there's a `numExecutors: 4`. The
recommendation is that the number of executors should be based on the number of
cores on the machine, minus one or two for best utilization.

The `labelString` directive is useful for telling Jenkins how this agent can be
used best. When a job specifies `agent { label 'docker' }`, Jenkins will pick
either of them. The labels are not restricted, so you can add as many as you'd
like, and even neglect it.

```yaml
jenkins:
  # ....
  nodes:
    - permanent:
        labelString: "linux docker test"
        mode: NORMAL
        name: "utility-node"
        remoteFS: "/var/lib/jenkins"
        launcher:
          SSHLauncher:
            host: "ben"
            port: 22
            credentialsId: ssh_with_passphrase_provided
            launchTimeoutSeconds: 60
            maxNumRetries: 3
            retryWaitTime: 30
    - permanent:
        labelString: "linux docker test"
        mode: NORMAL
        name: "utility-node-2"
        numExecutors: 4
        remoteFS: "/var/lib/jenkins"
        launcher:
          SSHLauncher:
            host: "jerry"
            port: 22
            credentialsId: ssh_with_passphrase_provided
            launchTimeoutSeconds: 60
            maxNumRetries: 3
            retryWaitTime: 30
```

### Tasks

- Paste the section above into your configuration under the `jenkins` section
- Reload the new configuration, and see that Jenkins adds two new agents
  under the master node.
- Note that there's also a difference in the number of executors for the two new
  agents.

> Note: Under the log for the two new agents,
> you can see this warning. This is because Jenkins logs into the agents for the
> first time, and no verification has been performed. You can safely ignore this.
> It's similar to when you SSH into a new machine where you'll be prompted about
> the identity of the new machine.

```text
[08/21/20 15:24:27] [SSH] WARNING: SSH Host Keys are not being verified.
Man-in-the-middle attacks may be possible against this connection.
```

## Other methods

We've covered how to add new nodes using the SSH protocol where the master
connects to the agent. In addition to this,Jenkins can start machines using
[the swarm plugin](https://plugins.jenkins.io/swarm/).

To illustrate this, we have created a Dockerfile with the swarm agent on.
It is called [Dockerfile.swarm.agent](Dockerfile.swarm.agent)

To add the swarm agent to the mix, this snippet needs to be pasted into the
`docker-compose.yml` file:

```yaml
swarm:
  build:
    context: .
    dockerfile: Dockerfile.swarm.agent
  networks:
    - jenkins-net
```

### Tasks

- add the `swarm` plugin to [plugins.txt](plugins.txt)
- update the `docker-compose.yml` file, adding a new container using the
  `Dockerfile.swarm.agent` file instead.
- Do a `docker-compose up -d` to start the new container, and rebuild Jenkins.
- Log into Jenkins and under nodes, make sure you see the new node added.

## Note

The master/slaves has been a term from when Jenkins was created. In the light
of replacing vocabulary referencing slavery in the software industry, this is
unfortunate legacy that is out of our hands. This means that you'll meet in
Jenkins context the terms agents and slaves, which are used for the same
concept, basically another machine/instance that does work. The master term is
still actively used.

### Clean-up

`docker-compose down -v`

## Additional reading

The [Jenkins WIKI](https://wiki.jenkins.io/display/JENKINS/Distributed+builds)
has more information on distributed builds.
