# Taller 11 – Domain-Driven Design (Banco de Preguntas Saber PRO)

Laboratorio de Ingeniería de Software II – Universidad del Cauca – 2026.2
Grupo 1: Joseph David Trujillo Gómez · Juan Pablo Hernández

Capa de dominio **pura** (Java 17, sin Spring ni JPA). La única dependencia del
proyecto es JUnit 5, y solo en el ámbito de pruebas.

## Estructura

```
src/main/java/co/edu/unicauca/saberpro/domain/
├── model/        Pregunta (Aggregate Root), Enunciado, Justificacion,
│                 OpcionRespuesta (Value Objects), EstadoPregunta (enum)
├── exception/    DomainException, ReglaNegocioException
└── repository/   PreguntaRepository (interfaz pura)
src/test/java/.../model/   Pruebas JUnit 5 de las invariantes
docs/                      Diagramas (.dot de Graphviz y .png)
```

## Ejecutar las pruebas (Debian / Linux)

```bash
sudo apt install -y openjdk-17-jdk maven     # solo la primera vez
mvn test
```

## Invariantes cubiertas

- Enunciado: no nulo ni vacío.
- Justificación: mínimo 15 caracteres y sin palabras prohibidas.
- Opción de respuesta: texto no vacío.
- Pregunta: máximo 4 opciones, sin textos repetidos, modificable solo en BORRADOR.
- Envío a revisión: exactamente 4 opciones, exactamente 1 clave, enunciado y justificación definidos.
- Ciclo de vida: solo transiciones permitidas entre estados.
