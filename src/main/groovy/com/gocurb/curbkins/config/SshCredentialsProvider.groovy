package com.gocurb.curbkins.config

import com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey
import com.cloudbees.plugins.credentials.CredentialsScope

/**
 * Created by sgarlick on 6/17/15.
 */
class SshCredentialsProvider {

    def getCurbkinsGithubCredentials() {
        def privateKeySource = new BasicSSHUserPrivateKey.UsersPrivateKeySource()
        return new BasicSSHUserPrivateKey(CredentialsScope.GLOBAL, 'curbkins-github',
                                          'curbkins-github', privateKeySource, '', '')
    }
}
