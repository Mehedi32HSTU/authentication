package com.reve.authentication.server;

import com.reve.authentication.server.payload.response.MessageResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@SpringBootApplication
@RestController
public class AuthenticationServerApplication {
	public static long appStartTime;

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServerApplication.class, args);
		appStartTime = System.currentTimeMillis();
	}

	@GetMapping("/")
	public ResponseEntity<?> index() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(new MessageResponse("AUTHENTICATION-SERVICE APPLICATION IS RUNNING SINCE : " +
						new Date(AuthenticationServerApplication.appStartTime)));
	}

}
