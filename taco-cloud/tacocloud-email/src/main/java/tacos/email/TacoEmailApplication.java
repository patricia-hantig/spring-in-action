package tacos.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// this is the separate application that needs to be run
@SpringBootApplication
public class TacoEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacoEmailApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
