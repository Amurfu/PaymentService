## Payment‑Service – Guía rápida (`HELP.md`)

> Micro‑servicio Java 17 / Spring Boot 3.2 que permite **registrar pagos, consultar su información, cambiar su estatus** y publicar dicho cambio en **RabbitMQ**.
> Persiste datos en **MySQL 8** y expone una API REST documentada con **Swagger UI**.

---

### 1. Arquitectura de carpetas (resumen)

```
payment-service/
├─ src/main/java/com/amurfu/paymentservice
│  ├─ controller/        ← REST endpoints
│  ├─ dto/               ← Request / Response (ApiResponse wrapper)
│  ├─ model/             ← Entidades JPA (Payment, PaymentStatus)
│  ├─ repository/        ← Spring‑Data JPA
│  ├─ service/           ← Lógica de negocio + Mapper
│  └─ messaging/         ← Publicador RabbitMQ
├─ src/main/resources/
│  └─ application.yml    ← Config local + perfil docker
├─ docker/
│  └─ 01_init.sql        ← Script para crear tablas y catálogo
├─ Dockerfile            ← Imagen de la app
├─ docker-compose.yml    ← MySQL + RabbitMQ + app
└─ pom.xml
```

---

### 2. Requisitos de sistema

| Herramienta        | Versión                 |
| ------------------ | ----------------------- |
| **JDK**            | 17 LTS                  |
| **Maven**          | 3.9+                    |
| **Docker Desktop** | 25.x (WSL 2 habilitado) |
| **Git**            | opcional                |

---

### 3. Ejecución **local** (sin Docker)

1. **Levanta MySQL y RabbitMQ locales**
   *MySQL* en `localhost:3306`, BD `payments`, usuario `root` / `Root` (o ajusta `application.yml`).
   *RabbitMQ* en `localhost:5672` (guest/guest).

2. **Compila y ejecuta**

   ```bash
   mvn clean spring-boot:run
   ```

3. **Swagger UI**
   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

4. **API endpoints principales**

   | Método | Ruta                    | Descripción                                 |
      | ------ | ----------------------- | ------------------------------------------- |
   | POST   | `/payments`             | Crea un pago                                |
   | GET    | `/payments/{id}`        | Consulta un pago                            |
   | PATCH  | `/payments/{id}/status` | Cambia estatus (PENDIENTE/PAGADO/CANCELADO) |

---

### 4. Ejecución con **Docker Compose**

> Recomendado: todo se aísla en contenedores.

```bash
# 1. Construir la imagen de la app
mvn -DskipTests clean package         # genera target/*.jar
docker compose build

# 2. Levantar la pila completa (MySQL + RabbitMQ + app)
docker compose up -d

# 3. Logs en vivo (opcional)
docker compose logs -f app
```

Accesos:

* **API / Swagger** → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **RabbitMQ UI**   → [http://localhost:15672](http://localhost:15672) (guest/guest)
* **MySQL**         → `localhost:13306` (root/Root)

> El perfil `docker` del `application.yml` se activa automáticamente (variable `SPRING_PROFILES_ACTIVE=docker` en `docker-compose.yml`).

---

### 5. Comandos útiles

```bash
# detener y borrar contenedores + volúmenes
docker compose down -v                

# reconstruir solo la app tras cambiar código
mvn package -DskipTests
docker compose build app
docker compose up -d app
```

---

### 6. Formato de respuesta API

Todas las respuestas siguen el wrapper:

```json
{
  "statusCode": "OK" | "ERROR",
  "message": "Descripción corta",
  "codeResponse": 200,
  "payload": { ... }          // puede ser null
}
```

Errores de validación (`400`) devuelven:

```json
{
  "statusCode": "ERROR",
  "message": "Datos inválidos",
  "codeResponse": 400,
  "payload": {
    "monto": "debe ser mayor que 0"
  }
}
```

---

### 7. Variables de entorno clave (perfil docker)

| Variable      | Default  | Uso                                |
| ------------- | -------- | ---------------------------------- |
| `DB_HOST`     | mysql    | Host MySQL dentro de la red Docker |
| `DB_NAME`     | payments | Base de datos                      |
| `DB_USER`     | root     | Usuario                            |
| `DB_PASS`     | Root     | Contraseña                         |
| `RABBIT_HOST` | rabbitmq | Host de RabbitMQ                   |

---

### 8. Pruebas rápidas con `curl`

```bash
# Crear pago
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{"concepto":"Servicio","quienRealiza":"Ana","aQuienSePaga":"Proveedor","monto":1200}'

# Cambiar a PAGADO
curl -X PATCH http://localhost:8080/payments/1/status \
  -H "Content-Type: application/json" \
  -d '{"nuevoStatus":"PAGADO"}'
```

---
