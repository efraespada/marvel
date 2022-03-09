# marvel

Ejemplo de integración securizada de API de Marvel con Android Compose, MVI+ViewModel.

### Arquitectura

Se ha definido la arquitectura MVI (Model-View-Intent) con el concepto de ViewModel en la capa de presentación para el manego de eventos/estados.

Con el patrón MVI el usuario genera un `Intent` (acción) que el presentador observa para que el `Model` genere un nuevo `State` para la `View`.

#### ViewModel en la capa de presentación

El ViewModel retendrá:

- Instancia `Flow` que permitirá a la `View` observar los `Effect`.
> Efectos en pantalla como generación de Snackbars o cambios de navegación.
- Instancia `MutableState` para actualizar la información de la `View`.
> Los elementos `State` provocan el repintado de la interfaz de Compose una vez se detecta un cambio en la información contenida.

### Seguridad

Se ha securizado con [Stringcare](https://github.com/stringcare) tanto la `apiKey` como la `privateKey`:

```xml
<resources>
    <string name="apiKey" hidden="true">0dae07fa59576885c504ab94c45ddf0f</string>
    <string name="privateKey" hidden="true">b1da55b95adf3e0dc740f2494f3233088ff54d9b</string>
</resources>
```

<img width="60%" vspace="20" src="https://github.com/efraespada/marvel/raw/develop/images/secure.png">

> [Ejemplo sin securizar](https://github.com/efraespada/marvel/raw/develop/images/not_secure.png)

### Limpieza de codigo

Aunque es un ejemplo de implementación de API muy básico se ha mantenido un desglose de vista, ViewModel y capa de datos con `Hilt` y el formateo de `Ktlint`:

```bash
gradlew ktlintformat
```
```txt
// .editorconfig
[*.{kt,kts}]
disabled_rules=no-wildcard-imports,experimental:annotation
```

### Testing

- [`FlowInstrumentedTest`](https://github.com/efraespada/marvel/blob/master/app/src/androidTest/java/com/efraespada/marvel/FlowInstrumentedTest.kt): Test del flujo de la aplicación.
- [`SecurityInstrumentedTest`](https://github.com/efraespada/marvel/blob/master/app/src/androidTest/java/com/efraespada/marvel/SecurityInstrumentedTest.kt): Test de revelado de `apiKey` y `privateKey`.

### Build Variants

Se ha definido los entornos de `dev` y `prod` generando las buildVariants:

- `debugDev`
- `debugProd`
- `releaseDev`
- `releaseProd`

Los valores de `apiKey` y `privateKey` varían según el tipo de entorno.


### Librerías

- Compose
- Hilt
- Coil
- Retrofit
- Stringcare

