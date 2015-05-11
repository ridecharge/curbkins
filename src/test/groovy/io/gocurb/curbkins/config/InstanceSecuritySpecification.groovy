package io.gocurb.curbkins.config

import hudson.security.AuthorizationStrategy
import hudson.security.SecurityRealm
import jenkins.model.Jenkins
import spock.lang.Specification

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceSecuritySpecification extends Specification {
    Jenkins jenkins
    AuthorizationStrategy authorizationStrategy
    SecurityRealm securityRealm
    InstanceSecurity instanceSecurity
    def setup() {
        authorizationStrategy = Mock(AuthorizationStrategy)
        securityRealm = Mock(SecurityRealm)
        jenkins = Mock(Jenkins)
        instanceSecurity = new InstanceSecurity(instance: jenkins, securityRealm: securityRealm, authorizationStrategy: authorizationStrategy)
    }

    def "the authorizationStrategy is set"() {
        when:
        instanceSecurity.configure()

        then:
        jenkins.setAuthorizationStrategy(authorizationStrategy)
    }

    def "the securityRealm is set"() {
        when:
        instanceSecurity.configure()

        then:
        jenkins.setSecurityRealm(securityRealm)
    }

    def "the instance is saved"() {
        when:
        instanceSecurity.configure()

        then:
        jenkins.save()
    }
}
