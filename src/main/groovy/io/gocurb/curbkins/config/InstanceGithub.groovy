package io.gocurb.curbkins.config

import com.cloudbees.jenkins.Credential
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceGithub implements InstanceConfig{
    Jenkins instance
    def configure() {
        def oauthUser = ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_USER?raw'].execute().text
        def oauthToken = ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_TOKEN?raw'].execute().text

        def push = instance.getDescriptor("com.cloudbees.jenkins.GitHubPushTrigger")

        push.setManageHook(true)
        def creds = push.getCredentials()
        if(creds.size() == 0) {
            creds.add(new Credential(oauthUser, '', oauthToken))
        }

        push.save()
    }
}
