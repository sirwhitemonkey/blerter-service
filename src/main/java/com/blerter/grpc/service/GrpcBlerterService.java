package com.blerter.grpc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.blerter.constant.Status;
import com.blerter.factory.UtilsFactory;
import com.blerter.model.Response;
import com.blerter.model.Health.Builder;
import com.blerter.service.BlerterService;

/**
 * Grpc blerter service
 *
 */
@GRpcService
public class GrpcBlerterService extends BlerterServiceGrpc.BlerterServiceImplBase {

	protected static Logger logger = LogManager.getLogger(GrpcBlerterService.class);
	private transient UtilsFactory utilsFactory = UtilsFactory.factory();

	@Autowired
	protected BlerterService blerterService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#
	 * generateToken(com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void generateToken(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {

		String prefix = "generateToken() ";

		Response response = null;
		try {
			response = blerterService.generateToken(String.valueOf(System.currentTimeMillis()));
		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#checkToken
	 * (com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void checkToken(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {
		String prefix = "checkToken() ";

		Response response = null;
		try {
			response = blerterService.checkToken(request.getToken().getToken());
		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#postHealth
	 * (com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void postHealth(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {

		String prefix = "postHealth() ";
		Response response = null;
		try {
			response = blerterService.postHealth(request.getToken().getToken(), request.getHealth());
		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#putHealth(
	 * com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void putHealth(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {
		String prefix = "putHealth() ";

		Response response = null;
		try {
			response = blerterService.putHealth(request.getToken().getToken(), request.getHealth());
		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#
	 * deleteHealth(com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void deleteHealth(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {
		String prefix = "deleteHealth() ";

		Response response = null;
		try {
			response = blerterService.deleteHealth(request.getToken().getToken(), request.getId().getId());
		} catch (Exception exc) {
			exc.printStackTrace();
			Response.Builder responseBuilder = Response.newBuilder();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#getHealth(
	 * com.blerter.grpc.service.Request, io.grpc.stub.StreamObserver)
	 */
	public void getHealth(com.blerter.grpc.service.Request request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {
		String prefix = "getHealth() ";
		Response.Builder responseBuilder = Response.newBuilder();
		Response response = null;
		try {
			response = blerterService.getHealth(request.getToken().getToken(), request.getId().getId());
			if (response.getResponseCode() == Status.Ok.value().intValue()) {
				com.blerter.persistence.Health health = (com.blerter.persistence.Health) utilsFactory
						.deserialize(response.getData());
				Builder healthBuilder = com.blerter.model.Health.newBuilder();
				healthBuilder.setId(health.getId());
				healthBuilder.setName(health.getName());
				healthBuilder.setDescription(health.getDescription());
				if (health.getSafety() != null) {
					health.getSafety().forEach(obj -> {
						com.blerter.model.Safety.Builder safetyBuilder = com.blerter.model.Safety.newBuilder();
						safetyBuilder.setId(obj.getId());
						safetyBuilder.setName(obj.getName());
						safetyBuilder.setDescription(obj.getDescription());
						safetyBuilder.setIsActive(obj.getIsActive());
						healthBuilder.addSafety(safetyBuilder);
					});
				}
				responseBuilder.setResponseCode(Status.Ok.value());
				responseBuilder.addHealth(healthBuilder);
				response = responseBuilder.build();
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			responseBuilder.setResponseCode(Status.InternalServerError.value());
			responseBuilder.setInfo(exc.getMessage());
			response = responseBuilder.build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
		logger.info(prefix + " {service-grpc} completed");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.blerter.grpc.service.BlerterServiceGrpc.BlerterServiceImplBase#ping(com.blerter.model.Empty, io.grpc.stub.StreamObserver)
	 */
	public void ping(com.blerter.model.Empty request,
			io.grpc.stub.StreamObserver<com.blerter.model.Response> responseObserver) {
		Response.Builder responseBuilder = Response.newBuilder();
		responseBuilder.setResponseCode(Status.Ok.value());
		Response response = responseBuilder.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
