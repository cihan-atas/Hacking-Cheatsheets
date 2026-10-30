# Trivy Cheatsheet

[Trivy](https://github.com/aquasecurity/trivy) by Aqua Security is a comprehensive, multi-purpose security scanner for containers, filesystems, Git repositories, virtual machine images, Kubernetes clusters, and Infrastructure as Code (IaC) configurations. It detects OS and language vulnerabilities (CVEs), leaked secrets, misconfigurations, and generates Software Bill of Materials (SBOM).

---

## Table of Contents

- [Core Capabilities](#core-capabilities)
- [Installation](#installation)
- [Container Image Scanning (`trivy image`)](#container-image-scanning-trivy-image)
- [Filesystem & Repository Scanning (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Secret & Credential Detection (`--scanners secret`)](#secret--credential-detection---scanners-secret)
- [Kubernetes Cluster Auditing (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Infrastructure as Code (IaC) Misconfiguration Scanning](#infrastructure-as-code-iac-misconfiguration-scanning)
- [CI/CD Integration & Exit Codes](#cicd-integration--exit-codes)
- [SBOM Generation (CycloneDX & SPDX)](#sbom-generation-cyclonedx--spdx)

---

## Core Capabilities

- **Vulnerabilities:** Detects CVEs across Alpine, Debian, RHEL, Ubuntu, Amazon Linux, and language packages (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Misconfigurations:** Audits Dockerfiles, Kubernetes manifests, Terraform, CloudFormation, and Helm charts against CIS Benchmarks.
- **Secret Scanning:** Flags exposed API keys, private certificates, JWT tokens, and passwords.
- **Cluster Scanning:** Audits Kubernetes workloads and control planes live.
- **Fast & Lightweight:** Local SQLite database updated transparently without external daemon requirements.

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

## Container Image Scanning (`trivy image`)

### 1. Basic Image Scan
Scan a local or remote Docker image:
```bash
trivy image nginx:latest
```

### 2. Filter by Severity Level
Focus on actionable Critical and High vulnerabilities only:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```

### 3. Display Only Fixable Vulnerabilities
Filter out unpatched zero-days or unfixable upstream issues:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```

### 4. Scan Stored Tar Archive
Scan offline container images exported via `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```

---

## Filesystem & Repository Scanning (`trivy fs / repo`)

Scan source code trees, dependency lockfiles (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Scan Local Directory / Project
```bash
trivy fs /path/to/project
```

### 2. Scan Remote Git Repository Directly
```bash
trivy repo https://github.com/example/vulnerable-app
```

---

## Secret & Credential Detection (`--scanners secret`)

Detect hardcoded API keys, tokens, and private keys embedded in code or container layers:

```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```

---

## Kubernetes Cluster Auditing (`trivy k8s`)

Audit active workloads and configuration in a live Kubernetes cluster using your active `kubectl` context:

### 1. Audit Entire Cluster Workloads
```bash
trivy k8s --report summary cluster
```

### 2. Audit a Specific Namespace
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```

### 3. CIS Kubernetes Benchmark Compliance
```bash
trivy k8s --compliance k8s-cis cluster
```

---

## Infrastructure as Code (IaC) Misconfiguration Scanning

Detect risky configurations in Dockerfiles, Terraform, and Kubernetes YAML:

```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```

---

## CI/CD Integration & Exit Codes

Automate pipeline gates to block vulnerable builds from shipping:

```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```

---

## SBOM Generation (CycloneDX & SPDX)

Generate Software Bill of Materials (SBOM) for compliance and supply-chain auditing:

```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```
