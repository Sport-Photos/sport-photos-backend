package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.AddEventForm

import java.time.ZoneId
import java.util.concurrent.TimeUnit

class AddEventFormMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomAddEventForm(Map overriddenValues = [:]) {
        new AddEventForm(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            date   : faker.date().past(1000, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            name   : faker.book().title(),
            city   : faker.address().city(),
            country: faker.address().country()
        ]
    }
}
