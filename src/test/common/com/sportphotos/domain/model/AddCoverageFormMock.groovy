package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.AddCoverageForm

class AddCoverageFormMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomAddCoverageForm(Map overriddenValues = [:]) {
        new AddCoverageForm(randomValues() + overriddenValues)
    }

    static def randomValues() {
        [
            nick       : faker.ancient().god(),
            description: faker.chuckNorris().fact(),
            link       : faker.internet().url()
        ]
    }
}
