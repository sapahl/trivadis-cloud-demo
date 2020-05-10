package com.trivadis;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Liveness
public class FrontendHealthCheck implements HealthCheck {

    private HealthCheckResponse response = HealthCheckResponse.up("Started");

    public void setStatus(HealthCheckResponse status) {
        response = status;
    }

    @Override
    public HealthCheckResponse call() {
        return response;
    }
}
