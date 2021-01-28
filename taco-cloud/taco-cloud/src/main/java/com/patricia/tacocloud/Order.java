package com.patricia.tacocloud;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Order {

    @NotBlank(message = "Delivery Name is required")
    private String deliveryName;

    @NotBlank(message = "Delivery Street is required")
    private String deliveryStreet;

    @NotBlank(message = "Delivery City is required")
    private String deliveryCity;

    @NotBlank(message = "Delivery State is required")
    private String deliveryState;

    @NotBlank(message = "Delivery Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;
    // ex of a valid card number: 4111111111111111

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message="Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

    private Long id;            // field that uniquely identifies the object

    private Date placedAt;     // filed when an Order is placed

    private List<Taco> tacos = new ArrayList<>();

    public void addDesign(Taco design) {
        this.tacos.add(design);
    }
}

// ■ Validating form input:
// @NotNull - property isn't empty or null
// @CreditCardNumber - property value must be a valid credit card number that passes Luhn algorithm check
//                  - located in org.hibernate.validator package
// @Pattern(regex="...') - is used to create pattern "MM/YY" for expiration date of the credit card
// @Digits(integer=3, fraction=0, message="...") - property's value is exact 3 digits otherwise message is displayed
// - except @CreditCardNumber all the other annotations are located within javax.validation.constants package
