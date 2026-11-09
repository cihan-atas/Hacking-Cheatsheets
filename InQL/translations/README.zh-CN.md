<!-- language-home-navigation -->
[🏠 中文主页](../../README.zh-CN.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - 完整备忘单

[🇹🇷 Türkçe Sürüm]（翻译/README.tr.md）
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p对齐=“中心”>
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="网络安全">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p对齐=“中心”>
  <b>🕸️ Doyensec 开发的高级 CLI 安全框架和 Burp Suite 扩展，用于转储 GraphQL 内省模式、生成查询模板并审核 GraphQL API 安全性</b>
</p>

---

## 📋 目录

- [概述和 GraphQL 安全基础知识](#-overview--graphql-security-fundamentals)
  - [内省查询危险](#introspection-query-hazards)
- [安装](#-安装)
- [内省转储和模式生成](#-introspection-dumping--schema- Generation)
  - [CLI 自省查询](#cli-introspection-query)
  - [转储到架构定义语言 (SDL)](#dumping-to-schema-definition-language-sdl)
  - [生成准备运行的查询模板](#generate-ready-to-run-query-templates)
- [审核突变和敏感查询](#-auditing-mutations--sensitive-queries)
- [检测拒绝服务和批处理缺陷](#-Detecting-denial-of-service--batching-flaws)
  - [批量查询（数组批处理）](#batch-querying-array-batching)
  - [循环引用和嵌套查询](#circular-references--nested-queries)
- [快速参考表](#-quick-reference-table)
- [防御考虑和强化](#-defective-considerations--hardening)
- [法律免责声明](#-legal-disclaimer)

---

## 🎯 概述和 GraphQL 安全基础知识

与具有离散 URL (`/api/v1/users`) 的标准 REST API 不同，GraphQL 公开单个 HTTP 端点（通常是 `/graphql`），客户端通过查询和突变请求自定义数据形状。

### 自省查询危险
GraphQL 具有专为开发人员工具设计的内置自省系统（`__schema`、`__type`）。如果在生产中保持启用状态：
- 攻击者可以查询整个后端架构。
- 未发布的功能、内部管理突变（例如 `deleteUser`、`changeRole`）和私有字段完全暴露。

**InQL** 由 **Doyensec** 创建，可自动进行内省提取、解析架构、生成文档并构建即用型漏洞利用模板。

---

## 📥 安装
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 内省转储和模式生成

### CLI 自省查询
查询远程 GraphQL 端点，提取完整的架构，并将其保存到输出文件夹：
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*在输出目录中生成：*
- `schema.json`：原始 JSON 内省转储。
- `schema.graphql`：人类可读的模式定义语言（SDL）。
- `queries/`：为每个可用查询自动生成查询模板。
- `mutations/`：自动生成用于数据修改的突变模板。

### 经过身份验证的扫描和自定义标头
传递会话 cookie、授权令牌或自定义标头：
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### 转储到架构定义语言 (SDL)
将现有的原始 JSON 模式转换为 SDL：
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 审核突变和敏感查询

Review auto-generated mutation files in the output folder:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
使用curl或Burp Suite发送高影响力的突变：
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ 检测拒绝服务和批处理缺陷

### 批量查询（数组批处理）
测试服务器是否在单个 HTTP 请求中处理查询数组（绕过速率限制）：
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```

```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ 快速参考表

|目标|命令 |
|------------|---------|
| **转储内省** | `inql -t https://target.com/graphql -o ./out/` |
| **经过验证的内省**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **将 JSON 架构转换为 SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **生成 HTML 文档** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ 防守考虑和强化

1. **在生产中禁用自省：** 在 GraphQL 服务器配置（Apollo、Yoga、GraphQL-Java）中禁用 `introspection`。
2. **强制执行查询深度和成本分析：** 实施 `graphql-depth-limit` 等库或查询成本分析以删除深层嵌套查询。
3. **禁用批处理：** 如果不是绝对必要，则限制或禁用基于数组的批量查询执行。

---

## ⚠️ 法律免责声明

> [!警告]
> InQL 专为授权安全评估、渗透测试和 API 安全审计而开发。在未经授权的端点上提取模式或修改数据是非法的。