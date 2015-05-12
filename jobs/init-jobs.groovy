import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory

dslFactory.job('init-jobs') {
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
            additionalClasspath('build/libs')
            text(readFileFromWorkspace('jobs/generate-config-jobs.groovy'))
        }
    }
    publishers {
        downstream('generate-config-jobs')
    }
}
