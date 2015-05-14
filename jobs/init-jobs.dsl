import groovy.io.FileType
import hudson.FilePath
import hudson.model.Executor
import javaposse.jobdsl.dsl.DslFactory

def Map<String, String> jobScripts = [:]
try {
    //find files while on slave
    FilePath jobsDir = Executor.currentExecutor().getCurrentWorkspace().child('jobs')
    for (filePath in jobsDir.list()) {
        def jobName = filePath.name.split("\\.")[0]
        if (!['init-jobs', 'bootstrap'].contains(jobName)) {
            jobScripts[jobName] = "jobs/${jobName}.dsl"
        }
    }
} catch(NullPointerException e) {
    //for tests
    new File("jobs").eachFileRecurse(FileType.FILES) { file ->
        def jobName = file.path.split('/')[-1].split("\\.")[0]
        if (!['init-jobs', 'bootstrap'].contains(jobName)) {
            jobScripts[jobName] = "jobs/${jobName}.dsl"
        }
    }
}

def blockOns = ['generate-build-jobs': 'generate-config-jobs']

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
        if(blockOns[jobName]?.trim()) {
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
                try {

                    text(readFileFromWorkspace(jobPath))
                }
                catch (FileNotFoundException e)
                {
                    external(jobPath)
                }
            }
        }
    }
}
