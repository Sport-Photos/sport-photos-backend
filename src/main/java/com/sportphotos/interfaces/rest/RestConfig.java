package com.sportphotos.interfaces.rest;

import com.sportphotos.interfaces.rest.fake.FakeController;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({FakeController.class})
public class RestConfig {}
