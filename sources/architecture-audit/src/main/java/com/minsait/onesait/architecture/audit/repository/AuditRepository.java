package com.minsait.onesait.architecture.audit.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.minsait.onesait.architecture.audit.model.jpa.AuditModel;

public interface AuditRepository extends CrudRepository<AuditModel, UUID> {

}
