# Configuring jobs

At this point you have a cool Jenkins (configured as code!)
But notice how, every time you start it, there are no jobs.

> Jobs can be added through the UI, and if you've used Jenkins Pipelines
> it becomes a lot faster because you only have to paste in
> the contents of your pipeline to make it run.
> However, this is still manual configuration,
> and we'd like to avoid as much of that as possible.

> NB: for this exercise, we've commented out the `jenkins_home` volume
> in the docker-compose.yml. To show you the practicality of JCasC,
> we're going to make Jenkins truly ephemeral.

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

The job might fail because of "Script Security" that's good!
We only want to run things we've authorized. Locate "In Process Script Approval"
under Manage Jenkins, approve the scripts and run the job again.

!!!! COMMENT BEGIN Script Security is going to be annoying with an ephemeral Jenkins. !!!!
Disable for demo purposes? - in `jenkins.yaml`

```yaml
security:
  globaljobdslsecurityconfiguration:
    useScriptSecurity: false
```

!!!! COMMENT END Script Security

Two jobs should now appear `one` and `two`, run them and verify that they work.

> The JobDSL specifications are in the `jobs/seed-demo/jobdsl`-folder,
> each of these reference a pipeline in the `jobs/seed-demo/pipeline`-folder.

> The JobDSL-files must be in the same repository as the
> seed job, because they are read from the job's workspace when it's run.

> The pipelines however are referenced with the URL of their respective git repositories,
> inside the JobDSL files, so they could easily live in the individual repositories with their projects.

### Optional: Auto-bootstrapping Seed jobs

We still have to manually run the `super-seed`-job,
to add the other jobs to our Jenkins.
Imagine if we could trigger that automatically, say, just after Jenkins has started.

There's no "nice" way of having Jenkins run a job on startup,
you could do it with what's referred to as a "startup script,"
but we would like to use JCasC configuration for this,
because it means changes in less places.

So, firstly. Making the job trigger automatically.
That can be done by adding e.g. a `cron`-trigger, set to run every 2 minutes,
like below:

```yaml
job('super-seed') {
  triggers {
    // This trigger will be overwritten, it's just here to auto-trigger _one_ build.
    cron('H/2 * * * *')
  }
  scm {
  ...
```

Add this to the JCasC configuration, start Jenkins and
notice how your job is automatically triggered!
(After a couple of minutes maybe, this is a great chance to stretch your legs!)

Great! The job runs and adds job `one` and `two` to jenkins!

> NB: You may have to approve the scripts again.

But now we have a new problem, the seed job runs _every_ 2 minutes.

> NB: At this point you will need a fork of the repository

Add a copy of the seed-job to the `jobs/seed-demo/jobdsl`-folder,
that Jenkins is currently reading JobDSL from.
Name it `seed_job.groovy` or similar.
(NB: don't use dashes in filenames of JobDSL scripts.)
Remove the `cron(..)` part from the copy.

Start Jenkins and quickly! Go to the configuration of the seed job.
If it's running on localhost, this link might work <http://localhost:8080/job/super-seed/configure>

Notice that under "Build Triggers", "Build periodically" is enabled, with the value
`H/2 * * * *`.

Revisit this page after the job has run once.
The seed job should be overwritten with the configuration from the repository.
(If you don't want to wait, you can simply click the "Build now"
and verify that the trigger is gone.)

## More resources

[Jenkins, Online Meetup: From Freestyle jobs to Pipeline, with JobDSL
](https://www.youtube.com/watch?v=uhD49XXiRqY)
[Online Meetup: Configuration as Code of Jenkins (for Kubernetes)
](https://www.youtube.com/watch?v=KB7thPsG9VA&ab_channel=Jenkins)

[JobDSL Api Reference](https://jenkinsci.github.io/job-dsl-plugin/)
[Pipeline Syntax](https://www.jenkins.io/doc/book/pipeline/syntax/)
