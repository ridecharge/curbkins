package io.gocurb.curbkins.jobs

import io.gocurb.curbkins.config.*
import javaposse.jobdsl.dsl.DslFactory

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs {

    static Map<String, String> configJobs = [
            'jenkins-security-config': InstanceSecurity.name,
            'jenkins-git-config'     : InstanceGit.name,
            'jenkins-github-config'  : InstanceGithub.name,
            'jenkins-hipchat-config' : InstanceNotifications.name,
            'jenkins-ssh-config'     : InstanceSshCredentials.name,
            'jenkins-admin-config'   : InstanceAdmin.name,

    ]
    DslFactory dslFactory

    def configure() {
        def jobs = []
        for (entry in configJobs.entrySet()) {
            jobs.add(dslFactory.job(entry.getKey()) {
                blockOnUpstreamProjects()
                triggers {
                    upstream('generate-config-jobs')
                }
                scm {
                    git {
                        remote {
                            github('ridecharge/curbkins', 'https')
                            branch('master')
                        }
                    }
                }
                steps {
                    gradle {
                        tasks('build')
                    }
                    systemGroovyCommand(
                            "${entry.value}.get().configure()") {
                        classpath('$WORKSPACE/build/libs/curbkins.jar')
                    }
                }
            })
        }
        return jobs
    }
}
