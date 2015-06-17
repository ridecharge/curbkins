package com.gocurb.curbkins.jobs

import com.cloudbees.plugins.credentials.Credentials
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.google.common.collect.Lists
import spock.lang.Specification

/**
 * Created by sgarlick on 5/11/15.
 */
class IntanceSshCredentialsSpecification extends Specification {

    def systemCredentialsProvider
    def instanceSshCredentials
    def credentials

    def setup() {
        systemCredentialsProvider = Mock(SystemCredentialsProvider)
        systemCredentialsProvider.getCredentials() >> Lists.newArrayList()
        credentials = Mock(Credentials)
        instanceSshCredentials = new InstanceSshCredentials(
                systemCredentialsProvider: systemCredentialsProvider,
                credentials: credentials)
    }

    def "the credential is saved"() {
        when:
        instanceSshCredentials.configure()

        then:
        def creds = systemCredentialsProvider.getCredentials()
        creds.size() == 1
        creds.iterator().next() == credentials
        systemCredentialsProvider.save()
    }
}
