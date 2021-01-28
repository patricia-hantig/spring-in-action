package com.patricia.tacocloud;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Order {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Zip code is required")
    private String zip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;
}

// ■ Validating form input:
// @NotNull - property isn't empty or null
// @CreditCardNumber - property value must be a valid credit card number that passes Luhn algorithm check
//                  - located in org.hibernate.validator package
// @Pattern(regex="...') - is used to create pattern "MM/YY" for expiration date of the credit card
// @Digits(integer=3, fraction=0, message="...") - property's value is exact 3 digits otherwise message is displayed
// - except @CreditCardNumber all the other annotations are located within javax.validation.constants package
