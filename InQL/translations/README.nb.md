<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Nettsikkerhet">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</p>

<p align="center">
  <b>🕸️ Avansert CLI-sikkerhetsrammeverk og Burp Suite-utvidelse utviklet av Doyensec for å dumpe GraphQL-introspeksjonsskjemaer, generere spørringsmaler og revidere GraphQL API-sikkerhet</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview & GraphQL Security Fundamentals](#-overview--graphql-security-fundamentals)
  – [Introspection Query Hazards](#introspection-query-hazards)
- [Installasjon](#-installasjon)
- [Introspection Dumping & Schema Generation](#-introspection-dumping--schema-generation)
  - [CLI Introspection Query](#cli-introspection-query)
  - [Dumping til Schema Definition Language (SDL)](#dumping-to-schema-definition-language-sdl)
  - [Genererer Ready-to-Run Query Templates](#generating-ready-to-run-query-maler)
- [Revisjonsmutasjoner og sensitive spørringer](#-revisjonsmutasjoner--sensitive-spørringer)
– [Oppdager tjenestenekt og batchingfeil](#-detecting-denial-of-service--batching-feil)
  - [Batch Querying (Array Batching)](#batch-querying-array-batching)
  - [Sirkulære referanser og nestede søk](#cirkulære-referanser--nestede-søk)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og GraphQL Security Fundamentals

I motsetning til standard REST APIer med diskrete URL-er (`/api/v1/users`), avslører GraphQL et enkelt HTTP-endepunkt (vanligvis `/graphql`) der klienter ber om tilpassede dataformer via spørringer og mutasjoner.

### Introspeksjonsfarer
GraphQL har et innebygd introspeksjonssystem (`__schema`, `__type`) designet for utviklerverktøy. Hvis venstre aktivert i produksjon:
- Angripere kan spørre hele backend-skjemaet.
- Ikke-utgitte funksjoner, interne administrative mutasjoner (f.eks. `deleteUser`, `changeRole`) og private felt er fullstendig eksponert.

Laget av **Doyensec**, **InQL** automatiserer utvinning av introspeksjon, analyserer skjemaer, genererer dokumentasjon og bygger bruksklare utnyttelsesmaler.

---

## 📥 Installasjon
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

## 🚀 Introspeksjonsdumping og skjemagenerering

### CLI Introspection Query
Spør et eksternt GraphQL-endepunkt, trekk ut hele skjemaet og lagre det i en utdatamappe:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Generert i utdatakatalogen:*
- `schema.json`: Rå JSON-introspeksjonsdump.
- `schema.graphql`: Human-readable Schema Definition Language (SDL).
- `queries/`: Autogenererte spørringsmaler for alle tilgjengelige spørringer.
- `mutations/`: Autogenererte mutasjonsmaler for datamodifisering.

### Autentisert skanning og tilpassede topptekster
Informasjonskapsler for bestått økt, autorisasjonstokener eller egendefinerte overskrifter:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dumping til Schema Definition Language (SDL)
Konverter eksisterende rå JSON-skjema til SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Revisjon av mutasjoner og sensitive spørringer

Se gjennom automatisk genererte mutasjonsfiler i utdatamappen:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Send kraftige mutasjoner ved å bruke curl eller Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Oppdager tjenestenekt og batchingsfeil

### Batchspørring (matrisebatching)
Test om serveren behandler rekker av spørringer i en enkelt HTTP-forespørsel (omgår hastighetsbegrensning):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Sirkulære referanser og nestede søk
Test for rekursive relasjonskrasj:
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

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Dump introspeksjon** | `inql -t https://target.com/graphql -o ./out/` |
| **Autentisert introspeksjon**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **Konverter JSON-skjema til SDL** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **Generer HTML-dokumenter** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Defensive betraktninger og herding

1. **Deaktiver introspeksjon i produksjon:** Deaktiver `introspection` i GraphQL-serverkonfigurasjonen (Apollo, Yoga, GraphQL-Java).
2. **Håndhev spørringsdybde- og kostnadsanalyse:** Implementer biblioteker som `graphql-depth-limit` eller spørringskostnadsanalyse for å slippe dypt nestede spørringer.
3. **Deaktiver batching:** Begrens eller deaktiver array-basert batch-søking hvis det ikke er strengt nødvendig.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> InQL er utviklet for autoriserte sikkerhetsevalueringer, penetrasjonstester og API-sikkerhetsrevisjon. Det er ulovlig å trekke ut skjemaer eller endre data på uautoriserte endepunkter.