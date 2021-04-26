package com.dxesoft.hestiatest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.dxesoft.hestiatest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FracionamientoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Fracionamiento.class);
        Fracionamiento fracionamiento1 = new Fracionamiento();
        fracionamiento1.setId(1L);
        Fracionamiento fracionamiento2 = new Fracionamiento();
        fracionamiento2.setId(fracionamiento1.getId());
        assertThat(fracionamiento1).isEqualTo(fracionamiento2);
        fracionamiento2.setId(2L);
        assertThat(fracionamiento1).isNotEqualTo(fracionamiento2);
        fracionamiento1.setId(null);
        assertThat(fracionamiento1).isNotEqualTo(fracionamiento2);
    }
}
