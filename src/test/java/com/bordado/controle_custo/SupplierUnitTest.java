package com.bordado.controle_custo;

import com.bordado.controle_custo.entity.Supplier;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class SupplierUnitTest {

    @Test
    void testEquals() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("fulano");
        supplier.setContact("contato@fulano.com");

        Supplier supplier1 = new Supplier();
        supplier1.setId(1L);
        supplier1.setName("fulano");
        supplier1.setContact("contato@fulano.com");

        assertThat(supplier).isEqualTo(supplier1);

        supplier1.setId(2L);

        assertThat(supplier).isNotEqualTo(supplier1);

    }

    @Test
    void testHashCode() {

        Supplier supplier = new Supplier();
        supplier.setId(1L);

        Supplier supplier1 = new Supplier();
        supplier1.setId(1L);

        Supplier supplier2 = new Supplier();
        supplier2.setId(2L);

        assertThat(supplier.hashCode()).isEqualTo(supplier1.hashCode());
        assertThat(supplier.hashCode()).isNotEqualTo(supplier2.hashCode());
    }
}
