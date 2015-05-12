import jenkins.model.Jenkins

def jenkins = Jenkins.getInstance()
def initJob = jenkins.getItem('init-jobs')
jenkins.getQueue().schedule(initJob, 0)
