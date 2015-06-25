package com.gocurb.curbkins.jobs

import hockeyapp.HockeyappApplication
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 6/25/15.
 */
class InstanceHockeyApp {
    def instance

    def configure() {
        def desc = instance.instance.getDescriptorByType(HockeyappApplication.DescriptorImpl)
        desc.save()
    }

    static def get() {
        return new InstanceHockeyApp(instance: Jenkins.instance)
    }
}
