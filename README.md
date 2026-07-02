# Sistema de Reservas de Hotel "Los Andes" — Microservicios

Proyecto para la Tarea Autónoma de Microservicios (UTI). Contiene dos
microservicios Spring Boot (`svc-rooms`, `svc-reservations`), sus bases de
datos PostgreSQL independientes y un API Gateway Nginx, según lo pedido en
la consigna.

## Estructura

```
hotel-microservices/
├── svc-rooms/            # Microservicio de habitaciones (puerto 8081)
├── svc-reservations/     # Microservicio de reservas (puerto 8082)
├── nginx/nginx.conf      # API Gateway (puerto 80)
├── docker-compose.yml    # Orquesta todo el sistema
└── postman/              # Colección Postman de partida
```

## Opción A — Levantar todo con Docker (como pide la consigna)

Requiere Docker Desktop instalado y corriendo.

```bash
cd hotel-microservices
docker-compose up -d --build
```

Esto levanta: `rooms-db` (5433), `reservations-db` (5434), `svc-rooms`
(8081), `svc-reservations` (8082) y `nginx-gateway` (80). Todas las
peticiones de Postman deben apuntar a `http://localhost:80/api/v1/...`.

Para probar Resilience4j (Circuit Breaker / Retry / Fallback):

```bash
docker-compose stop svc-rooms      # simula la caída
# ... probar creación (503) y consulta (200 con mensaje de fallback) ...
docker-compose start svc-rooms     # recuperación automática
```

## Opción B — Abrir y correr en IntelliJ IDEA

1. **File → Open** y selecciona la carpeta `svc-rooms` (impórtalo como
   proyecto Maven). Repite para `svc-reservations` en otra ventana, o
   ábrelos como dos módulos del mismo proyecto raíz.
2. Levanta **solo las bases de datos** con Docker (no hace falta construir
   las imágenes de los microservicios):
   ```bash
   docker-compose up -d rooms-db reservations-db
   ```
3. En cada Run Configuration de IntelliJ (clic derecho sobre
   `SvcRoomsApplication` / `SvcReservationsApplication` → *Run* →
   *Edit Configurations*), añade la variable de entorno:
   ```
   SPRING_PROFILES_ACTIVE=local
   ```
   Esto activa `application-local.yml`, que apunta a
   `localhost:5433` / `localhost:5434` (los puertos publicados por Docker)
   y hace que `svc-reservations` llame a `svc-rooms` en
   `http://localhost:8081` en lugar del nombre de servicio Docker.
4. Ejecuta primero `SvcRoomsApplication`, luego `SvcReservationsApplication`.
5. Prueba directamente contra los microservicios (`localhost:8081`,
   `localhost:8082`) o levanta también `nginx-gateway` con Docker si quieres
   probar a través del Gateway en el puerto 80.

## Endpoints principales

**svc-rooms** (`/api/v1/rooms`): `GET /`, `GET /{id}`, `POST /`,
`GET /{id}/availability`, `PATCH /{id}/availability`.

**svc-reservations** (`/api/v1/reservations`): `POST /`, `GET /{id}`,
`GET /guest/{email}`, `PATCH /{id}/checkout`.

## Resiliencia (svc-reservations → svc-rooms)

Configurado en `svc-reservations/src/main/resources/application.yml`
(instancia `roomsService`):

- **Circuit Breaker**: ventana de 5 llamadas, 50% de fallos abre el
  circuito, espera 10s en estado abierto.
- **Retry**: hasta 3 intentos, 2s de espera entre cada uno.
- **Fallback**:
  - Al **crear** una reserva con `svc-rooms` caído → `503 Service
    Unavailable`, mensaje *"Room information temporarily unavailable"*.
  - Al **consultar** una reserva existente con `svc-rooms` caído →
    `200 OK`, con `roomNumber: "Room information temporarily
    unavailable"` (los datos propios de la reserva sí se devuelven, ya
    que viven en `reservations-db`).

## Postman

Importa `postman/Hotel-Microservices.postman_collection.json`. La variable
`gateway` apunta a `http://localhost:80` (a través de Nginx). Cambia
`roomId` / `reservationId` según los IDs reales que te devuelva tu
ambiente al crear los recursos.

## Nota

Este proyecto es un punto de partida funcional y completo según la
consigna. Antes de entregar: ejecútalo end-to-end, toma las capturas de
Postman/Docker pedidas, sube el código a un repositorio Git con historial
de commits, y graba el video demostrativo (máx. 5 min).
