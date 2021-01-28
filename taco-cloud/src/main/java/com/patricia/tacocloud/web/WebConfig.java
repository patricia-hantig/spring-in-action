package com.patricia.tacocloud.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// When a controller doesn't populate a model or process input (ex: HomeController)
// => we can define it like this:
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}
// this is a replacement for HomeController class
// another replacement for HomeController class would be to do this changes within TacoCloudApplication class - implement WebMvcConfigurer and override the method
// - Best practice: create WebConfig configuration class in order to keep the application configuration clean & simple

