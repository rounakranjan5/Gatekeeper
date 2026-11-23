package com.Gatekeeper.Api_Management_Service.service;

import com.Gatekeeper.Api_Management_Service.entity.ControlPlaneAuditRecord;
import com.Gatekeeper.Api_Management_Service.entity.Organization;
import com.Gatekeeper.Api_Management_Service.repository.ControlPlaneAuditRecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final ControlPlaneAuditRecordRepository controlPlaneAuditRecordRepository;

    public AuditService(ControlPlaneAuditRecordRepository controlPlaneAuditRecordRepository) {
        this.controlPlaneAuditRecordRepository = controlPlaneAuditRecordRepository;
    }

    public void record(Organization organization,String action,String resourceType,String resourceId,long startedAt,String details){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        String actor= authentication == null ? "system" : authentication.getName();

        ControlPlaneAuditRecord record=new ControlPlaneAuditRecord();
        record.setOrganization(organization);
        record.setActor(actor);
             record.setAction(action);
        record.setResourceType(resourceType);
        record.setResourceId(resourceId);
        record.setCorrelationId(getCorrelationId());
        record.setDurationMs((System.nanoTime() - startedAt) / 1000000);
        record.setResult("SUCCESS");
        record.setDetails(details);
        record.setCreatedAt(LocalDateTime.now());

        controlPlaneAuditRecordRepository.save(record);
    }

    private String getCorrelationId() {
        if(!(RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes)) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request.getHeader("X-Correlation-ID");
    }
}
