# Configuring jobs

Prereqs:

- add the jobdsl plugin <https://plugins.jenkins.io/job-dsl/>

## Endless list of problems

### Problem: manually configure jobs after restart

Solution: inline pipeline jobs into jenkins.yaml

### Problem: change by modification in jenkins.yaml

Solution: external pipeline jobs, referenced in jenkins.yaml

### Problem: addition by modification in jenkins.yaml

Solution: left as an exercise to the reader, seed-jobs (super-seed-jobs)

## List of inline pipeline jobs hardcoded into jenkins.yaml

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- jenkins.yaml, list of jobs in jenkins.yaml
- restart jenkins, every time, making changes to anything

### jobs-parameter in jenkins.yaml

```yaml
jenkins:
  systemMessage: "Jenkins says hello world :)\n\n"
  jobs:
  - script: |
      // jobDSL
```

JobDSL:

```groovy
pipelineJob('hello-pipeline-inline') {
  definition { cps { script("""
  // pipeline script
  """) } }
}
```

JobDsl Inlined in `jenkins.yaml`:

```yaml
  ...
  jobs:
  - script: |
      pipelineJob('hello-pipeline-inline') {
        definition { cps { script("""
          // pipeline script
        """) } }
      }
```

Pipeline in `pipeline/Jenkinsfile`, inlined below:

```yaml
  jobs:
  - script: |
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

> NB: when you add more pipeline jobs, the names must be unique,
> otherwise they will overwrite eachother.

## List of external pipeline jobs hardcoded into jenkins.yaml

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- bootstraps pipeline jobs from repositories
- restart jenkins on add/remove jobs / change job-config
- pipeline changes reloaded when pipeline runs

```yaml
  jobs:
  - script: |
      pipelineJob('hello-pipeline-ext') {
        definition { cpsScm {
            // git-config
            // script location
        } }
      }
```

```yaml
  jobs:
  - script: |
      pipelineJob('hello-pipeline-ext') {
        definition { cpsScm {
            scm {
              git {
                remote {
                  url ('https://github.com:praqma-training/jcasc-katas.git')
                }
              }
            }
            scriptPath("jobs/casc-config/pipeline/Jenkinsfile")
        } }
      }
```

> NB: The above links to the `praqma-training/jcasc-katas.git`-repository
> make changes so it matches your fork and reads your changes.

## jobdsl seed job hardcoded into jenkins.yaml

NB: Left as an exercise to the reader

See: <https://www.youtube.com/watch?v=uhD49XXiRqY>

- Jobdsl i jenkins.yaml, changes in jenkins.yaml
- bootstraps pipeline jobs from repositories
- restart jenkins on add/remove jobs / change seed-config

## Further readings

- ref jobdsl
- ref pipeline
