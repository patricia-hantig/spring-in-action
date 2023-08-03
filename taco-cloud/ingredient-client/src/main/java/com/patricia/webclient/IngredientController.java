package com.patricia.webclient;

import com.patricia.Ingredient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@Controller
@RequestMapping("/ingredients")
@Slf4j
public class IngredientController {

    private IngredientServiceClient client;

    public IngredientController(IngredientServiceClient client) {
        this.client = client;
    }

    @GetMapping
    public String ingredientsList(Model model) {
        log.info("Fetched all ingredients from a WebClient-based service.");
        model.addAttribute("ingredients", client.getAllIngredients());
        return "ingredientList";
    }

    @GetMapping("/{id}")
    public String ingredientDetailPage(@PathVariable("id") String id, Model model) {
        log.info("Fetched an ingredient from a WebClient-based service.");
        model.addAttribute("ingredient", client.getIngredientById(id));
        return "ingredientDetail";
    }

    @PostMapping
    public String postIngredient(@RequestBody Ingredient ingredient, Model model) {
        log.info("Added an ingredient with a WebClient-based service.");
        model.addAttribute("ingredient", client.postIngredient(ingredient));
        return "ingredientDetail";
    }

    @PutMapping("/{id}")
    public String updateIngredient(@PathVariable("id") String id, @RequestBody Ingredient ingredient, Model model) {
        log.info("Updated an ingredient from a WebClient-based service.");
        if (!ingredient.getId().equals(id)) {
            throw new IllegalStateException("Given ingredient's ID doesn't match the ID in the path.");
        }
        client.updateAMonoIngredient(ingredient);
        Mono<Ingredient> ingredientMono = Mono.just(ingredient);
        model.addAttribute("ingredient", ingredientMono);
        return "ingredientDetail";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIngredient(@PathVariable("id") String id,  Model model) {
        log.info("Deleted an ingredient from a WebClient-based service.");
        client.deleteIngredient(id);
    }
}
