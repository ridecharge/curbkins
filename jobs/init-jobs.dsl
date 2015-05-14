import groovy.io.FileType
import javaposse.jobdsl.dsl.DslFactory

def Map<String, String> jobScripts = [:]
new File("jobs").eachFileRecurse(FileType.FILES) { file ->
    def jobName = file.path.split('/')[-1].split("\\.")[0]
    if (!['init-jobs', 'bootstrap'].contains(jobName)) {
        jobScripts[jobName] = "jobs/${jobName}.dsl"
    }
}

def blockOns = ['generate-build-jobs': ['jenkins-.*-config', 'generate-config-jobs'].join('\n')]

def dslFactory = this as DslFactory
for (jobScript in jobScripts.entrySet()) {
    def jobName = jobScript.key
    def jobPath = jobScript.value
    dslFactory.job(jobName) {
        label('master')
        blockOnUpstreamProjects()
        scm {
            git {
                remote {
                    github('ridecharge/curbkins', 'https')
                    branch('master')
                }
            }
        }
        if (blockOns[jobName] != null) {
            blockOn(blockOns[jobName])
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
