package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.AddEventForm
import com.sportphotos.domain.events.model.Event
import com.sportphotos.domain.events.model.Location
import org.bson.BsonBinarySubType
import org.bson.types.Binary

import java.time.ZoneId
import java.util.concurrent.TimeUnit

import static com.sportphotos.domain.model.PhotoCoverageMock.randomPhotoCoverage

class EventMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomEvent(Map overriddenValues = [:]) {
        new Event(randomValues() + overriddenValues)
    }

    static def randomEvent(AddEventForm addEventForm) {
        def overrides = [
            date          : addEventForm.date,
            name          : addEventForm.name,
            location      : new Location(
                city: addEventForm.city,
                country: addEventForm.country
            ),
            photoCoverages: []
        ]

        randomEvent(overrides)
    }

    static def randomValues() {
        [
            id            : UUID.randomUUID().toString(),
            date          : faker.date().past(1000, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            name          : faker.book().title(),
            location      : new Location(
                city: faker.address().city(),
                country: faker.address().country()
            ),
            photoCoverages: [randomPhotoCoverage(), randomPhotoCoverage()],
            avatar        : new Binary(BsonBinarySubType.BINARY, faker.avatar().image().bytes)
        ]
    }
}
