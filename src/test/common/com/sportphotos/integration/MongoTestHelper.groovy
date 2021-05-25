package com.sportphotos.integration

import com.sportphotos.domain.events.model.Event
import com.sportphotos.domain.photographers.model.Photographer
import org.springframework.boot.test.context.TestComponent
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query

@TestComponent
class MongoTestHelper {

    MongoTemplate mongoTemplate

    MongoTestHelper(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate
    }

    def cleanDatabase() {
        mongoTemplate.remove(new Query(), Event)
        mongoTemplate.remove(new Query(), Photographer)
    }
}
