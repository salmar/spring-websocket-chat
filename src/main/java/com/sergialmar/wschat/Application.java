package com.sergialmar.wschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.WebSocketTraceChannelInterceptorAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebSocketTraceChannelInterceptorAutoConfiguration.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
