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

    def 'findById should throw ResourceNotFoundException when Event not found by given id'() {
        when:
            eventService.findById(UUID.randomUUID().toString())
        then:
            thrown(ResourceNotFoundException)
    }

    def 'findById should find Event by given id'() {
        given:
            def event = randomEvent()
            eventsRepository.save(event)
        when:
            Event found = eventService.findById(event.id)
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
            eventService.save(addEventForm, avatar)
        then:
            List<Event> events = eventsRepository.findAll()
            events.size() == 1
            events.find { it.name == addEventForm.name && it.date == addEventForm.date && it.avatar != null }
    }

    def 'save(PhotoCoverage) should store PhotoCoverage in database'() {
        given: 'create event'
            def addEventForm = randomAddEventForm()
            def avatar = mockMultipartFile()
            def event = eventService.save(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
        when:
            eventService.save(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
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
            def event = eventService.save(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
            def photoCoverage = eventService.save(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
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
            def event = eventService.save(addEventForm, avatar)
        and: 'create photo coverage'
            def addCoverageForm = randomAddCoverageForm()
            def bestPhoto = mockMultipartFile()
        and:
            def photoCoverage = eventService.save(event.id, addCoverageForm.nick, addCoverageForm, bestPhoto)
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
