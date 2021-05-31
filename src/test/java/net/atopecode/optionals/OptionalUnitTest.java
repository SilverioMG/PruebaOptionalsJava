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

import org.junit.jupiter.api.Test;

import net.atopecode.optionals.model.Person;

public class OptionalUnitTest {

	
	private Person getPersonTest() {
		return new Person("Fulano", "fulan@email.es");
	}
	
	@Test
	public void whenCreateFromNullValue_thenNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> {
					Person person = null;
					Optional<Person> personOpt = Optional.of(person);
				});			
	}
	
	@Test
	public void whenCreateFromNotNullValue_thenOk() {
		Person person = getPersonTest();
		Optional<Person> personOpt = Optional.of(person);
		System.out.println(personOpt.get());
		
		assertNotNull(personOpt.get());
	}
	
	@Test
	public void whenCreateFromNullValue_with_ofNullable_thenEmptyOptional() {
		Person person = null;
		Optional<Person> personOpt = Optional.ofNullable(person);

		assertTrue(personOpt.isEmpty());
		assertEquals(personOpt, Optional.empty());
	}
	
	@Test
	public void whenCreateFromNotNullValue_with_ofNullable_thenOk() {
		Person person = getPersonTest();
		Optional<Person> personOpt = Optional.ofNullable(person);
		
		assertFalse(personOpt.isEmpty());
		assertEquals(personOpt.get(), person);
	}	

	@Test
	public void whenCreateEmptyOptionalAndGet_thenNoSuchElementException() {
		Exception ex = assertThrows(NoSuchElementException.class, 
				() -> {
					Optional<String> emptyOptional = Optional.empty();
				    emptyOptional.get();
				});
		
		assertNotNull(ex);
	}
	
	@Test
	public void whenCreateNotEmptyOptionalAndGet_thenOk() {
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
		Person person = getPersonTest();
		String notEmptyValue = "NotEmptyOptional";
		Optional<String> emptyOptional = Optional.ofNullable(notEmptyValue);
		
		//El objeto 'emptyOptional' no está vacío, pero al utilizar 'orElse()' para recuperar su valor, se ejecutará de todas forma la función enviada como parámetro 'getPersonName()'.
		//Para evitar que esto suceda es mejor utilizar siempre 'orElseGet()', o si se utiliza 'orElse()' devolver siempre un valor directamente en vez de una función.
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
		Person person = getPersonTest();

		//Declaro la expresión Lambda en una variable para poder reutilizarla en ambos casos.
		String modifiedName = "Modified Name";
		Supplier<String> supplierLambda = () -> {
			person.setName(modifiedName);
			System.out.println("LambdaModifiedPersonName() executed!"); //Nunca se va a ejecutar esta función lambda.
			return person.getName();
		};
		
		/*String value = emptyOptional.orElseGet(() -> {
			person.setName(modifiedName);
			System.out.println("LambdaModifiedPersonName() executed!"); //Nunca se va a ejecutar esta función lambda.
			return person.getName();
		});*/

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
	

	
	
	
	
	@Test
	public void mapAndFlatMap() {
		Person person = getPersonTest();
		
		String emailWithMap = Optional.ofNullable(person)
				.map(Person::getEmail).orElse(null); //Con 'map()' se hace un wrapper del resultado 'T' dentro de un objeto 'Optinal<T>'.
		System.out.println("Con map(): " + emailWithMap);
		assertEquals(emailWithMap, person.getEmail());
		
		
		String emailWithFlatMap = Optional.ofNullable(person)
				.flatMap((p -> Optional.ofNullable(p.getEmail()))).orElse(null); //Con 'flatMap()' se devuelve directamente un 'Optinal<T>' con el resultado deseado.
		System.out.println("Con faltMap(): " + emailWithFlatMap);
		assertEquals(emailWithFlatMap, person.getEmail());
		
		assertEquals(emailWithMap, emailWithFlatMap);
	}
	
	//Notas:
	/*
	 * -Para crear un 'Optional' usar siempre 'Optional.ofNullable()' para evitar 'NullPointerExceptions'.
	 * -Para obtener el valor de un 'Optional' usar siempre 'orElse()' si se indica directamente un valor, sino tener en cuenta que si se utiliza una función siempre se va a ejecutar
	 *  aunque el 'Optional' no esté vacio. Para asegurarse de que cuando el Optional está vacío no se ejecute la función que devolverá su valor, usar 'orElseGet()'.  
	 */
}