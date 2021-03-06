package com.gocurb.curbkins.jobs

import groovy.io.FileType
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.MemoryJobManagement
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Tests that all dsl scripts in the jobs directory will compile.
 * Taken from https://github.com/sheehan/job-dsl-gradle-example
 */
class JobScriptsSpec extends Specification {

    @Unroll
    void 'test script #file.name'(File file) {
        given:
        JobManagement jm = new MemoryJobManagement()

        when:
        DslScriptLoader.runDslEngine(file.text, jm)

        then:
        noExceptionThrown()

        where:
        file << jobFiles
    }

    def getJobFiles() {
        def files = []
        ['jobs', 'views'].each { folder ->
            new File(folder).eachFileRecurse(FileType.FILES) {
                files << it
            }
        }
        return files
    }

}
