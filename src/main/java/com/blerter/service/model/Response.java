package com.blerter.service.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class Response implements Serializable {

	private Integer responseCode;
	private Object data;
}
