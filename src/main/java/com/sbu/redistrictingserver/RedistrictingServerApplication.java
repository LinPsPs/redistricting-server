package com.sbu.redistrictingserver;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sbu.redistrictingserver.model.DistrictPlan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

@SpringBootApplication
public class RedistrictingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedistrictingServerApplication.class, args);
		System.out.println("Hello from Server");
		new DistrictPlan("GA", "src/main/resources/Districts/GA/Georgia-50.json");
	}

}
