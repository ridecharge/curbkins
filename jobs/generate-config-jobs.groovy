import io.gocurb.curbkins.config.InstanceConfigJobs
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory

jobs =  new InstanceConfigJobs(dslFactory: dslFactory).configure()
