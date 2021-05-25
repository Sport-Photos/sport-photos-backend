package com.sportphotos.infrastructure.database

import com.sportphotos.Application
import com.sportphotos.integration.MongoDatabaseInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = [Application], webEnvironment = NONE)
@ActiveProfiles('test')
@ContextConfiguration(classes = [MongoTestConfig], initializers = [MongoDatabaseInitializer])
@interface MongoTest {
}
