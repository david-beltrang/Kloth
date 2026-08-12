# Estandarización del Sistema de Diseño Kloth

Este plan detalla la transición hacia un sistema de diseño centralizado en Kotlin para la aplicación Kloth, utilizando Jetpack Compose como motor principal de UI y dejando el XML solo para requisitos del sistema Android.

## Proposed Changes

### [Theme & Styling]

Se consolidará la paleta de colores y tipografía en los archivos Kotlin del paquete `ui.theme`.

#### [MODIFY] [Color.kt](file:///C:/Users/PIPE/Desktop/Universidad/6th/movil/Kloth/project/app/src/main/java/com/example/kloth/ui/theme/Color.kt)
- Definir todas las constantes de color de la marca (`KlothBlack`, `RedInferno`, `Sunset`, etc.) como fuente única de verdad.
- Agregar contenedores de superficie (`SurfaceContainer`) para mayor detalle en la UI.

#### [MODIFY] [Theme.kt](file:///C:/Users/PIPE/Desktop/Universidad/6th/movil/Kloth/project/app/src/main/java/com/example/kloth/ui/theme/Theme.kt)
- Mapear correctamente las constantes de `Color.kt` a `MaterialTheme.colorScheme` tanto en `LightColorScheme` como en `DarkColorScheme`.
- Asegurar que los componentes de Compose usen el esquema de colores del tema de forma automática.

#### [MODIFY] [Type.kt](file:///C:/Users/PIPE/Desktop/Universidad/6th/movil/Kloth/project/app/src/main/java/com/example/kloth/ui/theme/Type.kt)
- Estandarizar los estilos de texto (`display`, `headline`, `body`, `label`) para que coincidan con los diseños de los feed components.

#### [MODIFY] [colors.xml](file:///C:/Users/PIPE/Desktop/Universidad/6th/movil/Kloth/project/app/src/main/res/values/colors.xml)
- Reducir el archivo a los colores mínimos necesarios para el sistema (Status Bar, Iconos adaptativos, Splash Screen).
- Sincronizar los valores hexadecimales con los definidos en `Color.kt`.

---

## Verification Plan

### Manual Verification
- Verificar que la aplicación compile sin errores de recursos.
- Revisar visualmente los componentes del feed para confirmar que el tema se aplica correctamente.
- Probar el cambio entre modo claro y modo oscuro para asegurar la adaptabilidad.
