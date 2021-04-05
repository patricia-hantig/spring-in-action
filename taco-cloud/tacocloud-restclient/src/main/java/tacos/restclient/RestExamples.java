package tacos.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tacos.Ingredient;
import tacos.Taco;

import java.net.URI;
import java.util.List;

@SpringBootConfiguration
@ComponentScan
@Slf4j
public class RestExamples {

    public static void main(String[] args) {
        SpringApplication.run(RestExamples.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // ----------------------------- RestTemplate -----------------------------

    @Bean
    public CommandLineRunner fetchIngredients(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- GET -------------------------");
            log.info(" - getForObject() ");
            log.info("Getting ingredient by id - specifying parameters as varargs argument");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById("CHED"));
            log.info("Getting ingredient by id - specifying parameters with a map");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById2("LETC"));
            log.info("Getting ingredient by id - request with URI instead of String");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById3("COTO"));

            log.info(" - getForEntity() ");
            log.info("Getting ingredient by id - specifying parameters as varargs argument");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById4("SLSA"));
            log.info("Getting ingredient by id - specifying parameters with a map");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById5("SRCR"));
            log.info("Getting ingredient by id - request with URI instead of String");
            log.info("Ingredient: " + tacoCloudClient.getIngredientById6("CARN"));
        };
    }

    @Bean
    public CommandLineRunner updateIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- PUT -------------------------");
            log.info(" - put() ");
            log.info("Updating ingredient - specifying parameters as varargs argument");
            Ingredient before = tacoCloudClient.getIngredientById("LETC");
            log.info("BEFORE: " + before);
            tacoCloudClient.updateIngredient(new Ingredient("LETC", "Shredded Lettuce", Ingredient.Type.VEGGIES));
            Ingredient after = tacoCloudClient.getIngredientById("LETC");
            log.info("AFTER: " + after);

            log.info("Updating ingredient - specifying parameters with a map");
            Ingredient before2 = tacoCloudClient.getIngredientById("CARN");
            log.info("BEFORE: " + before2);
            tacoCloudClient.updateIngredient2(new Ingredient("CARN", "Siminitas Carnitas", Ingredient.Type.PROTEIN));
            Ingredient after2 = tacoCloudClient.getIngredientById("CARN");
            log.info("AFTER: " + after2);

            log.info("Updating ingredient - request with URI instead of String");
            Ingredient before3 = tacoCloudClient.getIngredientById("TMTO");
            log.info("BEFORE: " + before3);
            tacoCloudClient.updateIngredient3(new Ingredient("TMTO", "Diced Roma Tomatoes", Ingredient.Type.VEGGIES));
            Ingredient after3 = tacoCloudClient.getIngredientById("TMTO");
            log.info("AFTER: " + after3);
        };
    }

    @Bean
    public CommandLineRunner addIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- POST -------------------------");
            log.info(" - postForObject() ");
            log.info("Adding a new ingredient - specifying parameters as varargs argument");
            Ingredient chix = new Ingredient("CHIX", "Shredded Chicken", Ingredient.Type.PROTEIN);
            Ingredient chixAfter = tacoCloudClient.createIngredient(chix);
            log.info("AFTER-1: " + chixAfter);

            log.info("Adding a new ingredient - specifying parameters with a map");
            Ingredient beefFajita = new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN);
            Ingredient beefFajitaAfter = tacoCloudClient.createIngredient2(beefFajita);
            log.info("AFTER-2: " + beefFajitaAfter);

            log.info("Adding a new ingredient - request with URI instead of String");
            Ingredient shrimp = new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN);
            Ingredient shrimpAfter = tacoCloudClient.createIngredient3(shrimp);
            log.info("AFTER-3: " + shrimpAfter);

            log.info(" - postForLocation() ");
            log.info("Adding a new ingredient - specifying parameters as varargs argument");
            Ingredient corn = new Ingredient("CORN", "Corn", Ingredient.Type.VEGGIES);
            URI cornAfter = tacoCloudClient.createIngredient4(corn);
            log.info("AFTER-1: " + cornAfter);

            log.info("Adding a new ingredient - specifying parameters with a map");
            Ingredient blackBean = new Ingredient("BLBN", "Black Beans", Ingredient.Type.VEGGIES);
            URI blackBeanAfter = tacoCloudClient.createIngredient5(blackBean);
            log.info("AFTER-2: " + blackBeanAfter);

            log.info("Adding a new ingredient - request with URI instead of String");
            Ingredient pintoBean = new Ingredient("PIBN", "Pinto Bean", Ingredient.Type.VEGGIES);
            URI pintoBeanAfter = tacoCloudClient.createIngredient6(pintoBean);
            log.info("AFTER-3: " + pintoBeanAfter);

            log.info(" - postForEntity() ");
            log.info("Adding a new ingredient - specifying parameters as varargs argument");
            Ingredient feta = new Ingredient("FETA", "Feta Cheese", Ingredient.Type.CHEESE);
            Ingredient fetaAfter = tacoCloudClient.createIngredient7(feta);
            log.info("AFTER-1: " + fetaAfter);

            log.info("Adding a new ingredient - specifying parameters with a map");
            Ingredient mozzarella = new Ingredient("MOZZ", "Mozzarella Cheese", Ingredient.Type.CHEESE);
            Ingredient mozzarellaAfter = tacoCloudClient.createIngredient8(mozzarella);
            log.info("AFTER-2: " + mozzarellaAfter);

            log.info("Adding a new ingredient - request with URI instead of String");
            Ingredient gorgonzola = new Ingredient("GORG", "Gorgonzola Cheese", Ingredient.Type.CHEESE);
            Ingredient gorgonzolaAfter = tacoCloudClient.createIngredient9(gorgonzola);
            log.info("AFTER-3: " + gorgonzolaAfter);
        };
    }

    @Bean
    public CommandLineRunner deleteIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            log.info("----------------------- DELETE -------------------------");
            log.info(" - delete() ");
            // start by adding a few ingredients so that we can delete them later...
            Ingredient cucumber = new Ingredient("CUCM", "Cucumber", Ingredient.Type.VEGGIES);
            tacoCloudClient.createIngredient(cucumber);
            Ingredient eggplant = new Ingredient("EGPT", "Eggplant", Ingredient.Type.VEGGIES);
            tacoCloudClient.createIngredient(eggplant);
            Ingredient avocado  = new Ingredient("AVOC", "Avocado", Ingredient.Type.VEGGIES);
            tacoCloudClient.createIngredient(avocado);

            log.info("Removing an ingredient - specifying parameters as varargs argument");
            Ingredient beforeCucumber = tacoCloudClient.getIngredientById("CUCM");
            log.info("BEFORE: " + beforeCucumber);
            tacoCloudClient.deleteIngredient(beforeCucumber);
            Ingredient afterCucumber = tacoCloudClient.getIngredientById("CUCM");
            log.info("AFTER: " + afterCucumber);

            log.info("Removing an ingredient - specifying parameters with a map");
            Ingredient beforeEggplant = tacoCloudClient.getIngredientById("EGPT");
            log.info("BEFORE: " + beforeEggplant);
            tacoCloudClient.deleteIngredient2(beforeEggplant);
            Ingredient afterEggplant = tacoCloudClient.getIngredientById("EGPT");
            log.info("AFTER: " + afterEggplant);

            log.info("Removing an ingredient - request with URI instead of String");
            Ingredient beforeAvocado = tacoCloudClient.getIngredientById("AVOC");
            log.info("BEFORE: " + beforeAvocado);
            tacoCloudClient.deleteIngredient3(beforeAvocado);
            Ingredient afterAvocado = tacoCloudClient.getIngredientById("AVOC");
            log.info("AFTER: " + afterAvocado);
        };
    }

    // ----------------------------- Traverson -----------------------------

    // 1. declare a Traverson object as a bean & inject it when we will need it
    @Bean
    public Traverson traverson() {
        Traverson traverson = new Traverson(URI.create("http://localhost:8080/api"), MediaTypes.HAL_JSON);
        return traverson;
    }

    @Bean
    public CommandLineRunner traversonGetAllIngredients(TacoCloudClient tacoCloudClient) {
        return args -> {
          Iterable<Ingredient> ingredients = tacoCloudClient.getAllIngredientsWithTraverson();
            log.info("----------------------- GET INGREDIENTS WITH TRAVERSON -------------------------");
            for (Ingredient ingredient : ingredients) {
                log.info("   -  " + ingredient);
            }
        };
    }

    @Bean
    public CommandLineRunner traversonGetRecentTacos(TacoCloudClient tacoCloudClient) {
        return args -> {
            Iterable<Taco> tacos = tacoCloudClient.getRecentCreatedTacosWithTraverson();
            log.info("----------------------- GET RECENT TACOS WITH TRAVERSON -------------------------");
            for (Taco taco : tacos) {
                log.info("   -  " + taco);
            }
        };
    }

    @Bean
    public CommandLineRunner traversonSaveIngredient(TacoCloudClient tacoCloudClient) {
        return args -> {
            Ingredient guacamole = new Ingredient("GUAC", "Guacamole", Ingredient.Type.VEGGIES);
            tacoCloudClient.addIngredient(guacamole);
            List<Ingredient> allIngredients = tacoCloudClient.getAllIngredients();
            log.info("----------------------- ALL INGREDIENTS AFTER SAVING GUACAMOLE -------------------------");
            for (Ingredient ingredient : allIngredients) {
                log.info("   -  " + ingredient);
            }
            tacoCloudClient.deleteIngredient(guacamole);
            allIngredients = tacoCloudClient.getAllIngredients();
            log.info("----------------------- ALL INGREDIENTS AFTER REMOVING GUACAMOLE -------------------------");
            for (Ingredient ingredient : allIngredients) {
                log.info("   -  " + ingredient);
            }
        };
    }
}
