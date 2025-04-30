# Arcade (Vaadin)

---

## Enlace al repositorio

```
https://github.com/Yuste33/Arcade
```

---

## Descripción General
Java Puzzle Arcade es una aplicación monolítica desarrollada en Java utilizando el framework Vaadin, que integra tres puzzles clásicos del ámbito matemático y algorítmico:

♛ Problema de las N Reinas

♞ Recorrido del Caballo (Knight’s Tour)

🗼 Torres de Hanói

Cada juego cuenta con su propia interfaz interactiva desarrollada en Vaadin, accesible desde un menú principal unificado.

Además de la funcionalidad lúdica, la aplicación sirve como una práctica integral de buenas prácticas de desarrollo, diseño arquitectónico, y persistencia de datos.

---

## 🎯 Objetivos del Proyecto
Aplicar patrones de diseño creacionales y estructurales (GoF)

Diseñar una arquitectura modular basada en MVC

Construir una GUI moderna con Vaadin

Implementar persistencia de datos con Hibernate sobre una base de datos embebida H2

Estimular la creatividad del desarrollador mediante mejoras opcionales (por ejemplo, animaciones, validaciones interactivas, histórico de resultados)

---

## 🧩 Juegos Incluidos
N Reinas
Ubicar N reinas en un tablero N×N sin que se ataquen entre sí. Se utiliza un algoritmo de backtracking para encontrar una solución válida.

Recorrido del Caballo
Mover un caballo de ajedrez por todas las casillas del tablero sin repetir ninguna. Se usa un algoritmo recursivo para encontrar la secuencia completa de movimientos.

Torres de Hanói
Resolver el clásico problema de mover n discos de un poste a otro respetando las reglas del juego. Se implementa la solución recursiva óptima.

## 🧱 Arquitectura y Patrones de Diseño
La aplicación está estructurada en capas con separación clara entre modelo, vista, y controlador, y utiliza los siguientes patrones de diseño:


## 🔨 Patrones Creacionales
Singleton: Gestión de la SessionFactory de Hibernate para acceso a la base de datos.

Factory Method: Creación de instancias de juegos a partir del menú principal de manera polimórfica.

## 🏗️ Patrones Estructurales
Facade: Provee una interfaz simple para resolver cada puzzle, ocultando la complejidad del algoritmo.

## 💾 Persistencia de Datos
Se emplea Hibernate ORM y una base de datos H2 embebida para almacenar resultados de partidas automáticamente:

N Reinas: valor de N, éxito de la resolución, iteraciones usadas

Recorrido del Caballo: posición inicial, número de movimientos, éxito

Torres de Hanói: número de discos, movimientos realizados, éxito

Los datos se almacenan al finalizar cada juego, sin intervención manual. Opcionalmente, se puede consultar un historial de partidas desde la interfaz.

## 🖼️ Interfaz de Usuario (Vaadin)
Menú principal con acceso a los tres juegos

Formularios de configuración por juego (selección de parámetros)

Representación gráfica del tablero o estructura del puzzle

Feedback visual y textual sobre la resolución (correcto/incorrecto)

Opción de resolución automática y/o modo interactivo (si se implementa)
