# 🕸️ InQL - Complete Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)

```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```

<p align="center">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="center">
  <b>🕸️ Advanced CLI security framework and Burp Suite extension developed by Doyensec to dump GraphQL introspection schemas, generate query templates, and audit GraphQL API security</b>
</p>

---

## 📋 Table of Contents

- [Overview & GraphQL Security Fundamentals](#-overview--graphql-security-fundamentals)
  - [Introspection Query Hazards](#introspection-query-hazards)
- [Installation](#-installation)
- [Introspection Dumping & Schema Generation](#-introspection-dumping--schema-generation)
  - [CLI Introspection Query](#cli-introspection-query)
  - [Dumping to Schema Definition Language (SDL)](#dumping-to-schema-definition-language-sdl)
  - [Generating Ready-to-Run Query Templates](#generating-ready-to-run-query-templates)
- [Auditing Mutations & Sensitive Queries](#-auditing-mutations--sensitive-queries)
- [Detecting Denial of Service & Batching Flaws](#-detecting-denial-of-service--batching-flaws)
  - [Batch Querying (Array Batching)](#batch-querying-array-batching)
  - [Circular References & Nested Queries](#circular-references--nested-queries)
- [Quick Reference Table](#-quick-reference-table)
- [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
- [Legal Disclaimer](#-legal-disclaimer)

---

## 🎯 Overview & GraphQL Security Fundamentals

Unlike standard REST APIs with discrete URLs (`/api/v1/users`), GraphQL exposes a single HTTP endpoint (usually `/graphql`) where clients request custom data shapes via queries and mutations.

### Introspection Query Hazards
GraphQL features a built-in introspection system (`__schema`, `__type`) designed for developer tooling. If left enabled in production:
- Attackers can query the entire backend schema.
- Unreleased features, internal administrative mutations (e.g. `deleteUser`, `changeRole`), and private fields are completely exposed.

Created by **Doyensec**, **InQL** automates introspection extraction, parses schemas, generates documentation, and builds ready-to-use exploit templates.

---

## 📥 Installation

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

## 🚀 Introspection Dumping & Schema Generation

### CLI Introspection Query
Query a remote GraphQL endpoint, extract the complete schema, and save it to an output folder:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Generated in output directory:*
- `schema.json`: Raw JSON introspection dump.
- `schema.graphql`: Human-readable Schema Definition Language (SDL).
- `queries/`: Auto-generated query templates for every available query.
- `mutations/`: Auto-generated mutation templates for data modification.

### Authenticated Scanning & Custom Headers
Pass session cookies, authorization tokens, or custom headers:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```

### Dumping to Schema Definition Language (SDL)
Convert existing raw JSON schema to SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```

---

## 💥 Auditing Mutations & Sensitive Queries

Review auto-generated mutation files in the output folder:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```

Send high-impact mutations using curl or Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```

---

## ⚡ Detecting Denial of Service & Batching Flaws

### Batch Querying (Array Batching)
Test if the server processes arrays of queries in a single HTTP request (bypasses rate-limiting):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```

### Circular References & Nested Queries
Test for recursive relation crashes:
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

## ⚡ Quick Reference Table

| Objective | Command |
|-----------|---------|
| **Dump Introspection** | `inql -t https://target.com/graphql -o ./out/` |
| **Authenticated Introspection**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Convert JSON Schema to SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Generate HTML Docs** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Defensive Considerations & Hardening

1. **Disable Introspection in Production:** Disable `introspection` in your GraphQL server configuration (Apollo, Yoga, GraphQL-Java).
2. **Enforce Query Depth & Cost Analysis:** Implement libraries like `graphql-depth-limit` or query cost analysis to drop deeply nested queries.
3. **Disable Batching:** Limit or disable array-based batch query execution if not strictly necessary.

---

## ⚠️ Legal Disclaimer

> [!CAUTION]
> InQL is developed for authorized security evaluations, penetration tests, and API security auditing. Extracting schemas or modifying data on unauthorized endpoints is illegal.
