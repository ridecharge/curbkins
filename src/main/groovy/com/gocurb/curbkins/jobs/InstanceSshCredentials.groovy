package com.gocurb.curbkins.jobs

import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.gocurb.curbkins.config.SshCredentialsProvider
/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceSshCredentials {

    def systemCredentialsProvider
    def credentials

    def configure() {
        def creds = systemCredentialsProvider.credentials
        if (creds.size() == 0) {
            creds.add(credentials)
            systemCredentialsProvider.save()
        }
    }

    static def get() {
        def credentialsProvider = new SshCredentialsProvider()
        return new InstanceSshCredentials(
                systemCredentialsProvider: SystemCredentialsProvider.instance,
                credentials: credentialsProvider.curbkinsGithubCredentials
        )
    }
}
