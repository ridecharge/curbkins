package io.gocurb.curbkins.config
import com.amazonaws.services.ec2.AmazonEC2
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
        return new InstanceCloud(jenkins: Jenkins.instance, cloud: getAmazonEC2Cloud())
    }

    static def getInstanceId() {
        return ['curl', 'http://169.254.169.254/latest/meta-data/instance-id'].execute().text
    }

    static def getAmiId() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/ami?raw'].execute().text
    }

    static def getPrivateKey() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/private-key?raw'].
                execute().text
    }

    static def getAmazonEC2Cloud() {
        def ami = getAmiId()
        def AmazonEC2 amazonEC2 = new AmazonEC2Client()
        def DescribeInstancesRequest request = new DescribeInstancesRequest().
                withInstanceIds(getInstanceId())
        def response = amazonEC2.describeInstances(request)
        def instance = response.reservations[0].instances[0]
        def az = instance.placement.availabilityZone
        def region = az.substring(0, az.length() - 1)
        def subnet = instance.subnetId
        def securityGroups = instance.securityGroups.collect { securityGroup ->
            return securityGroup.groupName
        }.join(",")
        def instanceProfile = instance.iamInstanceProfile.arn
        def tags = instance.tags.findAll { tag ->
            tag.key in ['Environment', 'Role', 'Name']
        }
        def ec2Tags = tags.collect { tag ->
            if(tag.key == 'Name') {
                return new EC2Tag(tag.key, "${tag.value}-slave")
            }
            return new EC2Tag(tag)
        }
        def template = new SlaveTemplate(ami, '', null, securityGroups, '/var/lib/jenkins',
                                         InstanceType.M3Xlarge, '', Node.Mode.NORMAL,
                                         'jenkins-slave-curbix', '', '',
                                         """#!/bin/sh
                                         curl http://consul.gocurb.internal/v1/kv/userdata/curbkins-slave/script?raw | sh""",
                                         "4", 'ubuntu', new UnixData('', '22'), '', false, subnet,
                                         ec2Tags, "30", true, "4", instanceProfile,
                                         false, false, '', false,
                                         '')
        return new AmazonEC2Cloud(true, '', '', region, getPrivateKey(), '4',
                                  Lists.newArrayList(template));
    }
}
