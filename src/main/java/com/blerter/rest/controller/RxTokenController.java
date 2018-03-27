package com.blerter.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blerter.constant.Status;
import com.blerter.service.BlerterService;
import com.blerter.service.model.Response;

import reactor.core.publisher.Flux;

/**
 * Token controller
 *
 */
@RestController
public class RxTokenController {
	
	private static Logger logger = LogManager.getLogger(RxTokenController.class);
	@Autowired
	private BlerterService blerterService;
	
	public RxTokenController() {
		logger.info("<RxTokenController> initialised");
	}
	
	/**
	 * Post token
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/security/token", method = RequestMethod.POST)
	@ResponseBody
	public Flux<Response> rxPostToken(
			@RequestHeader(value = "user-agent", required = false) final String userAgent) throws Exception {
		String prefix = "rxPostHrxPostToken() ";
		logger.info(prefix + " start request user-agent:" + userAgent);

		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxGenerateToken(String.valueOf(System.currentTimeMillis()));
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}
	
	/**
	 * Check token
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/reactor/security/token", method = RequestMethod.GET)
	@ResponseBody
	public Flux<Response> rxCheckToken(
			@RequestHeader(value = "token", required = true) final String token,
			@RequestHeader(value = "user-agent", required = false) final String userAgent) throws Exception {
		String prefix = "rxCheckToken() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		Flux<Response> responses;
		Response response = new Response();
		try {
			responses = blerterService.rxCheckToken(token);
		} catch (Exception exc) {
			response.setResponseCode(Status.BadRequest.value());
			response.setData(exc.getMessage());
			responses = Flux.just(response);
		}
		logger.info(prefix + " end request ");
		return responses;
	}
}

