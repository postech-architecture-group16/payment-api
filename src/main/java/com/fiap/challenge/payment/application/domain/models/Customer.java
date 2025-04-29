package com.fiap.challenge.payment.application.domain.models;

import java.util.UUID;

public class Customer {

	private UUID id;
	
	private String name;
	
	private String email;
	
	private String documentId;

	public Customer(UUID id, String name, String email, String documentId) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.documentId = documentId;
	}

	
	public Customer(String name, String email, String customerId) {
		this.name = name;
		this.email = email;
		this.documentId = customerId;
	}
	
	public Customer(UUID customerId) {
		this.id = customerId;
	}

	public UUID getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}


	public String getEmail() {
		return email;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	
}
