package com.sportphotos.integration

import groovy.util.logging.Slf4j
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

@Slf4j
class MongoDatabaseInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("Overriding Spring Properties for mongodb")

        MongoDatabaseContainer.initialize()

        TestPropertyValues values = TestPropertyValues.of(
            "spring.data.mongodb.uri=" +
                "mongodb://" +
                MongoDatabaseContainer.mongo.getContainerIpAddress() +
                ":" +
                MongoDatabaseContainer.mongo.getMappedPort(27017) +
                "/?readPreference=primary&ssl=false&retrywrites=false",
            "spring.data.mongodb.database=test"
        )

        values.applyTo(applicationContext);
    }
}
