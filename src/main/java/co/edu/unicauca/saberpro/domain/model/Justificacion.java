package co.edu.unicauca.saberpro.domain.model;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Value Object inmutable: justificacion pedagogica de la clave.
 * Invariantes: minimo 15 caracteres (sin contar espacios de los extremos) y
 * ninguna de las palabras prohibidas por la guia de redaccion de reactivos.
 */
public final class Justificacion {

    public static final int LONGITUD_MINIMA = 15;

    /** Palabras que no se admiten en una justificacion (juicios sin argumento). */
    static final Set<String> PALABRAS_PROHIBIDAS =
            Set.of("obviamente", "evidentemente", "claramente", "trivial", "absurdo");

    private final String texto;

    public Justificacion(String texto) {
        if (texto == null || texto.trim().length() < LONGITUD_MINIMA) {
            throw new ReglaNegocioException(
                    "La justificación debe contener al menos " + LONGITUD_MINIMA + " caracteres.");
        }
        String limpio = texto.trim();
        String prohibida = buscarPalabraProhibida(limpio);
        if (prohibida != null) {
            throw new ReglaNegocioException(
                    "La justificación contiene una palabra prohibida: '" + prohibida + "'.");
        }
        this.texto = limpio;
    }

    private static String buscarPalabraProhibida(String texto) {
        String minusculas = texto.toLowerCase(Locale.ROOT);
        for (String palabra : PALABRAS_PROHIBIDAS) {
            Pattern p = Pattern.compile("(?<![\\p{L}])" + palabra + "(?![\\p{L}])");
            if (p.matcher(minusculas).find()) {
                return palabra;
            }
        }
        return null;
    }

    public String getTexto() {
        return texto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Justificacion)) return false;
        return texto.equals(((Justificacion) o).texto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto);
    }

    @Override
    public String toString() {
        return texto;
    }
}
