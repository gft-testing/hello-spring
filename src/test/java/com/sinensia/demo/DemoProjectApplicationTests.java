package com.sinensia.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DemoProjectApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void rootTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/", String.class))
				.isEqualTo("Hola ke ase");
	}

	@Test
	void helloTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/hello", String.class))
				.isEqualTo("Hello World!");
	}

	@Test
	void helloNameTest(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/hello?name=Javier", String.class))
				.isEqualTo("Hello Javier!");
	}

	@Test
	void helloNames(@Autowired TestRestTemplate restTemplate) {
		String[] arr = {"Javier","Javier+Arturo","Rodriguez"};
		for(String name: arr) {
			assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
					.isEqualTo("Hello "+name+"!");
		}
	}

	@Autowired TestRestTemplate restTemplate;
	@ParameterizedTest
	@ValueSource(strings = {
			"Javier","Hello Javier!",
			"Javier+Arturo","Hello Javier Arturo!",
			"Arturo",
			"Rodriguez"
	})
	void helloParamNames(String name) {
		assertThat(restTemplate.getForObject("/hello?name="+name, String.class))
				.isEqualTo("Hello "+name+"!");
	}


	@Test
	void canAdd(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1&b=2", String.class))
				.isEqualTo("3");
	}

	@Test
	void canAddZero(@Autowired TestRestTemplate restTemplate) {
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

}
