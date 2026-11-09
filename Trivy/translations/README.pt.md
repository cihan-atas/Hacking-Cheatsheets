<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do Trivy

[Trivy](https://github.com/aquasecurity/trivy) da Aqua Security é um scanner de segurança abrangente e multifuncional para contêineres, sistemas de arquivos, repositórios Git, imagens de máquinas virtuais, clusters Kubernetes e configurações de infraestrutura como código (IaC). Ele detecta vulnerabilidades de sistema operacional e de linguagem (CVEs), segredos vazados, configurações incorretas e gera lista de materiais de software (SBOM).

---

## Índice

- [Capacidades principais](#capacidades principais)
- [Instalação](#instalação)
- [Digitalização de imagem de contêiner (`trivy image`)](#container-image-scanning-trivy-image)
- [Verificação de sistema de arquivos e repositório (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [Detecção de segredos e credenciais (`--scanners secret`)](#secret--detecção de credenciais---scanners-secret)
- [Auditoria de cluster Kubernetes (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [Verificação de configuração incorreta de infraestrutura como código (IaC)](#infraestrutura-as-código-iac-misconfiguration-scanning)
- [Integração CI/CD e códigos de saída](#cicd-integration--exit-codes)
- [Geração SBOM (CycloneDX e SPDX)](#sbom-generação-cyclonedx--spdx)

---

## Capacidades principais

- **Vulnerabilidades:** Detecta CVEs em Alpine, Debian, RHEL, Ubuntu, Amazon Linux e pacotes de idiomas (Go, Python, Node.js, Java, Rust, Ruby, PHP, .NET).
- **Configurações incorretas:** Audita Dockerfiles, manifestos do Kubernetes, Terraform, CloudFormation e gráficos Helm em relação aos benchmarks CIS.
- **Verificação secreta:** sinaliza chaves de API expostas, certificados privados, tokens JWT e senhas.
- **Verificação de cluster:** audita cargas de trabalho e planos de controle do Kubernetes em tempo real.
- **Rápido e leve:** Banco de dados SQLite local atualizado de forma transparente, sem requisitos de daemons externos.

---

## Instalação
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

## Digitalização de imagem de contêiner (`trivy image`)

### 1. Digitalização básica de imagens
Digitalize uma imagem Docker local ou remota:
```bash
trivy image nginx:latest
```
### 2. Filtrar por nível de gravidade
Concentre-se apenas nas vulnerabilidades Críticas e Altas acionáveis:
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. Exibir apenas vulnerabilidades corrigíveis
Filtre problemas de dia zero não corrigidos ou problemas de upstream que não podem ser corrigidos:
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. Digitalize o arquivo Tar armazenado
Digitalize imagens de contêiner off-line exportadas via `docker save`:
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## Verificação de sistema de arquivos e repositório (`trivy fs / repo`)

Digitalize árvores de código-fonte, arquivos de bloqueio de dependência (`package-lock.json`, `poetry.lock`, `go.sum`, `pom.xml`):

### 1. Digitalize o diretório/projeto local
```bash
trivy fs /path/to/project
```
### 2. Digitalize o repositório Git remoto diretamente
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## Detecção de segredos e credenciais (`--scanners secret`)

Detecte chaves de API, tokens e chaves privadas codificadas incorporadas em camadas de código ou contêiner:
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Auditoria de cluster Kubernetes (`trivy k8s`)

Audite cargas de trabalho ativas e configuração em um cluster Kubernetes ativo usando seu contexto `kubectl` ativo:

### 1. Auditar cargas de trabalho inteiras do cluster
```bash
trivy k8s --report summary cluster
```
### 2. Audite um namespace específico
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. Conformidade de referência do CIS Kubernetes
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## Verificação de configuração incorreta de infraestrutura como código (IaC)

Detecte configurações arriscadas em Dockerfiles, Terraform e Kubernetes YAML:
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## Integração CI/CD e códigos de saída

Automatize as portas do pipeline para bloquear o envio de compilações vulneráveis:
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## Geração SBOM (CycloneDX e SPDX)

Gere lista de materiais de software (SBOM) para auditoria de conformidade e cadeia de suprimentos:
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```