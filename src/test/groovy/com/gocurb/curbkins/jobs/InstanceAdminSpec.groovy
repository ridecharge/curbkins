package com.gocurb.curbkins.jobs

import jenkins.model.JenkinsLocationConfiguration
import spock.lang.Specification
/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceAdminSpecification extends Specification {
    def adminEmailAddress
    def jenkinsLocationConfiguration
    def instanceAdmin
    def setup() {
        adminEmailAddress = "sysadmin@gocurb.com"
        jenkinsLocationConfiguration = Mock(JenkinsLocationConfiguration)
        instanceAdmin = new InstanceAdmin(adminEmailAddress: adminEmailAddress,
                                          jenkinsLocationConfiguration: jenkinsLocationConfiguration)

    }

    def "the admin email address is saved"() {
        when:
        instanceAdmin.configure()

        then:
        1 * jenkinsLocationConfiguration.setAdminAddress(adminEmailAddress)
        1 * jenkinsLocationConfiguration.save()
    }
}
