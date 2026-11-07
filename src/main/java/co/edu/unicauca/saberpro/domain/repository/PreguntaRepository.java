package co.edu.unicauca.saberpro.domain.repository;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida del dominio (interfaz pura). Su implementacion con JPA,
 * JDBC o memoria vive fuera del dominio, en la capa de infraestructura.
 * Se define un repositorio por agregado: solo existe para la raiz Pregunta.
 */
public interface PreguntaRepository {

    Pregunta guardar(Pregunta pregunta);

    Optional<Pregunta> buscarPorId(Long id);

    List<Pregunta> buscarPorEstado(EstadoPregunta estado);

    void eliminar(Long id);
}
