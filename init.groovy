import hudson.model.Node
import hudson.model.labels.LabelAtom
import jenkins.model.Jenkins

def jenkins = Jenkins.getInstance()
jenkins.labels.add(new LabelAtom('master'))
jenkins.setNumExecutors(2)
jenkins.setMode(Node.Mode.EXCLUSIVE)
def initJob = jenkins.getItem('init-jobs')
jenkins.getQueue().schedule(initJob, 0)
jenkins.save()
