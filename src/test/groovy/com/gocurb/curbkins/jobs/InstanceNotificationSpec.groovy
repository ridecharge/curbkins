package com.gocurb.curbkins.jobs

import jenkins.model.Jenkins
import jenkins.plugins.hipchat.HipChatNotifier
import spock.lang.Specification
/**
 * Created by sgarlick on 5/11/15.
 */
class InstanceNotificationSpecification extends Specification {
    def jenkins
    def instanceNotification
    def descriptor
    def oauthToken = "oauthToken"
    def notificationRoom = "notificationRoom"

    def setup() {
        jenkins = Mock(Jenkins)
        descriptor = Mock(HipChatNotifier.DescriptorImpl)
        jenkins.getDescriptorByType(HipChatNotifier.DescriptorImpl) >> descriptor
        instanceNotification = new InstanceNotifications(instance: jenkins, oauthToken: oauthToken, notificationRoom: notificationRoom)
    }

    def "the v2 api is being used"() {
        when:
        instanceNotification.configure()

        then:
        descriptor.setV2Enabled(true)
    }

    def "the oauth token is set"() {
        when:
        instanceNotification.configure()

        then:
        descriptor.setToken(oauthToken)
    }

    def "the notification room is set"() {
        when:
        instanceNotification.configure()

        then:
        descriptor.setRoom(notificationRoom)
    }

    def "the configuration is saved"() {
        when:
        instanceNotification.configure()

        then:
        descriptor.save()
    }
}
