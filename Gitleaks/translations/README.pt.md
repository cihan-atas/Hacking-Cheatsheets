<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gileaks - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="Segredos">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p alinhar="centro">
  <b>🔑 Ferramenta SAST projetada para encontrar e detectar segredos codificados como senhas, chaves de API e tokens em repositórios Git e históricos de commits</b>
</p>

---

## 📋 Índice

- [Visão geral e mecanismo de detecção](#-overview--detection-engine)
- [Instalação](#-instalação)
- [Verificação básica de segredos](#-basic-secret-scanning)
  - [Verificando um repositório Git](#scanning-a-git-repository)
  - [Verificando alterações não confirmadas](#scanning-uncommitted-changes)
  - [Verificação de arquivos e pastas simples](#scanning-plain-files--folders)
- [Intervalos e histórico de confirmação de segmentação](#-targeting-commit-ranges--history)
- [CI/CD e integração pré-commit](#-cicd--pre-commit-integration)
- [Regras de detecção personalizadas e lista de permissões](#-custom-detection-rules--allowlisting)
- [Formatos de saída e relatórios](#-formatos de saída--relatórios)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Remediação](#-considerações-defensivas--remediação)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e mecanismo de detecção

Segredos codificados (chaves de acesso AWS, chaves privadas SSH, tokens GitHub, webhooks Slack, credenciais de banco de dados) comprometidos inadvertidamente em repositórios git são um dos vetores mais comuns para comprometimento inicial da nuvem.

Criado por **Zachary Rice (@zricethezav)**, **Gitleaks** fornece análise ultrarrápida de entropia de Shannon e baseada em regex em:
- **Histórico completo do Git:** Audita cada branch, tag e commit histórico.
- **Ganchos de pré-confirmação:** Impede que os engenheiros comprometam credenciais acidentalmente antes que o código chegue aos servidores remotos.
- **Pipelines de CI/CD:** falha automaticamente em solicitações pull contendo vazamentos de credenciais.

---

## 📥 Instalação
```bash
# macOS (Homebrew)
brew install gitleaks

# Go install
go install github.com/zricethezav/gitleaks/v8@latest

# Docker
docker pull zricethezav/gitleaks:latest

# Linux binary download
curl -sSL https://github.com/zricethezav/gitleaks/releases/latest/download/gitleaks_linux_x64.tar.gz | tar -xz
sudo mv gitleaks /usr/local/bin/
```
---

## 🚀 Verificação secreta básica

### Verificando um repositório Git
Audita o histórico de commits do repositório atual:
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### Verificando alterações não confirmadas
Audita alterações de código preparadas e não preparadas antes de confirmar:
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### Verificando arquivos e pastas simples
Verifique diretórios não-git (por exemplo, firmware extraído, aplicativos móveis descompilados, pastas de configuração):
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ Intervalos e histórico de comprometimento de segmentação
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ Integração CI/CD e pré-commit

### Configuração da estrutura de pré-confirmação (`.pre-commit-config.yaml`)
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### Fluxo de trabalho de ações do GitHub
```yaml
name: Gitleaks Security Scan
on: [push, pull_request]
jobs:
  gitleaks:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0
      - uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```
---

## 🧩 Regras de detecção personalizada e lista de permissões

Configure padrões regex personalizados ou ignore falsos positivos usando `.gitleaks.toml`:
```toml
[extend]
useDefault = true

[allowlist]
description = "Ignore test fixtures and demo tokens"
paths = [
  '''tests/.*''',
  '''fixtures/.*'''
]
regexes = [
  '''TEST_API_KEY_.*'''
]

[[rules]]
id = "custom-internal-token"
description = "Internal Company JWT Secret"
regex = '''(?i)(corp_secret_[a-z0-9]{32})'''
secretGroup = 1
entropy = 3.5
keywords = ["corp_secret_"]
```
Execute com configuração personalizada:
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 Formatos de saída e relatórios
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificar todo o histórico do Git** | `gitleaks detect -v` |
| **Verificar alterações não confirmadas** | `gitleaks protect -v` |
| **Verificar apenas alterações preparadas** | `gitleaks protect --staged -v` |
| **Verificar diretório simples (sem Git)**| `gitleaks dir /path/to/folder -v` |
| **Exportar para JSON** | `gitleaks detect -r report.json -f json` |
| **Exportar para SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **Usar configuração de regras personalizadas** | `gitleaks detect -c .gitleaks.toml` |
| **Verificar filial específica** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ Considerações defensivas e remediação

1. **Revogação Imediata:** Assim que um segredo for detectado no histórico do git, considere-o totalmente comprometido e gire/revogue-o imediatamente.
2. **Reescrevendo o histórico do Git:** Remover o commit ou usar o `git filter-repo` / BFG Repo-Cleaner é necessário para limpar artefatos, mas nunca substitua reescritas de histórico pela rotação de credenciais.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Gileaks foi projetado para auditorias de segurança internas autorizadas, verificação contínua de vulnerabilidades de integração e revisão defensiva de código. A varredura de repositórios proprietários não autorizados sem permissão é estritamente proibida.