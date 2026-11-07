package co.edu.unicauca.saberpro.domain.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Estados del ciclo de vida de una pregunta y las transiciones permitidas
 * (Contexto de Ciclo de Vida).
 */
public enum EstadoPregunta {
    BORRADOR,
    PENDIENTE_REVISION,
    EN_REVISION,
    APROBADA,
    RECHAZADA;

    /** Estados a los que se puede pasar desde este estado. */
    public Set<EstadoPregunta> transicionesPermitidas() {
        switch (this) {
            case BORRADOR:
                return EnumSet.of(PENDIENTE_REVISION);
            case PENDIENTE_REVISION:
                return EnumSet.of(EN_REVISION);
            case EN_REVISION:
                return EnumSet.of(APROBADA, RECHAZADA);
            case RECHAZADA:
                return EnumSet.of(BORRADOR);
            case APROBADA:
            default:
                return EnumSet.noneOf(EstadoPregunta.class);
        }
    }

    public boolean puedeTransitarA(EstadoPregunta destino) {
        return transicionesPermitidas().contains(destino);
    }
}
