package com.minsait.onesait.architecture.audit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import com.minsait.onesait.architecture.audit.aop.AuditAspect;
import com.minsait.onesait.architecture.audit.processor.ConsoleProcessor;
import com.minsait.onesait.architecture.audit.processor.FileProcessor;
import com.minsait.onesait.architecture.audit.processor.FormatAuditEvent;
import com.minsait.onesait.architecture.audit.processor.JpaProcessor;
import com.minsait.onesait.architecture.audit.processor.KafkaProcessor;
import com.minsait.onesait.architecture.audit.processor.LoggerProcessor;
import com.minsait.onesait.architecture.audit.processor.PlatformAuditProcessor;

import brave.sampler.Sampler;

@Configuration
@Import({ AuditAspect.class, ConsoleProcessor.class, FileProcessor.class, FormatAuditEvent.class, KafkaProcessor.class,
		LoggerProcessor.class, PlatformAuditProcessor.class, AsyncConfiguration.class, AuditAsyncProperties.class,
		AuditProperties.class, JpaProcessor.class })
@EnableAspectJAutoProxy
public class AuditConfig {

	@Bean
	@ConditionalOnMissingBean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
