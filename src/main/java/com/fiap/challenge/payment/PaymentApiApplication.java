package com.fiap.challenge.payment;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableRabbit
@EnableMongoRepositories
@SpringBootApplication
public class PaymentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApiApplication.class, args);
	}

}
