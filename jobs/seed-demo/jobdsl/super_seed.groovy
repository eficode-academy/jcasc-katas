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
