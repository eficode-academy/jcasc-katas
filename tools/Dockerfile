FROM jenkins/jenkins:2.235.2

LABEL maintainer="ewe@praqma.net"

ENV JENKINS_HOME /var/jenkins_home
ENV DEBIAN_FRONTEND=noninteractive

ARG JAVA_OPTS
ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"

USER root

RUN apt-get update && \
  apt-get install -qq -y --no-install-recommends maven groovy &&	\
  rm -rf /var/lib/apt/lists/*

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
