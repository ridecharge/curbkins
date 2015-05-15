package io.gocurb.curbkins.config

import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.DescribeInstancesRequest
import com.amazonaws.services.ec2.model.InstanceType
import com.google.common.collect.Lists
import hudson.model.Node
import hudson.plugins.ec2.AmazonEC2Cloud
import hudson.plugins.ec2.EC2Tag
import hudson.plugins.ec2.SlaveTemplate
import hudson.plugins.ec2.UnixData
import hudson.slaves.Cloud
import jenkins.model.Jenkins
/**
 * Created by sgarlick on 5/14/15.
 */
class InstanceCloud {

    Jenkins jenkins
    Cloud cloud

    def configure() {
        if (jenkins.clouds.size() == 0) {
            jenkins.clouds.add(cloud)
            jenkins.save()
        }
    }

    static def get() {
        def amazonEC2 = new AmazonEC2Client()
        def instance = getInstance(amazonEC2, getInstanceId())
        def az = instance.placement.availabilityZone
        def region = az.substring(0, az.length() - 1)
        def slaveTemplate = getMainSlaveTemplate(instance, getAmiId())
        def cloud = getAmazonEC2Cloud(region, slaveTemplate, getPrivateKey())
        return new InstanceCloud(jenkins: Jenkins.instance,
                                 cloud: cloud)
    }

    private static def getInstanceId() {
        return ['curl', 'http://169.254.169.254/latest/meta-data/instance-id'].execute().text
    }

    private static def getAmiId() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/ami?raw'].execute().text
    }

    private static def getPrivateKey() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/private-key?raw'].
                execute().text
    }

    private static def getInstance(amazonEC2, String instanceId) {
        def DescribeInstancesRequest request = new DescribeInstancesRequest().
                withInstanceIds(instanceId)
        def response = amazonEC2.describeInstances(request)
        return response.reservations[0].instances[0]
    }

    private static def getTags(instance) {
        def tags = instance.tags.findAll { tag ->
            tag.key in ['Environment', 'Role', 'Name']
        }
        def ec2Tags = tags.collect { tag ->
            if (tag.key == 'Name') {
                return new EC2Tag(tag.key, "${tag.value}-slave")
            }
            return new EC2Tag(tag)
        }
        return ec2Tags
    }

    private static SlaveTemplate getMainSlaveTemplate(instance, amiId) {
        def subnet = instance.subnetId
        def securityGroups = instance.securityGroups.collect { securityGroup ->
            return securityGroup.groupName
        }.join(",")
        def instanceProfile = instance.iamInstanceProfile.arn
        def tags = getTags(instance)
        return new SlaveTemplate(amiId, '', null, securityGroups, '/var/lib/jenkins',
                                         InstanceType.M3Xlarge, '', Node.Mode.NORMAL,
                                         'jenkins-slave-curbix', '', '',
                                         """#!/bin/sh
                                         curl http://consul.gocurb.internal/v1/kv/userdata/curbkins-slave/script?raw | sh""",
                                         "4", 'ubuntu', new UnixData('', '22'), '', false, subnet,
                                         tags, "30", true, "4", instanceProfile,
                                         false, false, '', false,
                                         '')
    }

    static def getAmazonEC2Cloud(region, slaveTemplate, String privateKey) {
        return new AmazonEC2Cloud(true, '', '', region, privateKey, '4',
                                  Lists.newArrayList(slaveTemplate));
    }
}
