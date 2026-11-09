<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p align="centro">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="centro">
  <b>🕸️ Marco de seguridad CLI avanzado y extensión Burp Suite desarrollados por Doyensec para volcar esquemas de introspección GraphQL, generar plantillas de consulta y auditar la seguridad de la API GraphQL</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y fundamentos de seguridad de GraphQL] (#-overview--graphql-security-fundamentals)
  - [Peligros de consulta de introspección] (#introspección-peligros-de-consulta)
- [Instalación](#-instalación)
- [Volcado de introspección y generación de esquemas](#-introspección-dumping--generación de esquemas)
  - [Consulta de introspección CLI](#cli-introspection-query)
  - [Volcado al lenguaje de definición de esquemas (SDL)](#dumping-to-schema-definition-language-sdl)
  - [Generando plantillas de consulta listas para ejecutar](#generando-plantillas-de-consulta-listas-para-ejecutar)
- [Auditoría de mutaciones y consultas sensibles](#-auditación-mutaciones--consultas-sensibles)
- [Detección de denegación de servicio y fallas en lotes](#-detección-denegación-de-servicio--fallas-en-lotes)
  - [Consulta por lotes (procesamiento por lotes de matrices)](#batch-querying-array-batching)
  - [Referencias circulares y consultas anidadas](#referencias-circulares--consultas-anidadas)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y endurecimiento](#-consideraciones-defensivas--endurecimiento)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y fundamentos de seguridad de GraphQL

A diferencia de las API REST estándar con URL discretas (`/api/v1/users`), GraphQL expone un único punto final HTTP (generalmente `/graphql`) donde los clientes solicitan formas de datos personalizadas mediante consultas y mutaciones.

### Peligros de las consultas de introspección
GraphQL presenta un sistema de introspección integrado (`__schema`, `__type`) diseñado para herramientas de desarrollador. Si se deja habilitado en producción:
- Los atacantes pueden consultar todo el esquema de backend.
- Las funciones inéditas, las mutaciones administrativas internas (por ejemplo, `deleteUser`, `changeRole`) y los campos privados están completamente expuestos.

Creado por **Doyensec**, **InQL** automatiza la extracción de introspección, analiza esquemas, genera documentación y crea plantillas de exploits listas para usar.

---

## 📥 Instalación
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 Volcado de introspección y generación de esquemas

### Consulta de introspección CLI
Consulta un punto final GraphQL remoto, extrae el esquema completo y guárdalo en una carpeta de salida:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Generado en el directorio de salida:*
- `schema.json`: volcado de introspección JSON sin formato.
- `schema.graphql`: Lenguaje de definición de esquemas (SDL) legible por humanos.
- `queries/`: plantillas de consulta generadas automáticamente para cada consulta disponible.
- `mutations/`: Plantillas de mutación generadas automáticamente para modificación de datos.

### Escaneo autenticado y encabezados personalizados
Pase cookies de sesión, tokens de autorización o encabezados personalizados:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Volcado al lenguaje de definición de esquemas (SDL)
Convierta el esquema JSON sin formato existente a SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Auditoría de mutaciones y consultas sensibles

Revise los archivos de mutación generados automáticamente en la carpeta de salida:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Envíe mutaciones de alto impacto usando curl o Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Detección de denegación de servicio y fallas en el procesamiento por lotes

### Consulta por lotes (procesamiento por lotes de matrices)
Pruebe si el servidor procesa conjuntos de consultas en una única solicitud HTTP (evite la limitación de velocidad):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Referencias circulares y consultas anidadas
Prueba de fallos de relaciones recursivas:
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Introspección de volcado** | `inql -t https://target.com/graphql -o ./out/` |
| **Introspección autenticada**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Convertir esquema JSON a SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Generar documentos HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Consideraciones defensivas y endurecimiento

1. **Deshabilite la introspección en producción:** Deshabilite `introspection` en la configuración de su servidor GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Aplicar análisis de costos y profundidad de consultas:** Implemente bibliotecas como `graphql-depth-limit` o análisis de costos de consultas para eliminar consultas profundamente anidadas.
3. **Deshabilitar procesamiento por lotes:** Limite o deshabilite la ejecución de consultas por lotes basadas en matrices si no es estrictamente necesario.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> InQL está desarrollado para evaluaciones de seguridad autorizadas, pruebas de penetración y auditorías de seguridad de API. Extraer esquemas o modificar datos en puntos finales no autorizados es ilegal.