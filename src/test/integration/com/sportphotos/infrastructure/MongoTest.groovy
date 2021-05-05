package com.sportphotos.infrastructure

import com.sportphotos.integration.MongoDatabaseInitializer
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles('test')
@ContextConfiguration(
    initializers = [MongoDatabaseInitializer])
@ImportAutoConfiguration([MongoAutoConfiguration])
@interface MongoTest {
}
