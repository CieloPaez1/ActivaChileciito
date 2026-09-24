# Casos de Prueba de Seguridad - Módulo 1 (Activa Chilecito)

Este documento detalla las pruebas de seguridad de caja negra y validación de endpoints realizadas sobre el Módulo 1 (Seguridad y Autenticación) del sistema Activa Chilecito.

---

## 1. Pruebas de Autenticación (Login)

| ID Prueba | Descripción del Caso | Datos de Entrada | Resultado Esperado | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **SEC-001** | Inicio de sesión con credenciales válidas | Usuario y contraseña correctos | El servidor retorna código HTTP 200 OK y un token JWT válido en el cuerpo de la respuesta. | ✅ Éxito |
| **SEC-002** | Inicio de sesión con contraseña incorrecta | Usuario correcto, contraseña incorrecta | El servidor retorna HTTP 401 (Unauthorized) o 403 (Forbidden). No se emite token JWT. | ✅ Éxito |
| **SEC-003** | Inicio de sesión con usuario inexistente | Correo electrónico no registrado | El servidor retorna HTTP 404 (Not Found) o 401 (Unauthorized). | ✅ Éxito |
| **SEC-004** | Inyección SQL en formulario de Login | Usuario: `admin' OR '1'='1` | El servidor sanitiza la entrada mediante JPA/Hibernate y retorna HTTP 401. | ✅ Éxito |

---

## 2. Pruebas de Autorización y Protección de Rutas (JWT)

| ID Prueba | Descripción del Caso | Datos de Entrada | Resultado Esperado | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **SEC-005** | Acceso a endpoint protegido sin Token | Petición GET a `/api/usuarios` sin header `Authorization` | El filtro de seguridad (SecurityFilterChain) bloquea la petición. Retorna HTTP 403 (Forbidden) o 401 (Unauthorized). | ✅ Éxito |
| **SEC-006** | Acceso a endpoint protegido con Token inválido/expirado | Petición GET con token JWT manipulado o caducado | El servidor falla al validar la firma del token y retorna HTTP 401/403. Acceso denegado. | ✅ Éxito |
| **SEC-007** | Acceso a endpoint protegido con Token válido | Petición GET con token JWT correcto (Bearer Token) | El servidor valida el token, extrae el usuario del contexto y retorna HTTP 200 OK con los datos solicitados. | ✅ Éxito |

---

## 3. Pruebas de Registro y Gestión de Contraseñas

| ID Prueba | Descripción del Caso | Datos de Entrada | Resultado Esperado | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **SEC-008** | Registro con correo ya existente | POST a `/api/auth/registro` con email duplicado | El sistema detecta la duplicidad y retorna HTTP 409 (Conflict) o 400 (Bad Request). No se crea el usuario. | ✅ Éxito |
| **SEC-009** | Encriptación de contraseñas en Base de Datos | Inspección directa a la tabla `usuarios` en BD | Las contraseñas no se guardan en texto plano, sino que están hasheadas utilizando BCrypt (ej. formato `$2a$10$...`). | ✅ Éxito |
| **SEC-010** | Protección de contraseñas en la API | Solicitud GET de datos de perfil de usuario | El DTO de respuesta (UsuarioDto) omite el campo de la contraseña, evitando filtraciones en el Frontend. | ✅ Éxito |

---

## Conclusión de las Pruebas
El Módulo de Seguridad cumple con los estándares requeridos. Las rutas de la API están protegidas mediante **Spring Security y Tokens JWT (JSON Web Tokens)**. Las contraseñas están resguardadas mediante algoritmos de encriptación fuerte (**BCrypt**), previniendo vulnerabilidades críticas y garantizando el control de acceso adecuado.
