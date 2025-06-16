package intern.nhhtuan.toeic_mentor.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Value("${spring.flyway.locations}")
    private String[] flywayLocations;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .locations(flywayLocations)
                .baselineOnMigrate(true)
                .baselineVersion("1")
                .dataSource(datasourceUrl, datasourceUsername, datasourcePassword)
                .load();
        // This will automatically run the migrations on application startup
        // and create the Flyway schema history table if it doesn't exist.
        // If the table already exists, it will not be recreated.
        // Run spl file, if VERSION is NEWEST, it will run all migrations
        flyway.migrate();
        return flyway;
    }
}
