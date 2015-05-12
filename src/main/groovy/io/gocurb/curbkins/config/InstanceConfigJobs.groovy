package io.gocurb.curbkins.config

import javaposse.jobdsl.dsl.DslFactory

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs implements InstanceConfig {

    static Map<String, InstanceConfig> configJobs = [
            'jenkins-security-config'      : InstanceSecurity,
            'jenkins-git-config'           : InstanceGit,
            'jenkins-jenkins-github-config': InstanceGithub,
            'hipchat-config'               : InstanceNotifications,
            'jenkins-ssh-config'           : InstanceSshCredentials,
            'jenkins-admin-config'         : InstanceAdmin,

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
                    println(entry.value.class.name)
                    systemGroovyCommand(
                            "${entry.value.class.name}.get().configure()") {
                        classpath('build/libs/*')
                    }
                }
            })
        }
        return jobs
    }
}
