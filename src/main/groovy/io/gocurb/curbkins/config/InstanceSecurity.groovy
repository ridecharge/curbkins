package io.gocurb.curbkins.config

import hudson.security.AuthorizationStrategy
import hudson.security.Permission
import hudson.security.SecurityRealm
import jenkins.model.Jenkins
import hudson.security.GlobalMatrixAuthorizationStrategy
import org.jenkinsci.plugins.googlelogin.GoogleOAuth2SecurityRealm
import groovy.json.JsonSlurper

/**
 * Created by sgarlick on 5/8/15.
 * Configures Jenkins security via Google Login
 * and Consul for Permissions
 */
class InstanceSecurity implements InstanceConfig {

    Jenkins instance
    SecurityRealm securityRealm
    AuthorizationStrategy authorizationStrategy
    static Map<String, Permission> permissions = ['ADMINISTER': Jenkins.ADMINISTER, 'READ': Jenkins.READ]

    def configure() {
        instance.setSecurityRealm(securityRealm)
        instance.setAuthorizationStrategy(authorizationStrategy)
        instance.save()
    }

    static def get() {
        return new InstanceSecurity(instance: Jenkins.getInstance(),
                                    securityRealm: getGoogleSecurityRealm(),
                                    authorizationStrategy: getMatrixAuthorizationStrategy())
    }

    static def getMatrixAuthorizationStrategy() {
        def strategy = new GlobalMatrixAuthorizationStrategy()
        for (user in getUsers().values()) {
            strategy.add(permissions[user.jenkins_api_permission], user.email)
        }
        return strategy
    }

    static def getGoogleSecurityRealm() {
        def clientId = ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_CLIENT_ID?raw'].
                execute().text
        def clientSecret = ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_CLIENT_SECRET?raw'].
                execute().text
        def domain = ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_DOMAIN?raw'].
                execute().text
        return new GoogleOAuth2SecurityRealm(clientId, clientSecret, domain)
    }

    static def getUsers() {
        def kvs = new JsonSlurper().
                parseText(['curl', 'http://consul:8500/v1/kv/users?recurse'].execute().text)
        def users = [:]
        for (kv in kvs) {
            def split = kv.Key.split('/')
            def username = split[1]
            def prop = split[2]
            if (!users[username]) {
                users[username] = [:]
            }
            users[username][prop] = new String(kv.Value.decodeBase64())
        }
        return users
    }
}
