package com.bordado.controle_custo.exceptions;

public class SupplierNotFoundException extends RuntimeException {
  public SupplierNotFoundException(Long id) {
    super("Fornecedor não encontrado com ID: " + id);
  }
}
