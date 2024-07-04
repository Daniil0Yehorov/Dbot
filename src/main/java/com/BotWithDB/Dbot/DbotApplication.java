package com.BotWithDB.Dbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.BotWithDB.Dbot")
public class DbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbotApplication.class, args);
	}

}
