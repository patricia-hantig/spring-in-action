package tacos.web.api;

import org.junit.Test;
import org.junit.runner.RunWith;

// testing with a live server

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=DesignTacoControllerReactive.class)
public class DesignTacoControllerWebTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldReturnRecentTacos() throws IOException {
        webTestClient.get()
                .uri("/v2/design/recent")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[?(@.id == 'TACO1')].name").isEqualTo("Carnivore")
                .jsonPath("$[?(@.id == 'TACO2')].name").isEqualTo("Bovine Bounty")
                .jsonPath("$[?(@.id == 'TACO3')].name").isEqualTo("Veg-Out");
    }
}
