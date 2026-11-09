<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p align="centro">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Código de seguridad">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licencia AGPLv3">
</p>

<p align="centro">
  <b>🌐 Escáner secreto de alta precisión diseñado para buscar claves API, tokens y certificados privados filtrados en Git, S3, Docker y sistemas de archivos con validación en vivo</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y superpoder de la verificación en vivo](#-descripción general--el-superpoder-de-verificación-en vivo)
- [Comparación de TruffleHog vs Gitleaks] (#-comparación-trufflehog-vs-gitleaks)
- [Instalación](#-instalación)
- [Escaneando repositorios Git](#-scanning-git-repositories)
- [Escaneo de directorios y sistemas de archivos locales] (#-escaneo-sistemas-de-archivos-locales--directorios)
- [Escaneo de organizaciones GitHub completas] (#-escaneo-organizaciones-github-enteras)
- [Escaneo de depósitos S3 e imágenes de Docker] (#-scanning-s3-buckets--docker-images)
- [Filtrado de falsos positivos (`--only-verified`)](#-filtrado-falsos-positivos---solo-verificado)
- [Salida JSON e integración CI/CD](#-json-output--cicd-integration)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y la superpotencia de la verificación en vivo

Los desarrolladores frecuentemente ingresan credenciales con altos privilegios en repositorios de Git (claves de acceso de AWS, claves privadas de Stripe, webhooks de Slack, claves privadas de SSH, contraseñas de bases de datos). Incluso cuando se eliminan en confirmaciones posteriores, **estos secretos permanecen enterrados permanentemente en el historial de confirmaciones del repositorio**.

Escrito en Go por **Truffle Security**, **TruffleHog** analiza historiales de confirmación, sistemas de archivos y almacenamiento en la nube para descubrir secretos filtrados.

Su superpoder definitorio es la **Verificación en vivo**:
A diferencia de los escáneres de expresiones regulares tradicionales que inundan a los equipos de seguridad con miles de falsos positivos (por ejemplo, claves de documentación de ejemplo), TruffleHog contacta al proveedor de la nube respectivo (AWS, GitHub, Slack, OpenAI) para **verificar si el secreto extraído funciona activamente**.

---

## ⚖️ Comparación de TruffleHog y Gitleaks

| Característica | TrufaHog | fugas de git |
|---------|------------|----------|
| **Verificación de credenciales en vivo** | ✅ Sí (más de 750 detectores probados en vivo)| ❌ No (solo expresiones regulares) |
| **Fuentes de datos de destino** | Git, GitHub, S3, Docker, sistema de archivos| Git, archivos locales |
| **Velocidad en repositorios enormes** | Rápido (vaya a múltiples subprocesos) | Extremadamente rápido (expresión regular mínima) |
| **Tasa de falsos positivos** | Cerca de cero (con `--only-verified`) | Moderado (Requiere listas permitidas)|
| **Apto para ganchos previos a la confirmación** | ✅ Apoyado | ✅ Altamente optimizado |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```
---

## 🚀 Escaneando repositorios de Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Escaneo de directorios y sistemas de archivos locales

Escanee directorios locales, archivos de respaldo extraídos o raíces web desindexadas:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Escaneando organizaciones enteras de GitHub

Auditar todos los repositorios públicos pertenecientes a una organización:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Escaneo de imágenes de Docker y depósitos de S3
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Filtrado de falsos positivos (`--only-verified`)

Suprime todos los aciertos de expresiones regulares no verificadas y muestra **solo las credenciales activas confirmadas**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Salida JSON e integración CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear repositorio Git remoto** | `trufflehog git <GIT_URL>` |
| **Escanear secretos verificados únicamente** | `trufflehog git <GIT_URL> --only-verified` |
| **Escanear el sistema de archivos local** | `trufflehog filesystem <DIRECTORY>` |
| **Escanear organización GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Escanear imagen de Docker** | `trufflehog docker --image=<IMAGE>` |
| **Escanear depósito S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Exportar a JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Tráfico de verificación de API saliente:** Cuando se ejecuta sin `--no-verification`, TruffleHog se conecta a puntos finales de terceros (Slack, AWS, Stripe) para probar las claves. ¡Esto activa registros de autenticación en el inquilino de la nube de la víctima! Si el sigilo es primordial, deshabilite la validación en vivo:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Revocación de clave y respuesta a incidentes:** Cuando se descubren secretos válidos en repositorios públicos, rote inmediatamente las credenciales y verifique los registros de auditoría/CloudTrail para detectar accesos no autorizados.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está destinada a auditorías de seguridad autorizadas, operaciones del Red Team y evaluaciones de higiene de credenciales. Utilizar credenciales descubiertas para acceder a cuentas no autorizadas o entornos de nube privada es estrictamente ilegal.