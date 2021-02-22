package com.patricia.tacocloud;

import com.patricia.tacocloud.data.IngredientRepository;
import com.patricia.tacocloud.data.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    //@Profile("dev")				// this means - this bean will only be created when the profile 'dev' is active
    //@Profile({"dev", "qa"})		// this means - this bean will only be created when the profile 'dev' or 'qa' is active
    //@Profile("!prod")				// this means - this bean will only be created when the profile 'prod' is NOT active
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository, UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
            ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
            ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
            ingredientRepository.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
            ingredientRepository.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
            ingredientRepository.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
            ingredientRepository.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
            ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
            ingredientRepository.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
            ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));

            userRepository.save(new User("patri", encoder.encode("password"),
                    "Patricia", "10 StreetName", "San Francisco", "CA", "11111", "123-123-1234"));

        };
    }
    // this bean populates the Ingredient table in database when the application starts - is used instead of data.sql file & adds a user to User table
    // because we use Spring JPA, it will automatically create the needed tables - there is no need to have schema.sql file
}

// ■■■ Annotations:
// @Bean = is applied on a method to specify that it returns a bean to be managed by Spring context
// @Profile("!prod") = this bean will only be created when the profile 'prod' is NOT active
