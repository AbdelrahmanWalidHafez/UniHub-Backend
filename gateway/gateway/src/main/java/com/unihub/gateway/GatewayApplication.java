package com.unihub.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator uniHubRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p->p.path("/unihub/subscription/**")
						.filters(f->f.rewritePath("/unihub/subscription/(?<segment>.*)","/${segment}"))
						.uri("lb://SUBSCRIPTION"))
				.route(p->p.path("/unihub/universitymanagement/**")
						.filters(f->f.rewritePath("/unihub/universitymanagement/(?<segment>.*)","/${segment}"))
						.uri("lb://UNIVERSITYMANAGEMENT"))
				.route(p->p.path("/unihub/s3/**")
						.filters(f->f.rewritePath("/unihub/s3/(?<segment>.*)","/${segment}"))
						.uri("lb://S3"))
				.build();

	}
}
