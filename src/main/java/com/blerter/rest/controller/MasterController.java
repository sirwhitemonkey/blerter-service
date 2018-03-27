package com.blerter.rest.controller;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blerter.constant.Status;
import com.blerter.factory.ResponseFactory;
import com.blerter.factory.UtilsFactory;
import com.blerter.persistence.Health;
import com.blerter.service.BlerterService;
import com.blerter.service.model.Response;

/**
 * Master controller
 *
 */
@RestController
public class MasterController {

	private static Logger logger = LogManager.getLogger(MasterController.class);
	@Autowired
	private BlerterService blerterService;
	private transient ResponseFactory responseFactory = ResponseFactory.factory();
	private transient UtilsFactory utilsFactory = UtilsFactory.factory();

	public MasterController() {
		logger.info("<MasterController> initialised");
	}

	/**
	 * Get health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Response> initialise(
			@RequestHeader(value = "user-agent", required = false) final String userAgent) throws Exception {
		String prefix = "initialise() ";
		Response response = new Response();
		response.setResponseCode(Status.Ok.value());
		response.setData("Server health ok");
		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}

	/**
	 * Post health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/health", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Response> postHealth(
			@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token, @RequestBody final Health health)
			throws Exception {
		String prefix = "postHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Health.Builder healthBuilder = com.blerter.model.Health.newBuilder();
		healthBuilder.setId(health.getId());
		healthBuilder.setName(health.getName());
		healthBuilder.setDescription(health.getDescription());
		if (health.getSafety() != null) {
			health.getSafety().forEach(safety -> {
				com.blerter.model.Safety.Builder safetyBuilder = com.blerter.model.Safety.newBuilder();
				safetyBuilder.setId(safety.getId());
				safetyBuilder.setName(safety.getName());
				safetyBuilder.setDescription(safety.getDescription());
				safetyBuilder.setIsActive(safety.getIsActive());
				healthBuilder.addSafety(safetyBuilder);
			});
		}
		com.blerter.model.Response grpcResponse = blerterService.postHealth(token, healthBuilder.build());
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		response.setData(grpcResponse.getInfo());

		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}

	/**
	 * Post health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/health", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<Response> putHealth(
			@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token, @RequestBody final Health health)
			throws Exception {
		String prefix = "putHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Health.Builder healthBuilder = com.blerter.model.Health.newBuilder();
		healthBuilder.setId(health.getId());
		healthBuilder.setName(health.getName());
		healthBuilder.setDescription(health.getDescription());
		if (health.getSafety() != null) {
			health.getSafety().forEach(safety -> {
				com.blerter.model.Safety.Builder safetyBuilder = com.blerter.model.Safety.newBuilder();
				safetyBuilder.setId(safety.getId());
				safetyBuilder.setName(safety.getName());
				safetyBuilder.setDescription(safety.getDescription());
				safetyBuilder.setIsActive(safety.getIsActive());
				healthBuilder.addSafety(safetyBuilder);
			});
		}
		com.blerter.model.Response grpcResponse = blerterService.putHealth(token, healthBuilder.build());
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		response.setData(grpcResponse.getInfo());
		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}

	/**
	 * Delete health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/health", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Response> deleteHealth(
			@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token,
			@RequestParam(value = "id", required = true) final Long id) throws Exception {
		String prefix = "deleteHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Response grpcResponse = blerterService.deleteHealth(token, id);
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		response.setData(grpcResponse.getInfo());
		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}

	/**
	 * Get health
	 * 
	 * @param userAgent
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/health", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Response> getHealth(
			@RequestHeader(value = "user-agent", required = false) final String userAgent,
			@RequestHeader(value = "token", required = true) final String token,
			@RequestParam(value = "id", required = true) final Long id) throws Exception {
		String prefix = "getHealth() ";
		logger.info(prefix + " start request user-agent:" + userAgent);
		com.blerter.model.Response grpcResponse = blerterService.getHealth(token, id);
		Response response = new Response();
		response.setResponseCode(grpcResponse.getResponseCode());
		if (response.getResponseCode().equals(Status.Ok.value())) {
			com.blerter.persistence.Health health = (com.blerter.persistence.Health) utilsFactory
					.deserialize(grpcResponse.getData());
			response.setData(health);
		} else {
			response.setData(grpcResponse.getInfo());
		}

		logger.info(prefix + " end request ");
		ResponseEntity<Response> responseEntity = new ResponseEntity<>(response,
				responseFactory.getHttpStatus(response.getResponseCode()));
		return responseEntity;
	}
}

