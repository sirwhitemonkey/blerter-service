package com.blerter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;

import com.blerter.constant.Status;
import com.blerter.factory.UtilsFactory;
import com.blerter.grpc.client.GrpcMasterService;
import com.blerter.grpc.client.GrpcTokenService;
import com.blerter.model.Response;
import com.blerter.persistence.Health;
import com.blerter.persistence.Safety;

import reactor.core.publisher.Flux;

/**
 * Blerter service
 *
 */
@GRpcService
public class BlerterService {

	private static Logger logger = LogManager.getLogger(BlerterService.class);
	private transient GrpcMasterService grpcMasterServiceClient = GrpcMasterService.client();
	private transient GrpcTokenService grpcTokenServiceClient = GrpcTokenService.client();
	private transient UtilsFactory utilsFactory = UtilsFactory.factory();

	/**
	 * Generate token
	 * 
	 * @param identity
	 */
	public Response generateToken(String identity) {
		String prefix = "generateToken() ";
		logger.info(prefix);
		Response response = null;
		try {
			response = grpcTokenServiceClient.generateToken(identity);

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Generate token
	 * 
	 * @param identity
	 */
	public Flux<com.blerter.service.model.Response> rxGenerateToken(String identity) {
		String prefix = "rxGenerateToken() ";
		logger.info(prefix);
		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;
		try {
			grpcResponse = grpcTokenServiceClient.generateToken(identity);
			response.setResponseCode(grpcResponse.getResponseCode());
			response.setData(grpcResponse.getInfo());

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}
		return Flux.just(response);
	}
	
	/**
	 * Check token
	 * 
	 * @param identity
	 */
	public Flux<com.blerter.service.model.Response> rxCheckToken(String token) {
		String prefix = "rxCheckToken() ";
		logger.info(prefix);
		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;
		try {
			grpcResponse = grpcTokenServiceClient.checkToken(token);
			response.setResponseCode(grpcResponse.getResponseCode());
			response.setData(grpcResponse.getInfo());

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}
		return Flux.just(response);
	}

	/**
	 * Check token
	 * 
	 * @param identity
	 */
	public Response checkToken(String token) {
		String prefix = "checkToken() ";
		logger.info(prefix);
		Response response = null;
		try {
			response = grpcTokenServiceClient.checkToken(token);

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Post health
	 * 
	 * @param token
	 * @param health
	 */
	public Response postHealth(String token, com.blerter.model.Health request) {
		String prefix = "postHealth() ";
		logger.info(prefix);
		Response response = null;
		try {
			// validate token
			response = checkToken(token);
			if (response.getResponseCode() == Status.Ok.value().intValue()) {

				Health health = new Health();
				health.setId(request.getId());
				health.setName(request.getName());
				health.setDescription(request.getDescription());
				List<com.blerter.persistence.Safety> safety = new ArrayList<com.blerter.persistence.Safety>();
				if (request.getSafetyList() != null) {
					request.getSafetyList().forEach(obj -> {
						Safety sf = new Safety();
						sf.setId(obj.getId());
						sf.setName(obj.getName());
						sf.setDescription(obj.getDescription());
						sf.setIsActive(obj.getIsActive());
						sf.setHealth(health);
						safety.add(sf);
					});
					health.setSafety(safety);
				}

				response = grpcMasterServiceClient.postHealth(health);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Post health
	 * 
	 * @param token
	 * @param health
	 */
	public Flux<com.blerter.service.model.Response> rxPostHealth(String token, Flux<Health> flux) {
		String prefix = "rxPostHealth() ";
		logger.info(prefix);

		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;
		try {
			// validate token
			grpcResponse = checkToken(token);
			if (grpcResponse.getResponseCode() == Status.Ok.value().intValue()) {

				Health health = new Health();

				flux.subscribe(value -> {
					health.setId(value.getId());
					health.setName(value.getName());
					health.setDescription(value.getDescription());
					List<com.blerter.persistence.Safety> safety = new ArrayList<com.blerter.persistence.Safety>();
					if (value.getSafety() != null) {
						value.getSafety().forEach(obj -> {
							Safety sf = new Safety();
							sf.setId(value.getId());
							sf.setName(obj.getName());
							sf.setDescription(obj.getDescription());
							sf.setIsActive(obj.getIsActive());
							sf.setHealth(health);
							safety.add(sf);
						});
						health.setSafety(safety);
					}

				});
				logger.info(prefix + " -> post -> " + utilsFactory.toJson(health));
				grpcResponse = grpcMasterServiceClient.postHealth(health);
			}
			response.setResponseCode(grpcResponse.getResponseCode());
			response.setData(grpcResponse.getInfo());

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}

		return Flux.just(response);
	}

	/**
	 * Put health
	 * 
	 * @param token
	 * @param health
	 */
	public Response putHealth(String token, com.blerter.model.Health request) {
		String prefix = "putHealth() ";
		logger.info(prefix);
		Response response = null;
		try {
			// validate token
			response = checkToken(token);
			if (response.getResponseCode() == Status.Ok.value().intValue()) {

				Health health = new Health();
				health.setId(request.getId());
				health.setName(request.getName());
				health.setDescription(request.getDescription());
				List<com.blerter.persistence.Safety> safety = new ArrayList<com.blerter.persistence.Safety>();
				if (request.getSafetyList() != null) {
					request.getSafetyList().forEach(obj -> {
						Safety sf = new Safety();
						sf.setId(obj.getId());
						sf.setName(obj.getName());
						sf.setDescription(obj.getDescription());
						sf.setIsActive(obj.getIsActive());
						sf.setHealth(health);
						safety.add(sf);
					});
					health.setSafety(safety);
				}
				logger.info(prefix + " -> " + utilsFactory.toJson(health));
				response = grpcMasterServiceClient.putHealth(health);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Pust health
	 * 
	 * @param token
	 * @param health
	 */
	public Flux<com.blerter.service.model.Response> rxPutHealth(String token, Flux<Health> flux) {
		String prefix = "rxPutHealth() ";
		logger.info(prefix);

		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;
		try {
			// validate token
			grpcResponse = checkToken(token);
			if (grpcResponse.getResponseCode() == Status.Ok.value().intValue()) {

				Health health = new Health();

				flux.subscribe(value -> {

					health.setId(value.getId());
					health.setName(value.getName());
					health.setDescription(value.getDescription());
					List<com.blerter.persistence.Safety> safety = new ArrayList<com.blerter.persistence.Safety>();
					if (value.getSafety() != null) {
						value.getSafety().forEach(obj -> {
							Safety sf = new Safety();
							sf.setId(obj.getId());
							sf.setName(obj.getName());
							sf.setDescription(obj.getDescription());
							sf.setIsActive(obj.getIsActive());
							sf.setHealth(health);
							safety.add(sf);
						});
						health.setSafety(safety);
					}

				});
				logger.info(prefix + " -> " + utilsFactory.toJson(health));
				grpcResponse = grpcMasterServiceClient.putHealth(health);
			}
			response.setResponseCode(grpcResponse.getResponseCode());
			response.setData(grpcResponse.getInfo());

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}

		return Flux.just(response);
	}

	/**
	 * Delete health
	 * 
	 * @param token
	 * @param id
	 */
	public Response deleteHealth(String token, Long id) {
		String prefix = "deleteHealth() ";
		logger.info(prefix);
		Response response = null;

		try {
			// validate token
			response = checkToken(token);
			if (response.getResponseCode() == Status.Ok.value().intValue()) {
				response = grpcMasterServiceClient.deleteHealth(id);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Delete health
	 * 
	 * @param token
	 * @param id
	 */
	public Flux<com.blerter.service.model.Response> rxDeleteHealth(String token, Long id) {
		String prefix = "rxDeleteHealth() ";
		logger.info(prefix);
		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;

		try {
			// validate token
			grpcResponse = checkToken(token);
			if (grpcResponse.getResponseCode() == Status.Ok.value().intValue()) {
				grpcResponse = grpcMasterServiceClient.deleteHealth(id);
			}
			response.setResponseCode(grpcResponse.getResponseCode());
			response.setData(grpcResponse.getInfo());

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}
		return Flux.just(response);
	}

	/**
	 * Get health
	 * 
	 * @param token
	 * @param id
	 */
	public Response getHealth(String token, Long id) {
		String prefix = "getHealth() ";
		logger.info(prefix);
		Response response = null;
		try {
			// validate token
			response = checkToken(token);
			if (response.getResponseCode() == Status.Ok.value().intValue()) {
				response = grpcMasterServiceClient.getHealth(id);
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		return response;
	}

	/**
	 * Get health
	 * 
	 * @param token
	 * @param id
	 */
	public Flux<com.blerter.service.model.Response> rxGetHealth(String token, Long id) {
		String prefix = "rxGetHealth() ";
		logger.info(prefix);
		com.blerter.service.model.Response response = new com.blerter.service.model.Response();
		Response grpcResponse = null;
		try {
			// validate token
			grpcResponse = checkToken(token);
			if (grpcResponse.getResponseCode() == Status.Ok.value().intValue()) {
				grpcResponse = grpcMasterServiceClient.getHealth(id);
			}

			response.setResponseCode(grpcResponse.getResponseCode());
			if (grpcResponse.getResponseCode() == Status.Ok.value()) {
				response.setData(utilsFactory.deserialize(grpcResponse.getData()));
			} else {
				response.setData(grpcResponse.getInfo());
			}

		} catch (Exception exc) {
			exc.printStackTrace();
			response.setResponseCode(Status.InternalServerError.value());
			response.setData(exc.getMessage());
		}
		return Flux.just(response);
	}
}
