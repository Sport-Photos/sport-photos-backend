package com.sportphotos.infrastructure

import com.mongodb.client.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@MongoTest
class MongoDbTestIT extends Specification {

    @Autowired
    MongoClient mongoClient

    def 'should connect to mongo test database'() {
        expect:
            !mongoClient.listDatabaseNames().isEmpty()
    }
}
