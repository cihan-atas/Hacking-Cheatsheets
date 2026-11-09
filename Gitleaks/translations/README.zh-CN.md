<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔑 Gitleaks - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗ ██╗████████╗██╗     ███████╗ █████╗ ██╗  ██╗███████╗
  ██╔════╝ ██║╚══██╔══╝██║     ██╔════╝██╔══██╗██║ ██╔╝██╔════╝
  ██║  ███╗██║   ██║   ██║     █████╗  ███████║█████╔╝ ███████╗
  ██║   ██║██║   ██║   ██║     ██╔══╝  ██╔══██║██╔═██╗ ╚════██║
  ╚██████╔╝██║   ██║   ███████╗███████╗██║  ██║██║  ██╗███████║
   ╚═════╝ ╚═╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝
           Fast & Automated Secret Detection for Git Repositories
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/Gitleaks-Secret_Scanner-blue?style=for-the-badge" alt="Gitleaks">
  <img src="https://img.shields.io/badge/Author-Zachary_Rice-orange?style=for-the-badge" alt="zricethezav">
  <img src="https://img.shields.io/badge/Category-Source_Code_&_Secrets-green?style=for-the-badge" alt="秘密">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p对齐=“中心”>
  <b>🔑 SAST 工具，旨在查找和检测 Git 存储库和提交历史记录中的硬编码秘密，例如密码、API 密钥和令牌</b>
</p>

---

## 📋 目录

- [概述和检测引擎](#-overview--detection-engine)
- [安装](#-安装)
- [基本秘密扫描](#-basic-secret-scanning)
  - [扫描 Git 存储库](#scanning-a-git-repository)
  - [扫描未提交的更改](#scanning-uncommissed-changes)
  - [扫描普通文件和文件夹](#scanning-plain-files--folders)
- [定位提交范围和历史记录](#-targeting-commit-ranges--history)
- [CI/CD 和预提交集成](#-cicd--pre-commit-integration)
- [自定义检测规则和白名单](#-custom-detection-rules--allowlisting)
- [输出格式和报告](#-output-formats--reporting)
- [快速参考表](#-quick-reference-table)
- [防御考虑与补救](#-defective-considerations--remediation)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和检测引擎

无意中提交给 git 存储库的硬编码机密（AWS 访问密钥、SSH 私钥、GitHub 令牌、Slack webhook、数据库凭证）是初始云泄露的最常见向量之一。

**Gitleaks** 由 **Zachary Rice (@zricethezav)** 创建，提供基于正则表达式的超快速香农熵分析：
- **完整的 Git 历史记录：** 审核每个分支、标签和历史提交。
- **预提交挂钩：** 阻止工程师在代码到达远程服务器之前意外提交凭据。
- **CI/CD 管道：** 自动失败包含凭证泄漏的拉取请求。

---

## 📥 安装
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

## 🚀 基本秘密扫描

### 扫描 Git 存储库
审核当前存储库的提交历史记录：
```bash
# Run detection in current working repo
gitleaks detect -v

# Audit an external local repo path
gitleaks detect --source /path/to/repo -v
```
### 扫描未提交的更改
在提交之前审核暂存和未暂存的代码更改：
```bash
# Inspect unstaged working directory changes
gitleaks protect -v

# Scan staged changes only
gitleaks protect --staged -v
```
### 扫描普通文件和文件夹
扫描非 git 目录（例如，提取的固件、反编译的移动应用程序、配置文件夹）：
```bash
gitleaks dir /path/to/directory -v
```
---

## ⚡ 定位提交范围和历史记录
```bash
# Scan commits made in the last 7 days
gitleaks detect --log-opts="--since='7 days ago'" -v

# Scan specific branch or tag comparison
gitleaks detect --log-opts="origin/main..feature-branch" -v

# Target the latest 5 commits
gitleaks detect --log-opts="-n 5" -v
```
---

## ⚙️ CI/CD 和预提交集成

### 预提交框架配置（`.pre-commit-config.yaml`）
```yaml
repos:
  - repo: https://github.com/gitleaks/gitleaks
    rev: v8.18.2
    hooks:
      - id: gitleaks
```
### GitHub Actions 工作流程
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

## 🧩 自定义检测规则和白名单

使用 `.gitleaks.toml` 配置自定义正则表达式模式或忽略误报：
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
使用自定义配置运行：
```bash
gitleaks detect -c .gitleaks.toml -v
```
---

## 📊 输出格式和报告
```bash
# Export results to clean JSON format
gitleaks detect --report-path gitleaks-report.json --report-format json

# Export to SARIF (for GitHub Security tab integration)
gitleaks detect --report-path report.sarif --report-format sarif

# Export to CSV
gitleaks detect --report-path report.csv --report-format csv
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **扫描整个 Git 历史记录** | `gitleaks detect -v` |
| **扫描未提交的更改** | `gitleaks protect -v` |
| **仅扫描分阶段更改** | `gitleaks protect --staged -v` |
| **扫描普通目录（无 Git）**| `gitleaks dir /path/to/folder -v` |
| **导出为 JSON** | `gitleaks detect -r report.json -f json` |
| **导出至 SARIF** | `gitleaks detect -r report.sarif -f sarif` |
| **使用自定义规则配置** | `gitleaks detect -c .gitleaks.toml` |
| **扫描特定分支** | `gitleaks detect --log-opts="main..dev"` |

---

## 🛡️ 防御注意事项和补救措施

1. **立即撤销：** 一旦在 git 历史记录中检测到机密，就认为它已完全泄露并立即轮换/撤销它。
2. **重写 Git 历史记录：** 需要删除提交或使用 `git filter-repo` / BFG Repo-Cleaner 来清理工件，但切勿用历史记录重写来代替凭证轮换。

---

## ⚠️ 法律免责声明

> [!警告]
> Gitleaks 专为授权内部安全审计、持续集成漏洞检查和防御性代码审查而设计。未经许可，严禁扫描未经授权的专有存储库。