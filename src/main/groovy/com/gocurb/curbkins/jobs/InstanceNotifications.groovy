package com.gocurb.curbkins.jobs

import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import jenkins.model.Jenkins
import jenkins.plugins.hipchat.HipChatNotifier
/**
 * Created by sgarlick on 5/8/15.
 */
class InstanceNotifications {

    def instance
    def oauthToken
    def notificationRoom

    def configure() {
        def desc = instance.getDescriptorByType(HipChatNotifier.DescriptorImpl)
        desc.token = oauthToken
        desc.room = notificationRoom
        desc.v2Enabled = true
        desc.save()
    }

    static def get() {
        def consulConfigProvider = new ConsulCurlConfigProvider()
        return new InstanceNotifications(instance: Jenkins.instance,
                                         oauthToken: consulConfigProvider.hipchatOauthToken,
                                         notificationRoom: consulConfigProvider.hipchatNotificationRoom)
    }

}
