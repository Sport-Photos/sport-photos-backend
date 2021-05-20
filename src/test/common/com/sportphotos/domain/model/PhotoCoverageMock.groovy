package com.sportphotos.domain.model

import com.github.javafaker.Faker
import com.sportphotos.domain.events.AddCoverageForm
import com.sportphotos.domain.events.UpdatePhotoCoverageForm
import com.sportphotos.domain.events.model.PhotoCoverage
import org.bson.BsonBinarySubType
import org.bson.types.Binary

import static com.sportphotos.domain.model.PhotographerMock.randomPhotographer

class PhotoCoverageMock {

    static def random = new Random(1)
    static def faker = new Faker(random)

    static def randomPhotoCoverage(Map overriddenValues = [:]) {
        new PhotoCoverage(randomValues() + overriddenValues)
    }

    static def randomPhotoCoverage(AddCoverageForm addCoverageForm) {
        def overrides = [
            photographer: randomPhotographer(
                nickname: addCoverageForm.nick
            ),
            description : addCoverageForm.description,
            link        : addCoverageForm.link
        ]

        randomPhotoCoverage(overrides)
    }

    static def randomPhotoCoverage(UpdatePhotoCoverageForm addCoverageForm) {
        def overrides = [
            description: addCoverageForm.description,
            link       : addCoverageForm.link
        ]

        randomPhotoCoverage(overrides)
    }

    static def randomValues() {
        [
            id          : UUID.randomUUID().toString(),
            photographer: randomPhotographer(),
            description : faker.chuckNorris().fact(),
            link        : faker.internet().url(),
            bestPhoto   : new Binary(BsonBinarySubType.BINARY, faker.avatar().image().bytes)
        ]
    }
}
