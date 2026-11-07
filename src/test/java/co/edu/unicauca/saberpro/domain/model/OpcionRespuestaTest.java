package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpcionRespuestaTest {

    @Test
    void noDebePermitirTextoNuloOVacio() {
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta(null, true));
        assertThrows(ReglaNegocioException.class, () -> new OpcionRespuesta("   ", false));
    }

    @Test
    void debeConservarElIndicadorDeClave() {
        assertTrue(new OpcionRespuesta("A", true).isEsCorrecta());
        assertFalse(new OpcionRespuesta("B", false).isEsCorrecta());
    }

    @Test
    void sonIgualesSiTienenMismoTextoEIndicador() {
        assertEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", true));
        assertNotEquals(new OpcionRespuesta("A", true), new OpcionRespuesta("A", false));
    }
}
