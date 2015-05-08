package io.gocurb.curbkins.config

import jenkins.model.JenkinsLocationConfiguration
import spock.lang.Specification

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceAdminSpecification extends Specification {
    String adminEmailAddress
    JenkinsLocationConfiguration jenkinsLocationConfiguration
    InstanceAdmin instanceAdmin
    def setup() {
        adminEmailAddress = "sysadmin@gocurb.com"
        jenkinsLocationConfiguration = Mock(JenkinsLocationConfiguration)
        instanceAdmin = new InstanceAdmin(adminEmailAddress:adminEmailAddress,
                                          jenkinsLocationConfiguration: jenkinsLocationConfiguration)

    }

    def "the admin email address is saved"() {
        when:
        instanceAdmin.configure()

        then:
        jenkinsLocationConfiguration.save()
    }
}
