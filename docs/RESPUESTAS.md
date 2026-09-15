# Respuestas teóricas

## 1. Cambio al backend REST

Cuando llegue el backend cambiarán la implementación de datos —por ejemplo, se añadirá `LibroRepositorioRemoto`, `LectorRepositorioRemoto`, sus DTO y el cliente HTTP— y `dataModule` enlazará las interfaces con esas nuevas clases; los modelos, las interfaces `LibroRepository` y `LectorRepository`, los casos de uso, los ViewModels y las pantallas quedarán intactos porque la regla de dependencia hace que las capas externas dependan del dominio y no al revés.

## 2. Campos numéricos como String

Si `RegistrarLibroUseCase` recibiera `anio` y `ejemplares` como `Int`, perdería la posibilidad de distinguir un campo vacío de un texto que no es un entero y ya no podría producir los mensajes exactos de cada caso; la pantalla o el ViewModel tendría que convertir y validar antes, duplicando reglas de negocio en presentación y dificultando reutilizar el mismo comportamiento desde otra interfaz.

## 3. Repositorio como factory

Si el repositorio se registrara como `factory`, cada ViewModel o caso de uso podría recibir una lista en memoria distinta: el usuario registraría un libro o lector y luego no lo vería al recargar, o vería datos diferentes al volver a una pantalla; con `single`, todas las dependencias comparten el mismo catálogo y la misma cartera durante la ejecución de la aplicación.

## Salida de pruebas

Ejecución realizada en Windows con `gradlew.bat :shared:testAndroidHostTest --console=plain`:

```text
> Task :shared:compileAndroidHostTest
> Task :shared:testAndroidHostTest

BUILD SUCCESSFUL in 37s
33 actionable tasks: 21 executed, 12 up-to-date
Resultado XML: 36 pruebas, 0 fallos, 7 clases de prueba.
```
