import com.gocurb.curbkins.jobs.InstanceAdmin
import com.gocurb.curbkins.jobs.InstanceCloud
import com.gocurb.curbkins.jobs.InstanceConfigs
import com.gocurb.curbkins.jobs.InstanceGit
import com.gocurb.curbkins.jobs.InstanceGithub
import com.gocurb.curbkins.jobs.InstanceNotifications
import com.gocurb.curbkins.jobs.InstanceSecurity
import com.gocurb.curbkins.jobs.InstanceSshCredentials
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory
def instanceConfigs = ['jenkins-admin-config'   : InstanceAdmin,
                       'jenkins-cloud-config'   : InstanceCloud,
                       'jenkins-git-config'     : InstanceGit,
                       'jenkins-github-config'  : InstanceGithub,
                       'jenkins-hipchat-config' : InstanceNotifications,
                       'jenkins-security-config': InstanceSecurity,
                       'jenkins-ssh-config'     : InstanceSshCredentials]

new InstanceConfigs(dslFactory: dslFactory, instanceConfigs: instanceConfigs).create()
