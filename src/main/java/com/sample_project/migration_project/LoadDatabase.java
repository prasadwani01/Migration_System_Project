package com.sample_project.migration_project;

import com.sample_project.migration_project.model.Migrant;
import com.sample_project.migration_project.model.MigrationStatus;
import com.sample_project.migration_project.repository.MigrantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(MigrantRepository repository) {
        return args -> {
            // Only load data if the DB is empty
            if (repository.count() == 0) {
                System.out.println("Loading VALID dummy data...");

                Migrant m1 = new Migrant();
                m1.setFullName("John Doe");
                m1.setGovernmentId("USADEPT001"); // 10 chars, Uppercase, No Hyphens
                m1.setOriginState("California");
                m1.setDestinationState("Texas");
                m1.setMigrationDate(LocalDate.now());
                m1.setStatus(MigrationStatus.PENDING);
                repository.save(m1);

                Migrant m2 = new Migrant();
                m2.setFullName("Alice Smith");
                m2.setGovernmentId("UKHOME0002"); // 10 chars, Uppercase, No Hyphens
                m2.setOriginState("London");
                m2.setDestinationState("Manchester");
                m2.setMigrationDate(LocalDate.now().minusDays(5));
                m2.setStatus(MigrationStatus.APPROVED);
                repository.save(m2);

                System.out.println("Dummy data loaded successfully!");
            }
        };
    }
}