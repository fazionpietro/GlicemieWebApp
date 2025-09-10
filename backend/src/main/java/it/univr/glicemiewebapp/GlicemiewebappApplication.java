package it.univr.glicemiewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling 
@SpringBootApplication
public class GlicemiewebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlicemiewebappApplication.class, args);
	}

}
