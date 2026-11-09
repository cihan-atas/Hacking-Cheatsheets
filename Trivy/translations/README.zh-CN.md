<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 琐碎备忘单

Aqua Security 的 [Trivy](https://github.com/aquasecurity/trivy) 是一款全面的多用途安全扫描器，适用于容器、文件系统、Git 存储库、虚拟机映像、Kubernetes 集群和基础设施即代码 (IaC) 配置。它可以检测操作系统和语言漏洞 (CVE)、泄露的机密、错误配置，并生成软件物料清单 (SBOM)。

---

## 目录

- [核心能力](#核心能力)
- [安装](#安装)
- [容器图像扫描(`trivy image`)](#container-image-scanning-trivy-image)
- [文件系统和存储库扫描 (`trivy fs / repo`)](#filesystem--repository-scanning-trivy-fs--repo)
- [秘密和凭证检测 (`--scanners secret`)](#secret--凭证-检测---扫描仪-秘密)
- [Kubernetes 集群审计 (`trivy k8s`)](#kubernetes-cluster-auditing-trivy-k8s)
- [基础设施即代码 (IaC) 错误配置扫描](#infrastructure-as-code-iac-misconfiguration-scanning)
- [CI/CD 集成和退出代码](#cicd-integration--exit-codes)
- [SBOM 生成（CycloneDX 和 SPDX）](#sbom- Generation-cycledx--spdx)

---

## 核心能力

- **漏洞：** 检测 Alpine、Debian、RHEL、Ubuntu、Amazon Linux 和语言包（Go、Python、Node.js、Java、Rust、Ruby、PHP、.NET）中的 CVE。
- **错误配置：** 根据 CIS 基准审核 Dockerfile、Kubernetes 清单、Terraform、CloudFormation 和 Helm 图表。
- **秘密扫描：** 标记暴露的 API 密钥、私有证书、JWT 令牌和密码。
- **集群扫描：** 实时审核 Kubernetes 工作负载和控制平面。
- **快速且轻量级：** 本地 SQLite 数据库透明更新，无需外部守护程序要求。

 - -

＃＃ 安装
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

## 容器镜像扫描（`trivy image`）

### 1. 基本图像扫描
扫描本地或远程 Docker 镜像：
```bash
trivy image nginx:latest
```
### 2. 按严重级别过滤
仅关注可操作的严重和高漏洞：
```bash
trivy image --severity HIGH,CRITICAL python:3.9-slim
```
### 3. 仅显示可修复的漏洞
过滤掉未修补的零日或无法修复的上游问题：
```bash
trivy image --ignore-unfixed --severity HIGH,CRITICAL node:18-alpine
```
### 4. 扫描存储的 Tar 存档
扫描通过`docker save`导出的离线容器镜像：
```bash
docker save myapp:v1 -o myapp.tar
trivy image --input myapp.tar
```
---

## 文件系统和存储库扫描 (`trivy fs / repo`)

扫描源代码树、依赖锁文件（`package-lock.json`、`poetry.lock`、`go.sum`、`pom.xml`）：

### 1.扫描本地目录/项目
```bash
trivy fs /path/to/project
```
### 2.直接扫描远程Git仓库
```bash
trivy repo https://github.com/example/vulnerable-app
```
---

## 秘密和凭证检测 (`--scanners secret`)

检测嵌入在代码或容器层中的硬编码 API 密钥、令牌和私钥：
```bash
# Scan directory for secrets
trivy fs --scanners secret .

# Scan container image for secrets and vulnerabilities simultaneously
trivy image --scanners vuln,secret mycompany/api:latest
```
---

## Kubernetes 集群审计 (`trivy k8s`)

使用活动 `kubectl` 上下文审核实时 Kubernetes 集群中的活动工作负载和配置：

### 1. 审核整个集群工作负载
```bash
trivy k8s --report summary cluster
```
### 2. 审核特定命名空间
```bash
trivy k8s --namespace production --severity HIGH,CRITICAL all
```
### 3. CIS Kubernetes 基准合规性
```bash
trivy k8s --compliance k8s-cis cluster
```
---

## 基础设施即代码 (IaC) 错误配置扫描

检测 Dockerfiles、Terraform 和 Kubernetes YAML 中的有风险配置：
```bash
# Scan Terraform files for cloud security violations
trivy config ./terraform/

# Scan Dockerfile for root user or insecure exposed ports
trivy config Dockerfile
```
---

## CI/CD 集成和退出代码

自动化管道门以阻止运输易受攻击的构建：
```bash
# Break build (Exit code 1) if CRITICAL vulnerabilities exist
trivy image --exit-code 1 --severity CRITICAL myapp:build-42

# Generate SARIF report for GitHub Code Scanning
trivy image --format sarif --output trivy-results.sarif myapp:latest
```
---

## SBOM 生成（CycloneDX 和 SPDX）

生成软件物料清单 (SBOM) 以进行合规性和供应链审核：
```bash
# Generate CycloneDX JSON SBOM
trivy image --format cyclonedx --output sbom.json myapp:latest

# Generate SPDX JSON SBOM
trivy image --format spdx-json --output sbom-spdx.json myapp:latest
```