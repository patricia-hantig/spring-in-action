package com.patricia.webclient;

import lombok.Getter;

@Getter
public class UnknownIngredientException extends Exception {

    public UnknownIngredientException(String message) {
        super(message);
    }

}
