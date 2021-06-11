package tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.util.Arrays;

@Profile("!prod")
@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository, UserRepository userRepository,
                                        PasswordEncoder encoder, TacoRepository tacoRepository, PaymentMethodRepository paymentMethodRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
                Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
                Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
                Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
                Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
                Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
                Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
                Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
                Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
                Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);
                ingredientRepository.save(flourTortilla);
                ingredientRepository.save(cornTortilla);
                ingredientRepository.save(groundBeef);
                ingredientRepository.save(carnitas);
                ingredientRepository.save(tomatoes);
                ingredientRepository.save(lettuce);
                ingredientRepository.save(cheddar);
                ingredientRepository.save(jack);
                ingredientRepository.save(salsa);
                ingredientRepository.save(sourCream);

                userRepository.save(new User("patri", encoder.encode("password"),
                        "Patricia", "10 StreetName", "San Francisco", "CA", "11111", "123-123-1234", "patri@test.com"));

                userRepository.save(new User("patricia", encoder.encode("password"),
                        "Patricia", "10 StName", "San Francisco", "CA", "11111", "123-123-1234", "patri@test.com"));

                paymentMethodRepository.save(new PaymentMethod(userRepository.findByUsername("patricia"), "4111111111111111", "907", "07/21"));

                Taco taco1 = new Taco();
                taco1.setName("Carnivore");
                taco1.setIngredients(Arrays.asList(flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar));
                tacoRepository.save(taco1);

                Taco taco2 = new Taco();
                taco2.setName("Bovine Bounty");
                taco2.setIngredients(Arrays.asList(cornTortilla, groundBeef, cheddar, jack, sourCream));
                tacoRepository.save(taco2);

                Taco taco3 = new Taco();
                taco3.setName("Veg-Out");
                taco3.setIngredients(Arrays.asList(flourTortilla, cornTortilla, tomatoes, lettuce, salsa));
                tacoRepository.save(taco3);
            }
        };
    }
    // this bean populates the Ingredient table in database when the application starts - is used instead of data.sql file
    // it also adds a user to User table and 3 tacos to Taco_Order table
    // because we use Spring JPA, it will automatically create the needed tables - there is no need to have schema.sql file
}

// ■■■ Annotations:
// @Bean = is applied on a method to specify that it returns a bean to be managed by Spring context
// @Profile("!prod") = this bean will only be created when the profile 'prod' is NOT active
