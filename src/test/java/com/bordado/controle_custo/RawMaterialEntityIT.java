package com.bordado.controle_custo;

import com.bordado.controle_custo.entity.RawMaterial;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RawMaterialEntityIT {

    @Test
    void testEquals() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setId(1L);
        rawMaterial.setName("fulano");


        RawMaterial rawMaterial1 = new RawMaterial();
        rawMaterial1.setId(1L);
        rawMaterial1.setName("fulano");

        assertThat(rawMaterial).isEqualTo(rawMaterial1);

        rawMaterial1.setId(2L);

        assertThat(rawMaterial).isNotEqualTo(rawMaterial1);
        assertThat(rawMaterial).isNotEqualTo(null);

        assertThat(rawMaterial).isNotEqualTo(new Object());

    }

    @Test
    void testHashCode() {

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setId(1L);

        RawMaterial rawMaterial1 = new RawMaterial();
        rawMaterial1.setId(1L);

        RawMaterial rawMaterial2 = new RawMaterial();
        rawMaterial2.setId(2L);

        assertThat(rawMaterial.hashCode()).isEqualTo(rawMaterial1.hashCode());
        assertThat(rawMaterial.hashCode()).isNotEqualTo(rawMaterial2.hashCode());
    }

    @Test
    void testToString() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName("qualquer");

        assertThat(rawMaterial.toString()).isEqualTo("RawMaterial{id=null, name='qualquer', description='null', amount=0, cost=0, supplier=null}");
    }
}
