# Plataforma Móvil de Contenidos Técnicos y Relacionamiento — Master Martini Chile SPA

Aplicación móvil nativa para Android desarrollada en **Kotlin** con **Jetpack Compose**, **Material 3** y arquitectura **MVVM**, diseñada para resolver el desafío de difusión de contenidos técnicos (recetas, cursos de especialidad y videos técnicos) y relacionamiento comercial con clientes y prospectos del rubro gastronómico (panaderías, pastelerías, chocolaterías, heladerías, casinos y restaurantes).

## Integrante
* **Estudiante:** Alonso Raúl Mateo Randolph Marín
* **Asignatura:** DSY1105 — Desarrollo de Aplicaciones Móviles (Duoc UC)
* **Evaluación:** Evaluación Parcial 2 (Encargo)

## Características de Usabilidad y Accesibilidad (> 50 años)
* **Jerarquía Visual y Alto Contraste (Material 3):** Paleta corporativa Master Martini con textos de alta legibilidad (`18.sp` a `30.sp`), botones principales de `60.dp` de altura y tarjetas agrupadas.
* **Navegación Funcional en Máximo 3 Clics:** Barra inferior (`NavigationBar`) con íconos y etiquetas autoexplicativas siempre visibles, menú lateral (`ModalNavigationDrawer`) y barra superior (`TopAppBar`) con botón de retroceso explícito.
* **Diseño Adaptativo (`WindowSizeClass`):** Vistas optimizadas automáticamente para dispositivos `Compact` (teléfonos), `Medium` (plegables/tablets medianas) y `Expanded` (tablets grandes/horizontal).

## Funcionalidades Implementadas
1. **Gestión y Segmentación de Clientes y Prospectos (Guía 11):**
    * Formulario reactivo con `StateFlow` para registro de perfil comercial (Cliente / Prospecto), nombre, RUT de empresa, correo, teléfono, dirección, rubro, tamaño del negocio y encuesta de preferencias técnicas.
    * Validación lógica centralizada y desacoplada en `UsuarioViewModel` (incluyendo algoritmo chileno Módulo 11 para RUT, formato de correo y longitud de campos) con retroalimentación visual por campo (`isError`, `supportingText` e íconos de error).
    * Pantalla de resumen en tiempo real mediante `UsuarioViewModel` compartido.
2. **Catálogo y CRUD de Contenidos Técnicos con Persistencia Local SQLite (`Room` + `KSP`):**
    * Creación, lectura, filtrado funcional por categoría/tipo (`.filter`), edición y eliminación de Recetas, Cursos y Videos Técnicos asociados a insumos Master Martini.
3. **Persistencia de Preferencias y Animaciones Funcionales (Guía 12):**
    * Almacenamiento local persistente con **DataStore Preferences** (`EstadoDataStore`) para el modo especial accesible y alertas de cursos.
    * Simulación de carga inicial con `CircularProgressIndicator`, estados derivados con `derivedStateOf`, transiciones de color con `animateColorAsState(tween)` y confirmaciones animadas con `AnimatedVisibility` y `animateContentSize`.
4. **Integración de Recursos Nativos (Guía 13):**
    * Acceso seguro a **Galería** (`ActivityResultContracts.GetContent()`) y **Cámara** (`ActivityResultContracts.TakePicture()`) mediante `FileProvider` y permisos en tiempo de ejecución, renderizados en el componente circular reutilizable `ImagenInteligente`.

## Arquitectura del Proyecto (MVVM)
* `data/local/`: `AppDataBase`, `ContenidoDao`, `ContenidoEntity` (Room) y `EstadoDataStore` (DataStore Preferences).
* `data/repository/`: `ContenidoRepository`.
* `model/`: `Contenido`, `ContenidoUiState`, `ContenidoErrores`, `UsuarioUiState`, `UsuarioErrores`.
* `navigation/`: `Screen`, `NavigationEvent`, `AppNavigation`.
* `ui/components/`: `ImagenInteligente`, `TarjetaContenido`, `AppNavigationBars`.
* `ui/screens/`: `HomeScreen` (`Compacta`, `Mediana`, `Expandida`), `ContenidoListaScreen`, `ContenidoDetalleScreen`, `ContenidoFormularioScreen`, `RegistroScreen`, `ResumenRegistroScreen`, `ProfileScreen`, `SettingsScreen`.
* `ui/utils/`: `WindowSizeUtils`, `ValidadorUtils`, `ImageUriUtils`.
* `viewmodel/`: `MainViewModel`, `UsuarioViewModel`, `ContenidoViewModel`, `EstadoViewModel`.

## Pasos para Ejecutar el Proyecto
1. Clonar el repositorio desde GitHub:
   ```bash
   git clone <URL_DEL_REPOSITORIO>