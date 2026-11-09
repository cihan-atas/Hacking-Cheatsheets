<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# fcrackzip Cheatsheet

[fcrackzip](https://github.com/hyc/fcrackzip) ist ein schneller Multithread-Befehlszeilen-Passwort-Cracker für verschlüsselte ZIP-Archive. Es unterstützt sowohl wörterbuchbasierte Angriffe als auch optimierte Brute-Force-Angriffe gegen traditionelles ZipCrypto (PKZIP-Stream-Verschlüsselung) und kann die Archivintegrität ohne vollständige Extraktion überprüfen.

---

## Inhaltsverzeichnis

- [Kernfunktionen und Einschränkungen](#core-features--limitations)
- [Installation](#installation)
- [Dictionary Attacks (`-D -p`)](#dictionary-attacks--d--p)
- [Brute-Force-Angriffe (`-b`)](#brute-force-attacks--b)
- [Zeichensätze und Längenspezifikation](#character-sets--length-specification)
- [Benchmark- und Leistungsoptimierung](#benchmark--performance-optimization)
- [ZipCrypto vs. AES-256 (John/Hashcat-Alternative)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Kernfunktionen und Einschränkungen

- **Ultraschneller ZipCrypto-Cracker:** In C geschrieben, optimiert für den alten ZipCrypto-Algorithmus (Standard-PKZIP-Verschlüsselung).
- **Wörterbuchmodus:** Liest Kandidatenpasswörter nacheinander aus Wortlisten.
- **Brute-Force-Modus:** Erzeugt Kandidaten im laufenden Betrieb mithilfe konfigurierbarer Zeichensätze und Längengrenzen.
- **Archive Unzip Test (`-u`):** Verifiziert Kandidatenkennwörter durch einen Dekomprimierungsversuch, wodurch Fehlalarme praktisch ausgeschlossen werden.
- **Einschränkung:** Unterstützt die moderne WinZip AES-256-Verschlüsselung **nicht**. Für AES-256-ZIPs verwenden Sie `zip2john` + John the Ripper oder Hashcat (Modus 13600).

---

## Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Wörterbuchangriffe (`-D -p`)

Der schnellste Weg, mit gängigen Wortlisten zu testen:

### 1. Einfacher Wörterbuchangriff mit Unzip-Verifizierung
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: Entpackmodus (überprüft den CRC der extrahierten Datei, um Fehlalarme zu vermeiden).
- `-D`: Wörterbuch-Angriffsmodus.
- `-p <file>`: Pfad zur Wörterbuchdatei.

### 2. Ausführlicher Ausgabemodus
Getestete Geschwindigkeit, aktuelles Passwort und Debug-Informationen anzeigen:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Brute-Force-Angriffe (`-b`)

Wenn Wörterbuchangriffe fehlschlagen, können Sie mit Brute-Force gegen unbekannte Kurzpasswörter vorgehen:

### 1. Numerische Brute-Force-PINs (4 bis 6 Ziffern)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: Brute-Force-Modus.
- `-c 1`: Zeichensatz: nur Ziffern (`0-9`).
- `-l 4-6`: Längenbereich: mindestens 4, höchstens 6 Zeichen.

### 2. Alphanumerische Kleinbuchstaben (1 bis 5 Zeichen)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Kleinbuchstaben (`a-z`) und Ziffern (`0-9`).

### 3. Kleinbuchstaben + Großbuchstaben + Ziffern + Symbole
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Kleinbuchstaben (`a-z`).
- `-c A`: Großbuchstaben (`A-Z`).
- `-c 1`: Zahlen (`0-9`).
- `-c !`: Sondersymbole (`[!:$%&/()=?{[]}+*~#]`).

---

## Zeichensätze und Längenspezifikation

| Flagge | Zeichensatz enthalten | Beispielkandidaten |
|---|---|---|
| `-c a` | Kleinbuchstaben (`a-z`) | `admin`, `secret` |
| `-c A` | Großbuchstaben (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Ziffern (`0-9`) | `1234`, `2024` |
| `-c !` | Spezielle Satzzeichen | `!@#$` |
| `-c aA1` | Kombinierte alphanumerische Zeichen | `Admin123` |
| `-p <prefix>` | Anfänglicher Start-Seed-String | Nützlich, um Brute Force fortzusetzen |

Längengrenzen festlegen:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Benchmark- und Leistungsoptimierung

Testen Sie den CPU-Durchsatz auf Ihrem Computer:
```bash
fcrackzip -B
```
---

## ZipCrypto vs. AES-256 (John/Hashcat-Alternative)

Wenn `fcrackzip` `not a zipfile, or unsupported encryption type` zurückgibt, ist das Archiv mit AES-256 (WinZip AES) verschlüsselt. Verwenden Sie `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```