package io.gocurb.curbkins.config

import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceSshCredentials implements InstanceConfig {
    SystemCredentialsProvider systemCredentialsProvider
    def configure() {
        def creds = systemCredentialsProvider.getCredentials()
        if(creds.size() == 0) {
            def keySource = new BasicSSHUserPrivateKey.UsersPrivateKeySource()
            def cred = new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, 'curbkins-github', 'curbkins-github',keySource, '', '')
            creds.add(cred)
        }
        systemCredentialsProvider.save()
    }
}
