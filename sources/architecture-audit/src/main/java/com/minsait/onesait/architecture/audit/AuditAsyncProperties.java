package com.minsait.onesait.architecture.audit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "architecture.audit.async")
@EnableScheduling
@Data
public class AuditAsyncProperties {

	/**
	 * If it is enabled the transactional mode async mode will not work.
	 */
	private boolean enabled = true;

	/**
	 * Min Core Pool Size
	 */
	private int corePoolSize = 3;

	/**
	 * Max Pool Size
	 */
	private int maxPoolSize = Integer.MAX_VALUE;

	/**
	 * Queue Capacity
	 */
	private int queueCapacity = Integer.MAX_VALUE;
}
