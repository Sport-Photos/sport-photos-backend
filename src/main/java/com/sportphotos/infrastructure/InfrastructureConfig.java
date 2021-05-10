package com.sportphotos.infrastructure;

import com.sportphotos.infrastructure.algolia.AlgoliaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AlgoliaConfig.class})
public class InfrastructureConfig {}
