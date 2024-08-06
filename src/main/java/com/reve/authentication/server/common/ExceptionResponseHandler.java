package com.reve.authentication.server.common;

import com.reve.authentication.server.payload.response.MessageResponse;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ExceptionResponseHandler {

	public default ResponseEntity<?> handleException(Exception e) {
		LoggerFactory.getLogger(this.getClass()).error("Exception: {} occurred", e);
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new MessageResponse("Unexpected error. exception: " + e));
	}

	public default ResponseEntity<?> handleException(Exception e, String methodName) {
		LoggerFactory.getLogger(this.getClass()).error("Exception: {} occurred in {} method", e, methodName);
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new MessageResponse("Unexpected error. exception: " + e));
	}
}
