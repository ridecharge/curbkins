#!/bin/sh
export JENKINS_HOME=/var/lib/jenkins

mkdir /root/.ssh/
curl consul:8500/v1/kv/jenkins/config/GITHUB_PRIVATE_SSH_KEY\?raw > /root/.ssh/id_rsa
chmod 0400 /root/.ssh/id_rsa

exec /usr/bin/java -jar /usr/share/jenkins/jenkins.war
