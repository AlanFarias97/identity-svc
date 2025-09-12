package com.acme.identity.identity_svc;

import org.springframework.boot.SpringApplication;

public class TestIdentitySvcApplication {

	public static void main(String[] args) {
		SpringApplication.from(IdentitySvcApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
