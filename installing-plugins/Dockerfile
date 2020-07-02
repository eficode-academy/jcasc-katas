FROM jenkins/jenkins:2.235.1

LABEL maintainer="ewe@praqma.net"

ENV JENKINS_HOME /var/jenkins_home

ARG JAVA_OPTS
ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"
