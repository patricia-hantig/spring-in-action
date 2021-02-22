package com.patricia.tacocloud.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties(prefix = "taco.orders")
@Data
@Validated
public class OrderProps {

    @Min(value = 5, message = "must be between 5 and 25")
    @Max(value = 25, message = "must be between 5 and 25")
    private int pageSize = 20;
}
// this is a holder class = keeps configuration-specific details out of controllers & it also makes easy to share configuration properties among several beans
// @Component   = it's important because we need to inject it into the controller
//              - Spring component scanning will automatically discover it & create it as a bean in the Spring application context
//
// with @Validated, @Min and @Max = we validate the limit to be no less than 5 & no more than 25
