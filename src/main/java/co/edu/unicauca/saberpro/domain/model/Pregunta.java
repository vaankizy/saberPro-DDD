package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AGGREGATE ROOT del Banco de Preguntas Saber PRO.
 *
 * Es el unico punto de entrada para modificar el agregado (enunciado,
 * justificacion y opciones) y garantiza sus invariantes de negocio:
 * <ul>
 *   <li>Las opciones solo se modifican en estado BORRADOR.</li>
 *   <li>Nunca hay mas de 4 opciones ni opciones repetidas.</li>
 *   <li>Para enviarse a revision: enunciado y justificacion definidos,
 *       exactamente 4 opciones y exactamente 1 clave correcta.</li>
 *   <li>El estado solo cambia mediante transiciones permitidas.</li>
 * </ul>
 * Java puro: sin Spring, sin JPA, sin anotaciones de frameworks.
 */
public class Pregunta {

    public static final int NUMERO_OPCIONES = 4;

    private Long id;
    private Enunciado enunciado;
    private Justificacion justificacion;
    private final List<OpcionRespuesta> opciones = new ArrayList<>();
    private EstadoPregunta estado = EstadoPregunta.BORRADOR;

    public Pregunta() {
    }

    /** Reconstruye una pregunta ya identificada (p. ej. desde un repositorio). */
    public Pregunta(Long id) {
        this.id = id;
    }

    // ------------------------------------------------------------------
    // Comportamiento del Contexto de Autoria
    // ------------------------------------------------------------------

    public void definirEnunciado(String texto) {
        exigirBorrador("el enunciado");
        this.enunciado = new Enunciado(texto);
    }

    public void definirJustificacion(String texto) {
        exigirBorrador("la justificación");
        this.justificacion = new Justificacion(texto);
    }

    public void agregarOpcion(String texto, boolean esCorrecta) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se pueden modificar opciones en estado Borrador.");
        }
        if (this.opciones.size() >= NUMERO_OPCIONES) {
            throw new ReglaNegocioException("Una pregunta no puede tener más de 4 opciones.");
        }
        OpcionRespuesta nueva = new OpcionRespuesta(texto, esCorrecta);
        for (OpcionRespuesta existente : opciones) {
            if (existente.getTexto().equalsIgnoreCase(nueva.getTexto())) {
                throw new ReglaNegocioException("No se permiten opciones con texto repetido.");
            }
        }
        this.opciones.add(nueva);
    }

    // ------------------------------------------------------------------
    // Comportamiento del Contexto de Ciclo de Vida
    // ------------------------------------------------------------------

    public void enviarARevision() {
        validarInvariantesParaEnvio();
        transitarA(EstadoPregunta.PENDIENTE_REVISION);
    }

    public void iniciarRevision() {
        transitarA(EstadoPregunta.EN_REVISION);
    }

    public void aprobar() {
        transitarA(EstadoPregunta.APROBADA);
    }

    public void rechazar() {
        transitarA(EstadoPregunta.RECHAZADA);
    }

    /** Reabre una pregunta rechazada para corregirla. */
    public void devolverABorrador() {
        transitarA(EstadoPregunta.BORRADOR);
    }

    // ------------------------------------------------------------------
    // Invariantes
    // ------------------------------------------------------------------

    private void validarInvariantesParaEnvio() {
        if (this.opciones.size() != NUMERO_OPCIONES) {
            throw new ReglaNegocioException("Debe tener exactamente 4 opciones de respuesta.");
        }
        long correctas = opciones.stream().filter(OpcionRespuesta::isEsCorrecta).count();
        if (correctas != 1) {
            throw new ReglaNegocioException("Debe existir exactamente 1 opción correcta.");
        }
        if (this.enunciado == null) {
            throw new ReglaNegocioException("Debe tener un enunciado definido.");
        }
        if (this.justificacion == null) {
            throw new ReglaNegocioException("Debe tener una justificación definida.");
        }
    }

    private void exigirBorrador(String que) {
        if (this.estado != EstadoPregunta.BORRADOR) {
            throw new ReglaNegocioException("Solo se puede modificar " + que + " en estado Borrador.");
        }
    }

    private void transitarA(EstadoPregunta destino) {
        if (!estado.puedeTransitarA(destino)) {
            throw new ReglaNegocioException(
                    "Transición no permitida: " + estado + " -> " + destino + ".");
        }
        this.estado = destino;
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    /** La identidad la asigna la infraestructura una sola vez al persistir. */
    public void asignarId(Long id) {
        if (this.id != null) {
            throw new ReglaNegocioException("La pregunta ya tiene un identificador asignado.");
        }
        if (id == null) {
            throw new ReglaNegocioException("El identificador no puede ser nulo.");
        }
        this.id = id;
    }

    public Enunciado getEnunciado() {
        return enunciado;
    }

    public Justificacion getJustificacion() {
        return justificacion;
    }

    /** Vista de solo lectura: el agregado no expone su lista interna. */
    public List<OpcionRespuesta> getOpciones() {
        return Collections.unmodifiableList(opciones);
    }

    public EstadoPregunta getEstado() {
        return estado;
    }
}
