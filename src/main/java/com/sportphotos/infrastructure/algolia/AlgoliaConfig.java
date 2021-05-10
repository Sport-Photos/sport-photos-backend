package com.sportphotos.infrastructure.algolia;

import com.algolia.search.HttpRequester;
import com.algolia.search.JavaNetHttpRequester;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AlgoliaSearchClient.class})
public class AlgoliaConfig {

  @Bean
  public SearchClient searchClient(
      @Value("${algolia.application_id}") String applicationId,
      @Value("${algolia.api_key}") String apiKey) {

    SearchConfig config = new SearchConfig.Builder(applicationId, apiKey).build();
    HttpRequester javaNetHttpRequester = new JavaNetHttpRequester(config);

    return new SearchClient(config, javaNetHttpRequester);
  }
}
