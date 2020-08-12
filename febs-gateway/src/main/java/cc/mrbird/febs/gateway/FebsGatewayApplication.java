package cc.mrbird.febs.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author MrBird
 */

@EnableDiscoveryClient
@SpringBootApplication
public class FebsGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FebsGatewayApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
