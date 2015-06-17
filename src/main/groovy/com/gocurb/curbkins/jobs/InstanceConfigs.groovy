package com.gocurb.curbkins.jobs
/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigs {

    def instanceConfigs
    def dslFactory

    def create() {
        return instanceConfigs.collect { jobName, jobConfigClass ->
            return dslFactory.job(jobName) {
                label('master')
                blockOnUpstreamProjects()
                triggers {
                    upstream(jobName == 'jenkins-ssh-config' ? 'generate-config-jobs' :
                             'jenkins-ssh-config')
                }
                steps {
                    gradle {
                        tasks('build')
                    }
                    systemGroovyCommand("${jobConfigClass.name}.get().configure()") {
                        classpath('$WORKSPACE/build/libs/curbkins.jar')
                    }
                }
                scm {
                    git {
                        remote {
                            github('ridecharge/curbkins', 'https')
                            branch('master')
                        }
                    }
                }
            }
        }
    }
}
