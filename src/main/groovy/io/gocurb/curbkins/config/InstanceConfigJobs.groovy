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
                steps {
                    systemGroovyCommand(
                            "${entry.getValue().getClass().getName()}.get().configure()") {
                        classpath('/opt/jenkins/lib/curbkins.jar')
                    }
                }
            })
        }
        return jobs
    }
}
