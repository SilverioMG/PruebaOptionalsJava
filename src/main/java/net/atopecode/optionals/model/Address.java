package net.atopecode.optionals.model;

public class Address {
	
	private String street;
	
	private Integer number;
	
	private State state;
	
	public Address(String street, Integer number, State state) {
		this.street = street;
		this.number = number;
		this.state = state;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", number=" + number + ", state=" + state + "]";
	}
	
	
}
