<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p alinhar="centro">
  <b>🕸️ Estrutura de segurança CLI avançada e extensão Burp Suite desenvolvida pela Doyensec para despejar esquemas de introspecção GraphQL, gerar modelos de consulta e auditar a segurança da API GraphQL</b>
</p>

---

## 📋 Índice

- [Visão geral e fundamentos de segurança do GraphQL](#-overview--graphql-security-fundamentals)
  - [Riscos de consulta de introspecção](#introspection-query-hazards)
- [Instalação](#-instalação)
- [Dumping de introspecção e geração de esquema](#-dumping de introspecção - geração de esquema)
  - [Consulta de introspecção CLI](#cli-introspection-query)
  - [Dumping para linguagem de definição de esquema (SDL)](#dumping-to-schema-definition-linguagem-sdl)
  - [Gerando modelos de consulta prontos para execução](#generating-ready-to-run-query-templates)
- [Auditoria de mutações e consultas confidenciais](#-auditing-mutations--sensitive-queries)
- [Detectando falhas de negação de serviço e lotes](#-detecting-denial-of-service--batching-flaws)
  - [Consulta em lote (lote de array)](#batch-querying-array-batching)
  - [Referências circulares e consultas aninhadas](#referências circulares--consultas aninhadas)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Endurecimento](#-considerações defensivas--endurecimento)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e fundamentos de segurança do GraphQL

Ao contrário das APIs REST padrão com URLs discretos (`/api/v1/users`), o GraphQL expõe um único endpoint HTTP (geralmente `/graphql`) onde os clientes solicitam formas de dados personalizadas por meio de consultas e mutações.

### Riscos de consulta de introspecção
GraphQL apresenta um sistema de introspecção integrado (`__schema`, `__type`) projetado para ferramentas de desenvolvedor. Se deixado ativado na produção:
- Os invasores podem consultar todo o esquema de back-end.
- Recursos não lançados, mutações administrativas internas (por exemplo, `deleteUser`, `changeRole`) e campos privados são completamente expostos.

Criado por **Doyensec**, o **InQL** automatiza a extração de introspecção, analisa esquemas, gera documentação e cria modelos de exploração prontos para uso.

---

## 📥 Instalação
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

## 🚀 Despejo de introspecção e geração de esquema

### Consulta de introspecção CLI
Consulte um endpoint GraphQL remoto, extraia o esquema completo e salve-o em uma pasta de saída:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Gerado no diretório de saída:*
- `schema.json`: despejo de introspecção JSON bruto.
- `schema.graphql`: Linguagem de definição de esquema (SDL) legível por humanos.
- `queries/`: modelos de consulta gerados automaticamente para cada consulta disponível.
- `mutations/`: modelos de mutação gerados automaticamente para modificação de dados.

### Verificação autenticada e cabeçalhos personalizados
Passe cookies de sessão, tokens de autorização ou cabeçalhos personalizados:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dump para Linguagem de Definição de Esquema (SDL)
Converta o esquema JSON bruto existente em SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Auditoria de mutações e consultas confidenciais

Revise os arquivos de mutação gerados automaticamente na pasta de saída:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Envie mutações de alto impacto usando curl ou Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Detectando falhas de negação de serviço e lotes

### Consulta em lote (lote de array)
Teste se o servidor processa matrizes de consultas em uma única solicitação HTTP (ignora a limitação de taxa):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Referências circulares e consultas aninhadas
Teste para falhas de relação recursiva:
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

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Introspecção de despejo** | `inql -t https://target.com/graphql -o ./out/` |
| **Introspecção autenticada**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Converter esquema JSON em SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Gerar documentos HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Considerações defensivas e endurecimento

1. **Desative a introspecção na produção:** Desative o `introspection` na configuração do seu servidor GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Aplicar análise de custo e profundidade de consulta:** Implemente bibliotecas como `graphql-depth-limit` ou análise de custo de consulta para eliminar consultas profundamente aninhadas.
3. **Desativar lotes:** Limite ou desative a execução de consultas em lote baseadas em array, se não for estritamente necessário.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O InQL foi desenvolvido para avaliações de segurança autorizadas, testes de penetração e auditoria de segurança de API. Extrair esquemas ou modificar dados em endpoints não autorizados é ilegal.