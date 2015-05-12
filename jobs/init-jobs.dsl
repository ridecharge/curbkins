import groovy.io.FileType
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

def Map<String, String> jobScripts = [:]
new File("${workSpace}jobs").eachFileRecurse(FileType.FILES) { file ->
    def jobName = file.path.split('/')[-1].split("\\.")[0]
    if (!['init-jobs', 'bootstrap'].contains(jobName)) {
        jobScripts[jobName] = "jobs/${jobName}.dsl"
    }
}

def dslFactory = this as DslFactory
for (jobScript in jobScripts.entrySet()) {
    def jobName = jobScript.key
    def jobPath = jobScript.value
    dslFactory.job(jobName) {
        blockOnUpstreamProjects()
        scm {
            git {
                remote {
                    github('ridecharge/curbkins', 'https')
                    branch('master')
                }
            }
        }
        triggers {
            upstream('init-jobs')
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
    }
}
