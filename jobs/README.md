# Configuring jobs

At this point you have a cool Jenkins (configured as code!)
But notice how, every time you start it, there are no jobs.

> Jobs can be added through the UI, and if you've used Jenkins Pipelines
> it becomes a lot faster because you only have to paste in
> the contents of your pipeline to make it run.
> However, this is still manual configuration,
> and we'd like to avoid as much of that as possible.

## First steps

Look in the `pipeline/Jenkinsfile`, this is an example of a simple
Jenkins pipeline with a single `shell`-step that runs `echo "Hello, World!"`.

Look in the `casc-config/jenkins.yaml` file, and notice how the pipeline has been
embedded in a JobDSL `pipelineJob`-configuration.
This is the simplest way of adding a job to Jenkins.

> ### Pipelines or Jobs?
> A job like a "freestyle job" or "pipeline job" refers to the components
> in Jenkins that we can "Build Now" to make Jenkins or attached agents do work for us.
> The actual work is defined in a "freestyle job" as build steps, and
> in a pipeline job as a _pipeline_.

> To add the job to Jenkins we use `JobDSL` a "job configuration as code"
> plugin. (We could actually make both the "job"- and "buildsteps"- or
> "pipeline"-parts in JobDSL, but the Pipeline-format is newer and nicer.
> We use the JobDSL plugin for the job-part and Pipeline plugin for the
> pipeline-part.

### Tasks

Start Jenkins and look at the `hello-pipeline-inline` job we've created.

Look at the configuration of the job and compare it to the contents of the
`casc-config/jenkins.yaml`.

What problems can you think of with this approach?
For instance, how do we make changes to what's being done when we run the job?

- If we make the changes in the UI and click *Save*, they won't be persistent
  and will be reverted when we restart Jenkins.
- If we want to make persistent changes, we have to change the JCasC configuration.
  But then we have to restart Jenkins or otherwise reload the configuration to
  see the changes in the running instance.
  Futhermore, when we make changes to the job, we're making changes to the
  JCasC configuration and may accidentally break something in other parts.
  This is called "change by modification" and is an anti-pattern.

### Change by Addition

It would be much nicer if our `pipelineJob` referenced a pipeline that was
located elsewhere, so we could just make changes to e.g. he `pipeline/Jenkinsfile`
and Jenkins would reload it when we clicked `Build Now` on the job.

Change the contents of the `pipelineJob` in the JCasC configuration to,

```Jenkinsfile
```

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
