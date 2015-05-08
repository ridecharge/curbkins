FROM registry.gocurb.internal:80/jenkins

COPY jenkins-wrapper.sh /tmp/curbkins-wrapper.sh
RUN chmod 0500 /tmp/curbkins-wrapper.sh

VOLUME ["/var/run/docker.socket"]
EXPOSE 8080
ENTRYPOINT ["/tmp/curbkins-wrapper.sh"]
