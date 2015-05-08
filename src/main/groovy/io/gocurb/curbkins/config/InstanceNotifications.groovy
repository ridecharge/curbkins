package io.gocurb.curbkins.config

import jenkins.model.Jenkins

/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceNotifications implements InstanceConfig {
    Jenkins instance
    def configure() {
        def desc = instance.getDescriptor("jenkins.plugins.hipchat.HipChatNotifier")
        def oauthToken = ['curl', 'consul:8500/v1/kv/jenkins/config/HIPCHAT_OAUTH2_TOKEN?raw'].execute().text
        def notificationRoom = ['curl', 'consul:8500/v1/kv/jenkins/config/HIPCHAT_NOTIFICATION_ROOM_ID?raw'].execute().text
        desc.setToken(oauthToken)
        desc.setRoom(notificationRoom)
        desc.setV2Enabled(true)
        desc.save()
    }
}
