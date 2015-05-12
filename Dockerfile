FROM registry.gocurb.internal:80/jenkins

COPY curbkins-wrapper.sh /tmp/curbkins-wrapper.sh
RUN chmod 0500 /tmp/curbkins-wrapper.sh
RUN mkdir -p /var/lib/jenkins/jobs/init-jobs/
COPY init-jobs.xml /var/lib/jenkins/jobs/init-jobs/config.xml
COPY init.groovy /var/lib/jenkins/init.groovy
VOLUME ["/var/run/docker.socket"]
EXPOSE 8080
ENTRYPOINT ["/tmp/curbkins-wrapper.sh"]
