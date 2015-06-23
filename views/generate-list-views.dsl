import com.gocurb.curbkins.config.ConsulCurlConfigProvider
import javaposse.jobdsl.dsl.DslFactory

def dslFactory = this as DslFactory
def consulConfigProvider = new ConsulCurlConfigProvider()

consulConfigProvider.viewProperties.each { props ->
    dslFactory.listView(props.name) {
        description(props.description)
        filterBuildQueue()
        filterExecutors()
        statusFilter(StatusFilter.ENABLED)
        jobs {
            regex(props.regex)
        }
        columns {
            status()
            weather()
            name()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
    }
}

