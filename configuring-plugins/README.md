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

We are adding SSH Credentials


### Tasks

update the env file
down and up the docker-compose
add the jenkins configuration snippet

!!! make a pipeline that ssh into the instance provided with the SSH credentials that we are having.
test it out

## Further readings

https://github.com/jenkinsci/configuration-as-code-plugin/tree/master/demos