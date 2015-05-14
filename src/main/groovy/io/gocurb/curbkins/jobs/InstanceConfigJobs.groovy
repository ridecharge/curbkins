package io.gocurb.curbkins.jobs
import io.gocurb.curbkins.config.*
import javaposse.jobdsl.dsl.DslFactory
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
            'jenkins-cloud-config'   : InstanceCloud,

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
                    systemGroovyCommand("${entry.value.name}.get().configure()") {
                        classpath("/var/lib/jenkins/jobs/${name}/workspace/build/libs/curbkins.jar")
                    }
                }
            })
        }
        return jobs
    }
}
