import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory
def consulConfigProvider = new ConsulCurlConfigProvider()

consulConfigProvider.jobsPropertiesHash.each { jobName, config ->
    def repo = config['repo']
    def params = config['parameters']
    def downstreams = config['downstreams']
    def upstreams = config['upstreams']
    def bran = config['branch']
    def cmd = config['cmd']

    dslFactory.job(jobName) {
        logRotator(10, 10)
        blockOnUpstreamProjects()
        if (repo) {
            scm {
                git {
                    remote {
                        github("${repo}", 'ssh')
                        credentials('curbkins-github')
                        branch(bran)
                    }
                }
            }
            triggers {
                githubPush()
            }
        }
        if (params?.trim()) {
            parameters {
                for (param in params.split(',')) {
                    stringParam(param.trim())
                }
            }
        }
        steps {
            shell(cmd)
        }
        triggers {
            if (upstreams?.trim()) {
                for (up in upstreams.split(',')) {
                    upstream(up.trim())
                }
            }
        }
        publishers {
            if (downstreams?.trim()) {
                for (down in downstreams.split(',')) {
                    downstream(down.trim())
                }
            }
            configure { project ->
                project / 'publishers' << 'jenkins.plugins.hipchat.HipChatNotifier' {
                    startNotification true
                    notifyAborted true
                    notifyFailure true
                    notifyNotBuilt true
                    notifySuccess true
                    notifyUnstable true
                    notifyBackToNormal true
                }
            }
        }
    }
}
