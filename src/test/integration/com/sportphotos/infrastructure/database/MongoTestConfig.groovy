package com.sportphotos.infrastructure.database

import com.sportphotos.integration.MongoTestHelper
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.data.mongodb.core.MongoTemplate

@TestConfiguration
class MongoTestConfig {

    @Lazy
    @Bean
    MongoTestHelper mongoTestHelper(MongoTemplate mongoTemplate) {
        new MongoTestHelper(mongoTemplate)
    }
}