package examples;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfig {

    @Bean
    @Profile("dev")
    public FlywayMigrationStrategy migrateStrategyForProd() {

        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
