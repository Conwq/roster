package ru.sergey.patseev.authorization_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to run the authorization application.
 */
@SpringBootApplication
public class AuthorizationApplication {

	/**
	 * Main method to start the authorization application.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}
}
