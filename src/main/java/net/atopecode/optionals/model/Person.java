package net.atopecode.optionals.model;

import java.util.Optional;

public class Person {
	
	private String name;
	
	private String email;
	
	private Address address;
	
	public Person() {
		
	}
	
	public Person(String name, String email, Address address) {
		this.name = name;
		this.email = email;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	//Añadimos métodos 'getter' que devuelvan un 'Optional' de cada campo:
	public Optional<String> getOptionalName() {
		return Optional.ofNullable(name);
	}
	
	public Optional<String> getOptionalEmail() {
		return Optional.ofNullable(email);
	}

	public Optional<Address> getOptionalAddress() {
		return Optional.ofNullable(address);
	}
	
	
	@Override
	public String toString() {
		return "Person [name=" + name + ", email=" + email + ", address=" + address + "]";
	}	
	
}
