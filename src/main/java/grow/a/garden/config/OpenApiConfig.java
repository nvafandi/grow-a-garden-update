package grow.a.garden.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Grow A Garden Update Stock API",
                version = "v1",
                description = "Documentation for using this API"
        ),
        servers = {
                @Server(url = "/", description = "localhost")
        }
)
public class OpenApiConfig {
}
