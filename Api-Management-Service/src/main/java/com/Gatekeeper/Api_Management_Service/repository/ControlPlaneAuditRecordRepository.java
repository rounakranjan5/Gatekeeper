package com.Gatekeeper.Api_Management_Service.repository;

import com.Gatekeeper.Api_Management_Service.entity.ControlPlaneAuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlPlaneAuditRecordRepository extends JpaRepository<ControlPlaneAuditRecord,Long> {

    List<ControlPlaneAuditRecord> findAllByOrganizationIdOrderByCreatedAtDesc(Long organizationId);

}
