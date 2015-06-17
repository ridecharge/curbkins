import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory
dslFactory.job('init-jobs') {
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
    steps {
        gradle {
            tasks('build')
        }
        dsl {
            additionalClasspath('build/libs/curbkins.jar')
            external('jobs/init-jobs.dsl')
        }
    }
    triggers {
        upstream('curbkins')
    }
}
