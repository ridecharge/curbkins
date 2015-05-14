package io.gocurb.curbkins.config

import hudson.plugins.git.GitSCM
import jenkins.model.Jenkins

/**
 * Configures the jenkins git user
 * Created by sgarlick on 5/8/15.
 */
class InstanceGit implements InstanceConfig {

    Jenkins instance
    String globalConfigName
    String globalConfigEmail

    def configure() {
        def desc = instance.getDescriptorByType(GitSCM.DescriptorImpl)
        System.println(desc)
        desc.setGlobalConfigName(globalConfigName)
        desc.setGlobalConfigEmail(globalConfigEmail)
        desc.save()
    }

    static def get() {
        return new InstanceGit(instance: Jenkins.getInstance(),
                               globalConfigName: getGlobalConfigName(),
                               globalConfigEmail: getGlobalConfigEmail())
    }

    static def getGlobalConfigName() {
        return ['curl', 'consul.gocurb.internal/v1/kv/jenkins/config/GIT_COMMIT_NAME?raw'].execute().text
    }

    static def getGlobalConfigEmail() {
        return ['curl', 'consul.gocurb.internal/v1/kv/jenkins/config/GIT_COMMIT_EMAIL?raw'].execute().text
    }
}
