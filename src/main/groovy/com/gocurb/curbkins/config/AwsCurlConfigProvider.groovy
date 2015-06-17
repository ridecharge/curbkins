package com.gocurb.curbkins.config

/**
 * Created by sgarlick on 6/17/15.
 */
class AwsCurlConfigProvider {

    def getEc2InstanceId() {
        return ['curl', 'http://169.254.169.254/latest/meta-data/instance-id'].execute().text
    }
}
