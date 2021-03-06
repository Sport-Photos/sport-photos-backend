package com.sportphotos.domain.events

import com.sportphotos.domain.BaseDomainTest
import com.sportphotos.domain.DomainTest
import com.sportphotos.domain.ResourceNotFoundException
import com.sportphotos.domain.events.model.Event
import org.springframework.web.multipart.MultipartFile

import static com.sportphotos.domain.model.AddCoverageFormMock.randomAddCoverageForm
import static com.sportphotos.domain.model.AddEventFormMock.randomAddEventForm
import static com.sportphotos.domain.model.EventMock.randomEvent
import static com.sportphotos.domain.model.UpdatePhotoCoverageFormMock.randomUpdatePhotoCoverageForm

@DomainTest
class EventsServiceTest extends BaseDomainTest {

    def 'getEventById should throw ResourceNotFoundException when Event not found by given id'() {
        when:
            eventService.getEventById(UUID.randomUUID().toString())
        then:
            thrown(ResourceNotFoundException)
    }

    def 'getEventById should find Event by given id'() {
        given:
            def event = randomEvent()
            eventsRepository.save(event)
        when:
            Event found = eventService.getEventById(event.id)
        then:
            with(found) {
                it.id == event.id
            }
    }

    def 'save(Event) should store Event in database'() {
        given:
            def addEventForm = randomAddEventForm()
            def avatar = mockMultipartFile()
        when:
            def event = eventService.addEvent(addEventForm, avatar)
        then:
            def saved = eventsRepository.findById(event.id).get()
            saved.name == addEventForm.name
            saved.date == addEventForm.date
            saved.avatar != null
        and:
            eventsIndexProvider.findById(event.id).isPresent()
    }

    def 'deleteEvent should delete Event by given id'() {
        given:
            def event = randomEvent()
            eventsRepository.save(event)
        when:
            eventService.deleteEvent(event.id)
        then:
            !eventsRepository.findById(event.id).isPresent()
            !eventsIndexProvider.findById(event.id).isPresent()
    }

    def 'getAllPhotoCoverages should return all Photo Coverages for Event'() {
        given: 'create event'
            def event = randomEvent()
            eventsRepository.save(event)
        when:
            def photoCoverages = eventService.getAllPhotoCoverages(event.id)
        then:
            photoCoverages.size() == 2
            photoCoverages.find {
                it.photographer.nickname == event.photoCoverages[0].photographer.nickname &&
                    it.description == event.photoCoverages[0].description &&
                    it.link == event.photoCoverages[0].link
            }
            photoCoverages.find {
                it.photographer.nickname == event.photoCoverages[1].photographer.nickname &&
                    it.description == event.photoCoverages[1].description &&
                    it.link == event.photoCoverages[1].link
            }
    }

    def 'getPhotoCoverage should return Photo Coverage for Event'() {
        given: 'create event'
            def event = randomEvent()
            eventsRepository.save(event)
        when:
            def photoCoverage = eventService.getPhotoCoverage(event.id, event.photoCoverages[0].id)
        then:
            photoCoverage.photographer.nickname == event.photoCoverages[0].photographer.nickname
            photoCoverage.description == event.photoCoverages[0].description
            photoCoverage.link == event.photoCoverages[0].link
    }

    def 'save(PhotoCoverage) should store PhotoCoverage in database'() {
        given: 'create event'
            def addEventForm = randomAddEventForm()
            def avatar = mockMultipartFile()
            def event = eventService.addEvent(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
        when:
            eventService.addPhotoCoverage(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
        then:
            with(eventsRepository.findById(event.id).get()) {
                it.photoCoverages.size() == 1
                it.photoCoverages.find {
                    it.photographer.nickname == addCoverageForm.nick &&
                        it.description == addCoverageForm.description &&
                        it.link == addCoverageForm.link
                }
            }
    }

    def 'updatePhotoCoverage should store PhotoCoverage in database'() {
        given: 'create event'
            def addEventForm = randomAddEventForm()
            def avatar = mockMultipartFile()
            def event = eventService.addEvent(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
            def photoCoverage = eventService.addPhotoCoverage(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
        and: 'update photo coverage'
            def updatePhotoCoverageForm = randomUpdatePhotoCoverageForm()
        when:
            eventService.updatePhotoCoverage(event.id, photoCoverage.id, updatePhotoCoverageForm, bestPhoto)
        then:
            with(eventsRepository.findById(event.id).get()) {
                it.photoCoverages.size() == 1
                it.photoCoverages.find {
                    it.photographer.nickname == addCoverageForm.nick &&
                        it.description == updatePhotoCoverageForm.description &&
                        it.link == updatePhotoCoverageForm.link
                }
            }
    }

    def 'deletePhotoCoverage should delete PhotoCoverage from database'() {
        given: 'create event'
            def addEventForm = randomAddEventForm()
            def avatar = mockMultipartFile()
            def event = eventService.addEvent(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
        and:
            def photoCoverage = eventService.addPhotoCoverage(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
        when:
            eventService.deletePhotoCoverage(event.id, photoCoverage.id)
        then:
            with(eventsRepository.findById(event.id).get()) {
                it.photoCoverages.isEmpty()
            }
    }

    def mockMultipartFile() {
        def stub = Stub(MultipartFile);
        stub.getBytes() >> new byte[1]
        stub
    }

}
