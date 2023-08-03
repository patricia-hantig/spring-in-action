package com.patricia.webclient;

import com.patricia.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class IngredientServiceClient {

    // ======================================== GET Examples ========================================

    /*public Mono<Ingredient> getIngredientById(String ingredientId) {
        Mono<Ingredient> ingredient = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredient-service/ingredient/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    public Flux<Ingredient> getAllIngredients() {
        Flux<Ingredient> ingredients = WebClient.create()
                .get()
                .uri("http://localhost:8080/ingredient-service/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);
        ingredients.subscribe(
                i -> {
                    System.out.println("The ingredients are: " + i);
                });
        return ingredients;
    }*/


    // using an injected WebClient bean with a base URI

    @Autowired
    WebClient webClient;

    public Mono<Ingredient> getIngredientById(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    public Flux<Ingredient> getAllIngredients() {
        Flux<Ingredient> ingredients = webClient
                .get()
                .uri("/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);
        System.out.println("The ingredients are: ");
        ingredients.subscribe(
                i -> {
                    System.out.println("Ingredient: " + i);
                });
        return ingredients;
    }


    // timing out long-running requests

    public Flux<Ingredient> getIngredientData() {
        Flux<Ingredient> ingredients = webClient
                .get()
                .uri("/ingredientData")
                .retrieve()
                .bodyToFlux(Ingredient.class);
        ingredients.timeout(Duration.ofSeconds(10))
                .subscribe(
                        i -> {
                            System.out.println("The ingredients are: " + i);
                        },
                        e -> {
                            // handle timeout error
                            System.out.println("There was a timeout error");
                        }
                );
        return ingredients;
    }


    // ======================================== POST Examples ========================================

    public Mono<Ingredient> postMonoIngredient(Ingredient ingredient) {
        Mono<Ingredient> ingredientMono = Mono.just(ingredient);
        Mono<Ingredient> result = webClient
                .post()
                .uri("/ingredients")
                .body(ingredientMono, Ingredient.class)
                .retrieve()
                .bodyToMono(Ingredient.class);
        result.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
        });
        return result;
    }

    public Mono<Ingredient> postIngredient(Ingredient ingredient) {
        Mono<Ingredient> result = webClient
                .post()
                .uri("/ingredients")
                .syncBody(ingredient)
                .retrieve()
                .bodyToMono(Ingredient.class);
        result.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return result;
    }


    // ======================================== PUT Examples ========================================

    public Mono<Void> updateAMonoIngredient(Ingredient ingredient) {
        Mono<Void> result = webClient
                .put()
                .uri("/ingredients/{id}", ingredient.getId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .syncBody(ingredient)
                .retrieve()
                .bodyToMono(Void.class);
        result.subscribe();
        return result;
    }


    // ======================================== DELETE Examples ========================================

    public Mono<Void> deleteIngredient(String ingredientId) {
        Mono<Void> result = webClient
                .delete()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Void.class);
        result.subscribe();
        return result;
    }


    // ----------------------------------------------------- handling errors -----------------------------------------------------------------------
    // in order to test the NEXT 4 methods use the method you want to check within IngredientController.java's method: ingredientDetailPage() &
    // make sure you test with a URL that uses a wrong ingredient id for eg: localhost:8085/ingredients/LETCU


    // all examples until now have assumed a happy ending -> what happens with responses with 400-level or 500-level status codes
    // we will use onStatus() - for custom error handler

    // example 1: getIngredientById() - using WebClientResponseException

    public Mono<Ingredient> getIngredientById_with_WebClientResponseException(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                },
                error -> {
                    System.out.println(error.getMessage());
                });
        return ingredient;
    }

    // using a custom error handler -> we can provide code to a Throwable of our own choosing
    // we want the Mono to complete in error with a UnknownIngredientException -> we will use onStatus() method

    // example 2 : getIngredientById() - using custom exception
    // if the status code is a 400-level status code
    public Mono<Ingredient> getIngredientById_with_custom_error_handler(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.just(new UnknownIngredientException("Unknown ingredient")))
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    // example 3 : getIngredientById() - using custom exception
    // if the status code is an HTTP 404(NOT FOUND)
    public Mono<Ingredient> getIngredientById_with_custom_error_handler2(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND,
                        clientResponse -> Mono.just(new UnknownIngredientException("Unknown ingredient")))
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    // you can have as many calls to onStatus() as you need to handle any variety of HTTP status codes that might come back in the response

    // example 4 : getIngredientById() - using custom exception
    // multiple HTTP codes
    public Mono<Ingredient> getIngredientById_with_custom_error_handler_multipleHTTPcodes(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.just(new UnknownIngredientException("Unknown ingredient")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.just(new UnknownIngredientException("Unknown ingredient")))
                .bodyToMono(Ingredient.class);
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    // ----------------------------------------------------- exchanging requests -----------------------------------------------------------------------
    // in all examples until now we used retrieve() to signify sending a request when working with WebClient
    // retrieve() = returns an object of type ResponseSpec
    //              - ResponseSpec is ok for simple cases - if you need the response's header or cookie values - ResponseSpec is NOT going to work
    // exchange() = returns a Mono<ClientResponse> - on which we can apply reactive operations to inspect and use data from the entire response: payload, headers & cookies

    // example 1: using exchange() instead of retrieve() - the next method does the same thing as getIngredientById()
    public Mono<Ingredient> getIngredientById_with_exchange_method(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(Ingredient.class));
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }

    // example 2: suppose the response from the request might include a header named X_UNAVAILABLE with value of true &
    // we suppose the header exists & we want the resulting Mono to not return anything
    public Mono<Ingredient> getIngredientById_with_exchange_method_plus_headers(String ingredientId) {
        Mono<Ingredient> ingredient = webClient
                .get()
                .uri("/ingredients/{id}", ingredientId)
                .exchange()
                .flatMap(clientResponse -> {
                    if (clientResponse.headers().header("X_UNAVAILABLE").contains("true")) {
                        return Mono.empty();
                    }
                    return Mono.just(clientResponse);
                })
                .flatMap(clientResponse -> clientResponse.bodyToMono(Ingredient.class));
        ingredient.subscribe(
                i -> {
                    System.out.println("The ingredient is: " + i);
                });
        return ingredient;
    }
}
