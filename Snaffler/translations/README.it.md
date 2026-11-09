<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<palign="centro">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licenza GPLv3">
</p>

<palign="centro">
  <b>🌐 Strumento di dominio Windows automatizzato progettato per eseguire la scansione di condivisioni SMB, individuare file di configurazione interessanti, database KeePass e credenziali in testo normale</b>
</p>

---

## 📋 Sommario

- [Panoramica e problema della caccia alle azioni](#-panoramica--il-problema-della-caccia alle azioni)
- [Come funziona Snaffler](#-how-snaffler-funziona)
- [Opzioni di installazione ed esecuzione](#-installazione--opzioni-esecuzione)
- [Triage e livelli di gravità di Snaffler](#-snaffler-triage--livelli di gravità)
- [Flussi di lavoro di scansione comuni](#-common-scanning-workflows)
  - [Scansione della condivisione a livello di dominio](#scansione della condivisione a livello di dominio)
  - [Destinazione di host o condivisioni specifiche](#targeting-specific-hosts-or-shares)
  - [Esclusi computer e root DFS](#esclusi-computer--dfs-roots)
- [Target Juicy e regole di corrispondenza file](#-juicy-targets--file-match-rules)
- [Registrazione e analisi dell'output](#-output-logging--analysis)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e problema della caccia alle azioni

Negli ambienti Active Directory di medie e grandi dimensioni, i file server e le workstation aziendali ospitano migliaia di condivisioni di rete SMB aperte. Gli amministratori di sistema, gli sviluppatori e i dipendenti spesso lasciano indietro:

- 🔑 **Database KeePass (`.kdbx`):** Spesso contengono password principali o chiavi principali facilmente crackabili.
- ⚙️ **File di configurazione (`web.config`, `appsettings.json`):** contenenti stringhe di connessione SQL codificate, password degli account di servizio e chiavi API.
- 📜 **Script di distribuzione (`unattend.xml`, `.ps1`, `.bat`):** Credenziali amministrative codificate.
- 🔏 **Certificati privati ​​(`.pfx`, `.p12`, `.pem`, `.ppk`):** Utilizzabili per l'escalation dei privilegi o lo spostamento laterale.

La navigazione manuale di migliaia di condivisioni è impossibile. **Snaffler**, sviluppato da **l33tb455**, automatizza l'intero processo: interroga Active Directory per gli oggetti del computer, enumera le condivisioni SMB leggibili, percorre l'albero delle directory e valuta i file rispetto a set di regole regex curate.

---

## ⚙️Come funziona Snaffler
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Opzioni di installazione ed esecuzione

Snaffler è compilato come binario .NET autonomo:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### In esecuzione su Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Esecuzione tramite Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Triage e livelli di gravità di Snaffler

Snaffler classifica i risultati in categorie di urgenza codificate a colori:

- 🔴 **`{Red}` / Nero:** Ritrovamento decisamente di alto valore! Password in testo normale, chiavi private (`.pfx`, `.id_rsa`), token AWS, file di installazione automatica.
- 🟡 **`{Yellow}`:** Probabile reperto. File di configurazione con stringhe di password, stringhe di connessione al database, copie shadow.
- 🟢 **`{Green}`:** Informativo/Interesse. Nomi di file corrispondenti ai modelli amministrativi (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Flussi di lavoro di scansione comuni

### Scansione della condivisione a livello di dominio

Esegui la scansione di tutte le condivisioni accessibili nell'intero dominio Active Directory:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Targeting per host o condivisioni specifici

Quando vuoi evitare la scansione dell'intero dominio e concentrarti su un file server o un controller di dominio:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Esclusi computer e root DFS

Evita la scansione di computer lenti o sensibili:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Obiettivi succosi e regole di corrispondenza dei file

Per impostazione predefinita, Snaffler cerca le seguenti estensioni critiche:

| Estensione/File | Contenuti di alto valore |
|------------------|--------------------|
| `.kdbx` / `.kdb` | Casseforti di gestione password KeePass |
| `web.config` / `app.config` | Password del database in chiaro |
| `unattend.xml` / `sysprep.inf` | Password amministratore di Windows |
| `.ppk` / `.pem` / `id_rsa` | Chiavi private SSH |
| `.pfx` / `.p12` | Certificati Active Directory |
| `.rdp` / `.vnc` | Profili di connessione desktop remoto |
| `passwords.xlsx` / `creds.txt` | Elenchi delle password dei dipendenti |

---

## 📊 Registrazione e analisi dell'output
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione a livello di dominio** | `Snaffler.exe -s -o out.log` |
| **Computer singolo di destinazione** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Quota specifica target** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Ottimizza i thread di concorrenza**| `Snaffler.exe -s -t 120 -o out.log` |
| **Escludi host dalla scansione** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Limite massimo della dimensione del file**| `Snaffler.exe -s -z 5000000` *(Limite a 5 MB di file)* |
| **Esegui tramite assembly C2**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Enormi picchi di connessione SMB:** Snaffler si connette alla porta 445 su ogni computer del dominio in pochi minuti. Ciò genera migliaia di ID evento 4624 (tipo di accesso 3 riuscito - Rete) e ID evento 5140 (è stato effettuato l'accesso a un oggetto di condivisione di rete).
2. **Honeyfiles & Decoys:** i moderni difensori di Active Directory distribuiscono condivisioni Canary ed esca file `.kdbx` che attivano avvisi SOC istantanei quando vengono aperti da strumenti come Snaffler.
3. **Limiti di esecuzione:** imposta `-z` (dimensione massima in byte del file) per evitare il crash delle condivisioni di rete leggendo file di backup ISO o VHD multi-gigabyte.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Snaffler è progettato esclusivamente per test di penetrazione autorizzati, impegni di team rossi e audit di sicurezza interni. La scansione delle condivisioni di dominio senza autorizzazione costituisce violazione illegale della rete.