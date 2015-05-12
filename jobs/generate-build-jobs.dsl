import groovy.json.JsonSlurper
import javaposse.jobdsl.dsl.DslFactory

def Map<String, String> jobs =  {
    def json = new JsonSlurper().parseText(['curl', 'consul:8500/v1/kv/jenkins/jobs?recurse'].execute().text)
    jobs = [:]
    for(kv in json) {
        def splits = kv.Key.split('/')
        def name = splits[2]
        def prop = splits[3]
        if(!jobs[name]) {
            jobs[name] = [:]
        }
        jobs[name][prop] = new String(kv.Value.decodeBase64())
    }
    return jobs
}()
def dslFactory = this as DslFactory


for(j in jobs) {
    def jobName = j.key
    def config = j.value
    def repo = config['repo']
    def params = config['parameters']
    def downstreams = config['downstreams']
    def bran = config['branch']
    def cmd = config['cmd']

    dslFactory.job(jobName) {
        blockOnUpstreamProjects()
        logRotator(10, 10)

        if(repo) {
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
        if(params?.trim()) {
            parameters {
                for(param in params.split(',')) {
                    stringParam(param.trim())
                }
            }
        }
        steps {
            shell(cmd)
        }
        publishers {
            if(downstreams?.trim()) {
                for(down in downstreams.split(',')) {
                    downstream(down.trim())
                }
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
