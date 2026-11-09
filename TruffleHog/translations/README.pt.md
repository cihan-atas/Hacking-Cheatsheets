<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle Security">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="Código de Segurança">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licença AGPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Scanner secreto de alta precisão projetado para detectar chaves de API, tokens e certificados privados vazados em Git, S3, Docker e sistemas de arquivos com validação em tempo real</b>
</p>

---

## 📋 Índice

- [Visão geral e o superpoder da verificação ao vivo](#-overview--the-live-verification-superpower)
- [Comparação TruffleHog vs Gitleaks](#-trufflehog-vs-gitleaks-comparação)
- [Instalação](#-instalação)
- [Verificando repositórios Git](#-scanning-git-repositories)
- [Verificação de sistemas de arquivos e diretórios locais](#-scanning-local-filesystems--directories)
- [Verificando organizações inteiras do GitHub](#-scanning-entire-github-organizations)
- [Verificação de buckets S3 e imagens Docker](#-scanning-s3-buckets--docker-images)
- [Filtrando falsos positivos (`--only-verified`)](#-filtrando-falsos-positivos---apenas verificado)
- [Saída JSON e integração CI/CD](#-json-output--cicd-integration)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o superpoder da verificação ao vivo

Os desenvolvedores frequentemente comprometem credenciais de alto privilégio em repositórios Git (chaves de acesso AWS, chaves privadas Stripe, webhooks Slack, chaves privadas SSH, senhas de banco de dados). Mesmo quando excluídos em commits subsequentes, **esses segredos permanecem permanentemente enterrados no histórico de commits do repositório**.

Escrito em Go por **Truffle Security**, **TruffleHog** verifica históricos de commits, sistemas de arquivos e armazenamento em nuvem para descobrir segredos vazados.

Seu superpoder definidor é **Verificação ao vivo**:
Ao contrário dos scanners regex tradicionais que inundam as equipes de segurança com milhares de falsos positivos (por exemplo, exemplos de chaves de documentação), o TruffleHog entra em contato com o respectivo provedor de nuvem (AWS, GitHub, Slack, OpenAI) para **verificar se o segredo extraído está ativamente funcional**.

---

## ⚖️ Comparação de TruffleHog vs Gileaks

| Recurso | TrufaHog | Vazamentos |
|--------|------------|----------|
| **Verificação de credenciais ao vivo** | ✅ Sim (mais de 750 detectores testados ao vivo)| ❌ Não (somente Regex) |
| **Fontes de dados alvo** | Git, GitHub, S3, Docker, sistema de arquivos | Git, arquivos locais |
| **Velocidade em grandes repositórios** | Rápido (seja multithread) | Extremamente rápido (regex mínimo) |
| **Taxa de falsos positivos** | Perto de zero (com `--only-verified`) | Moderado (requer listas de permissões)|
| **Pré-commit Hook amigável** | ✅ Suportado | ✅ Altamente Otimizado |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux (via curl installer)
curl -sSfL https://raw.githubusercontent.com/trufflesecurity/trufflehog/main/scripts/install.sh | sudo sh -s -- -b /usr/local/bin

# Arch Linux (AUR)
yay -S trufflehog-bin

# Homebrew (macOS / Linux)
brew install trufflehog

# Docker Container
docker run -it --rm trufflesecurity/trufflehog:latest git https://github.com/target/repo
```
---

## 🚀 Verificando repositórios Git
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 Verificando sistemas de arquivos e diretórios locais

Verifique diretórios locais, arquivos de backup extraídos ou raízes da web desindexadas:
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 Verificando organizações inteiras do GitHub

Audite todos os repositórios públicos pertencentes a uma organização:
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ Digitalizando buckets S3 e imagens Docker
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 Filtrando falsos positivos (`--only-verified`)

Suprima todos os hits de regex não verificados e exiba **apenas credenciais ativas confirmadas**:
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 Saída JSON e integração CI/CD
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificar repositório Git remoto** | `trufflehog git <GIT_URL>` |
| **Verifique apenas segredos verificados** | `trufflehog git <GIT_URL> --only-verified` |
| **Verificar sistema de arquivos local** | `trufflehog filesystem <DIRECTORY>` |
| **Verifique a organização do GitHub** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **Verificar imagem do Docker** | `trufflehog docker --image=<IMAGE>` |
| **Escanear balde S3** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **Exportar para JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Tráfego de verificação de API de saída:** Ao executar sem `--no-verification`, o TruffleHog se conecta a endpoints de terceiros (Slack, AWS, Stripe) para testar chaves. Isso aciona logs de autenticação no locatário da nuvem da vítima! Se a furtividade for fundamental, desative a validação ao vivo:
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **Revogação de chave e resposta a incidentes:** quando segredos válidos são descobertos em repositórios públicos, alterne imediatamente as credenciais e verifique o CloudTrail/registros de auditoria para acesso não autorizado.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas destina-se a auditorias de segurança autorizadas, operações do Red Team e avaliações de higiene de credenciais. Utilizar credenciais descobertas para acessar contas não autorizadas ou ambientes de nuvem privada é estritamente ilegal.