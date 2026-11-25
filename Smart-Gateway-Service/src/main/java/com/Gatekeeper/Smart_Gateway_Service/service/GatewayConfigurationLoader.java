package com.Gatekeeper.Smart_Gateway_Service.service;

import com.Gatekeeper.Smart_Gateway_Service.client.ManagementApiClient;
import com.Gatekeeper.Smart_Gateway_Service.dto.GatewayRoute;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayConfigurationLoader implements ApplicationRunner {

    private final ManagementApiClient managementApiClient;

    public GatewayConfigurationLoader(ManagementApiClient managementApiClient) {
        this.managementApiClient = managementApiClient;
    }

    @Override
    public void run(ApplicationArguments args){
        refresh();
    }

    public void refresh(){

        List<GatewayRoute> routes=managementApiClient.fetchActiveRoutes();

        for (GatewayRoute route : routes){
            System.out.println("Route : "+route.getTargetPathPrefix());
        }

    }
}
