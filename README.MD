# 1. Nombre del Proyecto
**MasterMartini_Grupo3** — Plataforma Móvil de Contenidos Técnicos y Relacionamiento con Clientes (**Master Martini Chile SPA**)

Aplicación móvil nativa para Android desarrollada en **Kotlin**, **Jetpack Compose (Material 3)** y arquitectura **MVVM**, integrada con un microservicio REST propio en **Spring Boot + MySQL**, consumo de API externa vía **Retrofit**, persistencia local híbrida (**Room + DataStore**), recursos nativos (**Cámara y Galería**), suite de pruebas unitarias e instrumentadas (**Kotest, JUnit 5, MockK y Compose UI Test**) y distribución mediante **APK firmado digitalmente**.

---

# 2. Integrante
* **Estudiante:** Alonso Raúl Mateo Randolph Marín
* **Asignatura:** DSY1105 — Desarrollo de Aplicaciones Móviles (Duoc UC)
* **Evaluación:** Evaluación Parcial 4 (Encargo — Integración Backend, API Externa, Testing y APK Firmado)

---

# 3. Funcionalidades Implementadas
1. **Interfaz Accesible y Adaptativa (Material 3 + WindowSizeClass):**
   * Diseño de alto contraste con tipografía ampliada (`18.sp` a `30.sp`), botones principales de `60.dp`, navegación en máximo 3 clics (`NavigationBar`, `ModalNavigationDrawer` y `TopAppBar`) e interruptor persistente de **Alto Contraste** en `MasterMartiniTheme`.
   * Vistas adaptativas automáticas para pantallas `Compact` (`HomeScreenCompacta`), `Medium` (`HomeScreenMediana`) y `Expanded` (`HomeScreenExpandida`).
2. **Gestión y Validación de Clientes / Prospectos:**
   * Formulario reactivo con validación desacoplada en `UsuarioViewModel` y `ValidadorUtils` (algoritmo chileno Módulo 11 para RUT, correo, teléfono, contraseña, preferencias y términos) con retroalimentación visual por campo (`isError`, `supportingText` e íconos de error).
   * Sincronización del registro hacia el microservicio Spring Boot (`POST /api/v1/usuarios`) y pantalla de resumen en tiempo real (`ResumenRegistroScreen`).
3. **Catálogo y CRUD de Contenidos Técnicos (Spring Boot + MySQL + Room):**
   * Creación, listado, filtrado reactivo por tipo/categoría, visualización de detalle con fotografía adjunta, edición y eliminación de Recetas, Cursos y Videos Técnicos sincronizados en tiempo real con MySQL y con respaldo offline en Room SQLite.
4. **Comunidad Gastronómica — Consumo de API Externa (Retrofit + JSONPlaceholder):**
   * Módulo independiente conectado vía Retrofit a JSONPlaceholder soportando `GET`, `POST`, `PUT` y `DELETE` sin interferir con los datos locales ni el microservicio propio.
5. **Persistencia de Preferencias y Animaciones Funcionales:**
   * Almacenamiento de configuraciones con **DataStore Preferences**, loader inicial (`CircularProgressIndicator`), estados derivados (`derivedStateOf`) y animaciones (`animateColorAsState`, `AnimatedVisibility`, `animateContentSize` y transiciones `fadeIn`/`fadeOut` en `NavHost`).
6. **Recursos Nativos del Dispositivo:**
   * Integración segura de **Cámara** (`TakePicture` + `FileProvider` + permisos en tiempo de ejecución) y **Galería** (`GetContent`) renderizados con Coil en `ImagenInteligente`, `TarjetaContenido` y `ContenidoDetalleScreen`.
7. **Testing Híbrido (≥ 80% de Cobertura Lógica + UI Tests):**
   * 25 pruebas unitarias en **Kotest + JUnit 5 + MockK + Coroutines Test** cubriendo `ValidadorUtils`, `PostRepository`, `PostViewModel`, `ContenidoRepository`, `ContenidoViewModel`, `UsuarioViewModel`, `UsuarioRepository`, `EstadoViewModel` y `MainViewModel`.
   * Pruebas instrumentadas con **Compose UI Test** (`PostScreenTest` y `RegistroScreenTest`).

---

# 4. Endpoints Utilizados

## 4.1 API Externa — JSONPlaceholder
**Base URL:** `https://jsonplaceholder.typicode.com/`

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/posts` | Obtiene el listado de publicaciones de la comunidad externa. |
| `POST` | `/posts` | Crea una nueva publicación desde la pantalla `PostScreen`. |
| `PUT` | `/posts/{id}` | Actualiza título y contenido de una publicación existente. |
| `DELETE` | `/posts/{id}` | Elimina una publicación seleccionada. |

## 4.2 Microservicio Propio — `mastermartini-api` (Spring Boot + MySQL)
**Base URL (Emulador Android):** `http://10.0.2.2:8080/`  
**Base URL (Localhost PC):** `http://localhost:8080/`

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/v1/contenidos` | Lista todos los contenidos técnicos almacenados en MySQL. |
| `GET` | `/api/v1/contenidos/{id}` | Obtiene el detalle de un contenido técnico por ID. |
| `POST` | `/api/v1/contenidos` | Registra una nueva receta, curso o video técnico en MySQL. |
| `PUT` | `/api/v1/contenidos/{id}` | Actualiza un contenido técnico existente en MySQL. |
| `DELETE` | `/api/v1/contenidos/{id}` | Elimina un contenido técnico por ID en MySQL. |
| `GET` | `/api/v1/usuarios` | Lista los clientes y prospectos registrados. |
| `POST` | `/api/v1/usuarios` | Registra la ficha comercial y preferencias de un cliente/prospecto. |
| `PUT` | `/api/v1/usuarios/{id}` | Actualiza los datos comerciales de un usuario existente. |

---

# 5. Pasos para Ejecutar el Proyecto

## 5.1 Levantar la Base de Datos MySQL y el Microservicio Spring Boot
1. Iniciar **Laragon** y encender el servicio **MySQL** en el puerto `3306` (usuario `root`, sin contraseña por defecto).
2. Abrir el repositorio del microservicio `mastermartini-api` en IntelliJ IDEA (JDK 21) y ejecutar `MasterMartiniApiApplication` (o correr `mvn spring-boot:run`).
3. Verificar que la base `master_martini_db` se creó automáticamente y que `http://localhost:8080/api/v1/contenidos` retorna los 4 registros iniciales en JSON.

## 5.2 Ejecutar la Aplicación Móvil Android
1. Clonar este repositorio y abrir la carpeta del proyecto en **Android Studio** configurado con **Gradle JDK 21**.
2. Esperar a que finalice la sincronización de **Gradle y KSP** (`Sync Project with Gradle Files`).
3. Iniciar un emulador Android (API 26 o superior, que mapea `10.0.2.2:8080` al `localhost:8080` del PC) o conectar un dispositivo físico (en físico, actualizar `BASE_URL` en `BackendRetrofitInstance.kt` con la IP local del PC).
4. Ejecutar **Run 'app'** (`Shift + F10`).

## 5.3 Ejecutar Pruebas Unitarias y de UI
* **Tests unitarios (Kotest + MockK):**
  ```bash
  ./gradlew testDebugUnitTest