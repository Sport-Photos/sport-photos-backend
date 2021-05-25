package spec

import com.sportphotos.domain.events.EventsIndexProvider
import com.sportphotos.integration.MongoTestHelper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.MongoTemplate
import spock.mock.DetachedMockFactory

@TestConfiguration
class FunctionTestConfig {

    def factory = new DetachedMockFactory()

    @Lazy
    @Bean
    FunctionalTestHelper functionalTestHelper(@Value('${local.server.port}') int port) {
        new FunctionalTestHelper(port)
    }

    @Lazy
    @Bean
    MongoTestHelper mongoTestHelper(MongoTemplate mongoTemplate) {
        new MongoTestHelper(mongoTemplate)
    }

    @Primary
    @Bean
    EventsIndexProvider eventsIndexProvider() {
        factory.Mock(EventsIndexProvider)
    }
}
