package io.gocurb.curbkins.config

import com.cloudbees.jenkins.Credential
import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceSshCredentials implements InstanceConfig {
    SystemCredentialsProvider systemCredentialsProvider
    Credential credential
    def configure() {
        def creds = systemCredentialsProvider.getCredentials()
        if(creds.size() == 0) {
            creds.add(credential)
            systemCredentialsProvider.save()
        }
    }

    static def getCredentials() {
        def privateKeySource = new BasicSSHUserPrivateKey.UsersPrivateKeySource()
        return new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, 'curbkins-github', 'curbkins-github', privateKeySource, '', '')
    }
}
