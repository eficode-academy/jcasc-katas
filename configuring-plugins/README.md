# Configuring plugins

!!! JCasC structure in the documentation mimmics the same structure as the YAML structure

## Matrix based security

!!! Talk about matrix based security

### Task

Manage Jenkins -> Configure Global Security

* Enable security
* Authorization -> Matrix-based security
Look at it, but do not save. "leave page"

* find "matrix-auth" under https://github.com/jenkinsci/configuration-as-code-plugin/tree/master/demos
* It's a good starting example, try to implement and reload jenkins
* add `- "Job/Read:anonymous"`
* logout
* watch
* log in and make a single branch pipeline job with hello world
* Log out to see that you can still see it.

## Credentials

credentials is another root element, just like jenkins in YAML.
https://github.com/jenkinsci/configuration-as-code-plugin/tree/master/demos/credentials

We are adding SSH Credentials by adding a docker-compose secret.

Scope needs to be GLOBAL

!!! Any manually added credentials gets deleted when you reload jenkins

```
credentials:
  system:
    domainCredentials:
      - credentials:
          - basicSSHUserPrivateKey:
              scope: GLOBAL
              id: host_secret
              username: ubuntu
              description: "SSH passphrase with private key file. Private key provided"
              privateKeySource:
                directEntry:
                  privateKey: ${abc}

```

### Tasks

add secret to the docker-compose file
down and up the docker-compose
add the jenkins configuration snippet

!!! find a way to update known-hosts

!!! make a pipeline that ssh into the instance provided with the SSH credentials that we are having.
test it out

```groovy
pipeline {
   agent any

   stages {
    stage ('Deploy') {
    steps{
        sshagent(credentials : ['host_secret']) {
            sh 'ssh -oStrictHostKeyChecking=no ubuntu@104.155.51.100'
        }
    }
}
   }
}
```

## Further readings

https://github.com/jenkinsci/configuration-as-code-plugin/tree/master/demos