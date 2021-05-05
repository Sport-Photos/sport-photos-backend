package com.sportphotos.interfaces.rest.photographers;

import com.sportphotos.domain.events.model.Rating;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class PhotographersMapper {

  Rating map(AddRatingForm form) {
    return Rating.builder()
        .id(UUID.randomUUID().toString())
        .rate(form.getRate())
        .comment(form.getComment())
        .build();
  }
}
