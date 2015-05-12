package io.gocurb.curbkins.config

import jenkins.model.JenkinsLocationConfiguration

/**
 * Configures the instances sysadmin email address
 * Created by sgarlick on 5/8/15.
 */
class InstanceAdmin implements InstanceConfig {

    JenkinsLocationConfiguration jenkinsLocationConfiguration
    String adminEmailAddress

    def configure() {
        jenkinsLocationConfiguration.setAdminAddress(adminEmailAddress)
        jenkinsLocationConfiguration.save()
    }

    static def get() {
        return new InstanceAdmin(jenkinsLocationConfiguration: JenkinsLocationConfiguration.get(),
                                 adminEmailAddress: getAdminEmailAddress())
    }

    static def getAdminEmailAddress() {
        return ['curl', 'consul:8500/v1/kv/jenkins/config/JENKINS_ADMIN_ADDRESS?raw'].execute().text
    }
}
