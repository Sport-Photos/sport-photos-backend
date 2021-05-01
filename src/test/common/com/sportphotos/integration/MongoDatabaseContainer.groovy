package com.sportphotos.integration


import org.testcontainers.containers.MongoDBContainer
import spock.lang.Shared

class MongoDatabaseContainer {

    private static final Object lock = new Object()

    @Shared
    static MongoDBContainer mongo

    static def initialize() {
        synchronized (lock) {
            if (mongo == null) {
                mongo = new MongoDBContainer()
                mongo.start()
            }
        }
    }
}
