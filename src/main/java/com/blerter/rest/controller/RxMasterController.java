package com.blerter.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blerter.constant.Status;
import com.blerter.persistence.Health;
import com.blerter.service.BlerterService;
import com.blerter.service.model.Response;

import reactor.core.publisher.Flux;

/**
 * Master controller
 *
 */
@RestController
public class RxMasterController {

	private static Logger logger = LogManager.getLogger(MasterController.class);
	@Autowired
	private BlerterService blerterService;

	public RxMasterController() {
		logger.info("<RxMasterController> initialised");
	}

	/**
	 * Get health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor", method = RequestMethod.GET)
	@ResponseBody
	public Flux<Response> rxInitialise(@RequestHeader(value = "user-agent", required = false) final String userAgent)
			throws Exception {
		String prefix = "rxInitialise() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		Flux<Response> responses;
		Response response = new Response();
		try {

			response.setResponseCode(Status.Ok.value());
			response.setData("Server health ok");

		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
		}
		logger.info(prefix + " end request ");
		responses = Flux.just(response);
		return responses;
	}

	/**
	 * Post health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/admin/health", method = RequestMethod.POST)
	@ResponseBody
	public Flux<Response> rxPostHealth(@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token, @RequestBody final Health health)
			throws Exception {
		String prefix = "rxPostHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);

		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxPostHealth(token, Flux.just(health));
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}

	/**
	 * Put health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/admin/health", method = RequestMethod.PUT)
	@ResponseBody
	public Flux<Response> rxPutHealth(@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token, @RequestBody final Health health)
			throws Exception {
		String prefix = "rxPutHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);

		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxPutHealth(token, Flux.just(health));
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}

	/**
	 * Delete health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/admin/health", method = RequestMethod.DELETE)
	@ResponseBody
	public Flux<Response> rxDeleteHealth(@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token,
			@RequestParam(value = "id", required = true) final Long id) throws Exception {
		String prefix = "rxDeleteHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);

		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxDeleteHealth(token, id);
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}

	/**
	 * Get health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/admin/health", method = RequestMethod.GET)
	@ResponseBody
	public Flux<Response> rxGetHealth(@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token,
			@RequestParam(value = "id", required = true) final Long id) throws Exception {
		String prefix = "rxGetHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);

		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxGetHealth(token, id);
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}
}
