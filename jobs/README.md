# Configuring jobs

Prereqs:

- add the jobdsl plugin <https://plugins.jenkins.io/job-dsl/>

## List of inline pipeline jobs hardcoded into jenkins.yaml

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- jenkins.yaml, list of jobs in jenkins.yaml
- restart jenkins, every time, making changes to anything

### jobs-parameter jenkins.yaml

```yaml
  jobs: |
    pipelineJob('hello-pipeline-inline') {
      definition { cps { script("""
        pipeline {
          agent any
          stages {
            stage('Hello, World!') {
              steps {
                sh 'echo "Hello, World!"'
              }
            }
          }
        }
      """) } }
    }
```

## List of external pipeline jobs hardcoded into jenkins.yaml

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- bootstraps pipeline jobs from repositories
- restart jenkins on add/remove jobs / change job-config
- pipeline changes reloaded when pipeline runs

```yaml
  jobs: |
    pipelineJob('hello-pipeline-ext') {
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url ('https://github.com:praqma-training/jcasc-katas.git')
              }
            }
          }
          scriptPath("jobs/casc-config/pipeline/Jenkinsfile")
        }
      }
    }
```

> NB: The above links to the `praqma-training/jcasc-katas.git`-repository
> make changes so it matches your fork and reads your changes.

## jobdsl seed job hardcoded into jenkins.yaml

NB: Left as an exercise to the reader

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- bootstraps pipeline jobs from repositories
- restart jenkins on add/remove jobs / change seed-config

## Further readings
