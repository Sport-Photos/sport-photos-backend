package com.sportphotos.domain.photographers

import com.sportphotos.domain.BaseDomainTest
import com.sportphotos.domain.DomainTest
import com.sportphotos.domain.ResourceNotFoundException

import static com.sportphotos.domain.model.AddRatingFormMock.randomAddRatingForm
import static com.sportphotos.domain.model.PhotographerMock.randomPhotographer
import static com.sportphotos.domain.model.UpdateRatingFormMock.randomUpdateRatingForm

@DomainTest
class PhotographersServiceTest extends BaseDomainTest {

    def 'findById should throw ResourceNotFoundException when Photographer not found by given id'() {
        when:
            photographersService.findById(UUID.randomUUID().toString())
        then:
            thrown(ResourceNotFoundException)
    }

    def 'rate should add Rating to Photographer'() {
        given:
            def addRatingForm = randomAddRatingForm()
            def photographer = randomPhotographer(ratings: [])
            photographersRepository.save(photographer)
        when:
            photographersService.rate(photographer.id, addRatingForm)
        then:
            with(photographersService.findById(photographer.id)) {
                it.ratings.size() == 1
                it.ratings.find {
                    it.rate == addRatingForm.rate
                    it.comment == addRatingForm.comment
                }
            }
    }

    def 'updateRate should update Rating of Photographer'() {
        given:
            def addRatingForm = randomAddRatingForm()
            def photographer = randomPhotographer(ratings: [])
            photographersRepository.save(photographer)
            def rating = photographersService.rate(photographer.id, addRatingForm)
        and:
            def updateRatingForm = randomUpdateRatingForm()
        when:
            photographersService.updateRate(photographer.id, rating.id, updateRatingForm)
        then:
            with(photographersService.findById(photographer.id)) {
                it.ratings.size() == 1
                it.ratings.find {
                    it.rate == updateRatingForm.rate
                    it.comment == updateRatingForm.comment
                }
            }
    }
}
