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
            "spring.data.mongodb.host=" + MongoDatabaseContainer.mongo.getContainerIpAddress(),
            "spring.data.mongodb.port=" + MongoDatabaseContainer.mongo.getMappedPort(27017),
            "spring.data.mongodb.database=local"
        )

        values.applyTo(applicationContext);
    }
}
