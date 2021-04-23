package com.sbu.redistrictingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedistrictingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedistrictingServerApplication.class, args);
		System.out.println("Hello from Server");
	}

}
