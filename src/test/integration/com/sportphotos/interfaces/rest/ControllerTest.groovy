package com.sportphotos.interfaces.rest

import com.sportphotos.Application
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application])
@ContextConfiguration(classes = [RestConfig])
@ActiveProfiles('test')
@EnableAutoConfiguration(exclude = [MongoAutoConfiguration])
@interface ControllerTest {
}