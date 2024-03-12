package ru.sergey.patseev.migration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for database migration.
 */
@SpringBootApplication
public class MigrationServiceApplication {

	/**
	 * Application entry point.
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MigrationServiceApplication.class, args);
		System.exit(0);
	}
}