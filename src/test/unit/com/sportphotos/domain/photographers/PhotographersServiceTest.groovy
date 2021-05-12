package com.sportphotos.domain.photographers

import com.sportphotos.domain.BaseDomainTest
import com.sportphotos.domain.DomainTest
import com.sportphotos.domain.ResourceNotFoundException

import static com.sportphotos.domain.model.AddRatingFormMock.randomAddRatingForm
import static com.sportphotos.domain.model.PhotographerMock.randomPhotographer
import static com.sportphotos.domain.model.RatingMock.randomRating
import static com.sportphotos.domain.model.UpdateRatingFormMock.randomUpdateRatingForm

@DomainTest
class PhotographersServiceTest extends BaseDomainTest {

    def 'findById should throw ResourceNotFoundException when Photographer not found by given id'() {
        when:
            photographersService.findById(UUID.randomUUID().toString())
        then:
            thrown(ResourceNotFoundException)
    }

    def 'getRatings should get all Ratings for Photographer'() {
        given:
            def photographer = randomPhotographer()
            photographersRepository.save(photographer)
        when:
            def ratings = photographersService.getAllRatings(photographer.id)
        then:
            ratings.size() == 2
            ratings.find {
                it.id == photographer.ratings[0].id
                it.rate == photographer.ratings[0].rate
                it.comment == photographer.ratings[0].comment
            }
            ratings.find {
                it.id == photographer.ratings[1].id
                it.rate == photographer.ratings[1].rate
                it.comment == photographer.ratings[1].comment
            }
    }

    def 'getRating should get Rating for Photographer'() {
        given:
            def photographer = randomPhotographer()
            photographersRepository.save(photographer)
        when:
            def rating = photographersService.getRating(photographer.id, photographer.ratings[0].id)
        then:
            rating.id == photographer.ratings[0].id
            rating.rate == photographer.ratings[0].rate
            rating.comment == photographer.ratings[0].comment
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

    def 'deleteRate should delete Rating from database'() {
        given: 'create event'
            def photographer = randomPhotographer(ratings: [randomRating()])
            photographersRepository.save(photographer)
        when:
            photographersService.deleteRate(photographer.id, photographer.ratings[0].id)
        then:
            with(photographersRepository.findById(photographer.id).get()) {
                it.ratings.isEmpty()
            }
    }
}
