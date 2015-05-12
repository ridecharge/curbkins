import io.gocurb.curbkins.jobs.InstanceConfigJobs
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory

new InstanceConfigJobs(dslFactory: dslFactory).configure()
