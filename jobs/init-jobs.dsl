import groovy.io.FileType
import hudson.model.Executor
import javaposse.jobdsl.dsl.DslFactory

def Map<String, String> jobScripts = [:]
try {
    def jobsDir = Executor.currentExecutor().getCurrentWorkspace().child('jobs')
    jobsDir.list().each { filePath ->
        def jobName = filePath.name.split("\\.")[0]
        if (!['init-jobs', 'bootstrap'].contains(jobName)) {
            jobScripts[jobName] = "jobs/${jobName}.dsl"
        }
    }
} catch (NullPointerException e) {
    //for tests
    new File("jobs").eachFileRecurse(FileType.FILES) { file ->
        def jobName = file.path.split('/')[-1].split("\\.")[0]
        if (!['init-jobs', 'bootstrap'].contains(jobName)) {
            jobScripts[jobName] = "jobs/${jobName}.dsl"
        }
    }
}

def dslFactory = this as DslFactory
jobScripts.each { jobName, jobPath ->
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
        triggers {
            upstream(jobName == 'generate-config-jobs' ? 'init-jobs' : 'jenkins-ssh-config')
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
            hipChat {
                notifyBuildStart()
                notifySuccess()
                notifyAborted()
                notifyNotBuilt()
                notifyUnstable()
                notifyFailure()
                notifyBackToNormal()
            }
        }
    }
}
