package net.atopecode.optionals.model;

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

	@Override
	public String toString() {
		return "Person [name=" + name + ", email=" + email + ", address=" + address + "]";
	}	
	
}
