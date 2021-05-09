package com.sportphotos.domain;

import com.sportphotos.domain.events.EventMapper;
import com.sportphotos.domain.events.EventsService;
import com.sportphotos.domain.photographers.PhotographersMapper;
import com.sportphotos.domain.photographers.PhotographersService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  EventsService.class,
  EventMapper.class,
  PhotographersService.class,
  PhotographersMapper.class
})
public class DomainConfig {}
