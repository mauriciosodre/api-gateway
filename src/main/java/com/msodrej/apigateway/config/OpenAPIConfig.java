package com.msodrej.apigateway.config;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
public class OpenAPIConfig {

  @Bean
  @Lazy(value = false)
  public List<GroupedOpenApi> apis(
      SwaggerUiConfigParameters uiConfigParams, RouteDefinitionLocator locator) {

    var definitions = locator.getRouteDefinitions().collectList().block();

    Objects.requireNonNull(definitions).stream()
        .filter(routeDefinition -> routeDefinition.getId().matches(".*-service"))
        .forEach(
            routeDefinition -> {
              var name = routeDefinition.getId();
              uiConfigParams.addGroup(name);
              GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            });

    return new ArrayList<>();
  }
}
