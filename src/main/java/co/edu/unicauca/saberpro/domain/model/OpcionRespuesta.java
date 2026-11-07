package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Objects;

/**
 * Value Object inmutable: una opcion de respuesta (texto + indicador de clave).
 * Dos opciones con el mismo texto e igual indicador son la misma opcion.
 */
public final class OpcionRespuesta {

    private final String texto;
    private final boolean esCorrecta;

    public OpcionRespuesta(String texto, boolean esCorrecta) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El texto de la opción no puede ser nulo ni vacío.");
        }
        this.texto = texto.trim();
        this.esCorrecta = esCorrecta;
    }

    public String getTexto() {
        return texto;
    }

    /** Nombre fijado por la guia del taller (OpcionRespuesta::isEsCorrecta). */
    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpcionRespuesta)) return false;
        OpcionRespuesta otra = (OpcionRespuesta) o;
        return esCorrecta == otra.esCorrecta && texto.equals(otra.texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto, esCorrecta);
    }

    @Override
    public String toString() {
        return (esCorrecta ? "[CLAVE] " : "") + texto;
    }
}
