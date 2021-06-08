package net.atopecode.optionals.model;

import java.util.Optional;

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
	
	//Añadimos métodos 'getter' que devuelvan un 'Optional' de cada campo:
	public Optional<String> getOptionalStreet() {
		return Optional.ofNullable(street);
	}
	
	public Optional<Integer> getOptionalNumber() {
		return Optional.ofNullable(number);
	}
	
	public Optional<State> getOptionalState() {
		return Optional.ofNullable(state);
	}

	
	@Override
	public String toString() {
		return "Address [street=" + street + ", number=" + number + ", state=" + state + "]";
	}
	
	
}
