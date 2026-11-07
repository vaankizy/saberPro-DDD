package co.edu.unicauca.saberpro.domain.exception;

/**
 * Se lanza cuando se intenta violar una invariante o regla de negocio del
 * dominio (por ejemplo, enviar a revision una pregunta con menos de 4 opciones).
 */
public class ReglaNegocioException extends DomainException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
