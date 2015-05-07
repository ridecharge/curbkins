FROM registry.gocurb.internal:80/ansible

COPY jenkins-wrapper.sh /tmp/jenkins-wrapper.sh
RUN chmod 0500 /tmp/jenkins-wrapper.sh

VOLUME ["/var/run/docker.socket"]

EXPOSE 8080
ENTRYPOINT ["/tmp/jenkins-wrapper.sh"]
