package tacos.kitchen;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile({"jms-template", "jms-listener", "rabbitmq-template"})
@Configuration
public class WebConfig implements WebMvcConfigurer {

    //@Override
    public void addViewController(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/orders/receive");
    }
}
