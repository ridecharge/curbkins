import groovy.io.FileType
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory

def Map<String, String> jobScripts = [:]
def workSpace
try {
    workSpace = "${WORKSPACE}/"
}
catch (Exception) {
    workSpace = ""
}

new File("${workSpace}jobs").eachFileRecurse(FileType.FILES) { file ->
    def jobName = file.path.split('/')[1].split("\\.")[0]
    print(jobName)
    if (jobName != 'init-jobs') {
        jobScripts[jobName] = file.path
    }
}

for (jobScript in jobScripts.entrySet()) {
    dslFactory.job(jobScript.key) {
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
                additionalClasspath('build/libs/*')
                external(jobScript.value)
            }
        }
    }
}
