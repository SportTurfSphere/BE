package com.turf.cs;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.time.ZonedDateTime;

@SpringBootApplication
@EnableConfigServer
@Log4j2
public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

	@PostConstruct
	public void init() {
		log.info("Spring boot application running with UTC timezone: {}", ZonedDateTime.now());
	}


}
