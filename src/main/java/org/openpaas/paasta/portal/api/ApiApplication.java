package org.openpaas.paasta.portal.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
<<<<<<< Updated upstream
import org.springframework.context.annotation.ComponentScan;
=======
>>>>>>> Stashed changes

import org.springframework.context.annotation.ComponentScan;

<<<<<<< Updated upstream
@EnableCircuitBreaker
@EnableDiscoveryClient
=======
/**
 * All you need to run a Eureka registration server.
 *
 * @author Paul Chapman
 */
>>>>>>> Stashed changes
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"org.openpaas.paasta.portal.api"})
public class ApiApplication {

	/**
	 * Run the application using Spring Boot and an embedded servlet engine.
	 *
	 * @param args Program arguments - ignored.
	 */
	public static void main(String[] args) {
		// Tell server to look for registration.properties or registration.yml

		SpringApplication.run(ApiApplication.class, args);
	}







}