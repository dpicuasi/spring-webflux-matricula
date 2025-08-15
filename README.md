# Proyecto Spring WebFlux Matricula

Este proyecto es una API REST para la gestión de matrículas estudiantiles, desarrollada con Spring Boot WebFlux y MongoDB. El despliegue se realiza fácilmente usando Docker Compose.

## Características principales
- API reactiva para operaciones CRUD de matrícula, estudiante, curso y usuario.
- Seguridad JWT y roles (admin, user).
- Inicialización automática de datos de usuario y roles.
- Conexión a MongoDB en contenedor.

## Estructura principal
- **src/main/java/com/mitocode/model**: Entidades principales (`Matricula`, `Estudiante`, `Curso`, `User`, `Role`).
- **src/main/java/com/mitocode/repo**: Repositorios reactivos para MongoDB.
- **src/main/java/com/mitocode/controller**: Controladores y handlers para endpoints REST.
- **docker-compose.yml**: Orquestación de MongoDB y la app backend.
- **Dockerfile**: Construcción de la imagen backend.

## Requisitos previos
- [Docker y Docker Compose](https://docs.docker.com/get-docker/) instalados.
- [Java 21+](https://adoptium.net/) (solo si deseas compilar localmente).
- [Maven](https://maven.apache.org/) (solo si deseas compilar localmente).

## Pasos para desplegar el proyecto

### 1. Clona el repositorio
```bash
git clone <url-del-repo>
cd spring-webflux-matricula
```

### 2. Compila el proyecto (opcional si solo usas Docker Compose)
```bash
mvn clean package -DskipTests
```

### 3. Levanta los servicios
```bash
docker compose up -d --build
```
Esto creará y levantará dos contenedores:
- `mongodb`: Base de datos MongoDB en puerto 27017
- `matricula_app`: Backend Spring Boot en puerto 8080

### 4. Verifica que todo esté funcionando
Puedes probar el estado de la app con:
```bash
curl http://localhost:8080/actuator/health
```

## Endpoints disponibles

> **Nota:** Cambia `<jwt-token>` por el token real recibido en el login.

### Autenticación

#### POST `/login`
- **Descripción:** Obtiene un token JWT válido.
- **URL completa:** `http://localhost:8080/login`
- **Body ejemplo:**
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **Curl:**
  ```bash
  curl -X POST http://localhost:8080/login \
    -H "Content-Type: application/json" \
    -d '{"username":"admin","password":"admin123"}'
  ```
- **Respuesta exitosa:**
  ```json
  {
    "token": "<jwt-token>"
  }
  ```

---

### Cursos

#### GET `/api/v2/cursos`
- **Descripción:** Lista todos los cursos
- **URL completa:** `http://localhost:8080/api/v2/cursos`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/cursos
  ```

#### GET `/api/v2/cursos/{id}`
- **Descripción:** Obtiene curso por ID
- **URL completa:** `http://localhost:8080/api/v2/cursos/ID_CURSO`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/cursos/ID_CURSO
  ```

#### POST `/api/v2/cursos`
- **Descripción:** Crea nuevo curso
- **URL completa:** `http://localhost:8080/api/v2/cursos`
  - **Body ejemplo:**
    ```json
    {
      "nombre": "Ciencias Naturales",
      "siglas": "MAT101",
      "creditos": 4
    }
    ```
- **Curl:**
  ```bash
  curl -X POST http://localhost:8080/api/v2/cursos \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{
      "nombre": "Ciencias Naturales",
      "siglas": "MAT101",
      "creditos": 4
    }'
  ```

#### PUT `/api/v2/cursos/{id}`
- **Descripción:** Actualiza curso
- **URL completa:** `http://localhost:8080/api/v2/cursos/ID_CURSO`
- **Body ejemplo:**
  ```json
  {
    "nombre": "Matemáticas Avanzadas",
    "descripcion": "Curso avanzado"
  }
  ```
- **Curl:**
  ```bash
  curl -X PUT http://localhost:8080/api/v2/cursos/ID_CURSO \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{"nombre":"Matemáticas Avanzadas","descripcion":"Curso avanzado"}'
  ```

#### DELETE `/api/v2/cursos/{id}`
- **Descripción:** Elimina curso
- **URL completa:** `http://localhost:8080/api/v2/cursos/ID_CURSO`
- **Curl:**
  ```bash
  curl -X DELETE http://localhost:8080/api/v2/cursos/ID_CURSO \
    -H "Authorization: Bearer <jwt-token>"
  ```

#### (API funcional) `/api/cursos` (mismos ejemplos que REST clásico, solo cambia la ruta)

---

### Estudiantes

#### GET `/api/v2/estudiantes`
- **Descripción:** Lista todos los estudiantes
- **URL completa:** `http://localhost:8080/api/v2/estudiantes`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/estudiantes
  ```

#### GET `/api/v2/estudiantes/{id}`
- **Descripción:** Obtiene estudiante por ID
- **URL completa:** `http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE
  ```

#### GET `/api/v2/estudiantes/ordenados/asc`
- **Descripción:** Lista estudiantes ordenados por edad ascendente
- **URL completa:** `http://localhost:8080/api/v2/estudiantes/ordenados/asc`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/estudiantes/ordenados/asc
  ```

#### GET `/api/v2/estudiantes/ordenados/desc`
- **Descripción:** Lista estudiantes ordenados por edad descendente
- **URL completa:** `http://localhost:8080/api/v2/estudiantes/ordenados/desc`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/v2/estudiantes/ordenados/desc
  ```

#### POST `/api/v2/estudiantes`
- **Descripción:** Crea nuevo estudiante
- **URL completa:** `http://localhost:8080/api/v2/estudiantes`
  - **Body ejemplo:**
    ```json
    {
      "nombres": "Juan",
      "apellidos": "Pérez",
      "dni": "12345678",
      "edad": 22
    }
    ```
- **Curl:**
  ```bash
  curl -X POST http://localhost:8080/api/v2/estudiantes \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{
      "nombres": "Juan",
      "apellidos": "Pérez",
      "dni": "12345678",
      "edad": 22
    }'
  ```

#### PUT `/api/v2/estudiantes/{id}`
- **Descripción:** Actualiza estudiante
- **URL completa:** `http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE`
- **Body ejemplo:**
  ```json
  {
    "id": "ID_ESTUDIANTE",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "dni": "12345678",
    "edad": 23
  }
  ```
- **Curl:**
  ```bash
  curl -X PUT http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{
      "id": "ID_ESTUDIANTE",
      "nombres": "Juan",
      "apellidos": "Pérez",
      "dni": "12345678",
      "edad": 23
    }'
  ```

#### DELETE `/api/v2/estudiantes/{id}`
- **Descripción:** Elimina estudiante
- **URL completa:** `http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE`
- **Curl:**
  ```bash
  curl -X DELETE http://localhost:8080/api/v2/estudiantes/ID_ESTUDIANTE \
    -H "Authorization: Bearer <jwt-token>"
  ```

#### (API funcional) `/api/estudiantes` (mismos ejemplos que REST clásico, solo cambia la ruta)

---

### Matrículas

#### GET `/api/matriculas`
- **Descripción:** Lista todas las matrículas
- **URL completa:** `http://localhost:8080/api/matriculas`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/matriculas
  ```

#### GET `/api/matriculas/{id}`
- **Descripción:** Obtiene una matrícula por ID
- **URL completa:** `http://localhost:8080/api/matriculas/ID_MATRICULA`
- **Curl:**
  ```bash
  curl -H "Authorization: Bearer <jwt-token>" http://localhost:8080/api/matriculas/ID_MATRICULA
  ```

#### POST `/api/matriculas`
- **Descripción:** Crea una nueva matrícula
- **URL completa:** `http://localhost:8080/api/matriculas`
- **Body ejemplo:**
  ```json
  {
    "estudiante": {
        "id": "689e4252e4d37407a79cd9ee"
    },
    "cursos": [
        {
            "id": "689e8467fd811545a3c4c79c"
        }
    ],
    "fechaMatricula": "2024-08-14"
  }
  ```
- **Curl:**
  ```bash
  curl -X POST http://localhost:8080/api/matriculas \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{"estudianteId":"ID_ESTUDIANTE","cursoId":"ID_CURSO","fecha":"2024-08-14"}'
  ```

#### PUT `/api/matriculas/{id}`
- **Descripción:** Actualiza una matrícula existente
- **URL completa:** `http://localhost:8080/api/matriculas/ID_MATRICULA`
- **Body ejemplo:**
  ```json
  {
    "estudianteId": "ID_ESTUDIANTE",
    "cursoId": "ID_CURSO",
    "fecha": "2024-08-15"
  }
  ```
- **Curl:**
  ```bash
  curl -X PUT http://localhost:8080/api/matriculas/ID_MATRICULA \
    -H "Authorization: Bearer <jwt-token>" \
    -H "Content-Type: application/json" \
    -d '{"estudianteId":"ID_ESTUDIANTE","cursoId":"ID_CURSO","fecha":"2024-08-15"}'
  ```

#### DELETE `/api/matriculas/{id}`
- **Descripción:** Elimina una matrícula
- **URL completa:** `http://localhost:8080/api/matriculas/ID_MATRICULA`
- **Curl:**
  ```bash
  curl -X DELETE http://localhost:8080/api/matriculas/ID_MATRICULA \
    -H "Authorization: Bearer <jwt-token>"
  ```
---

> **Nota:** Todos los endpoints (excepto `/login`) requieren autenticación JWT en el header `Authorization`. Puedes obtener el token usando el endpoint de login.

## Datos de prueba iniciales
Al iniciar, se crean los siguientes usuarios por defecto:
- **admin / admin123** (roles: ADMIN, USER)
- **user / user123** (rol: USER)

## Troubleshooting
- Si el backend no arranca, revisa los logs con:
  ```bash
  docker logs matricula_app
  ```
- Si MongoDB no responde, revisa los logs con:
  ```bash
  docker logs mongodb
  ```
- Si cambias el código Java, recuerda recompilar y reconstruir la imagen:
  ```bash
  mvn clean package -DskipTests
  docker compose up -d --build
  ```

## Licencia
Proyecto de Daniel Picuasi Duque, con MITOCODE - Proyecto Final Spring Webflux. Uso libre para fines educativos.
