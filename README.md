# Pseudo-Intérprete Java

Este proyecto consiste en un intérprete desarrollado en **Java** capaz de procesar y ejecutar un lenguaje de pseudocódigo estructurado. El sistema sigue la arquitectura clásica de un compilador, transformando código fuente en acciones ejecutables mediante un análisis detallado.

## Características Principales

- **Análisis Multietapa:**
  - `Lexer`: Tokenización del código fuente.
  - `Parser`: Validación de la gramática y construcción de la estructura lógica.
  - `Interprete`: Ejecución de las instrucciones en tiempo real.
- **Estructuras de Control:** Soporte completo para bucles (`mientras`, `repite`) y condicionales (`si-entonces`).
- **Gestión de Ámbitos (Scopes):** Implementación de alcances Globales y Locales.
- **Manejo de Errores:** Excepciones personalizadas para identificar fallos.

## Arquitectura del Proyecto

El proyecto está organizado de forma modular para facilitar su escalabilidad:
- `Abstracción de Instrucciones`: Clases como `Asignacion`, `LlamadaMetodo`, `Escribir` y `Leer`.
- `Sistema de Tipos`: Manejo de tokens y tipos de datos incorporados.
- `Tabla de Símbolos`: Repositorio dinámico para el almacenamiento y recuperación de variables y métodos.

## Ejemplo de Código Soportado

```pseudo

inicio-programa
    variables: numeroDeMaterias, promedio, i, calificacion, suma

    leer numeroDeMaterias

    inicio-metodo
        nombre-metodo: sacarPromedio
        parametros: numeroDeMaterias, promedio, i, calificacion, suma

            mientras (i < numeroDeMaterias)
                leer calificacion
                suma = suma + calificacion
                i = i + 1
            fin-mientras

            promedio = suma / numeroDeMaterias

            si(promedio >= 6) entonces
               escribir "Aprobado con: ", promedio
            fin-si

            si(promedio == 10) entonces
                escribir "Excelente"
            fin-si

            si(promedio < 6) entonces
                escribir "Reprobado con: ", promedio
            fin-si
    fin-metodo

    sacarPromedio(numeroDeMaterias, promedio, i, calificacion, suma)

fin-programa
