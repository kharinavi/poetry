package ru.kharina.study.poetry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ru.kharina.study.poetry.property.RsaProperties.class)
public class PoetryApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoetryApplication.class, args);
	}

}
