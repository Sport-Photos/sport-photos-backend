package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.model.Photographer

import static com.sportphotos.domain.model.RatingMock.randomRating

class PhotographerMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomPhotographer(Map overriddenValues = [:]) {
        new Photographer(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            id      : UUID.randomUUID().toString(),
            nickname: faker.dragonBall().character(),
            ratings : [randomRating(), randomRating()]
        ]
    }
}
