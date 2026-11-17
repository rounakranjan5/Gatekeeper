package com.Gatekeeper.Api_Management_Service.service;

import com.Gatekeeper.Api_Management_Service.dto.requestDto.OrganizationRequest;
import com.Gatekeeper.Api_Management_Service.dto.requestDto.OrganizationStatusRequest;
import com.Gatekeeper.Api_Management_Service.dto.requestDto.TenantAdminRequest;
import com.Gatekeeper.Api_Management_Service.dto.responseDto.OrganizationResponse;
import com.Gatekeeper.Api_Management_Service.dto.responseDto.TenantAdminResponse;
import com.Gatekeeper.Api_Management_Service.entity.ManagementUser;
import com.Gatekeeper.Api_Management_Service.entity.Organization;
import com.Gatekeeper.Api_Management_Service.entity.OrganizationStatus;
import com.Gatekeeper.Api_Management_Service.entity.Role;
import com.Gatekeeper.Api_Management_Service.exception.ApiException;
import com.Gatekeeper.Api_Management_Service.repository.ManagementUserRepository;
import com.Gatekeeper.Api_Management_Service.repository.OrganizationRepository;
import com.Gatekeeper.Api_Management_Service.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final ManagementUserRepository managementUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public OrganizationService(OrganizationRepository organizationRepository, ManagementUserRepository managementUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.managementUserRepository = managementUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public OrganizationResponse create(OrganizationRequest request){
        long startedAt=System.nanoTime();

        checkUnique(request.getName(),request.getSlug(),null);

        Organization organization=new Organization();
        organization.setName(request.getName());
        organization.setSlug(request.getSlug());
        organization.setStatus(OrganizationStatus.PENDING);
        organization.setCreated_at(LocalDateTime.now());
        organization.setUpdated_at(LocalDateTime.now());

        organizationRepository.save(organization);

        return OrganizationResponse.from(organization);

    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> findAll(){
        List<OrganizationResponse> responses=new ArrayList<OrganizationResponse>();

        for (Organization organization : organizationRepository.findAllByStatusNot(OrganizationStatus.DELETED)){
            responses.add(OrganizationResponse.from(organization));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public OrganizationResponse findById(Long id){
        return OrganizationResponse.from(getOrganization(id));
    }

    @Transactional
    public OrganizationResponse update(Long id,OrganizationRequest request){
        long startedAt=System.nanoTime();
        Organization organization=getOrganization(id);

        checkUnique(request.getName(),request.getSlug(),id);

        organization.setName(request.getName());
        organization.setSlug(request.getSlug());
        organization.setUpdated_at(LocalDateTime.now());

        return OrganizationResponse.from(organization);

    }

    @Transactional
    public OrganizationResponse changeStatus(Long id, OrganizationStatusRequest request){
        long startedAt=System.nanoTime();

        Organization organization=getOrganization(id);

        if (request.getStatus()==OrganizationStatus.DELETED){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Use Delete to Soft Delete an organization");
        }

        organization.setStatus(request.getStatus());
        organization.setUpdated_at(LocalDateTime.now());

        return OrganizationResponse.from(organization);
    }


    @Transactional
    public void delete(Long id){
        long startedAt=System.nanoTime();

        Organization organization=getOrganization(id);
        organization.setStatus(OrganizationStatus.DELETED);
        organization.setDeleted_at(LocalDateTime.now());
        organization.setUpdated_at(LocalDateTime.now());
    }

    @Transactional
    public TenantAdminResponse createTenantAdmin(Long orgId, TenantAdminRequest request){
        long startedAt=System.nanoTime();
        Organization organization=getOrganization(orgId);

        if (managementUserRepository.findByOrganizationId(orgId).isPresent()){
            throw new ApiException(HttpStatus.CONFLICT,"This organization already has a tenant administrator");
        }

        if (managementUserRepository.existsByUsername(request.getUsername())){
            throw new ApiException(HttpStatus.CONFLICT,"Username already exists");
        }

        Role role=roleRepository.findByName("TENANT_ADMIN").orElseThrow(()->new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"TENANT_ADMIN role is missing"));

        ManagementUser user=new ManagementUser();
        user.setOrganization(organization);
        user.setRole(role);
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        managementUserRepository.save(user);

        return TenantAdminResponse.from(user);

    }

    @Transactional(readOnly = true)
    public TenantAdminResponse getTenantAdmin(Long id){
        return TenantAdminResponse.from(getTenantAdminEntity(id));
    }

    @Transactional
    public TenantAdminResponse updateTenantAdmin(Long orgId,TenantAdminRequest request){
        long startedAt=System.nanoTime();
        ManagementUser user= getTenantAdminEntity(orgId);

        if(!user.getUsername().equals(request.getUsername()) && managementUserRepository.existsByUsername(request.getUsername())){
            throw new ApiException(HttpStatus.CONFLICT,"Username already exists");
        }

        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setUpdated_at(LocalDateTime.now());

        return TenantAdminResponse.from(user);

    }

    @Transactional
    public void deleteTenantAdmin(Long orgId){
        long startedAt=System.nanoTime();

        ManagementUser user=getTenantAdminEntity(orgId);

        managementUserRepository.delete(user);

    }


    private ManagementUser getTenantAdminEntity(Long id){
        getOrganization(id);
        return managementUserRepository.findByOrganizationId(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Tenant Administrator Not Found"));
    }

    private Organization getOrganization(Long id){
        return organizationRepository.findByIdAndStatusNot(id,OrganizationStatus.DELETED).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Organization Not Found"));
    }

    private void checkUnique(String name,String slug,Long currentId){

        if(organizationRepository.existsByNameIgnoreCase(name)){

            Organization current= currentId==null ? null : organizationRepository.findById(currentId).orElse(null);

            if (current==null || !current.getName().equalsIgnoreCase(name)){
                throw new ApiException(HttpStatus.CONFLICT,"Organization name already exists");
            }

        }

        if (organizationRepository.existsBySlugIgnoreCase(slug)){
            Organization current= currentId==null ? null : organizationRepository.findById(currentId).orElse(null);

            if (current==null || !current.getSlug().equalsIgnoreCase(slug)){
                throw new ApiException(HttpStatus.CONFLICT,"Organization slug already exists");
            }
        }

    }

}
