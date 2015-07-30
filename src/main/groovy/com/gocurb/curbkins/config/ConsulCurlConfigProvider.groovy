package com.gocurb.curbkins.config

import groovy.json.JsonSlurper

/**
 * Created by sgarlick on 6/17/15.
 */
class ConsulCurlConfigProvider {

    def getAdminEmailAddress() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/JENKINS_ADMIN_ADDRESS?raw'].
                execute().text
    }


    def getSlaveAmiId() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/ami?raw'].execute().text
    }


    def getSlavePrivateKey() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/slave/private-key?raw'].
                execute().text
    }


    def getGitGlobalConfigName() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GIT_COMMIT_NAME?raw'].
                execute().text
    }


    def getGitGlobalConfigEmail() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GIT_COMMIT_EMAIL?raw'].
                execute().text
    }


    def getGithubUsername() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_USER?raw'].
                execute().text
    }


    def getGithubOauthToken() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GITHUB_OAUTH2_TOKEN?raw'].
                execute().text
    }


    def getHipchatOauthToken() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/HIPCHAT_OAUTH2_TOKEN?raw'].
                execute().text
    }


    def getHipchatNotificationRoom() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/HIPCHAT_NOTIFICATION_ROOM_ID?raw'].
                execute().text
    }


    def getGoogleSecurityRealmClientId() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_CLIENT_ID?raw'].
                execute().text
    }


    def getGoogleSecurityRealmClientSecret() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_CLIENT_SECRET?raw'].
                execute().text
    }


    def getGoogleSecurityRealmAuthDomain() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/GOOGLE_AUTH_DOMAIN?raw'].
                execute().text
    }

    def getHockeyAppToken() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/config/HOCKEYAPP_TOKEN?raw'].
                execute().text
    }

    def getJobs() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/jobs?recurse'].execute().text
    }

    def getJobsConsulKvs() {
        return new JsonSlurper().
                parseText(jobs)
    }

    def getJobsPropertiesHash() {
        return jobsConsulKvs.inject([:]) { map, kv ->
            def splits = kv.Key.split('/')
            def name = splits[2]
            def prop = splits[3]
            if (!map[name]) {
                map[name] = [:]
            }
            map[name][prop] = new String(kv.Value.decodeBase64())
            return map
        }
    }

    def getViews() {
        return ['curl', 'http://consul:8500/v1/kv/jenkins/views?recurse'].execute().text
    }

    def getViewsConsulKvs() {
        return new JsonSlurper().parseText(views)
    }

    def getViewsPropertiesHash() {
        return viewsConsulKvs.inject([:]) { map, kv ->
            def splits = kv.Key.split('/')
            def name = splits[2]
            def prop = splits[3]
            if (!map[name]) {
                map[name] = [:]
            }
            map[name][prop] = new String(kv.Value.decodeBase64())
            return map
        }
    }

    def getViewProperties() {
        viewsPropertiesHash.values()
    }


    def getUsers() {
        return ['curl', 'http://consul:8500/v1/kv/users?recurse'].execute().text
    }

    def getUsersConsulKvs() {
        return new JsonSlurper().
                parseText(users)
    }

    def getUsersPropertiesHash() {
        return usersConsulKvs.inject([:]) { map, kv ->
            def split = kv.Key.split('/')
            def name = split[1]
            def prop = split[2]
            if (!map[name]) {
                map[name] = [:]
            }
            map[name][prop] = new String(kv.Value.decodeBase64())
            return map
        }
    }

    def getUsersProperties() {
        return usersPropertiesHash.values()
    }
}

