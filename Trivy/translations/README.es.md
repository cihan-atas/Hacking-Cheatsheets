<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de trucos de Trivy

[Trivy](https://github.com/aquasecurity/trivy) de Aqua Security es un escáner de seguridad integral y multipropósito para contenedores, sistemas de archivos, repositorios Git, imágenes de máquinas virtuales, clústeres de Kubernetes y configuraciones de infraestructura como código (IaC). Detecta vulnerabilidades del sistema operativo y del lenguaje (CVE), secretos filtrados, configuraciones erróneas y genera una lista de materiales de software (SBOM).

---

## Tabla de contenidos

- [Capacidades básicas] (#capacidades básicas)
- [Instalación](#instalación)
- [Escaneo de imágenes de contenedor (`trivy image`)](#escaneo-de-imagen-de-contenedor-imagen-trivy)
- [Escaneo de repositorio y sistema de archivos (`trivy fs / repo`)](#sistema de archivos--escaneo-de-repositorio-trivy-fs--repo)
- [Detección de secretos y credenciales (`--scanners secret`)](#secret--detección-de-credenciales---escáneres-secreto)
- [Auditoría de clústeres de Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Escaneo de configuración incorrecta de infraestructura como código (IaC)] (#infraestructura-como-código-iac-malconfiguration-scanning)
- [Integración CI/CD y códigos de salida](#cicd-integration--exit-codes)
- [Generación SBOM (CycloneDX y SPDX)](#sbom-generación-cyclonedx--spdx)

---

## Capacidades principales

- **Vulnerabilidades:** Detecta CVE en Alpine, Debian, RHEL, Ubuntu, Amazon Linux y paquetes de idiomas (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Configuraciones erróneas:** Audita archivos Dockerfiles, manifiestos de Kubernetes, Terraform, CloudFormation y gráficos Helm con respecto a los puntos de referencia CIS.
- **Escaneo secreto:** Marca claves API expuestas, certificados privados, tokens JWT y contraseñas.
- **Escaneo de clústeres:** Audita las cargas de trabajo y los planos de control de Kubernetes en vivo.
- **Rápido y liviano:** Base de datos SQLite local actualizada de forma transparente sin requisitos de demonios externos.

---

## Instalación
```bash
# Debian / Ubuntu
sudo apt-get install wget apt-transport-https gnupg
wget -qO - https://aquasecurity.github.io/trivy-repo/deb/public.key | gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null
echo "deb [signed-by=/usr/share/keyrings/trivy.gpg] https://aquasecurity.github.io/trivy-repo/deb generic main" | sudo tee -a /etc/apt/sources.list.d/trivy.list
sudo apt-get update && sudo apt-get install -y trivy

# Arch Linux
sudo pacman -S trivy

# Homebrew (macOS / Linux)
brew install trivy

# Docker container
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image alpine:latest
```
---

## Escaneo de imágenes de contenedores (`trivy image`)

### 1. Escaneo de imágenes básico
Escanee una imagen de Docker local o remota:
```bash
trivy image nginx:latest
```
### 2. Filtrar por nivel de gravedad
Concéntrese únicamente en las vulnerabilidades críticas y altas procesables:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Mostrar solo vulnerabilidades reparables
Filtre los problemas de día cero sin parches o los problemas ascendentes que no se pueden solucionar:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Escanear el archivo Tar almacenado
Escanee imágenes de contenedores sin conexión exportadas a través de `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Escaneo de repositorios y sistemas de archivos (`trivy fs / repo`)

Escanear árboles de código fuente, archivos de bloqueo de dependencia (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Escanear directorio local/proyecto
```bash
trivy fs /path/to/project
```
### 2. Escanear el repositorio remoto de Git directamente
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Detección de secretos y credenciales (`--scanners secret`)

Detecte claves API codificadas, tokens y claves privadas integradas en código o capas de contenedor:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Auditoría del clúster de Kubernetes (`trivy k8s`)

Audite las cargas de trabajo activas y la configuración en un clúster de Kubernetes activo utilizando su contexto `kubectl` activo:

### 1. Auditar cargas de trabajo de todo el clúster
```bash
trivy k8s --report summary cluster
```
### 2. Auditar un espacio de nombres específico
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Cumplimiento del punto de referencia CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Escaneo de configuración incorrecta de infraestructura como código (IaC)

Detecte configuraciones riesgosas en Dockerfiles, Terraform y Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Integración CI/CD y códigos de salida

Automatice las puertas de los oleoductos para bloquear el envío de construcciones vulnerables:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## Generación SBOM (CycloneDX y SPDX)

Genere una lista de materiales de software (SBOM) para el cumplimiento y la auditoría de la cadena de suministro:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```