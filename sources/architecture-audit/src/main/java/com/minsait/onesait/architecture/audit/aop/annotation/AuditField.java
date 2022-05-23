package com.minsait.onesait.architecture.audit.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to audit fields and parameters
 * 
 * @author Architecture Team
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.PARAMETER })
public @interface AuditField {

	public int hideFirst() default 0;

	public int hideLast() default 0;

}
