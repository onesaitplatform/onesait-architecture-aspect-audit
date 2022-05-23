package com.minsait.onesait.architecture.audit;

import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "architecture.audit.async.enabled", havingValue = "true", matchIfMissing = false)
@EnableAsync
public class AsyncConfiguration {

	@Autowired
	private AuditProperties properties;

	@PostConstruct
	private void checkTransactional() {
		if (properties.isTransactional()) {
			throw new IllegalStateException(
					"Audit Library does not work with transactional and async mode enabled. They are incompatible.");
		}
	}

	@Bean(name = "auditAsyncExecutor")
	public Executor asyncExecutor(AuditAsyncProperties asyncProperties) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncProperties.getCorePoolSize());
		executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
		executor.setQueueCapacity(asyncProperties.getQueueCapacity());
		executor.setThreadNamePrefix("AsynchThread-");
		executor.initialize();
		return executor;
	}
}