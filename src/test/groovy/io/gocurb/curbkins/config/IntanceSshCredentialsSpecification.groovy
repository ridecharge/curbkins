package io.gocurb.curbkins.config

import com.cloudbees.jenkins.Credential
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.google.common.collect.Lists
import spock.lang.Specification
/**
 * Created by sgarlick on 5/11/15.
 */
class IntanceSshCredentialsSpecification extends Specification {
    SystemCredentialsProvider systemCredentialsProvider
    InstanceSshCredentials instanceSshCredentials
    Credential credential
    def setup() {
        systemCredentialsProvider = Mock(SystemCredentialsProvider)
        systemCredentialsProvider.getCredentials() >> Lists.newArrayList()
        credential = Mock(Credential)
        instanceSshCredentials = new InstanceSshCredentials(systemCredentialsProvider: systemCredentialsProvider,
                                                            credential: credential)
    }

    def "the credential is saved"() {
        when:
        instanceSshCredentials.configure()

        then:
        def creds = systemCredentialsProvider.getCredentials()
        creds.size() == 1
        creds.iterator().next() == credential
        systemCredentialsProvider.save()
    }
}
