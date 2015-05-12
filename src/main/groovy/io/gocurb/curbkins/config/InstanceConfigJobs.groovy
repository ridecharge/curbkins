package io.gocurb.curbkins.config
import javaposse.jobdsl.dsl.DslFactory
/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigJobs implements InstanceConfig {

    static Map<String, InstanceConfig> configJobs = [
            'init/security'  : InstanceSecurity,
            'init/git'       : InstanceGit,
            'init/github'    : InstanceGithub,
            'init/hipchat'   : InstanceNotifications,
            'init/github-ssh': InstanceSshCredentials,
            'init/admin'     : InstanceAdmin,

    ]
    DslFactory dslFactory

    static def get(dslFactory) {
        return new InstanceConfigJobs(
                                      dslFactory: dslFactory)
    }

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
