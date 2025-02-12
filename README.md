# API Autentication


## ACTUALIZAR REDME

Esta API REST proporciona un sistema completo para la gestión de cuentas bancarias, permitiendo a los usuarios realizar operaciones bancarias básicas de manera segura. El sistema implementa autenticación mediante JWT y permite:

- Gestión de usuarios: Registro de nuevos usuarios, actualización de datos y control de estado de cuentas.
- Autenticación segura: Sistema de login/logout con tokens JWT para proteger los endpoints.
- Administración de cuentas bancarias: Creación, consulta y gestión de cuentas asociadas a usuarios.
- Control de movimientos: Registro y consulta de transacciones (depósitos y retiros) con validación de saldos.
- Persistencia de datos: Almacenamiento seguro de la información en base de datos PostgreSQL.

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

## Descargar y Ejecutar la Imagen

- **Descargar la imagen desde GHCR**

    ```sh
    
    ```

- **Correr el contenedor**

    ```sh
    podman run --rm --name <CONTAINER-NAME> -p 8080:8080 -d <IMAGE-NAME>
    ```

## Generar imagen

- **Construir imagen**

    ```sh
    podman build -t <IMAGE-NAME> -f Containerfile . 
    ```

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- H2
- Lombok
- MapStruct
- JUnit 5 + Mockito
- Gradle

## Endpoints

### Usuarios

- POST `/api/v1/usuarios/registrar` - Registra un nuevo usuario
- PUT `/api/v1/usuarios/{id}` - Actualiza información de un usuario existente
- PUT `/api/v1/usuarios/{id}/inactivar` - Inactiva un usuario

### Autenticación

- POST `/api/v1/auth/login` - Inicia sesión y genera token JWT
- POST `/api/v1/auth/logout` - Cierra sesión y revoca token

### Cuentas Bancarias

- POST `/api/v1/cuentas` - Crea una nueva cuenta bancaria
- GET `/api/v1/cuentas` - Obtiene todas las cuentas del usuario
- GET `/api/v1/cuentas/{id}` - Obtiene una cuenta específica
- PUT `/api/v1/cuentas/{id}` - Actualiza información de una cuenta
- DELETE `/api/v1/cuentas/{id}` - Elimina una cuenta

### Movimientos

- POST `/api/v1/movimientos` - Registra un nuevo movimiento (depósito/retiro)
- GET `/api/v1/movimientos` - Obtiene historial de movimientos
- GET `/api/v1/movimientos/{id}` - Obtiene un movimiento específico

## Pasos de Instalación

1. **Clonar el Repositorio**

    ```bash
    git clone https://github.com/CharlSK8/CuentaBancaria.git
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

- La API estará disponible en `http://localhost:8080`.
- Puedes probar los endpoints utilizando herramientas como Postman, Insomnia o cURL.

## Endpoints Principales

Como se detalla en el README, podrás acceder a:

- Registro de usuarios: `POST /api/v1/usuarios/registrar`
- Login: `POST /api/v1/auth/login`
- Gestión de cuentas: `GET /api/v1/cuentas`, `POST /api/v1/cuentas`
- Gestión de movimientos: `GET /api/v1/movimientos`, `POST /api/v1/movimientos`

## Notas Adicionales

- Asegúrate de tener los puertos necesarios disponibles (por defecto 8080).
- La aplicación utiliza JWT para autenticación, por lo que necesitarás el token para acceder a los endpoints protegidos.
- Para pruebas, la base de datos H2 se reiniciará cada vez que se reinicie la aplicación.
