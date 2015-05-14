package io.gocurb.curbkins.jobs

import io.gocurb.curbkins.config.*
import javaposse.jobdsl.dsl.DslFactory
import jenkins.plugins.hipchat.HipChatNotifier

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs {

    static configJobs = [
            'jenkins-security-config': InstanceSecurity,
            'jenkins-git-config'     : InstanceGit,
            'jenkins-github-config'  : InstanceGithub,
            'jenkins-hipchat-config' : InstanceNotifications,
            'jenkins-ssh-config'     : InstanceSshCredentials,
            'jenkins-admin-config'   : InstanceAdmin,

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
                            """import ${entry.value.name}
                            ${entry.value.simpleName}.get().configure()""") {
                        classpath('$WORKSPACE/build/libs/curbkins.jar')
                    }
                }
                publishers {
                    configure { project ->
                        project / 'publishers' << HipChatNotifier {
                            startNotification true
                            notifyAborted true
                            notifyFailure true
                            notifyNotBuilt true
                            notifySuccess true
                            notifyUnstable true
                            notifyBackToNormal true
                        }
                    }
                }
            })
        }
        return jobs
    }
}
