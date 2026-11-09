<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<palign="centro">
  <b>🕸️ Framework di sicurezza CLI avanzato ed estensione Burp Suite sviluppati da Doyensec per eseguire il dump degli schemi di introspezione GraphQL, generare modelli di query e verificare la sicurezza dell'API GraphQL</b>
</p>

---

## 📋 Sommario

- [Panoramica e nozioni fondamentali sulla sicurezza di GraphQL](#-overview--graphql-security-fundamentals)
  - [Pericoli delle query di introspezione](#introspezione-query-hazards)
- [Installazione](#-installazione)
- [Dumping dell'introspezione e generazione di schemi](#-introspezione-dumping--generazione di schemi)
  - [Query di introspezione CLI](#cli-introspection-query)
  - [Dumping su Schema Definition Language (SDL)](#dumping-to-schema-definition-lingual-sdl)
  - [Generazione di modelli di query pronti per l'esecuzione](#generating-ready-to-run-query-templates)
- [Mutazioni di controllo e query sensibili](#-mutazioni di controllo--query-sensibili)
- [Rilevamento di errori di Denial of Service e batching](#-detecting-denial-of-service--batching-flaws)
  - [Query batch (batch di array)](#batch-querying-array-batching)
  - [Riferimenti circolari e query nidificate](#riferimenti-circolari--query-nidificate)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rafforzamento](#-considerazioni-difensive--rinforzo)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e fondamenti di sicurezza di GraphQL

A differenza delle API REST standard con URL discreti (`/api/v1/users`), GraphQL espone un singolo endpoint HTTP (solitamente `/graphql`) in cui i client richiedono forme di dati personalizzate tramite query e mutazioni.

### Pericoli delle query di introspezione
GraphQL dispone di un sistema di introspezione integrato (`__schema`, `__type`) progettato per gli strumenti di sviluppo. Se lasciato abilitato in produzione:
- Gli aggressori possono interrogare l'intero schema di backend.
- Funzionalità inedite, mutazioni amministrative interne (ad esempio `deleteUser`, `changeRole`) e campi privati ​​sono completamente esposti.

Creato da **Doyensec**, **InQL** automatizza l'estrazione dell'introspezione, analizza gli schemi, genera documentazione e crea modelli di exploit pronti all'uso.

---

## 📥Installazione
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

## 🚀 Dumping dell'introspezione e generazione di schemi

### Query di introspezione CLI
Interroga un endpoint GraphQL remoto, estrai lo schema completo e salvalo in una cartella di output:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Generato nella directory di output:*
- `schema.json`: dump dell'introspezione JSON non elaborato.
- `schema.graphql`: Schema Definition Language (SDL) leggibile dall'uomo.
- `queries/`: modelli di query generati automaticamente per ogni query disponibile.
- `mutations/`: modelli di mutazione generati automaticamente per la modifica dei dati.

### Scansione autenticata e intestazioni personalizzate
Passa cookie di sessione, token di autorizzazione o intestazioni personalizzate:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dumping nel linguaggio di definizione dello schema (SDL)
Converti lo schema JSON non elaborato esistente in SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Mutazioni di auditing e query sensibili

Esamina i file di mutazione generati automaticamente nella cartella di output:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Invia mutazioni ad alto impatto utilizzando curl o Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Rilevamento di attacchi di tipo Denial of Service e batching

### Interrogazioni batch (batch di array)
Verifica se il server elabora matrici di query in una singola richiesta HTTP (ignora la limitazione della velocità):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Riferimenti circolari e query annidate
Test per arresti anomali delle relazioni ricorsive:
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

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Dump Introspezione** | `inql -t https://target.com/graphql -o ./out/` |
| **Introspezione autenticata**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Converti schema JSON in SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Genera documenti HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Considerazioni difensive e rafforzamento

1. **Disabilita Introspezione in produzione:** Disabilita `introspection` nella configurazione del server GraphQL (Apollo, Yoga, GraphQL-Java).
2. **Applica l'analisi della profondità delle query e dei costi:** implementa librerie come `graphql-depth-limit` o l'analisi dei costi delle query per eliminare le query profondamente nidificate.
3. **Disabilita batch:** Limita o disabilita l'esecuzione di query batch basate su array se non strettamente necessario.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> InQL è sviluppato per valutazioni di sicurezza autorizzate, test di penetrazione e audit di sicurezza delle API. L'estrazione di schemi o la modifica dei dati su endpoint non autorizzati è illegale.