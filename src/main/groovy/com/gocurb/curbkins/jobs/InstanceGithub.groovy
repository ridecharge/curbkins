package com.gocurb.curbkins.jobs

import com.cloudbees.jenkins.Credential
import com.cloudbees.jenkins.GitHubPushTrigger
import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceGithub {

    def instance
    def username
    def oauthToken

    def configure() {
        def push = instance.getDescriptorByType(GitHubPushTrigger.DescriptorImpl)
        push.setManageHook(true)
        def creds = push.credentials
        if (creds.size() == 0) {
            creds.add(new Credential(username, '', oauthToken))
        }
        push.save()
    }

    static def get() {
        def consulConfigProvider = new ConsulCurlConfigProvider()
        return new InstanceGithub(instance: Jenkins.instance,
                                  username: consulConfigProvider.githubUsername,
                                  oauthToken: consulConfigProvider.githubOauthToken)
    }
}
