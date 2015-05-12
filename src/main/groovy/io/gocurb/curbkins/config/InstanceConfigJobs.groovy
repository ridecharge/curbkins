package io.gocurb.curbkins.config

import javaposse.jobdsl.dsl.DslFactory

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs implements InstanceConfig {

    static Map<String, String> configJobs = [
            'jenkins-security-config'      : InstanceSecurity.name,
            'jenkins-git-config'           : InstanceGit.name,
            'jenkins-jenkins-github-config': InstanceGithub.name,
            'jenkins-hipchat-config'       : InstanceNotifications.name,
            'jenkins-ssh-config'           : InstanceSshCredentials.name,
            'jenkins-admin-config'         : InstanceAdmin.name,

    ]
    DslFactory dslFactory

    def configure() {
        def jobs = []
        for (entry in configJobs.entrySet()) {
            jobs.add(dslFactory.job(entry.getKey()) {
                scm {
                    git {
                        remote {
                            github('ridecharge/curbkins', 'https')
                            branch('master')
                        }
                    }
                }
                steps {
                    println(entry.value)
                    systemGroovyCommand(
                            "${entry.value}.get().configure()") {
                        classpath('build/libs/*')
                    }
                }
            })
        }
        return jobs
    }
}
