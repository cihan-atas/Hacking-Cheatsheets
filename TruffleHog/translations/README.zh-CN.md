<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐽 TruffleHog - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ████████╗██████╗ ██╗   ██╗███████╗███████╗██╗     ███████╗██╗  ██╗ ██████╗  ██████╗ 
  ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██╔════╝██║     ██╔════╝██║  ██║██╔═══██╗██╔════╝ 
     ██║   ██████╔╝██║   ██║█████╗  █████╗  ██║     █████╗  ███████║██║   ██║██║  ███╗
     ██║   ██╔══██╗██║   ██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██║██║   ██║██║   ██║
     ██║   ██║  ██║╚██████╔╝██║     ██║     ███████╗███████╗██║  ██║╚██████╔╝╚██████╔╝
     ╚═╝   ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝  ╚═════╝ 
       Find Exposed Credentials & Secrets with Real-Time Active Verification
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/TruffleHog-Secret_Scanner-blue?style=for-the-badge" alt="TruffleHog">
  <img src="https://img.shields.io/badge/Developer-Truffle_Security-orange?style=for-the-badge" alt="Truffle 安全">
  <img src="https://img.shields.io/badge/Category-OSINT_&_Code_Security-green?style=for-the-badge" alt="代码安全">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="许可证 AGPLv3">
</p>

<p对齐=“中心”>
  <b>🌐高精度秘密扫描器，旨在通过实时验证来追查 Git、S3、Docker 和文件系统中泄露的 API 密钥、令牌和私有证书</b>
</p>

---

## 📋 目录

- [概述与实时验证超能力](#-overview--the-live-verification-superpower)
- [TruffleHog 与 Gitleaks 比较](#-trufflehog-vs-gitleaks-comparison)
- [安装](#-安装)
- [扫描 Git 存储库](#-scanning-git-repositories)
- [扫描本地文件系统和目录](#-scanning-local-filesystems--directories)
- [扫描整个 GitHub 组织](#-scanning-entire-github-organizations)
- [扫描 S3 存储桶和 Docker 镜像](#-scanning-s3-buckets--docker-images)
- [过滤误报 (`--only-verified`)](#-filtering-误报---仅验证)
- [JSON 输出和 CI/CD 集成](#-json-output--cicd-integration)
- [快速参考表](#-quick-reference-table)
- [防御考虑因素和 OPSEC](#-defective-considerations--opsec)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和实时验证超能力

开发人员经常将高权限凭证提交到 Git 存储库（AWS 访问密钥、Stripe 私钥、Slack webhooks、SSH 私钥、数据库密码）。即使在后续提交中删除，**这些秘密仍然永久埋藏在存储库提交历史记录中**。

**TruffleHog** 由 **Truffle Security** 用 Go 编写，扫描提交历史记录、文件系统和云存储以发现泄露的秘密。

其定义的超能力是**实时验证**：
传统的正则表达式扫描仪会向安全团队带来数千个误报（例如示例文档密钥），而传统的正则表达式扫描仪则不同，TruffleHog 会联系相应的云提供商（AWS、GitHub、Slack、OpenAI）来**验证提取的秘密是否有效**。

---

## ⚖️ TruffleHog 与 Gitleaks 比较

|特色 |松露猪 |吉特泄密 |
|---------|------------|----------|
| **实时凭证验证** | ✅ 是（750 多个探测器经过现场测试）| ❌ 否（仅限正则表达式）|
| **目标数据源** | Git、GitHub、S3、Docker、文件系统| Git，本地文件 |
| **大型回购的速度** |快速（多线程）|极快（最小正则表达式）|
| **误报率** |接近零（使用 `--only-verified`）|中等（需要许可名单）|
| **预提交挂钩友好** | ✅ 支持 | ✅ 高度优化 |

---

## 📥 安装
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

## 🚀 扫描 Git 存储库
```bash
# Scan a remote Git repository (including entire commit history)
trufflehog git https://github.com/targetcorp/public-project

# Scan an internal on-premise Git repository with authentication
trufflehog git https://username:token@gitlab.corp.local/repo.git

# Scan only the most recent commit (great for pre-commit hooks)
trufflehog git file://./ --since-commit HEAD~1
```
---

## 📂 扫描本地文件系统和目录

扫描本地目录、提取的备份存档或取消索引的 Web 根目录：
```bash
# Scan current directory recursively
trufflehog filesystem .

# Scan specific sensitive configuration directory
trufflehog filesystem /var/www/html/ --exclude-paths tests/,vendor/
```
---

## 🏢 扫描整个 GitHub 组织

审核属于某个组织的所有公共存储库：
```bash
# Scan all public repos of a GitHub organization
trufflehog github --org=targetorg --token=YOUR_GITHUB_TOKEN

# Scan all private and public repositories across a personal account
trufflehog github --user=targetuser --token=YOUR_GITHUB_TOKEN
```
---

## ☁️ 扫描 S3 存储桶和 Docker 镜像
```bash
# Scan an AWS S3 bucket for exposed credentials in stored logs and backups
trufflehog s3 --bucket=target-backup-bucket

# Scan a Docker container image layers for baked-in secrets
trufflehog docker --image=targetcorp/api-server:latest
```
---

## 🎯 过滤误报 (`--only-verified`)

抑制所有未经验证的正则表达式命中并显示**仅确认的活动凭据**：
```bash
# Report ONLY confirmed functional credentials verified against providers
trufflehog git https://github.com/target/repo --only-verified

# Scan filesystem for verified secrets only
trufflehog filesystem /opt/backups/ --only-verified
```
---

## 📊 JSON 输出和 CI/CD 集成
```bash
# Export findings to structured JSON format
trufflehog git https://github.com/target/repo --json > secrets_report.json

# Parse live AWS keys with jq
cat secrets_report.json | jq -r 'select(.Verified == true) | .Raw'
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描远程 Git 存储库** | `trufflehog git <GIT_URL>` |
| **仅扫描已验证的秘密** | `trufflehog git <GIT_URL> --only-verified` |
| **扫描本地文件系统** | `trufflehog filesystem <DIRECTORY>` |
| **扫描 GitHub 组织** | `trufflehog github --org=<ORG> --token=<PAT>` |
| **扫描 Docker 镜像** | `trufflehog docker --image=<IMAGE>` |
| **扫描 S3 存储桶** | `trufflehog s3 --bucket=<BUCKET_NAME>` |
| **导出为 JSON** | `trufflehog git <GIT_URL> --json` |

---

## 🛡️ 防御考虑和 OPSEC

1. **出站 API 验证流量：** 在没有 `--no-verification` 的情况下运行时，TruffleHog 连接到第三方端点（Slack、AWS、Stripe）以测试密钥。这会触发受害者云租户中的身份验证日志！如果隐秘性至关重要，请禁用实时验证：
   ```bash
   trufflehog git <URL> --no-verification
   ```
2. **密钥撤销和事件响应：** 当在公共存储库中发现有效机密时，立即轮换凭证并检查 CloudTrail/审核日志是否存在未经授权的访问。

---

## ⚠️ 法律免责声明

> [!警告]
> 此备忘单适用于授权安全审核、红队操作和凭证卫生评估。利用发现的凭据访问未经授权的帐户或私有云环境是严格非法的。