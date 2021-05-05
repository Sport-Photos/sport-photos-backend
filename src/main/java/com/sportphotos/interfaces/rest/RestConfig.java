package com.sportphotos.interfaces.rest;

import com.sportphotos.interfaces.rest.events.EventMapper;
import com.sportphotos.interfaces.rest.events.EventsController;
import com.sportphotos.interfaces.rest.photographers.PhotographersController;
import com.sportphotos.interfaces.rest.photographers.PhotographersMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  ExceptionMappingConfig.class,
  EventsController.class,
  EventMapper.class,
  PhotographersController.class,
  PhotographersMapper.class
})
public class RestConfig {}
