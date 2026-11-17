package com.Gatekeeper.Api_Management_Service.dto.responseDto;

import com.Gatekeeper.Api_Management_Service.entity.Organization;
import com.Gatekeeper.Api_Management_Service.entity.OrganizationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrganizationResponse {

    private Long id;
    private String name;
    private String slug;
    private OrganizationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrganizationResponse from(Organization organization){
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getSlug(),
                organization.getStatus(),
                organization.getCreated_at(),
                organization.getUpdated_at()
        );
    }

}
