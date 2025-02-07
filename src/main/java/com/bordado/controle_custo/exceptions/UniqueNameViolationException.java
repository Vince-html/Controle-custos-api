package com.bordado.controle_custo.exceptions;

public class UniqueNameViolationException extends RuntimeException {
    public UniqueNameViolationException(String message) {
        super(message);
    }
}
