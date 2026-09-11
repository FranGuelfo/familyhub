# 🏡 FamilyHub API — Modern Backend Architecture

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0%2B-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4%2B-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21_LTS-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![CI/CD](https://img.shields.io/badge/GitHub_Actions-Passing-2088FF?logo=githubactions&logoColor=white)](https://github.com)
[![OpenAPI](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?logo=swagger&logoColor=black)](http://localhost:8080/swagger-ui/index.html)

**FamilyHub** es una API REST empresarial desarrollada en **Kotlin** y **Spring Boot** bajo los principios de **Arquitectura Hexagonal (Ports & Adapters)** y **Domain-Driven Design (DDD)**.

El proyecto resuelve la gestión integral de un núcleo familiar (miembros, tareas compartidas, asignación de vehículos y gestión de plazas de garaje comunitarias), priorizando desacoplamiento de infraestructura, seguridad granular stateless y despliegue continuo contenerizado.

---

## 🏛️ Decisiones de Arquitectura e Ingeniería

                   ┌──────────────────────────────────────────────┐
                   │               Driving Adapters               │
                   │   (REST Controllers / Swagger UI / OpenAPI)  │
                   └──────────────────────┬───────────────────────┘
                                          │ Inbound Ports
                                          ▼
                   ┌──────────────────────────────────────────────┐
                   │               Application Layer              │
                   │           (Use Cases & Domain Services)      │
                   └──────────────────────┬───────────────────────┘
                                          │
                                          ▼
                   ┌──────────────────────────────────────────────┐
                   │                 Domain Core                  │
                   │   (Pure Kotlin Entities, Aggregates & VOs)   │
                   └──────────────────────▲───────────────────────┘
                                          │ Outbound Ports
                                          │
                   ┌──────────────────────┴───────────────────────┐
                   │               Driven Adapters                │
                   │   (Spring Data JPA, PostgreSQL, Spring Sec)  │
                   └──────────────────────────────────────────────┘

### 1. Desacoplamiento del Dominio (Pure Kotlin)
* El núcleo de negocio es completamente agnóstico al framework: las entidades y objetos de valor (Value Objects como `LicensePlate`, `SpotStatus`) no contienen anotaciones de base de datos ni dependencias externas.
* Toda comunicación entre el mundo exterior y el dominio se rige por interfaces (puertos de entrada y salida).

### 2. Seguridad Stateless con JWT y RBAC
* **Autenticación y Autorización:** Implementación sin estado basada en JSON Web Tokens (firmados con claves de 256 bits).
* **Control de Acceso Basado en Roles (RBAC):** Restricciones declarativas mediante `@EnableMethodSecurity` y `@PreAuthorize("hasRole('ADMIN')")`.
* **Tratamiento Defensivo de Excepciones:** Intercepción centralizada de errores a través de `AuthenticationEntryPoint`, `AccessDeniedHandler` y un `GlobalExceptionHandler` unificado que devuelve respuestas estandarizadas RFC 7807 (evitando fugas de stack trace).

### 3. Estrategia de Testing Piramidal
* **Slice Tests Web Aislados:** Pruebas de seguridad y controladores usando `@WebMvcTest` con `@MockitoBean` para validar filtros de seguridad, tokens y códigos HTTP (401, 403, 200, 201) en milisegundos sin levantar Hibernate ni bases de datos.
* **Pruebas de Integración y Persistencia:** Validación de mapeo relacional y ciclos de vida JPA contra PostgreSQL.

### 4. Empaquetado Docker Multicapa y Producción Hardened
* **Multi-Stage Build (3 etapas):** `builder` (compilación y caché de Gradle) $\rightarrow$ `extractor` (separación de capas con `jarmode=tools`) $\rightarrow$ imagen final de ejecución de peso reducido (<180 MB).
* **Principio de Mínimo Privilegio:** Ejecución del proceso Java bajo un usuario no-root (`familyhub:familyhub`).
* **Afinación de JVM en Contenedores:** Banderas optimizadas `-XX:+UseContainerSupport` y `-XX:MaxRAMPercentage=75.0` para respetar los límites de memoria del orquestador.

### 5. CI/CD Automatizado (GitHub Actions)
* Pipeline de dos fases:
    1. **Job `test`:** Levanta un contenedor de servicio efímero de PostgreSQL (`healthcheck` activo con `pg_isready`), inyecta variables de entorno y ejecuta la suite de tests unitarios y de integración.
    2. **Job `build-docker`:** Compila la imagen Docker multicapa únicamente si el paso de tests finaliza en verde, usando caché nativa de GitHub Actions (`type=gha`).

---

## 🛠️ Stack Tecnológico

| Capa / Área | Tecnologías |
|---|---|
| **Lenguaje** | Kotlin 2.0 (JVM 21 LTS) |
| **Framework** | Spring Boot 3.4+ (Web, Security, Data JPA, Validation) |
| **Base de Datos** | PostgreSQL 16 + HikariCP |
| **Migraciones** | Flyway Migration Support |
| **Documentación** | SpringDoc OpenAPI 3 / Swagger UI |
| **Contenedores** | Docker, Docker Compose |
| **Integración Continua** | GitHub Actions Workflows |
| **Testing** | JUnit 5, Mockito, MockMvc, AssertJ |

---

## 📂 Estructura del Proyecto

```text
com.franguelfo.familyhub
├── domain/                    # Núcleo del Dominio (Zero dependencias de Spring)
│   ├── family/                # Entidades, Enums y Puertos (FamilyRepository)
│   ├── member/                # Miembros y Roles (ADMIN, MEMBER)
│   └── vehicle/               # Agregados de Vehículos y Plazas de Garaje
├── application/               # Casos de Uso y Servicios de Aplicación
│   ├── auth/                  # Lógica de Login y Registro
│   ├── family/                # Coordinación de la lógica familiar
│   └── vehicle/               # Asignación y liberación de plazas
└── infrastructure/            # Adaptadores de Entrada y Salida
    ├── persistence/           # Entidades JPA, Mapeadores y Repositorios SQL
    ├── security/              # Filtros JWT, Configuración de Seguridad y Handlers
    └── web/                   # REST Controllers, DTOs y Configuración de Swagger
🚀 Puesta en Marcha
Prerrequisitos
Docker Desktop (motor Linux/WSL2 activo).

Git.

1. Clonar el repositorio
Bash
git clone [https://github.com/tu-usuario/familyhub.git](https://github.com/tu-usuario/familyhub.git)
cd familyhub
2. Configurar variables de entorno
Crea un archivo .env en la raíz (puedes tomar este de referencia):

Properties
DB_NAME=familyhub_db
DB_USER=familyhub_prod_user
DB_PASSWORD=SuperSecretProdPassword2026!
JWT_SECRET=9a8b7c6d5e4f3a2b1c0d9e8f7a6b5c4d3e2f1a0b9c8d7e6f5a4b3c2d1e0f9a8b
JWT_EXPIRATION=86400000
3. Levantar la infraestructura en producción local
Ejecuta la orquestación completa (PostgreSQL + API):

Bash
docker compose -f docker-compose.prod.yml up -d --build
Comprueba el arranque con:

Bash
docker logs -f familyhub-prod-api
📖 Documentación Interactiva de la API (Swagger UI)
Una vez levantada la aplicación, accede desde tu navegador a:

👉 http://localhost:8080/swagger-ui/index.html

Cómo autenticarse en Swagger UI:
Dirígete a la sección Autenticación y ejecuta POST /api/auth/login o POST /api/auth/register.

Copia el token JWT devuelto en la respuesta.

Haz clic en el botón superior derecho Authorize (candado).

Pega el token directamente (sin añadir el prefijo Bearer) y pulsa Authorize.

Todos los endpoints protegidos por RBAC enviarán automáticamente la cabecera Authorization: Bearer <token>.

🧪 Ejecución de Tests
Para ejecutar la batería completa de pruebas unitarias y de integración localmente:

Bash
# Ejecutar toda la suite de pruebas
./gradlew test

# Ejecutar únicamente las pruebas web de seguridad aisladas
./gradlew test --tests SecurityAccessControlTest

👨‍💻 Autor
Fran Güelfo — Backend Software Developer

LinkedIn: tu-perfil-de-linkedin

GitHub: @tu-usuario

Correo: tu-email@dominio.com