package com.Gatekeeper.Api_Management_Service.repository;

import com.Gatekeeper.Api_Management_Service.entity.Organization;
import com.Gatekeeper.Api_Management_Service.entity.OrganizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsBySlugIgnoreCase(String slug);

    List<Organization> findAllByStatusNot(OrganizationStatus status);

    Optional<Organization> findByIdAndStatusNot(Long id,OrganizationStatus status);

}
