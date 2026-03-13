package com.unihub.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
						.filters(f->f
								.rewritePath("/unihub/subscription/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://SUBSCRIPTION"))
				.route(p->p.path("/unihub/universitymanagement/**")
						.filters(f->f
								.rewritePath("/unihub/universitymanagement/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://UNIVERSITYMANAGEMENT"))
				.route(p->p.path("/unihub/s3/**")
						.filters(f->f
								.rewritePath("/unihub/s3/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://S3"))
				.route(p->p.path("/unihub/usage/**")
						.filters(f->f
								.rewritePath("/unihub/usage/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://USAGE"))
				.route(p->p.path("/unihub/announcement/**")
						.filters(f->f
								.rewritePath("/unihub/announcement/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://ANNOUNCEMENT"))
				.route(p->p.path("/unihub/ai/**")
						.filters(f->f
								.rewritePath("/unihub/ai/(?<segment>.*)","/${segment}")
								.requestRateLimiter(config ->config.setRateLimiter(redisRateLimiter()).setKeyResolver(routeKeyResolver()) ))
						.uri("lb://AI"))
				.build();
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(5,10,1);
	}

	@Bean
	KeyResolver routeKeyResolver() {
		return exchange -> {
			String key = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
					.map(String::valueOf)
					.orElse("Anonymous");
			return Mono.just(key);
		};
	}
}
