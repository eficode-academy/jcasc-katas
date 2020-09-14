# Tools (draft)

Tools are one of the the essential items in Jenkins that you can use in
pipelines.  JCasC provides a mechanism to configure them, provided that they're
installed on the instance.  In this kata, we'll only the Jenkins master for
convenience. The Jenkins image that we use in the [Dockerfile](Dockerfile) will
install Maven and Groovy, which we'll use in the exercises.

By default, Jenkins does not configure any tools for you. This means that your
jobs will fail when trying to use tools. You can try to create a sample
pipeline. As a pre-exercise you can configure a pipeline based on a template in
Jenkins. **New Item** -> **Pipeline** and then choose a name. Under the
definition, you can select "Github + Maven" from the "try sample Pipeline..."
drop-down menu. You don't need to edit the script, so just save it, and then
subsequently start it. The job will be unsuccessful, because we have not yet
configured any tools.

JCasC supports these tools:

- git
- jdk
- jgit
- jgitapache
- maven

## Tasks

Since Maven is installed in the Docker image, we can add it very easily to the
list of the tools.

```
tool:
  maven:
    installations:
      - name: "maven"
        home: /usr/bin/mvn
```

You can now try to re-run the pipeline that we just configured before, and
check that it doesn't fail.

# Additional resources
