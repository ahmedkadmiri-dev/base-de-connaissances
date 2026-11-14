package com.example;

import com.example.aspect.ControllerLoggingAspect;
import com.example.aspect.RepositoryLoggingAspect;
import com.example.aspect.ServiceLoggingAspect;
import com.example.repositories.ReferentielRepository;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestingPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingPocApplication.class, args);
	}
}
