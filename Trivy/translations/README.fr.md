<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire Trivy

[Trivy](https://github.com/aquasecurity/trivy) d'Aqua Security est un scanner de sécurité complet et polyvalent pour les conteneurs, les systèmes de fichiers, les référentiels Git, les images de machines virtuelles, les clusters Kubernetes et les configurations Infrastructure as Code (IaC). Il détecte les vulnérabilités du système d'exploitation et du langage (CVE), les fuites de secrets, les erreurs de configuration et génère une nomenclature logicielle (SBOM).

---

## Table des matières

- [Capacités de base](#core-capabilities)
- [Installation](#installation)
- [Numérisation d'image de conteneur (`trivy image`)](#container-image-scanning-trivy-image)
- [Analyse du système de fichiers et du référentiel (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Détection des secrets et des informations d'identification (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Audit de cluster Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Analyse des erreurs de configuration de l'Infrastructure as Code (IaC)](#infrastructure-as-code-iac-misconfiguration-scanning)
- [Codes d'intégration et de sortie CI/CD](#cicd-integration--exit-codes)
- [Génération SBOM (CycloneDX & SPDX)](#sbom-genération-cyclonedx--spdx)

---

## Capacités de base

- **Vulnérabilités :** Détecte les CVE sur Alpine, Debian, RHEL, Ubuntu, Amazon Linux et les packages de langues (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Mauvaises configurations :** Audite les fichiers Dockerfile, les manifestes Kubernetes, Terraform, CloudFormation et les graphiques Helm par rapport aux benchmarks CIS.
- **Analyse secrète :** Signale les clés API, les certificats privés, les jetons JWT et les mots de passe exposés.
- **Analyse de cluster :** Audite les charges de travail et les plans de contrôle Kubernetes en direct.
- **Rapide et léger :** Base de données SQLite locale mise à jour de manière transparente sans exigences de démon externe.

---

##Installation
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

## Analyse d'image de conteneur (`trivy image`)

### 1. Numérisation d'image de base
Scanner une image Docker locale ou distante :
```bash
trivy image nginx:latest
```
### 2. Filtrer par niveau de gravité
Concentrez-vous uniquement sur les vulnérabilités critiques et élevées exploitables :
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Afficher uniquement les vulnérabilités corrigibles
Filtrez les problèmes Zero Day non corrigés ou les problèmes en amont non réparables :
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Analyser les archives Tar stockées
Analysez les images de conteneurs hors ligne exportées via `docker save` :
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Analyse du système de fichiers et du référentiel (`trivy fs / repo`)

Analyser les arborescences de code source, les fichiers de verrouillage de dépendance (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`) :

### 1. Analyser le répertoire/projet local
```bash
trivy fs /path/to/project
```
### 2. Analyser directement le référentiel Git distant
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Détection des secrets et des informations d'identification (`--scanners secret`)

Détectez les clés API, les jetons et les clés privées codés en dur intégrés dans les couches de code ou de conteneur :
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Audit de cluster Kubernetes (`trivy k8s`)

Auditez les charges de travail actives et la configuration dans un cluster Kubernetes en direct à l'aide de votre contexte `kubectl` actif :

### 1. Auditer l'intégralité des charges de travail du cluster
```bash
trivy k8s --report summary cluster
```
### 2. Auditer un espace de noms spécifique
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Conformité au benchmark CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Analyse des erreurs de configuration de l'Infrastructure as Code (IaC)

Détectez les configurations à risque dans Dockerfiles, Terraform et Kubernetes YAML :
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Intégration CI/CD et codes de sortie

Automatisez les portes du pipeline pour empêcher l’expédition des versions vulnérables :
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## Génération SBOM (CycloneDX et SPDX)

Générez une nomenclature logicielle (SBOM) pour l'audit de conformité et de chaîne d'approvisionnement :
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```