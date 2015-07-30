package com.gocurb.curbkins.jobs

import hockeyapp.HockeyappApplication
import jenkins.model.Jenkins
import spock.lang.Specification
/**
 * Created by sgarlick on 6/25/15.
 */
class InstanceHockAppSpec extends Specification {

    def instanceHockeyApp
    def jenkins
    def descriptor
    def token = 'abc'

    def setup() {
        jenkins = Mock(Jenkins)
        descriptor = Mock(HockeyappApplication.DescriptorImpl)
        jenkins.getDescriptorByType(HockeyappApplication.DescriptorImpl) >> descriptor

        instanceHockeyApp = new InstanceHockeyApp(instance: jenkins)
    }
}
