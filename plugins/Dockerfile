FROM jenkins/jenkins:2.204.2

LABEL maintainer="ewe@praqma.net"

ENV JENKINS_HOME /var/jenkins_home

ARG JAVA_OPTS
ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"
