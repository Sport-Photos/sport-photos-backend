package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.model.Rating

class RatingMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomRating(Map overriddenValues = [:]) {
        new Rating(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            id     : UUID.randomUUID().toString(),
            rate   : faker.number().numberBetween(1, 10),
            comment: faker.shakespeare().hamletQuote()
        ]
    }
}
