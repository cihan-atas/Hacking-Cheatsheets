<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Trivy Cheatsheet

[Trivy](https://github.com/aquasecurity/trivy) von Aqua Security ist ein umfassender, vielseitiger Sicherheitsscanner für Container, Dateisysteme, Git-Repositorys, Images virtueller Maschinen, Kubernetes-Cluster und Infrastructure as Code (IaC)-Konfigurationen. Es erkennt Betriebssystem- und Sprachschwachstellen (CVEs), durchgesickerte Geheimnisse und Fehlkonfigurationen und generiert eine Software-Bill of Materials (SBOM).

---

## Inhaltsverzeichnis

- [Kernfähigkeiten](#core-capabilities)
- [Installation](#installation)
- [Container-Image-Scanning (`trivy image`)](#container-image-scanning-trivy-image)
- [Dateisystem- und Repository-Scanning (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Geheimnis- und Anmeldeinformationserkennung (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Kubernetes Cluster Auditing (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Infrastructure as Code (IaC) Fehlkonfigurations-Scanning](#infrastructure-as-code-iac-misconfiguration-scanning)
- [CI/CD-Integration und Exit-Codes](#cicd-integration--exit-codes)
- [SBOM-Generierung (CycloneDX & SPDX)](#sbom-generation-cyclonedx--spdx)

---

## Kernkompetenzen

- **Schwachstellen:** Erkennt CVEs in Alpine, Debian, RHEL, Ubuntu, Amazon Linux und Sprachpaketen (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Fehlkonfigurationen:** Prüft Dockerfiles, Kubernetes-Manifeste, Terraform, CloudFormation und Helm-Charts anhand von CIS-Benchmarks.
- **Geheimes Scannen:** Kennzeichnet offengelegte API-Schlüssel, private Zertifikate, JWT-Tokens und Passwörter.
- **Cluster-Scanning:** Überprüft Kubernetes-Arbeitslasten und Steuerungsebenen live.
- **Schnell und leichtgewichtig:** Lokale SQLite-Datenbank transparent aktualisiert, ohne dass externe Daemon-Anforderungen erforderlich sind.

---

## Installation
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

## Container-Image-Scannen (`trivy image`)

### 1. Grundlegender Bildscan
Scannen Sie ein lokales oder Remote-Docker-Image:
```bash
trivy image nginx:latest
```
### 2. Filtern Sie nach Schweregrad
Konzentrieren Sie sich nur auf umsetzbare kritische und hohe Schwachstellen:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Nur behebbare Schwachstellen anzeigen
Filtern Sie ungepatchte Zero-Day-Probleme oder nicht behebbare Upstream-Probleme heraus:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Gespeichertes Tar-Archiv scannen
Über `docker save` exportierte Offline-Containerbilder scannen:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Dateisystem- und Repository-Scanning (`trivy fs / repo`)

Quellcodebäume und Abhängigkeitssperrdateien scannen (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Lokales Verzeichnis/Projekt scannen
```bash
trivy fs /path/to/project
```
### 2. Remote-Git-Repository direkt scannen
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Erkennung von Geheimnissen und Anmeldeinformationen (`--scanners secret`)

Erkennen Sie fest codierte API-Schlüssel, Token und private Schlüssel, die in Code- oder Containerschichten eingebettet sind:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Kubernetes-Clusterüberwachung (`trivy k8s`)

Überwachen Sie aktive Workloads und Konfigurationen in einem Live-Kubernetes-Cluster mithilfe Ihres aktiven `kubectl`-Kontexts:

### 1. Gesamte Cluster-Workloads prüfen
```bash
trivy k8s --report summary cluster
```
### 2. Prüfen Sie einen bestimmten Namespace
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. CIS Kubernetes Benchmark-Konformität
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Infrastructure as Code (IaC)-Fehlkonfigurationsscan

Erkennen Sie riskante Konfigurationen in Dockerfiles, Terraform und Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## CI/CD-Integration und Exit-Codes

Automatisieren Sie Pipeline-Gates, um den Versand anfälliger Builds zu verhindern:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## SBOM-Generierung (CycloneDX & SPDX)

Generieren Sie Software Bill of Materials (SBOM) für Compliance und Lieferkettenprüfung:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```