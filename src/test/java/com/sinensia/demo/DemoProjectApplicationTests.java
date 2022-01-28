package com.sinensia.demo;

import org.junit.jupiter.api.Test;
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
	void canAdd(@Autowired TestRestTemplate restTemplate) {
		assertThat(restTemplate.getForObject("/add?a=1&b=2", String.class))
				.isEqualTo("3");
	}

}
