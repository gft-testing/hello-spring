package com.sinensia.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoProjectApplicationTests {

	@Autowired TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void rootTest() {
		assertThat(restTemplate.getForObject("/", String.class))
				.isEqualTo("Hola ke ase");
	}

	@Test
	void helloTest() {
		assertThat(restTemplate.getForObject("/hello", String.class))
				.isEqualTo("Hello World!");
	}

	@Test
	void helloNameTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/hello?name=Javier", String.class))
				.isEqualTo("Hello Javier!");
	}

	@Test
	void helloNames() {
		String[] arr = {"Javier","Javier+Arturo","Rodriguez"};
		for(String name: arr) {
			assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
					.isEqualTo("Hello "+name+"!");
		}
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"Javier",
			"Javier+Arturo",
			"Arturo",
			"Rodriguez"
	})
	void helloParamNames(String name) {
		assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
				.isEqualTo("Hello "+name+"!");
	}

	@DisplayName("test multiple input values")
	@ParameterizedTest(name="[{index}] ({arguments}) \"{0}\" -> \"{1}\" ")
	@CsvSource({
			"a,            Hello a!",
			"b,            Hello b!",
			",             Hello null!",
			"'',           Hello World!",
			"' ',          Hello  !",
			"first+last,   Hello first last!"
	})
	void helloParamNamesCsv(String name, String expected) {
		assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
				.isEqualTo(expected);
	}

	@Test
	void canAdd() {
		assertThat(restTemplate.getForObject("/add?a=1&b=2", String.class))
				.isEqualTo("3");
	}

	@Test
	void canAddZero() {
		assertThat(restTemplate.getForObject("/add?a=0&b=2", String.class))
				.isEqualTo("2");
	}

	@Test
	void canAddNegative(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1&b=-2", String.class))
				.isEqualTo("-1");
	}

	@Test
	void canAddNullA(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=&b=2", String.class))
				.isEqualTo("2");
	}

	@Test
	void canAddNullB(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1&b=", String.class))
				.isEqualTo("1");
	}

	@Test
	void canAddFraction(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1.5&b=2", String.class))
				.isEqualTo("3.5");
	}

	@Test
	void canAddMultiple(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1&b=2", String.class))
				.isEqualTo("3");
		assertThat(restTemplate.getForObject("/add?a=0&b=2", String.class))
				.isEqualTo("2");
		assertThat(restTemplate.getForObject("/add?a=1&b=-2", String.class))
				.isEqualTo("-1");
		assertThat(restTemplate.getForObject("/add?a=&b=2", String.class))
				.isEqualTo("2");
		assertThat(restTemplate.getForObject("/add?a=1.5&b=2", String.class))
				.isEqualTo("3.5");
		assertThat(restTemplate.getForObject("/add?a=1&b=", String.class))
				.isEqualTo("1");
	}

	@DisplayName("multiple additions")
	@ParameterizedTest(name="[{index}] {0} + {1} = {2}")
	@CsvSource({
			"1,   2,   3",
			"1,   1,   2",
			"1.0, 1.0, 2",
			"1,  -2,  -1",
			"1.5, 2,   3.5",
			"'',  2,   2",
			"1.5, 1.5, 3"
	})
	void canAddCsvParameterized(String a, String b, String expected) {
		assertThat(restTemplate.getForObject("/add?a="+a+"&b="+b, String.class))
				.isEqualTo(expected);
	}

	@Test
	void canAddExceptionJsonString() {
		assertThat(restTemplate.getForObject("/add?a=string&b=1", String.class).indexOf("Bad Request"))
				.isGreaterThan(-1);
	}

	@Test
	void canAddFloat() {
		assertThat(restTemplate.getForObject("/add?a=1.5&b=2", Float.class))
				.isEqualTo(3.5f);
	}

	@Test
	void canAddFloatException() {
		Exception thrown = assertThrows(RestClientException.class, ()->{
			restTemplate.getForObject("/add?a=hola&b=2", Float.class);
		});
	}

}
