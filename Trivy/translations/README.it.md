<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Trivy Cheatsheet

[Trivy](https://github.com/aquasecurity/trivy) di Aqua Security è uno scanner di sicurezza completo e multiuso per contenitori, file system, repository Git, immagini di macchine virtuali, cluster Kubernetes e configurazioni Infrastructure as Code (IaC). Rileva vulnerabilità del sistema operativo e del linguaggio (CVE), segreti trapelati, configurazioni errate e genera distinta base del software (SBOM).

---

## Sommario

- [Funzionalità principali](#capacità-core)
- [Installazione](#installazione)
- [Scansione dell'immagine del contenitore (`trivy image`)](#container-image-scanning-trivy-image)
- [Scansione file system e repository (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Rilevamento di segreti e credenziali (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Controllo del cluster Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Scansione errata configurazione Infrastructure as Code (IaC)](#infrastructure-as-code-iac-misconfiguration-scanning)
- [Integrazione CI/CD e codici di uscita](#cicd-integration--exit-codes)
- [Generazione SBOM (CycloneDX e SPDX)](#sbom-generazione-cyclonedx--spdx)

---

## Funzionalità principali

- **Vulnerabilità:** Rileva CVE su Alpine, Debian, RHEL, Ubuntu, Amazon Linux e pacchetti linguistici (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Errori di configurazione:** controlla Dockerfile, manifest Kubernetes, Terraform, CloudFormation e grafici Helm rispetto ai benchmark CIS.
- **Scansione segreta:** contrassegna chiavi API, certificati privati, token JWT e password esposti.
- **Scansione cluster:** controlla in tempo reale i carichi di lavoro e i piani di controllo Kubernetes.
- **Veloce e leggero:** Database SQLite locale aggiornato in modo trasparente senza requisiti di demoni esterni.

---

## Installazione
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

## Scansione immagine contenitore (`trivy image`)

### 1. Scansione di immagini di base
Scansiona un'immagine Docker locale o remota:
```bash
trivy image nginx:latest
```
### 2. Filtra per livello di gravità
Concentrarsi solo sulle vulnerabilità critiche e elevate su cui è possibile intervenire:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Visualizza solo le vulnerabilità risolvibili
Filtra gli zero-day senza patch o i problemi upstream non risolvibili:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Scansiona l'archivio Tar memorizzato
Scansiona le immagini del contenitore offline esportate tramite `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Scansione file system e repository (`trivy fs / repo`)

Scansione alberi del codice sorgente, file di blocco delle dipendenze (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Scansione della directory/progetto locale
```bash
trivy fs /path/to/project
```
### 2. Scansione diretta del repository Git remoto
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Rilevamento di segreti e credenziali (`--scanners secret`)

Rileva chiavi API hardcoded, token e chiavi private incorporate nel codice o nei livelli del contenitore:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Controllo del cluster Kubernetes (`trivy k8s`)

Controlla i carichi di lavoro attivi e la configurazione in un cluster Kubernetes live utilizzando il contesto `kubectl` attivo:

### 1. Controllare i carichi di lavoro dell'intero cluster
```bash
trivy k8s --report summary cluster
```
### 2. Controlla uno spazio dei nomi specifico
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Conformità al benchmark CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Scansione degli errori di configurazione dell'Infrastructure as Code (IaC).

Rileva configurazioni rischiose in Dockerfiles, Terraform e Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Integrazione CI/CD e codici di uscita

Automatizza i controlli della pipeline per impedire la spedizione delle build vulnerabili:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## Generazione SBOM (CycloneDX e SPDX)

Genera distinta base software (SBOM) per la conformità e il controllo della catena di fornitura:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```