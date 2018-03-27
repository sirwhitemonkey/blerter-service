package com.blerter.rest.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.blerter.constant.Status;
import com.blerter.persistence.Health;
import com.blerter.service.BlerterService;
import com.blerter.service.model.Response;

import reactor.core.publisher.Flux;

@Configuration
public class RouterMasterController {

	@Autowired
	private BlerterService blerterService;

	@Bean
	public RouterFunction<?> reactor() {

		return nest(path("/router"),

				route(RequestPredicates.GET("/"), request -> {
					Flux<Response> responses;
					Response response = new Response();
					boolean hasException = false;
					try {

						response.setResponseCode(Status.Ok.value());
						response.setData("Server health ok");

					} catch (Exception exc) {
						response.setResponseCode(Status.BadRequest.value());
						response.setData(exc.getMessage());
						hasException = true;
					}
					responses = Flux.just(response);
					if (hasException) {
						return badRequest().body(responses, Response.class);
					} else {
						return ok().body(responses, Response.class);
					}

				}).andRoute(RequestPredicates.POST("/admin/health"), request -> {
					Flux<Response> responses;
					Response response = new Response();
					boolean hasException = false;
					try {
						final String token = request.headers().header("token").get(0);

						if (token == null || (token != null && token.isEmpty())) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("Token is required");
							responses = Flux.just(response);

						} else {
							responses = blerterService.rxPostHealth(token, request.bodyToFlux(Health.class));

						}
					} catch (Exception exc) {
						response.setResponseCode(Status.BadRequest.value());
						response.setData(exc.getMessage());
						responses = Flux.just(response);
						hasException = true;
					}
					if (hasException) {
						return badRequest().body(Flux.just(response), Response.class);
					} else {
						return ok().body(responses, Response.class);
					}

				}).andRoute(RequestPredicates.PUT("/admin/health"), request -> {
					Flux<Response> responses;
					Response response = new Response();
					boolean hasException = false;
					try {
						final String token = request.headers().header("token").get(0);

						if (token == null || (token != null && token.isEmpty())) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("Token is required");
							responses = Flux.just(response);

						} else {
							responses = blerterService.rxPutHealth(token, request.bodyToFlux(Health.class));

						}
					} catch (Exception exc) {
						response.setResponseCode(Status.BadRequest.value());
						response.setData(exc.getMessage());
						responses = Flux.just(response);
						hasException = true;
					}
					if (hasException) {
						return badRequest().body(responses, Response.class);
					} else {
						return ok().body(responses, Response.class);
					}

				}).andRoute(RequestPredicates.DELETE("/admin/health"), request -> {
					Flux<Response> responses;
					Response response = new Response();
					boolean hasException = false;
					try {
						final String token = request.headers().header("token").get(0);

						final Long id = new Long(request.queryParam("id").get());

						if (token == null || (token != null && token.isEmpty())) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("Token is required");
							responses = Flux.just(response);

						} else if (id == null || (id != null && id == 0)) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("id is required");
							responses = Flux.just(response);

						} else {
							responses = blerterService.rxDeleteHealth(token, id);

						}
					} catch (Exception exc) {
						response.setResponseCode(Status.BadRequest.value());
						response.setData(exc.getMessage());
						responses = Flux.just(response);
						hasException = true;
					}
					if (hasException) {
						return badRequest().body(responses, Response.class);
					} else {
						return ok().body(responses, Response.class);
					}

				}).andRoute(RequestPredicates.GET("/admin/health"), request -> {
					Flux<Response> responses;
					Response response = new Response();
					boolean hasException = false;
					try {
						final String token = request.headers().header("token").get(0);

						final Long id = new Long(request.queryParam("id").get());

						if (token == null || (token != null && token.isEmpty())) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("Token is required");
							responses = Flux.just(response);

						} else if (id == null || (id != null && id == 0)) {
							response.setResponseCode(Status.BadRequest.value());
							response.setData("id is required");
							responses = Flux.just(response);

						} else {
							responses = blerterService.rxGetHealth(token, id);

						}
					} catch (Exception exc) {
						response.setResponseCode(Status.BadRequest.value());
						response.setData(exc.getMessage());
						responses = Flux.just(response);
						hasException = true;
					}
					if (hasException) {
						return badRequest().body(responses, Response.class);
					} else {
						return ok().body(responses, Response.class);
					}
				}));

	}

}
