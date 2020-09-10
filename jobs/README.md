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

Replace the jobs-section of the JCasC configuration with the following:

```yaml
jobs:
- script: |
    pipelineJob('hello-pipeline-external') {
      definition { cpsScm {
          scm {
            git {
              remote {
                url ('https://github.com/eficode-academy/jcasc-katas.git')
              }
            }
          }
          scriptPath("jobs/pipeline/Jenkinsfile")
      } }
    }
```

Restart Jenkins and visit the configuration of the "hello-pipeline-external" job.

Notice how the pipeline isn't embedded anymore.
Instead it's referencing a pipeline on a remote location.

Run the job and verify that it works.

### Optional: Change the pipeline

To change the pipeline, we need to make changes to the git repository.

Fork the `jcasc-katas` repository on GitHub and make a change to the file in
`jobs/pipeline/Jenkinsfile` either by e.g. using the online editor.

Update the JCasC configuration so it points to your repository instead of the
one owned by `eficode-academy`. NB: make sure you update the JCasC configuration
that you use when you start Jenkins. Don't update the one in your repository,
if you're starting Jenkins from the `eficode-academy` one.

### Optional: Seed jobs

So, we can make changes to our pipeline without changing the JCasC configuration,
but we're still making changes whenever we're adding new jobs, i.e.
we're appending them to the list in the `jenkins.yaml` like below:

```yaml
jobs:
- script: |
    pipelineJob('hello-pipeline-external') {
      ...
    }
    pipelineJob('another-pipeline-external') {
      ...
    }
    ...
```

It would be nice if we could have a single job inside the JCasC configuration,
which when running, would add all the other jobs, which would read the pipelines,
so we "never" had to make changes to the JCasC configuration!
(At least not when we had to make changes to the jobs that Jenkins have available.)

Such a jobs are called a Seed job, and looks like the one below:

```yaml
jobs:
  - script: |
      job('super-seed') {
        scm {
          git {
            remote {
              url ('https://github.com/eficode-academy/jcasc-katas.git')
            }
          }
        }
        steps {
          dsl {
            external('jobs/seed-demo/jobdsl/**/*.groovy')
          }
        }
      }
```

The seed job has a single build step, which is a `dsl` (JobDSL) step.
It reads all of the JobDSL-groovy files, in the `jobs/seed-demo/jobdsl`-folder.

Change the JCasC configuration to use this one, and start Jenkins.

After starting Jenkins, notice that there's only one job. Run it and see what happens.

The JobDSL specifications are in the `jobs/seed-demo/jobdsl`-folder,
each of these reference a pipeline in the `jobs/seed-demo/pipeline`-folder.

The JobDSL-files must be in the same repository as the
seed job, because they are read from the job's workspace when it's run.

The pipelines however are referenced with the URL of their respective git repositories,
inside the JobDSL files, so they could easily live in the individual repositories with their projects.

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
