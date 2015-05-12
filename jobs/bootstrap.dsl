import javaposse.jobdsl.dsl.DslFactory
def dslFactory = this as DslFactory
dslFactory.job('bootstrap') {
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
            external('jobs/init-jobs.dsl')
        }
    }
}
