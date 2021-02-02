package com.patricia.tacocloud;

import com.patricia.tacocloud.data.IngredientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication												// Spring Boot Application
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);	// runs the application
																	// performs the bootstrapping of the application by creating the Spring application context
	}

	@Bean
	public CommandLineRunner dataLoader(IngredientRepository ingredientRepository) {
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
		};
	}
	// this method populates the Ingredient table in database - is used instead of data.sql file
	// because we use Spring JPA, it will automatically create the needed tables - there is no need to have schema.sql file
}

// ■■■ Annotations:
// @Bean = is applied on a method to specify that it returns a bean to be managed by Spring context