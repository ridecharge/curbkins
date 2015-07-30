import com.gocurb.curbkins.jobs.*
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory
def instanceConfigs = ['jenkins-admin-config'    : InstanceAdmin,
                       'jenkins-cloud-config'    : InstanceCloud,
                       'jenkins-git-config'      : InstanceGit,
                       'jenkins-github-config'   : InstanceGithub,
                       'jenkins-hipchat-config'  : InstanceNotifications,
                       'jenkins-security-config' : InstanceSecurity,
                       'jenkins-ssh-config'      : InstanceSshCredentials,
                       'jenkins-hockeyapp-config': InstanceHockeyApp]


new InstanceConfigs(dslFactory: dslFactory, instanceConfigs: instanceConfigs).create()
