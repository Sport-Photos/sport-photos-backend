package com.sportphotos.domain.fake;

import java.time.LocalDate;
import lombok.Value;

@Value
public class FakeOutput {
  Integer id;
  String name;
  String description;
  String status;
  LocalDate dateOfSomething;
}
