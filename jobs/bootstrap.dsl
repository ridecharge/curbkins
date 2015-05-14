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
            additionalClasspath('build/libs/curbkins.jar')
            try {

                text(readFileFromWorkspace('jobs/init-jobs.dsl'))
            }
            catch (FileNotFoundException e)
            {
                external('jobs/init-jobs.dsl')
            }
        }
    }
}
