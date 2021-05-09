package com.sportphotos.domain.photographers;

import com.sportphotos.domain.photographers.model.Rating;
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
