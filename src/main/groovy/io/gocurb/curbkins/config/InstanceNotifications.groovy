package io.gocurb.curbkins.config

import jenkins.model.Jenkins
import jenkins.plugins.hipchat.HipChatNotifier

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceNotifications implements InstanceConfig {

    Jenkins instance
    String oauthToken
    String notificationRoom

    def configure() {
        def desc = instance.getDescriptorByType(HipChatNotifier.DescriptorImpl)
        desc.setToken(oauthToken)
        desc.setRoom(notificationRoom)
        desc.setV2Enabled(true)
        desc.save()
    }

    static def get() {
        return new InstanceNotifications(instance: Jenkins.getInstance(),
                                         oauthToken: getOauthToken(),
                                         notificationRoom: getNotificationRoom())
    }

    static def getOauthToken() {
        return ['curl', 'consul:8500/v1/kv/jenkins/config/HIPCHAT_OAUTH2_TOKEN?raw'].execute().text
    }

    static def getNotificationRoom() {
        return ['curl', 'consul:8500/v1/kv/jenkins/config/HIPCHAT_NOTIFICATION_ROOM_ID?raw'].
                execute().text
    }
}
