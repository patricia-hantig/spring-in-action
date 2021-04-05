package tacos.restclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tacos.Ingredient;
import tacos.Taco;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
public class TacoCloudClient {

    private RestTemplate restTemplate;
    private Traverson traverson;

    public TacoCloudClient(RestTemplate restTemplate, Traverson traverson) {
        this.restTemplate = restTemplate;
        this.traverson = traverson;
    }

    // ----------------------------- RestTemplate -----------------------------

    // ====================== GET Examples ======================

    // getForObject() = returns a domain object

    // 1. specify parameters as varargs argument
    public Ingredient getIngredientById(String ingredientId) {
        return restTemplate.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);
    }

    // 2. specify parameters with a map
    public Ingredient getIngredientById2(String ingredientId) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        return restTemplate.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, urlVariables);
    }

    // 3. request with URI instead of String
    public Ingredient getIngredientById3(String ingredientId) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
        return restTemplate.getForObject(url, Ingredient.class);
    }
    // this requires to construct the URI object before calling getForObject()

    // getForEntity() = returns a ResponseEntity object that wraps the domain object

    // 1. specify parameters as varargs argument
    public Ingredient getIngredientById4(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity;
        responseEntity = restTemplate.getForEntity("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);

        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info(String.format("Fetched time: " + date));

        return responseEntity.getBody();
    }

    // 2. specify parameters with a map
    public Ingredient getIngredientById5(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity;
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        responseEntity = restTemplate.getForEntity("http://localhost:8080/ingredients/{id}", Ingredient.class, urlVariables);

        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info(String.format("Fetched time: " + date));

        return responseEntity.getBody();
    }

    // 3. request with URI instead of String
    public Ingredient getIngredientById6(String ingredientId) {
        ResponseEntity<Ingredient> responseEntity;
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredientId);
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
        responseEntity = restTemplate.getForEntity(url, Ingredient.class);

        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info(String.format("Fetched time: " + date));

        return responseEntity.getBody();
    }


    // ====================== PUT Examples ======================

    // put() = accepts an Object which will be serialized & sent to the given URL

    // 1. specify parameters as varargs argument
    public void updateIngredient(Ingredient ingredient) {
        restTemplate.put("http://localhost:8080/ingredients/{id}", ingredient, ingredient.getId());
    }

    // 2. specify parameters with a map
    public void updateIngredient2(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        restTemplate.put("http://localhost:8080/ingredients/{id}", ingredient, urlVariables);
    }

    // 3. request with URI instead of String
    public void updateIngredient3(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
        restTemplate.put(url, ingredient);
    }


    // ====================== POST Examples ======================

    // postForObject() = posts data to the URL & returns the object created (the resource)

    // 1. specify parameters as varargs argument
    public Ingredient createIngredient(Ingredient ingredient) {
        return restTemplate.postForObject("http://localhost:8080/ingredients", ingredient, Ingredient.class);
    }

    // 2. specify parameters with a map
    public Ingredient createIngredient2(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        return restTemplate.postForObject("http://localhost:8080/ingredients", ingredient, Ingredient.class, urlVariables);
    }

    // 3. request with URI instead of String
    public Ingredient createIngredient3(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients").build(urlVariables);
        return restTemplate.postForObject(url, ingredient, Ingredient.class);
    }

    // postForLocation() = posts data to the URL & returns the URI of the newly created resource

    // 1. specify parameters as varargs argument
    public URI createIngredient4(Ingredient ingredient) {
        return restTemplate.postForLocation("http://localhost:8080/ingredients", ingredient);
    }

    // 2. specify parameters with a map
    public URI createIngredient5(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        return restTemplate.postForLocation("http://localhost:8080/ingredients", ingredient, urlVariables);
    }

    // 3. request with URI instead of String
    public URI createIngredient6(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients").build(urlVariables);
        return restTemplate.postForLocation(url, ingredient);
    }

    // postForEntity() = posts data to the URL & returns a ResponseEntity containing an object mapped from the response body

    // 1. specify parameters as varargs argument
    public Ingredient createIngredient7(Ingredient ingredient) {
        ResponseEntity<Ingredient> responseEntity = restTemplate.postForEntity("http://localhost:8080/ingredients", ingredient, Ingredient.class);
        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info("New resource created at " + date);
        return responseEntity.getBody();
    }

    // 2. specify parameters with a map
    public Ingredient createIngredient8(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        ResponseEntity<Ingredient> responseEntity = restTemplate.postForEntity("http://localhost:8080/ingredients",ingredient, Ingredient.class, urlVariables);
        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info("New resource created at " + date);
        return responseEntity.getBody();
    }

    // 3. request with URI instead of String
    public Ingredient createIngredient9(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients").build(urlVariables);
        ResponseEntity<Ingredient> responseEntity = restTemplate.postForEntity(url, ingredient, Ingredient.class);
        Long longDate = responseEntity.getHeaders().getDate();
        Date date = new Date(longDate);
        log.info("New resource created at " + date);
        return responseEntity.getBody();
    }


    // ====================== DELETE Examples ======================

    // delete() = performs an HTTP DELETE request on a resource at a specified URL

    // 1. specify parameters as varargs argument
    public void deleteIngredient(Ingredient ingredient) {
        restTemplate.delete("http://localhost:8080/ingredients/{id}", ingredient.getId());
    }

    // 2. specify parameters with a map
    public void deleteIngredient2(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        restTemplate.delete("http://localhost:8080/ingredients/{id}", urlVariables);
    }

    // 3. request with URI instead of String
    public void deleteIngredient3(Ingredient ingredient) {
        Map<String, String> urlVariables = new HashMap<>();
        urlVariables.put("id", ingredient.getId());
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/ingredients/{id}").build(urlVariables);
        restTemplate.delete(url);
    }

    // ====================== EXCHANGE Examples ======================

    // get all ingredients:

    public List<Ingredient> getAllIngredients() {
        return restTemplate.exchange("http://localhost:8080/ingredients", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Ingredient>>() {}).getBody();
    }

    public List<Ingredient> getAllIngredients2() {
        ResponseEntity<Ingredient[]> responseEntity = restTemplate.getForEntity("http://localhost:8080/ingredients", Ingredient[].class);
        return Arrays.asList(responseEntity.getBody());
    }


    // ----------------------------- Traverson -----------------------------

    public Iterable<Ingredient> getAllIngredientsWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType = new ParameterizedTypeReference<CollectionModel<Ingredient>>() {};

        CollectionModel<Ingredient> ingredientCollectionModel =
                traverson.follow("ingredients").toObject(ingredientType);

        Collection<Ingredient> ingredients = ingredientCollectionModel.getContent();
        return ingredients;
    }

    public Iterable<Taco> getRecentCreatedTacosWithTraverson() {
        ParameterizedTypeReference<CollectionModel<Taco>> tacoType = new ParameterizedTypeReference<CollectionModel<Taco>>() {};

        /*CollectionModel<Taco> tacoCollectionModel =
                traverson.follow("tacos").follow("recents").toObject(tacoType);*/

        CollectionModel<Taco> tacoCollectionModel =
                traverson.follow("tacos", "recents").toObject(tacoType);

        return tacoCollectionModel.getContent();
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        String ingredientURL = traverson.follow("ingredients").asLink().getHref();
        return restTemplate.postForObject(ingredientURL, ingredient, Ingredient.class);
    }

}
