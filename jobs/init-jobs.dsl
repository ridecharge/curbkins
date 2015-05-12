import io.gocurb.curbkins.jobs.InstanceConfigJobs
import io.gocurb.curbkins.jobs.InstanceJobs
import javaposse.jobdsl.dsl.DslFactory

def workSpace
try {
    // Jenkins Build variable
    workSpace = "${WORKSPACE}/"
}
catch (Exception) {
    // Not defined means we in test
    workSpace = ""
}

def Map<String, Class<InstanceJobs>> configs = ['generate-config-jobs.dsl': InstanceConfigJobs]
def jobNames = configs.keySet()
def dslFactory = this as DslFactory
for (jobName in jobNames) {
    def jobPath = "jobs/${jobName}"
    dslFactory.job(jobName) {
        scm {
            git {
                remote {
                    github('ridecharge/curbkins', 'https')
                    branch('master')
                }
            }
        }
        triggers {
            githubPush()
        }
        steps {
            gradle {
                tasks('build')
            }
            dsl {
                additionalClasspath('build/libs/curbkins.jar')
                external(jobPath)
            }
        }
        publishers {
            downstream("")
        }
    }
}
