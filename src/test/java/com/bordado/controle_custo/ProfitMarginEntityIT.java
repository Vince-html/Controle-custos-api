package com.bordado.controle_custo;

import com.bordado.controle_custo.entity.ProfitMargin;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfitMarginEntityIT {

    @Test
    void testEquals() {
        ProfitMargin profitMargin = new ProfitMargin();
        profitMargin.setId(1L);
        profitMargin.setDescription("fulano");


        ProfitMargin profitMargin1 = new ProfitMargin();
        profitMargin1.setId(1L);
        profitMargin1.setDescription("fulano");

        assertThat(profitMargin).isEqualTo(profitMargin1);

        profitMargin1.setId(2L);

        assertThat(profitMargin).isNotEqualTo(profitMargin1);

        assertThat(profitMargin).isNotEqualTo(null);
        assertThat(profitMargin).isNotEqualTo(new Object());
    }

    @Test
    void testHashCode() {

        ProfitMargin profitMargin = new ProfitMargin();
        profitMargin.setId(1L);

        ProfitMargin profitMargin1 = new ProfitMargin();
        profitMargin1.setId(1L);

        ProfitMargin profitMargin2 = new ProfitMargin();
        profitMargin2.setId(2L);

        assertThat(profitMargin.hashCode()).isEqualTo(profitMargin1.hashCode());
        assertThat(profitMargin.hashCode()).isNotEqualTo(profitMargin2.hashCode());
    }

}
