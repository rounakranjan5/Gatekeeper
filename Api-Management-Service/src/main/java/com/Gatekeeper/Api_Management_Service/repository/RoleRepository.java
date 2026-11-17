package com.Gatekeeper.Api_Management_Service.repository;

import com.Gatekeeper.Api_Management_Service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);

}
