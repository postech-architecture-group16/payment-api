package com.fiap.challenge.payment.infra.database.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Generated;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Generated
public class DynamoDbConfig {

	@Bean
	public DynamoDbClient dynamoDbClient() {
		return DynamoDbClient.builder()
			.region(Region.US_EAST_1)
		    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
			.build();
	}

}