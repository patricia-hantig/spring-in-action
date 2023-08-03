package com.patricia.define_functional_request_handlers;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.PaymentMethod;
import tacos.Taco;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.TacoRepository;
import tacos.data.UserRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;

@Configuration
@EnableJpaRepositories(basePackages = "tacos.data")
@EntityScan(basePackages = "tacos")
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<?> helloRouterFunction() {
        return route(GET("/hello"), request -> ok().body(just("Hello World!"), String.class))
                .andRoute(GET("/bye"), request -> ok().body(just("See ya!"), String.class))
                .andRoute(GET("/"), request -> ok().body(just("Welcome to Reactive APIs Examples!"), String.class));
    }

    @Autowired
    private TacoRepository tacoRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Bean
    public RouterFunction<?> routerFunction() {
        return route(GET("/v2/design/recent"), this::recents)
                /*.andRoute(POST("/v2/design"), this::postTaco)*/;
    }

    // this method works WITHOUT modifying TacoRepository class to work with ReactiveCrudRepository
    private Mono<ServerResponse> recents(ServerRequest serverRequest) {
       return ServerResponse.ok().body(Flux.fromIterable(tacoRepository.findAll()).take(12), Taco.class);
    }

    // this method works AFTER modifying TacoRepository class
    // after changing TacoRepository class to work with ReactiveCrudRepository - test this method & comment the above one:
    /*public Mono<ServerResponse> recents(ServerRequest request) {
        return ServerResponse.ok()
                .body(tacoRepository.findAll().take(12), Taco.class);
    }*/

    // this method works AFTER modifying TacoRepository class
    // after changing TacoRepository class to work with ReactiveCrudRepository - test this method:
    /*public Mono<ServerResponse> postTaco(ServerRequest serverRequest) {
        Mono<Taco> taco = serverRequest.bodyToMono(Taco.class);
        Mono<Taco> savedTaco = tacoRepository.save(taco);
        return ServerResponse
                .created(URI.create(
                        "http://localhost:8080/design/taco/" +
                                savedTaco.getId()))
                .body(savedTaco, Taco.class);
    }*/
    //http://localhost:8080/design/

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
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

}
