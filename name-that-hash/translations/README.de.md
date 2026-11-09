<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Name-That-Hash (nth) Cheatsheet

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) ist eine moderne, ultraschnelle Hash-Identifikations-Engine, die unbekannte Hashes sowie in Text oder Dateien eingebettete Hashes genau identifiziert und direkte Hashcat- (`-m`) und John the Ripper- (`--format`) Modi ausgibt. Es dekodiert automatisch verschachtelte Kodierungen wie Base64 und Hex.

---

## Inhaltsverzeichnis

- [Hauptfunktionen](#key-features)
- [Installation](#installation)
- [CLI-Hash-Identifikation](#cli-hash-identification)
- [Multi-Hash & Dateiverarbeitung](#multi-hash--file-processing)
- [Hashcat & John the Ripper Format-Ausgabe](#hashcat--john-the-ripper-format-output)
- [Automatische Dekodierung und Beliebtheitsfilter](#auto-decode--popularity-filters)
- [Integration mit Cracking Pipelines](#integration-with-cracking-pipelines)

---

## Hauptmerkmale

- **Über 300 Hash-Typen:** Behandelt moderne Algorithmen, Active Directory-Formate, Datenbank-Hashes, CMS-Signaturen und esoterische Chiffren.
- **Hashcat & John Flag Mapping:** Zeigt sofort die Werte `-m <mode>` und `--format=<format>` an.
- **Automatische Dekodierung:** Entfernt Base64-, Hex- und URL-Kodierung vor der Berechnung des Hash-Typs.
- **Sub-String-/Textsuche:** Findet Hashes, die in Protokollen, Speicherauszügen oder Konfigurationsdateien vergraben sind.
- **Beliebtheitssortierung:** Ordnet die Ergebnisse nach der tatsächlichen Nutzungswahrscheinlichkeit.

---

## Installation
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## CLI-Hash-Identifikation

### 1. Identifizieren Sie einen einzelnen Hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identifizieren Sie die Hashcat- und John-Formatmodi direkt
Zeigen Sie nur Hash-Typen an, die die Hashcat- oder John-Cracking-Modi unterstützt haben:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Detaillierte Informationen anzeigen (Beschreibung, Geschwindigkeit, Beispiele)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Multi-Hash- und Dateiverarbeitung

### 1. Identifizieren Sie Hashes in einer Datei (Zeile für Zeile)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtern Sie die Ergebnisse nur nach den beliebtesten Hashes
Reduzieren Sie Fehlalarme aus unbekannten Hash-Typen, indem Sie nach Beliebtheit filtern:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Suchen und extrahieren Sie Hashes, die in unstrukturierten Text eingebettet sind
Durchsuchen Sie Rohspeicherzeichenfolgen, Protokolldateien oder Quellcode nach Hashes:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Ausgabe im Hashcat- und John the Ripper-Format

`nth` macht es einfach, Hashes in Cracker einzuspeisen, ohne sich die Modusnummern zu merken:

| Beispiel-Hash-Format | Erkannter Algorithmus | Hashcat-Modus (`-m`) | John-Format (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP-Röstung | `18200` | `krb5asrep` |

---

## Automatische Dekodierung und Beliebtheitsfilter

Viele Anwendungen speichern Hashes vor dem Einfügen in die Datenbank in Base64 oder Hex. `nth` kann dies automatisch umkehren:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integration mit Cracking Pipelines

Leiten Sie mithilfe der automatisierten Identifizierung schnell einen unbekannten Hash an Hashcat weiter:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```