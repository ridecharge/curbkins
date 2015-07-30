package com.gocurb.curbkins.jobs

import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import hockeyapp.HockeyappRecorder
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 6/25/15.
 */
class InstanceHockeyApp {

    def instance
    def defaultToken

    def configure() {
        def desc = instance.instance.getDescriptorByType(HockeyappRecorder.DescriptorImpl)
        desc.defaultToken = defaultToken
        desc.save()
    }

    static def get() {
        def configProvider = new ConsulCurlConfigProvider()
        return new InstanceHockeyApp(instance: Jenkins.instance,
                                     defaultToken: configProvider.hockeyAppToken)
    }
}
