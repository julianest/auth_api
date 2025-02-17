# API Autentication

Este proyecto es una aplicación que realiza operaciones de registro, actualización, inactividad, login y logout de usuario. El sistema implementa autenticación mediante JWT y permite:

- Gestión de usuarios: Registro de nuevos usuarios, actualización de datos y control de estado de cuentas.
- Autenticación segura: Sistema de login/logout con tokens JWT para proteger los endpoints.
- Persistencia de datos: Almacenamiento seguro de la información en base de datos DBH2.

El proyecto está construido siguiendo las mejores prácticas de desarrollo, implementando una arquitectura en capas, pruebas unitarias y documentación clara de los endpoints disponibles.

## Dockerizar la Aplicación desde GHCR

Este documento proporciona los pasos para obtener, ejecutar y administrar un contenedor Docker con una imagen almacenada en GitHub Container Registry (GHCR).

## Prerrequisitos

- Tener instalado [Podman](https://podman.io/)
- Acceso a GitHub Container Registry (GHCR)
- Haber iniciado sesión en GHCR con Docker:

  ```sh
  podman login ghcr.io -u <USERNAME> -p <PASSWORD> ghcr.io 
  ```

## Descargar y ejecutar el proyecto en contenedor

- **Crear network**

    ```sh
    podman network create red-bank
    ```

- **Descargar la imagen desde GHCR**

    ```sh
    podman pull ghcr.io/julianest/auth_api:v1.0.0 
    ```

- **Correr el contenedor**

    ```sh
    podman run --rm --name auth --network=red-bank -p 8082:8082 -d ghcr.io/julianest/auth_api:v1.0.0 
    ```

## Generar imagen local

- **Construir imagen**

    ```sh
    podman build -t auth_api -f Containerfile . 
    ```

- **Correr el contenedor**

    ```sh
    podman run --rm --name auth --network=red-bank -p 8082:8082 -d auth_api
    ```

## Tecnologías Utilizadas

Este proyecto utiliza una variedad de tecnologías y bibliotecas para proporcionar una funcionalidad completa y robusta. A continuación se enumeran las principales tecnologías utilizadas:

- **Java 17**: Lenguaje de programación principal utilizado para desarrollar la aplicación.
- **Spring Boot**: Framework utilizado para crear aplicaciones basadas en Spring de manera rápida y sencilla.
- **Lombok**: Herramienta que reduce el código boilerplate mediante anotaciones.
- **Mockito**: Framework de pruebas utilizado para crear mocks y realizar pruebas unitarias.
- **JUnit 5**: Framework de pruebas utilizado para escribir y ejecutar pruebas unitarias.
- **DBH2**: Base de datos en memoria utilizada para pruebas y desarrollo.
- **Swagger**: Herramienta utilizada para documentar y probar APIs RESTful.
- **Git**: Sistema de control de versiones utilizado para el control de versiones del código fuente.
- **Gradle**: Herramienta de construcción utilizada para compilar y ejecutar la aplicación.
- **ActiveMQ**: Broker de mensajería utilizado para la comunicación asincrónica entre servicios.
- **Podman**: Herramienta para la gestión de contenedores sin necesidad de un demonio en segundo plano, compatible con Docker y enfocada en la seguridad.

## Endpoints

### Usuarios

- POST `/api/v1/usuarios/registrar` - Registra un nuevo usuario
- PUT `/api/v1/usuarios/{id}` - Actualiza información de un usuario existente
- PUT `/api/v1/usuarios/{id}/inactivar` - Inactiva un usuario

### Autenticación

- POST `/api/v1/auth/login` - Inicia sesión y genera token JWT
- POST `/api/v1/auth/logout` - Cierra sesión y revoca token

## Pasos de Instalación

1. **Clonar el Repositorio**

    ```bash
    git clone https://github.com/julianest/auth_api.git
    ```

2. **Compilar el Proyecto**

    ```bash
    ./gradlew build  
    ```

3. **Configurar la Base de Datos**

    El proyecto utiliza H2 (base de datos en memoria), por lo que no requiere configuración adicional de base de datos.

4. **Ejecutar la Aplicación**

    Hay dos formas de ejecutar el proyecto:

    **Opción 1: Desde Gradle/Maven**

    ```bash
    ./gradlew bootRun
    ```

    **Opción 2: Desde el IDE**

    1. Abrir el proyecto en tu IDE preferido (IntelliJ IDEA, Eclipse, etc.).
    2. Localizar la clase principal (debe tener la anotación `@SpringBootApplication`).
    3. Ejecutar como aplicación Java.
   

## Verificación

Una vez iniciada la aplicación:

- La API estará disponible en `http://localhost:8082`.
- Puedes probar los endpoints utilizando herramientas como Postman, Insomnia o cURL.


### Documentación de Swagger

Swagger es una herramienta poderosa para documentar y probar APIs RESTful. En este proyecto, se ha utilizado Swagger para generar automáticamente la documentación de la API, lo que facilita a los desarrolladores y a otros interesados comprender y probar los endpoints disponibles.


#### Acceso a la Documentación de Swagger

La documentación de Swagger para esta aplicación está disponible en la siguiente URL:

`http://localhost:8082/webjars/swagger-ui/index.html`

Al acceder a esta URL, se puede visualizar una interfaz gráfica que muestra todos los endpoints disponibles, junto con sus métodos HTTP, parámetros requeridos, y posibles respuestas. Además, Swagger permite probar directamente los endpoints desde la interfaz, lo que facilita la verificación y el debugging de la API.


## Notas Adicionales

- Asegúrate de tener los puertos necesarios disponibles (por defecto 8082).
- La aplicación utiliza JWT para autenticación, por lo que necesitarás el token para acceder a los endpoints protegidos.
- Para pruebas, la base de datos H2 se reiniciará cada vez que se reinicie la aplicación.
