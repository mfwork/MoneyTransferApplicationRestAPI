package com.revolut.moneytransfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.core.JsonProcessingException;

@Provider
public class JsonProcessingExceptionMapper implements ExceptionMapper<JsonProcessingException> {
	public JsonProcessingExceptionMapper() {
	}

	public Response toResponse(JsonProcessingException exception) {
		Error error = new Error();
		error.key = "bad-json";
		error.message = exception.getMessage();
		return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
	}

	public static class Error {
		public String key;
		public String message;

		public Error() {
		}
	}
}
