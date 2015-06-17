package com.gocurb.curbkins.jobs
import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import hudson.security.GlobalMatrixAuthorizationStrategy
import jenkins.model.Jenkins
import org.jenkinsci.plugins.googlelogin.GoogleOAuth2SecurityRealm
/**
 * Created by sgarlick on 5/8/15.
 * Configures Jenkins security via Google Login
 * and Consul for Permissions
 */
class InstanceSecurity {

    def instance
    def securityRealm
    def authorizationStrategy

    def configure() {
        instance.setSecurityRealm(securityRealm)
        instance.setAuthorizationStrategy(authorizationStrategy)
        instance.save()
    }

    static def get() {
        def consulConfigProvider = new ConsulCurlConfigProvider()
        return new InstanceSecurity(instance: Jenkins.getInstance(),
                                    securityRealm: getGoogleSecurityRealm(consulConfigProvider),
                                    authorizationStrategy: getMatrixAuthorizationStrategy(consulConfigProvider))
    }

    static def getMatrixAuthorizationStrategy(consulConfigProvider) {
        def permissions = ['ADMINISTER': Jenkins.ADMINISTER, 'READ': Jenkins.READ]
        def strategy = new GlobalMatrixAuthorizationStrategy()
        for (user in consulConfigProvider.usersProperties) {
            strategy.add(permissions[user.jenkins_api_permission], user.email)
        }
        return strategy
    }

    static def getGoogleSecurityRealm(consulConfigProvider) {
        def clientId = consulConfigProvider.googleSecurityRealmClientId
        def clientSecret = consulConfigProvider.googleSecurityRealmClientSecret
        def domain = consulConfigProvider.googleSecurityRealmAuthDomain
        return new GoogleOAuth2SecurityRealm(clientId, clientSecret, domain)
    }
}
