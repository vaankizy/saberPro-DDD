package co.edu.unicauca.saberpro.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPreguntaTest {

    @Test
    void borradorSoloPuedeIrAPendienteRevision() {
        assertTrue(EstadoPregunta.BORRADOR.puedeTransitarA(EstadoPregunta.PENDIENTE_REVISION));
        assertFalse(EstadoPregunta.BORRADOR.puedeTransitarA(EstadoPregunta.APROBADA));
    }

    @Test
    void aprobadaEsUnEstadoFinal() {
        assertTrue(EstadoPregunta.APROBADA.transicionesPermitidas().isEmpty());
    }

    @Test
    void rechazadaPuedeVolverABorrador() {
        assertTrue(EstadoPregunta.RECHAZADA.puedeTransitarA(EstadoPregunta.BORRADOR));
    }
}
