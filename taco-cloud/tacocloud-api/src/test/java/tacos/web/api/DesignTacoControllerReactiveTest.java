package tacos.web.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.Ingredient;
import tacos.Taco;
import tacos.data.TacoRepositoryReactive;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DesignTacoControllerReactiveTest {
    // --------------------------------------- Test GET requests ---------------------------------------

    @Test
    public void shouldReturnRecentTacos() {

        // create some test data
        Taco[] tacos = {
                testTaco(1L), testTaco(2L), testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L), testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L), testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L), testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        // mock TacoRepositoryReactive
        TacoRepositoryReactive tacoRepositoryReactive = Mockito.mock(TacoRepositoryReactive.class);
        when(tacoRepositoryReactive.findAll()).thenReturn(tacoFlux);

        // create a WebTestClient
        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoControllerReactive(tacoRepositoryReactive)).build();

        // request recent tacos & verify the expected response
        testClient.get()
                .uri("/v2/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$").isNotEmpty()
                .jsonPath("$[0].id").isEqualTo(tacos[0].getId().toString())
                .jsonPath("$[0].name").isEqualTo("Taco 1")
                .jsonPath("$[1].id").isEqualTo(tacos[1].getId().toString())
                .jsonPath("$[1].name").isEqualTo("Taco 2")
                .jsonPath("$[11].id").isEqualTo(tacos[11].getId().toString())
                .jsonPath("$[11].name").isEqualTo("Taco 12")
                .jsonPath("$[12]").doesNotExist();
    }

    @Test
    public void shouldReturnRecentTacos2() {

        Taco[] tacos = {
                testTaco(1L), testTaco(2L), testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L), testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L), testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L), testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepositoryReactive tacoRepositoryReactive = Mockito.mock(TacoRepositoryReactive.class);
        when(tacoRepositoryReactive.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoControllerReactive(tacoRepositoryReactive)).build();

        testClient.get()
                .uri("/v2/design/recent")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(recentJson);
    }

    @Test
    public void shouldReturnRecentTacos3() {

        Taco[] tacos = {
                testTaco(1L), testTaco(2L), testTaco(3L), testTaco(4L),
                testTaco(5L), testTaco(6L), testTaco(7L), testTaco(8L),
                testTaco(9L), testTaco(10L), testTaco(11L), testTaco(12L),
                testTaco(13L), testTaco(14L), testTaco(15L), testTaco(16L)
        };
        Flux<Taco> tacoFlux = Flux.just(tacos);

        TacoRepositoryReactive tacoRepositoryReactive = Mockito.mock(TacoRepositoryReactive.class);
        when(tacoRepositoryReactive.findAll()).thenReturn(tacoFlux);

        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoControllerReactive(tacoRepositoryReactive)).build();

        testClient.get()
                .uri("/v2/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Taco.class)
                .contains(Arrays.copyOf(tacos, 12));
    }

    private Taco testTaco(Long number) {
        Taco taco = new Taco();
        taco.setId(number);
        taco.setName("Taco " + number);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
        ingredients.add(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
        taco.setIngredients(ingredients);
        return taco;
    }

    ClassPathResource recentResource = new ClassPathResource("/recent-tacos.json");
    String recentJson;
    {
        try {
            recentJson = StreamUtils.copyToString(recentResource.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --------------------------------------- Test POST requests ---------------------------------------
    @Test
    public void shouldSaveATaco() {

        // set up test data
        TacoRepositoryReactive tacoRepositoryReactive = Mockito.mock(TacoRepositoryReactive.class);
        Mono<Taco> unsavedTacoMono = Mono.just(testTaco(null));
        Taco savedTaco = testTaco(null);
        savedTaco.setId(1L);
        Mono<Taco> savedTacoMono = Mono.just(savedTaco);

        // mock TacoRepositoryReactive
        when(tacoRepositoryReactive.save(any())).thenReturn(savedTacoMono);

        // create WebTestClient
        WebTestClient testClient = WebTestClient.bindToController(new DesignTacoControllerReactive(tacoRepositoryReactive)).build();

        // POST a taco & verify the response
        testClient.post()
                .uri("/v2/design")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Taco.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Taco.class)
                .isEqualTo(savedTaco);
    }

}

// we use WebTestClient to test DesignTacoController
