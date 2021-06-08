package net.atopecode.optionals.model;

import java.util.Optional;

public class State {
	
	private String name;
	
	private Integer code;
	
	public State(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	//Añadimos métodos 'getter' que devuelvan un 'Optional' de cada campo:
	public Optional<String> getOptionalName() {
		return Optional.ofNullable(name);
	}
	
	public Optional<Integer> getOptionalCode() {
		return Optional.ofNullable(code);
	}
	
	
	@Override
	public String toString() {
		return "State [name=" + name + ", code=" + code + "]";
	}
	
	
}
