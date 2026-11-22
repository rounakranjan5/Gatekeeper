package com.Gatekeeper.Api_Management_Service.controller;

import com.Gatekeeper.Api_Management_Service.dto.responseDto.RouteResponse;
import com.Gatekeeper.Api_Management_Service.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gateway/routes")
public class GatewayConfigurationController {

    private final RouteService routeService;

    public GatewayConfigurationController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    ResponseEntity<List<RouteResponse>> getActiveRoutes(){
        return ResponseEntity.ok(routeService.findAllActiveForGateway());
    }

}
