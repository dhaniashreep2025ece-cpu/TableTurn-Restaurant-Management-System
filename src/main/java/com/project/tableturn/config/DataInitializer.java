package com.project.tableturn.config;

import com.project.tableturn.model.RestaurantTable;
import com.project.tableturn.repository.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(TableRepository tableRepo) {
        return args -> {
            if (tableRepo.count() == 0) {
                tableRepo.save(new RestaurantTable(1, 2));
                tableRepo.save(new RestaurantTable(2, 4));
                tableRepo.save(new RestaurantTable(3, 4));
                tableRepo.save(new RestaurantTable(4, 6));
                tableRepo.save(new RestaurantTable(5, 8));
                System.out.println(">>> 5 Initial Restaurant Tables successfully seeded into tableturn_db.");
            }
        };
    }
}