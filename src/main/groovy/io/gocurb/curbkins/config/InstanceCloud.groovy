package io.gocurb.curbkins.config

import hudson.plugins.ec2.AmazonEC2Cloud
import hudson.plugins.ec2.SlaveTemplate
import hudson.slaves.Cloud
import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/14/15.
 */
class InstanceCloud {

    Jenkins jenkins
    Cloud cloud

    def configure() {
        jenkins.clouds.add(cloud)
        jenkins.save()
    }

    static def get() {
        return new InstanceCloud(jenkins: Jenkins.instance, cloud: getAmazonEC2Cloud())
    }
    static def getAmazonEC2Cloud() {
        SlaveTemplate template = new SlaveTemplate()
        return new AmazonEC2Cloud(true, '', '', 'us-east-1', '', '3', null);
    }
}
