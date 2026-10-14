# ActivaChilecito

> **Plataforma digital para la organización de actividades deportivas y reserva de espacios deportivos en Chilecito**  
> **Trabajo Final Integrador de Grado**  
> **Universidad Nacional de Chilecito (UNdeC) — Escuela de Ingeniería**

---

## Autores y Equipo Académico

* **Autores:**
  * **Páez Campillay Cielo Ailén** — *Optando al título de Ingeniera en Sistemas* — `cieloopaezz@gmail.com`
  * **Gómez Carlos Agustín** — *Optando al título de Licenciado en Sistemas* — `mumugomez19@gmail.com`
* **Equipo de Cátedra de Trabajo Final (2026):**
  * Dr. Ing. Raúl Omar Moralejo (`romoralejo@gmail.com`)
  * Ing. Matías Pérez (`mataguper20@gmail.com`)
  * Ing. Mara Lía Rovero (`mrovero@undec.edu.ar`)

---

## Descripción del Proyecto

En la ciudad de Chilecito (La Rioja, Argentina), la organización de actividades deportivas y el alquiler de canchas se gestiona habitualmente mediante canales informales (WhatsApp, llamadas telefónicas y anotaciones manuales en papel). Esta dinámica ocasiona dispersión de información, dificultades para completar equipos deportivos, superposiciones de turnos y una falta de información analítica para los dueños de los predios.

**ActivaChilecito** es una solución integral web responsiva (Single Page Application) que centraliza la interacción deportiva local bajo dos roles autónomos:
* **Deportista:** Explora canchas disponibles en tiempo real, solicita reservas de turnos, organiza actividades comunitarias abiertas y se inscribe a encuentros deportivos.
* **Dueño de Complejo:** Administra la ficha comercial de su establecimiento, configura sus canchas y horarios de atención, aprueba o rechaza solicitudes de reserva y analiza reportes analíticos de ocupación y horas pico.

---

## Arquitectura del Sistema

El proyecto sigue los principios de la **Arquitectura Hexagonal (Puertos y Adaptadores)** y **Clean Architecture**, garantizando el aislamiento del núcleo del negocio (*Core*) respecto de los frameworks e infraestructura técnica (*Adapters*):

```
ActivaChilecito/
├── backend/
│   ├── Core/                  # Lógica de dominio pura en Java (Entidades, Casos de Uso, Puertos)
│   │   ├── src/main/java/model/      # Usuario, Cancha, Reserva, Actividad, etc.
│   │   └── src/main/java/usecase/    # RegistrarUsuario, CrearReserva, etc.
│   └── Adapter/               # Adaptadores de entrada y salida (Spring Boot, REST, JPA)
│       ├── src/main/java/.../controller/  # Endpoints REST (Auth, Canchas, Reservas)
│       ├── src/main/java/.../config/      # Seguridad Spring Security, JWT, CORS, OAuth2
│       └── src/main/java/.../entity/data/ # Entidades JPA (UsuarioData, ComplejoData)
└── frontend/                  # Single Page Application (Angular 18, TypeScript, Tailwind CSS)
    └── src/app/features/      # Módulos SPA (auth, perfil, complejos, reservas, actividades)
```

---

## Stack Tecnológico

* **Backend:** Java 21 (LTS), Spring Boot 3.3.x, Spring Security 6, Spring Data JPA, Maven.
* **Autenticación y Seguridad:** JSON Web Tokens (JWT - RFC 7519), Google OAuth 2.0 (RFC 6749), Criptografía BCrypt ($2^{12}$ iteraciones).
* **Base de Datos:** PostgreSQL 16 (Motor relacional ACID, restricciones de exclusión para no-solapamiento de turnos, soporte de datos semiestructurados con JSONB).
* **Frontend:** Angular 18 (SPA), TypeScript, Tailwind CSS, RxJS, HTML5.
* **Metodología de Desarrollo:** OpenUP (Open Unified Process).

---

## Puesta en Marcha en Entorno Local

### 1. Requisitos Previos Instalados
* **Java Development Kit (JDK):** Versión 21 o superior.
* **Apache Maven:** Versión 3.9 o superior.
* **Node.js & npm:** Node.js v18+ y npm v9+.
* **Angular CLI:** `npm install -g @angular/cli@18`.
* **PostgreSQL:** Versión 15 o 16 en ejecución en el puerto `5432`.

---

### 2. Configuración de Base de Datos
Acceder a PostgreSQL (vía psql o pgAdmin) y crear la base de datos:
```sql
CREATE DATABASE activachilecito;
```

Las credenciales por defecto configuradas en `backend/Adapter/src/main/resources/application.properties` son:
* URL: `jdbc:postgresql://localhost:5432/activachilecito`
* Usuario: `postgres`
* Contraseña: `password` *(ajustar según su entorno local)*

---

### 3. Ejecución del Backend (Spring Boot)

Desde la raíz del repositorio, abrir una terminal y ejecutar:

```bash
cd backend/Adapter
mvn clean install
mvn spring-boot:run
```
El servidor backend iniciará en el puerto `8080`:  
* **API Base URL:** `http://localhost:8080`

---

### 4. Ejecución del Frontend (Angular)

En otra terminal, desde la raíz del repositorio:

```bash
cd frontend
npm install
npm start
```
La aplicación cliente se compilará y quedará accesible en:  
* **Frontend URL:** `http://localhost:4200`

---

## Pruebas Automatizadas

Para ejecutar las pruebas unitarias y de integración de los casos de uso:
```bash
cd backend/Core
mvn test
```

---

## Licencia y Derechos Académicos

Este proyecto ha sido desarrollado con fines académicos en el marco del **Trabajo Final de Grado** para la obtención de los títulos de **Ingeniera en Sistemas** y **Licenciado en Sistemas** en la **Universidad Nacional de Chilecito (UNdeC)**.
