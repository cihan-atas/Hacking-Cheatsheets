<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Trivy Cheatsheet

[Trivy](https://github.com/aquasecurity/trivy) av Aqua Security er en omfattende sikkerhetsskanner for flere formål for containere, filsystemer, Git-repositories, virtuelle maskinbilder, Kubernetes-klynger og IaC-konfigurasjoner (Infrastructure as Code). Den oppdager OS- og språksårbarheter (CVE), lekkede hemmeligheter, feilkonfigurasjoner og genererer Software Bill of Materials (SBOM).

---

## Innholdsfortegnelse

- [Kjerneegenskaper](#kjernefunksjoner)
- [Installasjon](#installation)
- [Container Image Scanning (`trivy image`)](#container-image-scanning-trivy-image)
- [Filesystem & Repository Scanning (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Hemmelighets- og legitimasjonsdeteksjon (`--scanners secret`)](#hemmelig--legitimasjonsdeteksjon---skannere-hemmelig)
- [Kubernetes Cluster Auditing (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Infrastructure as Code (IaC) Misconfiguration Scanning](#infrastructure-as-code-iac-misconfiguration-scanning)
- [CI/CD-integrasjon og utgangskoder](#cicd-integrasjon--utgangskoder)
- [SBOM Generation (CycloneDX & SPDX)](#sbom-generation-cyclonedx--spdx)

---

## Kjernefunksjoner

- **Sårbarheter:** Oppdager CVE-er på tvers av Alpine, Debian, RHEL, Ubuntu, Amazon Linux og språkpakker (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Feilkonfigurasjoner:** Reviderer Dockerfiler, Kubernetes-manifester, Terraform-, CloudFormation- og Helm-diagrammer mot CIS-benchmarks.
- **Hemmelig skanning:** flagger utsatte API-nøkler, private sertifikater, JWT-tokens og passord.
- **Klyngeskanning:** Overvåker Kubernetes-arbeidsbelastninger og kontrollplaner live.
- **Rask og lett:** Lokal SQLite-database oppdatert transparent uten eksterne demonkrav.

---

## Installasjon
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

## Beholderbildeskanning (`trivy image`)

### 1. Grunnleggende bildeskanning
Skann et lokalt eller eksternt Docker-bilde:
```bash
trivy image nginx:latest
```
### 2. Filtrer etter alvorlighetsgrad
Fokuser kun på handlingsbare kritiske og høye sårbarheter:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Vis bare løsebare sårbarheter
Filtrer ut uopprettede nulldager eller ufiksbare oppstrømsproblemer:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Skann lagret tjærearkiv
Skann offline beholderbilder eksportert via `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Filsystem- og arkivskanning (`trivy fs / repo`)

Skann kildekodetrær, avhengighetslåsefiler (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Skann lokal katalog / prosjekt
```bash
trivy fs /path/to/project
```
### 2. Skann Remote Git Repository direkte
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Deteksjon av hemmelig og legitimasjon (`--scanners secret`)

Oppdag hardkodede API-nøkler, tokens og private nøkler innebygd i kode- eller beholderlag:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Kubernetes Cluster Auditing (`trivy k8s`)

Overvåk aktive arbeidsbelastninger og konfigurasjon i en live Kubernetes-klynge ved å bruke din aktive `kubectl`-kontekst:

### 1. Revider hele klyngearbeidsbelastninger
```bash
trivy k8s --report summary cluster
```
### 2. Revider et spesifikt navneområde
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. CIS Kubernetes Benchmark Compliance
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Infrastructure as Code (IaC) feilkonfigurasjonsskanning

Oppdag risikofylte konfigurasjoner i Dockerfiles, Terraform og Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## CI/CD-integrasjon og utgangskoder

Automatiser rørledningsporter for å blokkere sårbare bygg fra frakt:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## SBOM Generation (CycloneDX & SPDX)

Generer Software Bill of Materials (SBOM) for overholdelse og revisjon av forsyningskjeden:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```