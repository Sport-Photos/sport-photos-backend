package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.UpdatePhotoCoverageForm

class UpdatePhotoCoverageFormMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomUpdatePhotoCoverageForm(Map overriddenValues = [:]) {
        new UpdatePhotoCoverageForm(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            description: faker.chuckNorris().fact(),
            link       : faker.internet().url()
        ]
    }
}
