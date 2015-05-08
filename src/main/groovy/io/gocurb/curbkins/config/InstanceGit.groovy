package io.gocurb.curbkins.config

import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceGit implements InstanceConfig {
    Jenkins instance
    def configure() {
        def desc = instance.getDescriptor("hudson.plugins.git.GitSCM")
        def globalConfigName = ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_TOKEN?raw'].execute().text
        def globalConfigEmail = ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_TOKEN?raw'].execute().text

        desc.setGlobalConfigName(globalConfigName)
        desc.setGlobalConfigEmail(globalConfigEmail)
        desc.save()
    }
}
