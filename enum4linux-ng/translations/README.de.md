<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# enum4linux-ng Cheatsheet

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) ist eine Neufassung der nächsten Generation des in Python 3 geschriebenen Legacy-Tools `enum4linux`. Es führt eine umfassende Windows- und Samba-Informationserfassung über SMB (Server Message Block), NetBIOS, RPC (Remote Procedure Call) und LDAP durch. Es extrahiert Benutzerkonten, Gruppenmitgliedschaften, freigegebene Ordner, Passwortrichtlinien, Betriebssysteminformationen und RID-Zyklen.

---

## Inhaltsverzeichnis

- [Wichtige Verbesserungen gegenüber Legacy enum4linux](#key-improvements-over-legacy-enum4linux)
- [Installation & Anforderungen](#installation--requirements)
- [Grundlegende SMB- und Domänenaufzählung](#basic-smb--domain-enumeration)
- [Authentifizierungsmodi (Nullsitzung vs. gültiger Benutzer)](#authentication-modes-null-session-vs-valid-user)
- [Gezielte Aufzählungsmodule](#targeted-enumeration-modules)
- [RID Cycling & User Account Harvesting](#rid-cycling--user-account-harvesting)
- [Berichte exportieren (JSON, YAML, Text)](#exporting-reports-json-yaml-text)
- [Defensive Hardening für SMB & RPC](#defensive-hardening-for-smb--rpc)

---

## Wichtige Verbesserungen gegenüber dem Vorgänger enum4linux

- **Moderne Python 3-Engine:** Multithread-Ausführung basierend auf `impacket`, wodurch alte Perl-Skriptabstürze vermieden werden.
- **Verbesserte Betriebssystemerkennung:** Präzise Erkennung von Windows Server (2012–2025), Windows 10/11 und modernen Samba-Versionen.
- **Strukturierte Berichterstattung:** Nativer Export nach JSON und YAML für eine nahtlose SIEM- oder Skripting-Integration.
- **Intelligenteres RID-Cycling:** Flexible Bereichsanpassungen und automatische Administrator-/Gast-RID-Erkennung.
- **Farbige CLI-Ausgabe:** Klare visuelle Indikatoren für Sicherheitsfehlkonfigurationen und Nullsitzungen.

---

## Installation und Anforderungen
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## Grundlegende SMB- und Domänenaufzählung

### 1. Standardaufzählungslauf (`-A`)
Führen Sie alle nicht-invasiven Aufzählungsprüfungen für das Ziel aus:
```bash
enum4linux-ng -A 192.168.1.50
```
Dieses Modul fragt Folgendes ab:
- NetBIOS-Namen und Arbeitsgruppen-/Domänennamen.
- Betriebssystemversion und -architektur.
- Freigegebene Verzeichnisse und Zugriffsberechtigungen.
- Passwortrichtlinie (Sperrschwelle, Mindestlänge, Verlauf).
- Benutzer und lokale Gruppen über RPC/SAMR.

---

## Authentifizierungsmodi (Nullsitzung vs. gültiger Benutzer)

### 1. Anonyme/Null-Sitzung (`-u '' -p ''`)
Testen Sie, ob das Ziel nicht authentifizierte IPC$-Verbindungen zulässt:
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Authentifizierte Domäne / lokales Konto
Geben Sie gültige Anmeldeinformationen mit geringen Berechtigungen an, um die erfassten Daten erheblich zu erweitern:
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Spezifischen Domänennamen angeben (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Gezielte Aufzählungsmodule

Führen Sie bestimmte Module aus, um den Netzwerk-Fußabdruck zu minimieren und Warnungen bei der Erkennung von Eindringlingen zu vermeiden:

### 1. Freigegebene Ordner und Zugriffsberechtigungen (`-S`)
Listen Sie Freigaben und Kommentare auf und prüfen Sie den Lese-/Schreibzugriff:
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Extraktion der Passwortrichtlinie (`-P`)
Identifizieren Sie Schwellenwerte für die Kontosperrung, um das Passwort-Spraying sicher zu planen:
```bash
enum4linux-ng -P 192.168.1.50
```
Wichtige zu beobachtende Ergebnisse:
- **Mindestlänge des Passworts:** z. B. 8 Zeichen.
- **Kontosperrschwelle:** z. B. 5 Versuche (nicht schneller als das Sperrfenster sprühen!).
- **Sperrzähler zurücksetzen:** z. B. 30 Minuten.

### 3. Nur Benutzer und Gruppen (`-U` und `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Nur Betriebssystem- und NetBIOS-Informationen (`-O` und `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## RID Cycling & User Account Harvesting

Wenn die Benutzeraufzählung über Standard-RPC-Abfragen eingeschränkt ist, führen Sie einen Relative Identifier (RID)-Zyklus über SAMR-Abfrageaufrufe durch:

### 1. Standard-RID-Cycling (Bereich 500–1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Benutzerdefinierter RID-Bereich (z. B. 500 bis 5000 für große Organisationen)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Extrahieren Sie die saubere Benutzernamen-Wortliste
Filtern Sie entdeckte RID-Benutzernamen in eine Textdatei zum Passwort-Spraying:
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Berichte exportieren (JSON, YAML, Text)

Exportieren Sie Aufzählungsergebnisse zur Dokumentation oder zum programmgesteuerten Parsen:

### 1. In allen Formaten gleichzeitig exportieren (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Erzeugt:
- `/tmp/target_recon.json`
- `/tmp/target_recon.yaml`
- `/tmp/target_recon.txt`

### 2. In ein bestimmtes Format exportieren
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Defensive Härtung für SMB und RPC

| Vektor | Risiko | Härtungsempfehlung |
|---|---|---|
| **Null-Sitzung (Anonymer IPC$)** | Aufklärung nicht authentifizierter Domänen | Legen Sie `RestrictNullSessAccess=1` in der Windows-Registrierung fest. Konfigurieren Sie `RestrictAnonymous=2` in SAM. |
| **SMBv1 aktiv** | EternalBlue (MS17-010) & MITM-Angriffe | Deaktivieren Sie SMBv1 vollständig auf allen Endpunkten und Domänencontrollern. |
| **Klartext-Freigabeberechtigungen** | Sensible Dateien und Anmeldeinformationen offengelegt | Freigabeberechtigungen prüfen; Erzwingen Sie die zugriffsbasierte Aufzählung (ABE). |
| **RID-Cycling-Enumeration** | Vollständige Extraktion der Benutzerkontenliste | Beschränken Sie die Aufzählung anonymer SAM-Konten über die lokale Sicherheitsrichtlinie. |