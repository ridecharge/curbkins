import io.gocurb.curbkins.config.InstanceConfigJobs
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory

dslFactory.job('generate-config-jobs') {
    for(jobEntry in InstanceConfigJobs.configJobs) {

    }
}
jobs = InstanceConfigJobs.get(dslFactory).configure()
