package io.gocurb.curbkins.config

import javaposse.jobdsl.dsl.DslFactory
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs implements InstanceConfig {

    Map<String, InstanceConfig> configJobs = [
            'init/security'  : InstanceSecurity,
            'init/git'       : InstanceGit,
            'init/github'    : InstanceGithub,
            'init/hipchat'   : InstanceNotifications,
            'init/github-ssh': InstanceSshCredentials,
            'init/admin'     : InstanceAdmin,

    ]
    DslFactory dslFactory
    Jenkins jenkins

    static def get(dslFactory) {
        return new InstanceConfigJobs(jenkins: Jenkins.getInstance(),
                                      dslFactory: dslFactory)
    }

    def configure() {
        for (entry in configJobs.entrySet()) {
            dslFactory.job(entry.getKey()) {
                steps {
                    systemGroovyCommand(
                            "${entry.getValue().getClass().getName()}.get().configure()") {
                        classpath('/opt/jenkins/lib/curbkins.jar')
                    }
                }
            }
        }
    }
}
