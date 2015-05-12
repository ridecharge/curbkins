package io.gocurb.curbkins.config

import com.cloudbees.jenkins.Credential
import com.cloudbees.jenkins.GitHubPushTrigger
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceGithub implements InstanceConfig {

    Jenkins instance
    String username
    String oauthToken

    def configure() {
        def push = instance.getDescriptorByType(GitHubPushTrigger.DescriptorImpl)
        push.setManageHook(true)
        def creds = push.getCredentials()
        if (creds.size() == 0) {
            creds.add(new Credential(username, '', oauthToken))
        }
        push.save()
    }

    static def get() {
        return new InstanceGithub(instance: Jenkins.getInstance(),
                                  username: getUsername(),
                                  oauthToken: getOauthToken())
    }

    static def getUsername() {
        return ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_USER?raw'].execute().text
    }

    static def getOauthToken() {
        return ['curl', 'consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_TOKEN?raw'].execute().text.
                execute().text
    }
}
