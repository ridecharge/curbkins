package com.gocurb.curbkins.jobs

import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import hudson.plugins.git.GitSCM
import jenkins.model.Jenkins
/**
 * Configures the jenkins git user
 * Created by sgarlick on 5/8/15.
 */
class InstanceGit {

    def instance
    def globalConfigName
    def globalConfigEmail

    def configure() {
        def desc = instance.getDescriptorByType(GitSCM.DescriptorImpl)
        desc.setGlobalConfigName(globalConfigName)
        desc.setGlobalConfigEmail(globalConfigEmail)
        desc.save()
    }

    static def get() {
        def consulConfigProvider = new ConsulCurlConfigProvider()
        return new InstanceGit(instance: Jenkins.instance,
                               globalConfigName: consulConfigProvider.gitGlobalConfigName,
                               globalConfigEmail: consulConfigProvider.gitGlobalConfigEmail)
    }

}
