package com.sportphotos.domain

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.test.context.ContextConfiguration

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableConfigurationProperties
@ContextConfiguration(
    classes = [
        DomainConfig,
        DomainTestConfig
    ])
@interface DomainTest {

}