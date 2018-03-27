package com.blerter.factory;

import org.springframework.http.HttpStatus;

import com.blerter.constant.Status;


/**
 * Response template factory.
 */
public final class ResponseFactory {

	private static ResponseFactory instance;

	private ResponseFactory() {
	}

	/**
	 * Instance
	 */
	public static ResponseFactory factory() {
		if (instance == null) {
			instance = new ResponseFactory();
		}
		return instance;
	}

	/** Filter http status
	 * @param responseCode
	 */
	public HttpStatus getHttpStatus(Integer responseCode) {
		if (responseCode.equals(Status.Ok.value())) {
			return HttpStatus.OK;
		} else if (responseCode.equals(Status.BadRequest.value())) {
			return HttpStatus.BAD_REQUEST;
		} else if (responseCode.equals(Status.InternalServerError.value())) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.SERVICE_UNAVAILABLE;
	}
	
	
}
