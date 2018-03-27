package com.blerter.rest.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blerter.factory.ResponseFactory;
import com.blerter.service.BlerterService;
import com.blerter.service.model.Response;

/**
 * Token controller
 *
 */
@RestController
public class TokenController {
	
	private static Logger logger = LogManager.getLogger(TokenController.class);
	@Autowired
	private BlerterService blerterService;
	private transient ResponseFactory responseFactory = ResponseFactory.factory();
	
	public TokenController() {
		logger.info("<TokenController> initialised");
	}
	
	/**
	 * Post token
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/security/token", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> postToken(
			@RequestHeader(value = "user-agent", required = false) final String userAgent) throws Exception {
		String prefix = "postToken() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Response grpcResponse = blerterService.generateToken(String.valueOf(System.currentTimeMillis()));
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		response.setData(grpcResponse.getInfo());
		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}
	
	/**
	 * Check token
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/security/token", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Response> checkToken(
			@RequestHeader(value = "token", required = true) final String token,
			@RequestHeader(value = "user-agent", required = false) final String userAgent) throws Exception {
		String prefix = "checkToken() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Response grpcResponse = blerterService.checkToken(token);
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		response.setData(grpcResponse.getInfo());
		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}
}

