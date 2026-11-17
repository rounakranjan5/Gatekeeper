package com.Gatekeeper.Api_Management_Service.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType;
    private Long expireInSeconds;
    private String role;
    private Long organizationId;

}
