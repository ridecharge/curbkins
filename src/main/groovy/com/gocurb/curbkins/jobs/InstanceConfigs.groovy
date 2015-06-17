package com.gocurb.curbkins.jobs
/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceConfigs {
    def instanceConfigs
    def dslFactory

    def create() {
        def jobs = []

        instanceConfigs.each { jobName, jobConfigClass ->
            jobs.add(dslFactory.job(jobName) {
                label('master')
                blockOnUpstreamProjects()
                triggers {
                    upstream('generate-config-jobs')
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
            })
        }
        return jobs
    }
}
