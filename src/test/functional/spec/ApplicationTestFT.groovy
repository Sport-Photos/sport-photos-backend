package spec

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import spock.lang.Specification

@FunctionalTest
class ApplicationTestFT extends Specification {

    @Autowired
    ApplicationContext context

    def 'should start Spring context'() {
        expect:
            context
    }
}
