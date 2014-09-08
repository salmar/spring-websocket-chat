package com.sergialmar.wschat.config;

import java.io.IOException;
import java.net.ServerSocket;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import redis.embedded.RedisServer;

@Configuration
public class RedisConfig {

	@Bean
	public RedisServerBean redisServer() {
		return new RedisServerBean();
	}

	class RedisServerBean implements InitializingBean, DisposableBean {
		private RedisServer redisServer;


		@Override
		public void afterPropertiesSet() throws Exception {
			redisServer = new RedisServer(getPort());
			redisServer.start();
		}

		@Override
		public void destroy() throws Exception {
			if(redisServer != null) {
				redisServer.stop();
			}
		}
	}

	@Bean
	public JedisConnectionFactory connectionFactory() throws Exception {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setPort(getPort());
		return factory;
	}

	@Bean
	public RedisTemplate<String,ExpiringSession> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, ExpiringSession> template = new RedisTemplate<String, ExpiringSession>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setConnectionFactory(connectionFactory);
		return template;
	}

	@Bean
	public RedisOperationsSessionRepository sessionRepository(RedisTemplate<String, ExpiringSession> redisTemplate) {
		return new RedisOperationsSessionRepository(redisTemplate);
	}



	private Integer availablePort;

	private int getPort() throws IOException {
		if(availablePort == null) {
			ServerSocket socket = new ServerSocket(0);
			availablePort = socket.getLocalPort();
			socket.close();
		}
		return availablePort;
	}
}