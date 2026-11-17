package com.Gatekeeper.Api_Management_Service.controller;

import com.Gatekeeper.Api_Management_Service.dto.requestDto.OrganizationRequest;
import com.Gatekeeper.Api_Management_Service.dto.requestDto.OrganizationStatusRequest;
import com.Gatekeeper.Api_Management_Service.dto.requestDto.TenantAdminRequest;
import com.Gatekeeper.Api_Management_Service.dto.responseDto.OrganizationResponse;
import com.Gatekeeper.Api_Management_Service.dto.responseDto.TenantAdminResponse;
import com.Gatekeeper.Api_Management_Service.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platform/organizations")
public class PlatformOrganizationController {

    private final OrganizationService organizationService;

    public PlatformOrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // organization endpoints

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.create(request));
    }

    @GetMapping
    public  ResponseEntity<List<OrganizationResponse>> findAll(){
        return ResponseEntity.ok(organizationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(organizationService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable Long id,@Valid @RequestBody OrganizationRequest request){
        return ResponseEntity.ok(organizationService.update(id,request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrganizationResponse> changeStatus(@PathVariable Long id,@Valid @RequestBody OrganizationStatusRequest request){
        return ResponseEntity.ok(organizationService.changeStatus(id,request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Tenant Admin endpoints

    @PostMapping("/{orgId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> createTenantAdmin(@PathVariable Long orgId, @Valid @RequestBody TenantAdminRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.createTenantAdmin(orgId,request));
    }

    @GetMapping("/{orgId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> getTenantAdmin(@PathVariable Long orgId){
        return ResponseEntity.ok(organizationService.getTenantAdmin(orgId));
    }


    @PutMapping("/{orgId}/tenant-admin")
    public ResponseEntity<TenantAdminResponse> updateTenantAdmin(@PathVariable Long orgId,@Valid @RequestBody TenantAdminRequest request){
        return ResponseEntity.ok(organizationService.updateTenantAdmin(orgId,request));
    }

    @DeleteMapping("/{orgId}/tenant-admin")
    public ResponseEntity<Void> deleteTenantAdmin(@PathVariable Long orgId){
        organizationService.deleteTenantAdmin(orgId);
        return ResponseEntity.noContent().build();
    }

}
