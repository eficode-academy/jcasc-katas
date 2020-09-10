pipelineJob('one') {
  definition { cpsScm {
      scm {
        git {
          remote {
            url ('https://github.com/eficode-academy/jcasc-katas.git')
          }
        }
      }
      scriptPath("jobs/seed-demo/pipeline/one.Jenkinsfile")
  } }
}
