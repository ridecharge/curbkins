package com.gocurb.curbkins.jobs

import com.cloudbees.jenkins.GitHubPushTrigger
import com.google.common.collect.Lists
import jenkins.model.Jenkins
import spock.lang.Specification

/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceGithubSpecification extends Specification {

    def instanceGithub
    def jenkins
    def descriptor
    def username = "username"
    def oauthToken = "oauthToken"

    def setup() {
        jenkins = Mock(Jenkins)
        descriptor = Mock(GitHubPushTrigger.DescriptorImpl)
        jenkins.getDescriptorByType(GitHubPushTrigger.DescriptorImpl) >> descriptor
        descriptor.getCredentials() >> Lists.newArrayList()

        instanceGithub = new InstanceGithub(instance: jenkins, username: username,
                                            oauthToken: oauthToken)
    }

    def "the github credentials are set"() {
        when:
        instanceGithub.configure()

        then:
        def creds = descriptor.getCredentials()
        creds.size() == 1
        def cred = creds.iterator().next()
        cred.oauthAccessToken == oauthToken
        cred.username == username

    }

    def "the github hooks are automanaged"() {
        when:
        instanceGithub.configure()

        then:
        1 * descriptor.setManageHook(true)
    }

    def "the github configuration is saved"() {
        when:
        instanceGithub.configure()

        then:
        1 * descriptor.save()
    }
}
