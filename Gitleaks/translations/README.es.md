<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p align="centro">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Secretos">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Ir">
</p>

<p align="centro">
  <b>🔑 Herramienta SAST diseñada para buscar y detectar secretos codificados como contraseñas, claves API y tokens en repositorios de Git e historiales de confirmación</b>
</p>

---

## 📋 Tabla de contenidos

- [Motor de descripción general y detección] (#-descripción general--motor-detección)
- [Instalación](#-instalación)
- [Escaneo secreto básico] (#-escaneo-secreto-básico)
  - [Escaneando un repositorio Git](#scanning-a-git-repository)
  - [Escaneando cambios no confirmados](#escaneo-cambios-no confirmados)
  - [Escaneo de archivos y carpetas sin formato](#escaneo-archivos-sin formato--carpetas)
- [Historial y rangos de compromiso de orientación](#-rangos-de-compromiso de orientación--historial)
- [CI/CD e integración previa a la confirmación](#-cicd--integración previa a la confirmación)
- [Reglas de detección personalizadas y listas de permitidos](#-reglas-de-detección-personalizadas--listas de permitidos)
- [Formatos de salida e informes](#-formatos-de-salida--informes)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y remediación](#-consideraciones-defensivas--remediación)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Motor de detección y descripción general

Los secretos codificados (claves de acceso de AWS, claves privadas SSH, tokens de GitHub, webhooks de Slack, credenciales de bases de datos) comprometidos inadvertidamente en repositorios de git son uno de los vectores más comunes para el compromiso inicial de la nube.

Creado por **Zachary Rice (@zricethezav)**, **Gitleaks** proporciona un análisis de entropía de Shannon y basado en expresiones regulares ultrarrápido en:
- **Historial completo de Git:** Audita cada rama, etiqueta y confirmación histórica.
- **Ganchos de confirmación previa:** impide que los ingenieros confirmen credenciales accidentalmente antes de que el código llegue a los servidores remotos.
- **Canalizaciones de CI/CD:** Falla automáticamente las solicitudes de extracción que contienen fugas de credenciales.

---

## 📥 Instalación
```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```
---

## 🚀 Escaneo secreto básico

### Escaneando un repositorio Git
Audita el historial de confirmaciones del repositorio actual:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Escaneando cambios no confirmados
Audita los cambios de código preparados y no preparados antes de confirmarlos:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Escaneo de archivos y carpetas simples
Escanee directorios que no sean de git (por ejemplo, firmware extraído, aplicaciones móviles descompiladas, carpetas de configuración):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Historial y rangos de compromiso de orientación
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD e integración previa a la confirmación

### Configuración del marco de confirmación previa (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Flujo de trabajo de acciones de GitHub
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```
---

## 🧩 Reglas de detección personalizadas y listas de permitidos

Configure patrones de expresiones regulares personalizados o ignore los falsos positivos usando `.gitleaks.toml`:
```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```
Ejecutar con configuración personalizada:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Formatos de salida e informes
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear todo el historial de Git** | `gitleaks detect -v` |
| **Escanear cambios no confirmados** | `gitleaks protect -v` |
| **Escanear cambios preparados únicamente** | `gitleaks protect --staged -v` |
| **Escanear directorio simple (sin Git)**| `gitleaks dir /path/to/folder -v` |
| **Exportar a JSON** | `gitleaks detect -r report.json -f json` |
| **Exportar a SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Usar configuración de reglas personalizadas** | `gitleaks detect -c .gitleaks.toml` |
| **Escanear rama específica** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Consideraciones defensivas y remediación

1. **Revocación inmediata:** Una vez que se detecta un secreto en el historial de git, considérelo completamente comprometido y gírelo/revoquelo inmediatamente.
2. **Reescribir el historial de Git:** Es necesario eliminar la confirmación o usar `git filter-repo` / BFG Repo-Cleaner para eliminar artefactos, pero nunca sustituir la rotación de credenciales por reescrituras del historial.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Gitleaks está diseñado para auditorías de seguridad internas autorizadas, verificación de vulnerabilidades de integración continua y revisión de código defensivo. Está estrictamente prohibido escanear repositorios propietarios no autorizados sin permiso.