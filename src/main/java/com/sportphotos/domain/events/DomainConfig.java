package com.sportphotos.domain.events;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EventsService.class, PhotographersService.class})
public class DomainConfig {}
