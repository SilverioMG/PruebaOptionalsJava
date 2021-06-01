package net.atopecode.optionals.repository;

import net.atopecode.optionals.model.Address;
import net.atopecode.optionals.model.Person;
import net.atopecode.optionals.model.State;

public class PersonRepository {

	private State getState() {
		return new State("State1", 12345);
	}
	
	private Address getAddress() {
		Address address = getAddressWithStateNull();
		address.setState(getState());
		return address;
	}
	
	private Address getAddressWithStateNull() {
		return new Address("Street1", 55, null);
	}
	
	public Person getPersonWithAddressNull() {
		return new Person("Fulano", "fulano@email.es", null);
	}
	
	public Person getPersonTest() {
		Address address = getAddress();
		Person person = getPersonWithAddressNull();
		person.setAddress(address);
		return person;
	}
}
