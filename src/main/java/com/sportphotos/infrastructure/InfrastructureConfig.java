package com.sportphotos.infrastructure;

import com.sportphotos.infrastructure.algolia.AlgoliaConfig;
import com.sportphotos.infrastructure.database.init.DatabaseInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AlgoliaConfig.class, DatabaseInitializer.class})
public class InfrastructureConfig {}
