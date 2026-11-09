<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<palign="centro">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<palign="centro">
  <b>🌲 Enumerare ed eseguire il dump di zone e record Active Directory Integrated DNS (ADIDNS) su LDAP senza trasferimenti di zona DNS (AXFR)</b>
</p>

---

## 📋 Sommario

- [Panoramica e contesto tecnico](#-panoramica--contesto-tecnico)
- [Installazione](#-installazione)
- [Metodi di autenticazione](#-metodi-di-autenticazione)
  - [Autenticazione tramite password](#autenticazione-password)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Biglietto Kerberos (ccache)](#kerberos-ticket-ccache)
- [Comandi di dumping principale](#-comandi-core-dumping)
  - [Dump della zona DNS Active Directory standard](#standard-active-directory-dns-zone-dump)
  - [Dumping dei record rimossi/eliminati](#dumping-tombstoned--deleted-records)
  - [Dumping dei DNS della foresta e delle zone DNS del dominio](#dumping-forest-dns--domain-dns-zones)
  - [Targeting per zone DNS specifiche](#targeting-specific-dns-zones)
- [Formati di esportazione e post-elaborazione](#-export-formats--post-elaborazione)
  - [CSV Output](#csv-output)
  - [Integrazione con BloodHound e Nmap](#integrating-with-bloodhound--nmap)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Overview & Technical Background

Negli ambienti Windows Active Directory standard, il DNS è solitamente integrato in Active Directory (**Active Directory-Integrated DNS/ADIDNS**). In queste configurazioni, i record DNS non vengono archiviati in semplici file flat di testo, ma come oggetti LDAP all'interno della partizione del dominio (come `DC=DomainDnsZones` e `DC=ForestDnsZones`).

I trasferimenti di zona DNS tradizionali (`AXFR`) sono quasi universalmente disabilitati sui controller di dominio. Tuttavia, **qualsiasi utente di dominio autenticato** (anche account con privilegi limitati o account di servizio compromessi) dispone delle autorizzazioni di lettura sui nodi LDAP che memorizzano i record ADIDNS per impostazione predefinita.

Sviluppato da **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** si connette al controller di dominio tramite LDAP o LDAPS, interroga le partizioni ADIDNS ed estrae tutti i record (A, AAAA, CNAME, SRV, TXT, MX e record contrassegnati per rimozione definitiva), creando un inventario accurato di ogni computer, workstation e servizio Web interno nel dominio.

---

## 📥Installazione
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Metodi di autenticazione

### Autenticazione tramite password
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Passa l'hash (NTLM)
Se hai estratto gli hash NTLM tramite Mimikatz o Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Ticket Kerberos (ccache)
Utilizzando un ticket Kerberos TGT/TGS esistente archiviato in `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Comandi di dumping del nucleo

### Dump della zona DNS di Active Directory standard
Scarica tutti i record DNS attivi dalla partizione DomainDnsZones predefinita:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Questo genera un file denominato `records.csv` nella directory di lavoro corrente.

### Dump dei record rimossi/eliminati
Il DNS di Windows contrassegna i record eliminati come "rimossi" per diversi giorni prima dell'eliminazione fisica. Questi record spesso rivelano sistemi dismessi, server legacy o ambienti di test:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping dei DNS della foresta e delle zone DNS del dominio
Estrai record da partizioni DNS a livello di foresta:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Targeting per zone DNS specifiche
Se il target gestisce più zone o sottodomini DNS split-brain:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Comunicazione crittografata LDAPS sicura
Impedisci alle credenziali di attraversare la rete in testo normale:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Formati di esportazione e post-elaborazione

### Uscita CSV
Per impostazione predefinita, `adidnsdump` emette `records.csv`. I campi includono:
- `zone`: la zona DNS a cui appartiene il record.
- `name`: registra il nome host o l'etichetta.
- `type`: tipo di record (A, AAAA, CNAME, SRV, ecc.).
- `value`: indirizzo IP, nome host o stringa di destinazione.

### Integrazione con BloodHound e Nmap
Estrai tutti gli indirizzi IP univoci dai record scaricati per la scansione automatizzata:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Dump ADIDNS standard** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Passa l'Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Autenticazione biglietto Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Elimina i record rimossi**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Zone forestali discarica** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Specificare la zona target** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Applica LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Monitoraggio delle query LDAP:** abilita i criteri di controllo avanzati per l'accesso al servizio directory (ID evento 4662) per avvisare in caso di query ad alto volume su `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Limita ACL ADIDNS:** Limita le autorizzazioni di lettura sul contenitore `MicrosoftDNS` agli amministratori di dominio o a ruoli amministrativi specifici se i non amministratori non richiedono l'introspezione DNS interna.
3. **Honeytokens:** imposta record DNS attraenti (ad esempio, `backup-vault.corp.local` o `admin-portal.corp.local`) che puntano a indirizzi IP honeypot.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> adidnsdump è destinato esclusivamente a valutazioni di sicurezza autorizzate, controlli di Active Directory e test di penetrazione negli ambiti consentiti. Interrogare servizi di directory su reti non autorizzate è illegale.