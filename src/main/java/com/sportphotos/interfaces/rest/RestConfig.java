package com.sportphotos.interfaces.rest;

import com.sportphotos.interfaces.rest.events.EventsController;
import com.sportphotos.interfaces.rest.photographers.PhotographersController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ExceptionMappingConfig.class, EventsController.class, PhotographersController.class})
public class RestConfig {}
