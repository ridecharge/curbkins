package com.gocurb.curbkins.jobs

import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import jenkins.model.JenkinsLocationConfiguration
/**
 * Configures the instances sysadmin email address
 * Created by sgarlick on 5/8/15.
 */
class InstanceAdmin  {

    def jenkinsLocationConfiguration
    def adminEmailAddress

    def configure() {
        jenkinsLocationConfiguration.adminAddress = adminEmailAddress
        jenkinsLocationConfiguration.save()
    }

    static def get() {
        def consulConfigProvider = new ConsulCurlConfigProvider()
        return new InstanceAdmin(jenkinsLocationConfiguration: JenkinsLocationConfiguration.get(),
                                 adminEmailAddress: consulConfigProvider.adminEmailAddress)
    }
}
