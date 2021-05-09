package com.sportphotos;

import com.sportphotos.domain.DomainConfig;
import com.sportphotos.infrastructure.InfrastructureConfig;
import com.sportphotos.interfaces.rest.RestConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import({DomainConfig.class, RestConfig.class, InfrastructureConfig.class})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
