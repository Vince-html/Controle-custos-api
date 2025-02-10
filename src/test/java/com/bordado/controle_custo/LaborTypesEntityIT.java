package com.bordado.controle_custo;

import com.bordado.controle_custo.entity.LaborTypes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LaborTypesEntityIT {

    @Test
    void testEquals() {
        LaborTypes laborTypes = new LaborTypes();
        laborTypes.setId(1L);
        laborTypes.setTypeLabor("fulano");


        LaborTypes laborTypes1 = new LaborTypes();
        laborTypes1.setId(1L);
        laborTypes1.setTypeLabor("fulano");

        assertThat(laborTypes).isEqualTo(laborTypes1);

        laborTypes1.setId(2L);

        assertThat(laborTypes).isNotEqualTo(laborTypes1);

        assertThat(laborTypes).isNotEqualTo(null);
        assertThat(laborTypes).isNotEqualTo(new Object());
    }

    @Test
    void testHashCode() {

        LaborTypes laborTypes = new LaborTypes();
        laborTypes.setId(1L);

        LaborTypes laborTypes1 = new LaborTypes();
        laborTypes1.setId(1L);

        LaborTypes laborTypes2 = new LaborTypes();
        laborTypes2.setId(2L);

        assertThat(laborTypes.hashCode()).isEqualTo(laborTypes1.hashCode());
        assertThat(laborTypes.hashCode()).isNotEqualTo(laborTypes2.hashCode());
    }

}
