package com.sportphotos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class ApplicationFT {

  @Autowired private ApplicationContext context;

  @Test
  public void shouldStartSpringContext() {
    // expect
    Assertions.assertThat(context).isNotNull();
  }
}
