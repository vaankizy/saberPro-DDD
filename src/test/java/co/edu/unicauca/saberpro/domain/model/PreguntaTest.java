package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreguntaTest {

    /** Pregunta en BORRADOR con enunciado, justificacion, 4 opciones y 1 clave. */
    private Pregunta preguntaCompleta() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("¿Qué patrón separa la lógica de negocio de la interfaz?");
        p.definirJustificacion("MVC separa modelo, vista y controlador.");
        p.agregarOpcion("MVC", true);
        p.agregarOpcion("Singleton", false);
        p.agregarOpcion("Factory", false);
        p.agregarOpcion("Observer", false);
        return p;
    }

    // ---- Prueba sugerida por la guia del taller ----

    @Test
    void noDebePermitirEnviarARevisionSinCuatroOpciones() {
        Pregunta pregunta = new Pregunta();
        pregunta.agregarOpcion("Opción A", true);
        assertThrows(ReglaNegocioException.class, pregunta::enviarARevision);
    }

    // ---- Invariante: exactamente 4 opciones ----

    @Test
    void noDebePermitirMasDeCuatroOpciones() {
        Pregunta p = preguntaCompleta();
        assertThrows(ReglaNegocioException.class, () -> p.agregarOpcion("Quinta", false));
        assertEquals(4, p.getOpciones().size());
    }

    @Test
    void noDebePermitirEnviarConTresOpciones() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion("Justificación suficientemente larga.");
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void noDebePermitirOpcionesConTextoRepetido() {
        Pregunta p = new Pregunta();
        p.agregarOpcion("MVC", true);
        assertThrows(ReglaNegocioException.class, () -> p.agregarOpcion("mvc", false));
    }

    // ---- Invariante: exactamente 1 clave ----

    @Test
    void noDebePermitirEnviarSinOpcionCorrecta() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion("Justificación suficientemente larga.");
        p.agregarOpcion("A", false);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarConDosOpcionesCorrectas() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.definirJustificacion("Justificación suficientemente larga.");
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", true);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    // ---- Enunciado y justificacion obligatorios para el envio ----

    @Test
    void noDebePermitirEnviarSinEnunciado() {
        Pregunta p = new Pregunta();
        p.definirJustificacion("Justificación suficientemente larga.");
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void noDebePermitirEnviarSinJustificacion() {
        Pregunta p = new Pregunta();
        p.definirEnunciado("Enunciado de prueba");
        p.agregarOpcion("A", true);
        p.agregarOpcion("B", false);
        p.agregarOpcion("C", false);
        p.agregarOpcion("D", false);
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void noDebePermitirJustificacionCortaAlDefinirla() {
        Pregunta p = new Pregunta();
        assertThrows(ReglaNegocioException.class, () -> p.definirJustificacion("corta"));
        assertNull(p.getJustificacion());
    }

    // ---- Camino feliz y ciclo de vida ----

    @Test
    void debeEnviarARevisionCuandoCumpleTodasLasInvariantes() {
        Pregunta p = preguntaCompleta();
        assertDoesNotThrow(p::enviarARevision);
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, p.getEstado());
    }

    @Test
    void unaPreguntaNuevaNaceEnBorrador() {
        assertEquals(EstadoPregunta.BORRADOR, new Pregunta().getEstado());
    }

    @Test
    void noDebePermitirModificarOpcionesFueraDeBorrador() {
        Pregunta p = preguntaCompleta();
        p.enviarARevision();
        assertThrows(ReglaNegocioException.class, () -> p.agregarOpcion("Otra", false));
    }

    @Test
    void noDebePermitirModificarEnunciadoNiJustificacionFueraDeBorrador() {
        Pregunta p = preguntaCompleta();
        p.enviarARevision();
        assertThrows(ReglaNegocioException.class, () -> p.definirEnunciado("Nuevo enunciado"));
        assertThrows(ReglaNegocioException.class,
                () -> p.definirJustificacion("Nueva justificación larga."));
    }

    @Test
    void noDebePermitirEnviarDosVecesLaMismaPregunta() {
        Pregunta p = preguntaCompleta();
        p.enviarARevision();
        assertThrows(ReglaNegocioException.class, p::enviarARevision);
    }

    @Test
    void noDebePermitirAprobarUnBorrador() {
        assertThrows(ReglaNegocioException.class, () -> preguntaCompleta().aprobar());
    }

    @Test
    void debeRecorrerElCicloCompletoHastaAprobada() {
        Pregunta p = preguntaCompleta();
        p.enviarARevision();
        p.iniciarRevision();
        p.aprobar();
        assertEquals(EstadoPregunta.APROBADA, p.getEstado());
        assertThrows(ReglaNegocioException.class, p::rechazar);
    }

    @Test
    void unaPreguntaRechazadaPuedeCorregirseYReenviarse() {
        Pregunta p = preguntaCompleta();
        p.enviarARevision();
        p.iniciarRevision();
        p.rechazar();
        p.devolverABorrador();
        assertEquals(EstadoPregunta.BORRADOR, p.getEstado());
        assertDoesNotThrow(p::enviarARevision);
    }

    // ---- Encapsulamiento del agregado ----

    @Test
    void laListaDeOpcionesExpuestaEsDeSoloLectura() {
        Pregunta p = preguntaCompleta();
        assertThrows(UnsupportedOperationException.class,
                () -> p.getOpciones().add(new OpcionRespuesta("Intrusa", false)));
    }

    @Test
    void elIdentificadorSoloSeAsignaUnaVez() {
        Pregunta p = new Pregunta();
        p.asignarId(7L);
        assertEquals(Long.valueOf(7L), p.getId());
        assertThrows(ReglaNegocioException.class, () -> p.asignarId(8L));
    }
}
