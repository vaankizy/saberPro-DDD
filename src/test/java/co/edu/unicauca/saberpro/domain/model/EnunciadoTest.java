package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnunciadoTest {

    @Test
    void noDebePermitirEnunciadoNulo() {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(null));
    }

    @Test
    void noDebePermitirEnunciadoVacioOEnBlanco() {
        assertThrows(ReglaNegocioException.class, () -> new Enunciado(""));
        assertThrows(ReglaNegocioException.class, () -> new Enunciado("    "));
    }

    @Test
    void debeRecortarEspaciosDeLosExtremos() {
        assertEquals("¿Cuál es la capital de Colombia?",
                new Enunciado("  ¿Cuál es la capital de Colombia?  ").getTexto());
    }

    @Test
    void dosEnunciadosConElMismoTextoSonIguales() {
        assertEquals(new Enunciado("Texto"), new Enunciado("Texto"));
        assertEquals(new Enunciado("Texto").hashCode(), new Enunciado("Texto").hashCode());
    }
}
