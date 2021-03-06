package com.gocurb.curbkins.jobs

import hudson.security.AuthorizationStrategy
import hudson.security.SecurityRealm
import jenkins.model.Jenkins
import spock.lang.Specification

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceSecuritySpecification extends Specification {

    def jenkins
    def authorizationStrategy
    def securityRealm
    def instanceSecurity

    def setup() {
        authorizationStrategy = Mock(AuthorizationStrategy)
        securityRealm = Mock(SecurityRealm)
        jenkins = Mock(Jenkins)
        instanceSecurity = new InstanceSecurity(instance: jenkins, securityRealm: securityRealm,
                                                authorizationStrategy: authorizationStrategy)
    }

    def "the authorizationStrategy is set"() {
        when:
        instanceSecurity.configure()

        then:
        1 * jenkins.setAuthorizationStrategy(authorizationStrategy)
    }

    def "the securityRealm is set"() {
        when:
        instanceSecurity.configure()

        then:
        1 * jenkins.setSecurityRealm(securityRealm)
    }

    def "the instance is saved"() {
        when:
        instanceSecurity.configure()

        then:
        1 * jenkins.save()
    }
}
