<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL – Vollständiger Cheatsheet

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
  <b>🕸️ Erweitertes CLI-Sicherheitsframework und Burp Suite-Erweiterung, entwickelt von Doyensec, um GraphQL-Introspektionsschemata zu sichern, Abfragevorlagen zu generieren und die GraphQL-API-Sicherheit zu prüfen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und GraphQL-Sicherheitsgrundlagen](#-overview--graphql-security-fundamentals)
  - [Gefahren bei Introspection-Abfragen](#introspection-query-hazards)
- [Installation](#-Installation)
- [Introspection Dumping & Schemagenerierung](#-introspection-dumping--schema-generation)
  - [CLI-Introspection-Abfrage](#cli-introspection-query)
  - [Dumping in die Schema Definition Language (SDL)](#dumping-to-schema-definition-sprache-sdl)
  - [Generieren von betriebsbereiten Abfragevorlagen](#generating-ready-to-run-query-templates)
- [Überwachung von Mutationen und sensiblen Abfragen](#-auditing-mutations--sensitive-queries)
- [Denial-of-Service- und Batching-Fehler erkennen](#-detecting-denial-of-service--batching-flaws)
  - [Batch-Abfrage (Array-Batching)](#batch-querying-array-batching)
  - [Zirkuläre Referenzen und verschachtelte Abfragen](#circular-references--nested-queries)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Härtung](#-defensive-überlegungen--härtung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und GraphQL-Sicherheitsgrundlagen

Im Gegensatz zu Standard-REST-APIs mit diskreten URLs (`/api/v1/users`) stellt GraphQL einen einzelnen HTTP-Endpunkt (normalerweise `/graphql`) bereit, an dem Clients über Abfragen und Mutationen benutzerdefinierte Datenformen anfordern.

### Gefahren bei Introspektion-Abfragen
GraphQL verfügt über ein integriertes Introspektionssystem (`__schema`, `__type`), das für Entwicklertools entwickelt wurde. Wenn in der Produktion aktiviert bleibt:
– Angreifer können das gesamte Backend-Schema abfragen.
– Unveröffentlichte Funktionen, interne administrative Mutationen (z. B. `deleteUser`, `changeRole`) und private Felder werden vollständig offengelegt.

**InQL** wurde von **Doyensec** entwickelt und automatisiert die Introspektionsextraktion, analysiert Schemata, generiert Dokumentation und erstellt gebrauchsfertige Exploit-Vorlagen.

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

## 🚀 Selbstbeobachtungs-Dumping und Schemagenerierung

### CLI-Introspektion-Abfrage
Fragen Sie einen Remote-GraphQL-Endpunkt ab, extrahieren Sie das vollständige Schema und speichern Sie es in einem Ausgabeordner:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*Im Ausgabeverzeichnis generiert:*
- `schema.json`: Roher JSON-Introspektion-Dump.
- `schema.graphql`: Menschenlesbare Schemadefinitionssprache (SDL).
- `queries/`: Automatisch generierte Abfragevorlagen für jede verfügbare Abfrage.
- `mutations/`: Automatisch generierte Mutationsvorlagen zur Datenänderung.

### Authentifiziertes Scannen und benutzerdefinierte Header
Übergeben Sie Sitzungscookies, Autorisierungstoken oder benutzerdefinierte Header:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### Dumping in die Schema Definition Language (SDL)
Konvertieren Sie das vorhandene JSON-Rohschema in SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 Prüfung von Mutationen und sensiblen Abfragen

Überprüfen Sie automatisch generierte Mutationsdateien im Ausgabeordner:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
Senden Sie Mutationen mit großer Auswirkung mit Curl oder Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡ Erkennung von Denial-of-Service- und Batch-Fehlern

### Batch-Abfrage (Array-Batching)
Testen Sie, ob der Server Arrays von Abfragen in einer einzigen HTTP-Anfrage verarbeitet (umgeht die Ratenbegrenzung):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### Zirkelverweise und verschachtelte Abfragen
Test auf Abstürze bei rekursiven Beziehungen:
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

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Dump-Selbstbeobachtung** | `inql -t https://target.com/graphql -o ./out/` |
| **Authentifizierte Selbstbeobachtung**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| **JSON-Schema in SDL konvertieren** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **HTML-Dokumente generieren** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ Defensive Überlegungen und Abhärtung

1. **Introspektion in der Produktion deaktivieren:** Deaktivieren Sie `introspection` in Ihrer GraphQL-Serverkonfiguration (Apollo, Yoga, GraphQL-Java).
2. **Abfragetiefe und Kostenanalyse erzwingen:** Implementieren Sie Bibliotheken wie `graphql-depth-limit` oder die Abfragekostenanalyse, um tief verschachtelte Abfragen zu löschen.
3. **Batching deaktivieren:** Beschränken oder deaktivieren Sie die Ausführung von Array-basierten Batch-Abfragen, wenn dies nicht unbedingt erforderlich ist.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> InQL wurde für autorisierte Sicherheitsbewertungen, Penetrationstests und API-Sicherheitsprüfungen entwickelt. Das Extrahieren von Schemata oder das Ändern von Daten auf nicht autorisierten Endpunkten ist illegal.