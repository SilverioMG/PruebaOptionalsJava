package net.atopecode.optionals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.atopecode.optionals.model.Address;
import net.atopecode.optionals.model.Person;
import net.atopecode.optionals.model.State;
import net.atopecode.optionals.repository.PersonRepository;

//Notas:
/*
 * -Para crear un 'Optional' usar siempre 'Optional.ofNullable()' para evitar 'NullPointerExceptions' (porque con 'Optional.get()' si el Optional está vacío se produce una Exception).
 * -Para obtener el valor de un 'Optional' usar siempre 'orElse()' si se indica directamente un valor, sino tener en cuenta que si se utiliza una función siempre se va a ejecutar
 *  aunque el 'Optional' no esté vacio. Para asegurarse de que cuando el Optional está vacío no se ejecute la función que devolverá su valor, usar 'orElseGet()'.  
 */

public class OptionalUnitTest {
	
	private PersonRepository personRepository;
	
	@BeforeEach
	public void init() {
		personRepository = new PersonRepository();
	}
	
	//Creation tests:
	@Test
	public void whenCreateFrom_NullValue_then_NullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> {
					Person person = null;
					Optional<Person> personOpt = Optional.of(person);
				});			
	}
	
	@Test
	public void whenCreateFrom_NotNullValue_then_Ok() {
		Person person = personRepository.getPersonTest();
		Optional<Person> personOpt = Optional.of(person);
		System.out.println(personOpt.get());
		
		assertNotNull(personOpt.get());
	}
	
	@Test
	public void whenCreateFrom_NullValue_with_ofNullable_then_EmptyOptional() {
		Person person = null;
		Optional<Person> personOpt = Optional.ofNullable(person);

		assertTrue(personOpt.isEmpty());
		assertEquals(personOpt, Optional.empty());
	}
	
	@Test
	public void whenCreateFrom_NotNullValue_with_ofNullable_then_Ok() {
		Person person = personRepository.getPersonTest();
		Optional<Person> personOpt = Optional.ofNullable(person);
		
		assertFalse(personOpt.isEmpty());
		assertEquals(personOpt.get(), person);
	}	

	
	//Get tests:
	@Test
	public void checkingOptionalValue() {
		Optional<String> emptyOptional = Optional.ofNullable(null);
		boolean isEmpty = emptyOptional.isEmpty();
		assertTrue(isEmpty);
		
		Optional<String> notEmptyOptional = Optional.ofNullable("Valor1");
		isEmpty = notEmptyOptional.isEmpty();
		assertFalse(isEmpty);
	}
	
	@Test
	public void gettingValueFrom_EmptyOptional_then_NoSuchElementException() {
		Exception ex = assertThrows(NoSuchElementException.class, 
				() -> {
					Optional<String> emptyOptional = Optional.empty();
				    emptyOptional.get();
				});
		
		assertNotNull(ex);
	}
	
	@Test
	public void gettingValueFrom_NotEmptyOptional_then_Ok() {
		Optional<String> emptyOptional = Optional.of("Value");
		String value = emptyOptional.get();
		System.out.println(value);
	}
	
	@Test
	public void safeGetFromOptional_with_orElse() {
		String defaultValue = "Default Value";
		Optional<String> emptyOptional = Optional.ofNullable(null);
		String value = emptyOptional.orElse(defaultValue);
		
		assertEquals(value, defaultValue);
	}
	
	@Test
	public void safeGetFromOptional_with_orElse_alwaysExecuteFunction() {
		Person person = personRepository.getPersonTest();
		String notEmptyValue = "NotEmptyOptional";
		Optional<String> emptyOptional = Optional.ofNullable(notEmptyValue);
		
		//El objeto 'emptyOptional' no está vacío, pero al utilizar 'orElse()' para recuperar su valor, se ejecutará de todas forma la función enviada como parámetro 'getPersonName()'.
		//Para evitar que esto suceda es mejor utilizar siempre 'orElseGet()', o si se utiliza 'orElse()' indicar siempre un valor directamente en vez de una función.
		String modifiedName = "Modified Name";
		String value = emptyOptional.orElse(getModifiedPersonName(person, modifiedName));
		
		assertNotNull(value);
		assertEquals(value, notEmptyValue);
		assertEquals(person.getName(), modifiedName);
	}
	
	private String getModifiedPersonName(Person person, String modifiedName) {
		person.setName(modifiedName);
		System.out.println("getModifiedPersonName() executed!");
		
		return person.getName();
	}
	
	@Test
	public void safeGetFromOptional_with_orElseGet_ExecuteFunction_onlyWhen_emptyOptional() {
		Person person = personRepository.getPersonTest();

		//Declaro la expresión Lambda en una variable para poder reutilizarla en ambos casos.
		String modifiedName = "Modified Name";
		Supplier<String> supplierLambda = () -> {
			person.setName(modifiedName);
			System.out.println("LambdaModifiedPersonName() executed!"); //Nunca se va a ejecutar esta función lambda.
			return person.getName();
		};

		//Caso 1: El Optional tiene valor y por lo tanto no se ejecuta la función lambda del 'orElseGet()':
		String notEmptyValue = "NotEmptyOptional";
		Optional<String> emptyOptional = Optional.ofNullable(notEmptyValue);
		String resultOptional = emptyOptional.orElseGet(supplierLambda);
		assertNotNull(resultOptional);
		assertEquals(resultOptional, notEmptyValue);
		assertNotEquals(person.getName(), modifiedName);
		
		//Caso 2: El Optional está vacío (no tiene valor) y se ejecuta la función lambda del 'orElseGet()':
		emptyOptional = Optional.ofNullable(null);
		resultOptional = emptyOptional.orElseGet(supplierLambda);
		assertNotNull(resultOptional);
		assertEquals(resultOptional, modifiedName);
		assertEquals(person.getName(), modifiedName);	
	}
		
	//Null Checks validations without using 'if' staments:
	@Test
	public void getAddressField_with_AddressNull() {
		Person personWithAddressNull = personRepository.getPersonWithAddressNull();
		
		//Se lanza una Exception ya que 'personWithAddressNull.address' vale 'null'.
		assertThrows(NullPointerException.class,
				() -> {
					String streetFails = personWithAddressNull.getAddress().getStreet();
				});
		
		//No se lanza Exception y se obtiene el valor 'null' por defecto para el campo 'street' al no existir 'Address' para el objeto 'Person'.
		String street = Optional.ofNullable(personWithAddressNull)
				.map(person -> person.getAddress())
				.map(address -> address.getStreet()) //La lambda de este 'map()' para obtener el campo 'street' no se llega a ejecutar porque el 'map()' anterior devuelve un 'Optional' vacío (null/empty).
				.orElseGet(() -> null);	
		assertTrue(street == null);
	}
	
	@Test
	public void getAddressField_with_AddressNotNull() {
		Person personWithAddress = personRepository.getPersonTest();
		String street = personWithAddress.getAddress().getStreet();
		assertTrue(street != null);
				
		//En este caso se utiliza 'MethodReference' en el método 'map()' en vez de expresiones lambda pero el resultado es el mismo.
		String streetOptional = Optional.ofNullable(personWithAddress)
				.map(Person::getAddress)
				.map(Address::getStreet)
				.orElseGet(() -> null);	
		assertTrue(streetOptional != null);
		assertEquals(streetOptional, street);
	}
	
	@Test
	public void getStateField_with_StateNull() {
		Person personWithStateNull = personRepository.getPersonWithStateNull();
		
		//Se lanza una Exception ya que 'personWithStateNull.getAdress().getState()' vale 'null'.
		assertThrows(NullPointerException.class,
				() -> {
					String stateNameFails = personWithStateNull.getAddress().getState().getName();
				});
		
		//No se lanza Exception y se obtiene el valor 'null' por defecto para el campo 'stateName' al no existir 'State' para el campo 'Person.Address'.
		String stateName = Optional.ofNullable(personWithStateNull)
				.map(Person::getAddress)
				.map(Address::getState)
				.map(State::getName) //La lambda de este 'map()' para obtener el campo 'name' no se llega a ejecutar porque el 'map()' anterior devuelve un 'Optional' vacío (null/emtpy).
				.orElseGet(() -> null);
		assertTrue(stateName == null);
	}
	
	@Test
	public void getStateField_with_StateNotNull() {
		Person personWithState = personRepository.getPersonTest();
		String stateName = personWithState.getAddress().getState().getName();
		assertTrue(stateName != null);
		
		String stateNameOptional = Optional.ofNullable(personWithState)
				.map(person -> person.getAddress())
				.map(address -> address.getState())
				.map(state -> state.getName())
				.orElseGet(() -> null);
		assertTrue(stateName != null);
		assertEquals(stateNameOptional, stateName);
	}
	
	//Map vs FlatMap Test:
	@Test
	public void mapAndFlatMap() {
		Person person = personRepository.getPersonTest();
		
		String emailWithMap = Optional.ofNullable(person)
				.map(Person::getEmail).orElse(null); //Con 'map()' se hace un wrapper del resultado 'T' dentro de un objeto 'Optional<T>'.
		System.out.println("Con map(): " + emailWithMap);
		assertEquals(emailWithMap, person.getEmail());
		
		//'flatMap()' se utiliza en el caso de que los getters de un objeto devuelvan un 'Optional<T>' en vez de directamente el valor 'T'. Si se quiere utilizar 'map()' con un getter
		//de ese tipo, el resultado del 'map()' sería un 'Optional<Optional<T>>', por eso se utiliza 'flatMap()', porque no hace el wrapper en un 'Optional<T>' del resultado y devolvería
		//un 'Optional<T>' que es lo que se necesita en ese caso en concreto.
		String emailWithFlatMap = Optional.ofNullable(person)
				.flatMap((p -> Optional.ofNullable(p.getEmail()))).orElse(null); //Con 'flatMap()' se devuelve directamente un 'Optional<T>' con el resultado deseado.
		System.out.println("Con faltMap(): " + emailWithFlatMap);
		assertEquals(emailWithFlatMap, person.getEmail());
		
		assertEquals(emailWithMap, emailWithFlatMap);
	}
	
}