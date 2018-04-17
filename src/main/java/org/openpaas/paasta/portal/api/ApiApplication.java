package org.openpaas.paasta.portal.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * All you need to run a Eureka registration server.
 * 
 * @author Paul Chapman
 */
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