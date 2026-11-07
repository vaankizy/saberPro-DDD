package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JustificacionTest {

    @Test
    void noDebePermitirJustificacionNula() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion(null));
    }

    @Test
    void noDebePermitirMenosDeQuinceCaracteres() {
        assertThrows(ReglaNegocioException.class, () -> new Justificacion("muy corta"));
    }

    @Test
    void debeContarLongitudSinLosEspaciosDeLosExtremos() {
        // 14 caracteres reales rodeados de espacios: sigue siendo invalida
        assertThrows(ReglaNegocioException.class, () -> new Justificacion("    12345678901234    "));
    }

    @Test
    void debeAceptarExactamenteQuinceCaracteres() {
        Justificacion j = new Justificacion("123456789012345");
        assertEquals(15, j.getTexto().length());
    }

    @Test
    void noDebePermitirPalabrasProhibidas() {
        assertThrows(ReglaNegocioException.class,
                () -> new Justificacion("La opcion B es obviamente la correcta."));
        assertThrows(ReglaNegocioException.class,
                () -> new Justificacion("Claramente esta es la respuesta esperada."));
    }

    @Test
    void laPalabraProhibidaSeDetectaSinImportarMayusculas() {
        assertThrows(ReglaNegocioException.class,
                () -> new Justificacion("La opcion C es OBVIAMENTE la clave."));
    }

    @Test
    void noDebeConfundirPalabrasProhibidasConPalabrasMasLargas() {
        assertDoesNotThrow(() -> new Justificacion("La opcion A es la clave por la ley de Ohm."));
    }

    @Test
    void esInmutableYRecortaEspacios() {
        Justificacion j = new Justificacion("   La clave aplica la ley de Ohm.   ");
        assertEquals("La clave aplica la ley de Ohm.", j.getTexto());
    }
}
