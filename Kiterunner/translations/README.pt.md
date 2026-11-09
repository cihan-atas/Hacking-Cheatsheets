<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web e API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="Licença Apache 2.0">
</p>

<p alinhar="centro">
  <b>🌐 Rota de força bruta de API REST e GraphQL de alto desempenho projetada para mapear arquiteturas de API modernas usando conjuntos de dados contextuais</b>
</p>

---

## 📋 Índice

- [Visão geral e o problema de descoberta de API](#-overview--the-api-discovery-problem)
- [Comparação de ferramentas: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Instalação](#-instalação)
- [Baixando conjuntos de dados do Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [APIs de verificação (`kr scan`)](#-scanning-apis-kr-scan)
- [Força bruta de lista de palavras padrão (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Cabeçalhos, Cookies e Autenticação](#-cabeçalhos-cookies--autenticação)
- [Filtragem e códigos de status](#-filtering--status-codes)
- [Repetindo rotas descobertas (saída `curl`)](#-replaying-discovered-routes-curl-output)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o problema de descoberta de API

As ferramentas tradicionais de descoberta de conteúdo da web (`ffuf`, `dirsearch`, `gobuster`) usam dicionários simples de uma única palavra (por exemplo, `admin`, `api`, `login`). Embora isso funcione para hierarquias de arquivos tradicionais, **falha nas APIs REST e GraphQL modernas**.

APIs modernas usam rotas aninhadas e multicamadas, como:
`/api/v2/organizations/{org_id}/users/export?format=json`

O **Kiterunner**, criado por **Assetnote**, resolve isso compilando OpenAPI, Swagger, Postman e definições de esquema do mundo real em conjuntos de dados binários compactos (`.kite`). Em vez de adivinhar palavras aleatórias, o Kiterunner reconstrói estruturas hierárquicas inteiras de chamadas de API com métodos HTTP corretos (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Comparação de ferramentas: Kiterunner vs ffuf vs dirsearch

| Recurso | Kiterunner (`kr`) | affu | pesquisa direta |
|--------|-------------------|------|-----------|
| **Foco no alvo principal** | REST, GraphQL, OpenAPI | Fuzzing geral da Web | Força bruta de diretório |
| **Conscientização sobre hierarquia de rotas**| ✅ Nativo (arquivos `.kite`) | ❌ Lista de palavras plana | ❌ Lista de palavras plana |
| **Troca de verbos HTTP** | ✅ Testes GET, POST, PUT, DELETE| ❌ Configuração manual | ❌ Limitado |
| **Análise Swagger/OpenAPI** | ✅ Ingestão de esquema nativo | ❌ Não | ❌ Não |
| **Velocidade e simultaneidade** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Python) |

---

## 📥 Instalação
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Baixando conjuntos de dados do Assetnote Kite

O Kiterunner depende de conjuntos de dados de rotas `.kite` pré-construídos, criados a partir de milhões de APIs públicas analisadas:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 APIs de verificação (`kr scan`)

O comando `scan` usa conjuntos de dados `.kite` para sondar alvos usando hierarquias de rotas contextuais:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Força bruta de lista de palavras padrão (`kr brute`)

Se você quiser usar listas de palavras `.txt` padrão enquanto aproveita as vantagens do mecanismo API multimétodo do Kiterunner:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Cabeçalhos, cookies e autenticação

As APIs frequentemente exigem JWTs, tokens de portador ou cabeçalhos de organização personalizados:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filtragem e códigos de status
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Repetindo rotas descobertas (saída `curl`)

O Kiterunner pode gerar rotas descobertas como comandos `curl` acionáveis, permitindo a reprodução instantânea no terminal ou no Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **API de digitalização com conjunto de dados pequeno** | `kr scan <URL> -w routes-small.kite` |
| **API de digitalização com grande conjunto de dados** | `kr scan <URL> -w routes-large.kite` |
| **Verificar vários hosts** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Lista de palavras de texto de força bruta** | `kr brute <URL> -w wordlist.txt` |
| **Autenticar com token do portador**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Excluir códigos de status** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Saída para arquivo JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Volume massivo de solicitações:** O dicionário `routes-large.kite` gera milhões de solicitações. Certifique-se de que a limitação de taxa (`-q 200`) esteja habilitada ao testar ambientes de produção.
2. **Métodos de mudança de estado:** Kiterunner envia solicitações `POST`, `PUT` e `DELETE`. Em APIs de teste não protegidas, isso pode modificar ou excluir inadvertidamente registros de back-end.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Kiterunner foi projetado apenas para testes de segurança de API autorizados e engajamentos de recompensas de bugs. A difusão de APIs não autorizadas é estritamente ilegal.