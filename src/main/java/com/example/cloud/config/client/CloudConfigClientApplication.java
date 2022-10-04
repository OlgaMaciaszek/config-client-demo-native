package com.example.cloud.config.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Mono;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CloudConfigClientApplication {


	String injected;
	private static final Log LOG = LogFactory.getLog(CloudConfigClientApplication.class);

	CloudConfigClientApplication(@Value("${test}") String injected) {
		this.injected = injected;
	}

	public static void main(String[] args) {
		SpringApplication.run(CloudConfigClientApplication.class, args);
	}

	@GetMapping("/")
	Mono<String> test() {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Test");
		}
		return Mono.just("Test: " + injected + ", " + LOG.isTraceEnabled());
	}

}

class CloudConfigClientApplicationHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.reflection()
				.registerType(TypeReference.of(ConfigClientAutoConfiguration.class),
						hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS))
				.registerType(TypeReference.of(CloudConfigClientApplication.class),
						hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.DECLARED_FIELDS,
								MemberCategory.INVOKE_DECLARED_METHODS));
	}
}
