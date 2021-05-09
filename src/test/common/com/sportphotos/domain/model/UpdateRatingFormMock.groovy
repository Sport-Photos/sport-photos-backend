package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.photographers.UpdateRatingForm

class UpdateRatingFormMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomUpdateRatingForm(Map overriddenValues = [:]) {
        new UpdateRatingForm(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            rate   : faker.number().numberBetween(1, 10),
            comment: faker.shakespeare().hamletQuote()
        ]
    }
}
