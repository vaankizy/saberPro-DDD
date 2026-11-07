package co.edu.unicauca.saberpro.domain.exception;

/**
 * Excepcion base de la capa de dominio. Toda violacion de una regla del
 * negocio se comunica con una subclase de esta excepcion.
 */
public class DomainException extends RuntimeException {

    public DomainException(String mensaje) {
        super(mensaje);
    }
}
