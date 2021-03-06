package com.sportphotos.domain

import com.sportphotos.domain.events.InMemoryEventsIndexProvider
import com.sportphotos.domain.events.InMemoryEventsRepository
import com.sportphotos.domain.photographers.InMemoryPhotographersRepository
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import

@TestConfiguration
@Import([
    InMemoryEventsRepository,
    InMemoryPhotographersRepository,
    InMemoryEventsIndexProvider
])
class DomainTestConfig {
}
