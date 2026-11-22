package com.Gatekeeper.Api_Management_Service.dto.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteStatusRequest {
    @NotNull
    private Boolean active;
}
