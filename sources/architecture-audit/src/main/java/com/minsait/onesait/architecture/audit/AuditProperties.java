package com.minsait.onesait.architecture.audit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "architecture.audit")
@EnableScheduling
@Data
@Validated
public class AuditProperties {

	/**
	 * Disable all Audit event point cuts
	 */
	private boolean enabled = true;

	/**
	 * Project name
	 */
	private String app = "Audit Library";

	/**
	 * Event properties
	 */
	private Event event;

	/**
	 * Audit Library to write logs to various destinations
	 */
	private Destination destination;

	/**
	 * Enable transactional audit mode. It throws a AuditException when it is not
	 * possible audit the resource
	 */
	private boolean transactional = false;

	/**
	 * Enable audit of response of methods
	 */
	private boolean output = false;

	@Data
	public static class Destination {

		/**
		 * Console processor configuration properties
		 */
		private Console console;

		/**
		 * Logger processor configuration properties
		 */
		private Logger logger;

		/**
		 * File processor configuration properties
		 */
		private File file;

		/**
		 * Kafka processor configuration properties
		 */
		private Kafka kafka;

		/**
		 * Platform Audit processor configuration properties
		 * <p>
		 * Audit against Onesait Platform
		 */
		private Platform platform;

		/**
		 * Postgresql Audit processor configuration properties
		 */
		private JpaProcessor jpa = new JpaProcessor();
	}

	@Data
	public static class Event {

		/**
		 * Event separator
		 */
		private String separator = "#";
	}

	@Data
	public static class Console {

		/**
		 * Enable Console processor
		 */
		private boolean enabled = false;

	}

	@Data
	public static class Logger {

		/**
		 * Enable Logger processor
		 */
		private boolean enabled = false;

	}

	@Data
	public static class File {

		/**
		 * Enable File processor
		 */
		private boolean enabled = false;

		/**
		 * File name where to store the audit
		 */
		private String name = "output.txt";

		/**
		 * Path where to store the audit file
		 */
		private String path = "./";
	}

	@Data
	public static class Kafka {

		/**
		 * Enable Kafka processor
		 */
		private boolean enabled = false;

		/**
		 * Topic where to store the audit
		 */
		private String topic = "audit";

	}

	@Data
	public static class Platform {

		/**
		 * Enable Platform Audit processor
		 */
		private boolean enabled = false;

		/**
		 * Platform Audit URL
		 */
		private String auditUrl;

		/**
		 * Platform Login configuration
		 */
		private Login login;
	}

	@Data
	@Validated
	public static class JpaProcessor {

		/**
		 * Enable postgreSQL processor
		 */
		private boolean enabled = false;
	}

	@Data
	public static class Login {

		/**
		 * Onesait Platform Login URL
		 */
		private String url;

		/**
		 * Onesait Platform User application
		 */
		private String user;

		/**
		 * Onesait Platform User's password
		 */
		private String password;

		/**
		 * Onesait Platform
		 */
		private String grant_type;

		/**
		 * Onesait Platform
		 */
		private String scope;

		/**
		 * Onesait Platform
		 */
		private String vertical;
	}
}
