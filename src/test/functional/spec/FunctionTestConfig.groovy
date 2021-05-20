package spec

import com.sportphotos.domain.events.EventsIndexProvider
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
    FunctionalTestHelper functionalTestHelper(@Value('${local.server.port}') int port, MongoTemplate mongoTemplate) {
        new FunctionalTestHelper(port, mongoTemplate)
    }

    @Primary
    @Bean
    EventsIndexProvider eventsIndexProvider() {
        factory.Mock(EventsIndexProvider)
    }
}
