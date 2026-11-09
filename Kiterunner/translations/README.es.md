<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p align="centro">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web y API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="Licencia Apache 2.0">
</p>

<p align="centro">
  <b>🌐 API REST y GraphQL de alto rendimiento enrutan fuerza bruta diseñada para mapear arquitecturas API modernas utilizando conjuntos de datos contextuales</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y problema de descubrimiento de API] (#-descripción general--el-problema-de-descubrimiento-de-api)
- [Comparación de herramientas: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Instalación](#-instalación)
- [Descarga de conjuntos de datos de Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [API de escaneo (`kr scan`)](#-scanning-apis-kr-scan)
- [Lista de palabras estándar de fuerza bruta (`kr brute`)](#-lista de palabras estándar-fuerza-bruta-kr-brute)
- [Encabezados, cookies y autenticación](#-encabezados-cookies--autenticación)
- [Filtrado y códigos de estado] (#-filtrado--códigos-de-estado)
- [Reproducción de rutas descubiertas (salida `curl`)](#-replaying-discovered-routes-curl-output)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y el problema de descubrimiento de API

Las herramientas tradicionales de descubrimiento de contenido web (`ffuf`, `dirsearch`, `gobuster`) utilizan diccionarios planos de una sola palabra (por ejemplo, `admin`, `api`, `login`). Si bien esto funciona para jerarquías de archivos tradicionales, **falla con las API REST y GraphQL modernas**.

Las API modernas utilizan rutas anidadas de varios niveles, como:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, creado por **Assetnote**, resuelve este problema compilando definiciones de esquemas, OpenAPI, Swagger, Postman y del mundo real en conjuntos de datos binarios compactos (`.kite`). En lugar de adivinar palabras aleatorias, Kiterunner reconstruye estructuras jerárquicas completas de llamadas API con métodos HTTP correctos (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Comparación de herramientas: Kiterunner vs ffuf vs dirsearch

| Característica | Corredor de cometas (`kr`) | ffuf | búsqueda de directorios |
|---------|------------------|--------------|-----------|
| **Enfoque objetivo principal** | DESCANSO, GraphQL, OpenAPI | Fuzzing web general | Directorio de fuerza bruta |
| **Conciencia de jerarquía de rutas**| ✅ Nativo (archivos `.kite`) | ❌ Lista plana de palabras | ❌ Lista plana de palabras |
| **Intercambio de verbos HTTP** | ✅ Pruebas OBTENER, PUBLICAR, PONER, BORRAR| ❌ Configuración manual | ❌ Limitado |
| **Análisis de Swagger/OpenAPI** | ✅ Ingestión de esquemas nativos | ❌ No | ❌ No |
| **Velocidad y simultaneidad** | ⭐⭐⭐⭐⭐ (Ir motor) | ⭐⭐⭐⭐⭐ (Ir motor) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Instalación
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Descarga de conjuntos de datos de Assetnote Kite

Kiterunner se basa en conjuntos de datos de rutas `.kite` prediseñados creados a partir de millones de API públicas analizadas:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 API de escaneo (`kr scan`)

El comando `scan` utiliza conjuntos de datos `.kite` para sondear objetivos mediante jerarquías de rutas contextuales:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Fuerza bruta de lista de palabras estándar (`kr brute`)

Si desea utilizar listas de palabras `.txt` estándar mientras aprovecha el motor API multimétodo de Kiterunner:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Encabezados, cookies y autenticación

Las API frecuentemente requieren JWT, tokens de portador o encabezados de organización personalizados:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filtrado y códigos de estado
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Reproducción de rutas descubiertas (salida `curl`)

Kiterunner puede generar rutas descubiertas como comandos `curl` procesables, lo que permite la reproducción instantánea en la terminal o Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **API de escaneo con conjunto de datos pequeño** | `kr scan <URL> -w routes-small.kite` |
| **API de escaneo con un gran conjunto de datos** | `kr scan <URL> -w routes-large.kite` |
| **Escanear varios hosts** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Lista de palabras de texto de fuerza bruta** | `kr brute <URL> -w wordlist.txt` |
| **Autenticarse con token de portador**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Excluir códigos de estado** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Salida a archivo JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Volumen de solicitudes masivo:** El diccionario `routes-large.kite` genera millones de solicitudes. Asegúrese de que la limitación de velocidad (`-q 200`) esté habilitada al probar entornos de producción.
2. **Métodos de cambio de estado:** Kiterunner envía solicitudes `POST`, `PUT` y `DELETE`. En las API de prueba no reforzadas, esto puede modificar o eliminar registros de backend sin darse cuenta.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Kiterunner está diseñado para pruebas de seguridad de API autorizadas y compromisos de recompensas por errores únicamente. Difuminar API no autorizadas es estrictamente ilegal.