package com.minsait.onesait.architecture.audit.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to no audit fields and parameters
 * 
 * @author Architecture Team
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.PARAMETER, ElementType.FIELD })
public @interface AuditIgnore {

}
