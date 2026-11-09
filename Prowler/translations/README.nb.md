<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p align="center">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="center">
  <b>🌩️ Bransjestandard åpen kildekode-sikkerhetsverktøy for AWS, Azure, Google Cloud (GCP) og Kubernetes stillingsvurdering, revisjon av feilkonfigurasjoner og samsvarsverifisering</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon](#-installasjon)
– [AWS Security Auditing](#-aws-security-auditing)
  – [Standard kontorevisjon](#default-account-audit)
  - [Målrettingsspesifikke tjenester (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  – [Targeting Specific AWS Regions](#targeting-specific-aws-regions)
  – [Multi-Account Scanning with AWS Organizations](#multi-account-scanning-with-aws-organizations)
– [Azure & Google Cloud (GCP) Auditing](#-azure--google-cloud-gcp-auditing)
  - [Microsoft Azure Assessment](#microsoft-azure-assessment)
  – [Google Cloud Platform (GCP) Assessment](#google-cloud-platform-gcp-assessment)
- [Compliance Frameworks (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Rapportering og utdataformater (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Opprinnelig laget av **Toni de la Fuente (@toniblyx)**, **Prowler** er et viktig sikkerhetsvurderingsverktøy for flere skyer. Den utfører automatiserte evalueringer på tvers av infrastruktur-som-en-tjeneste (IaaS) og plattformtjenester for å oppdage:
- Offentlig lesbare/skrivbare S3-bøtter og lagringskontoer.
- Rotkontoer mangler Multi-Factor Authentication (MFA).
- Overprivilegerte IAM-policyer med jokertegn `*`-tillatelser.
- Ukrypterte databaser, disker og sikkerhetskopier.
- Åpne sikkerhetsgrupper som utsetter administrasjonsporter (SSH 22, RDP 3389) for `0.0.0.0/0`.
- Manglende overholdelse av CIS Benchmarks, ISO 27001, HIPAA, PCI-DSS og GDPR.

---

## 📥 Installasjon
```bash
# Using pip / pipx (Recommended)
pipx install prowler
# Or
pip install prowler

# Using Docker
docker pull prowlercloud/prowler:latest

# Verify installation
prowler -v
```
---

## ☁️ AWS-sikkerhetsrevisjon

Sørg for at miljøet ditt har konfigurert gyldig AWS-legitimasjon (`aws configure`, miljøvariabler eller IAM-forekomstprofil):

### Standard kontorevisjon
Kjører alle innebygde sjekker på tvers av alle aktiverte tjenester og regioner:
```bash
prowler aws
```
### Målretting mot spesifikke tjenester (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```

```bash
prowler aws --region us-east-1,eu-west-1
```
### Skanning av flere kontoer med AWS-organisasjoner
Revider alle kontoer under en AWS-organisasjon ved å påta seg en sentralisert ledelsesrolle:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Azure og Google Cloud (GCP) revisjon

### Microsoft Azure-vurdering
Autentiser via Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```

```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Samsvarsrammeverk (CIS, ISO27001, PCI-DSS)

Prowler kategoriserer sjekker i offisielle samsvarsstandarder:
```bash
# Run CIS AWS Foundations Benchmark v3.0
prowler aws --compliance cis_3.0_aws

# Run PCI-DSS Compliance Audit
prowler aws --compliance pci_3.2.1_aws

# Run ISO 27001 Evaluation
prowler aws --compliance iso27001_2013_aws

# List all supported compliance frameworks
prowler aws --list-compliance
```
---

## 📊 Rapporterings- og utdataformater (HTML, JSON, CSV)

Som standard sender Prowler ut rapporter til en `output/`-katalog:
- **Interaktiv HTML:** Visuelt dashbord som viser feilslåtte, beståtte og dempet kontroller.
- **CSV:** Enkel å importere til regneark eller billettsystemer.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub og SIEM-kompatibel telemetri.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Full AWS-revisjon** | `prowler aws` |
| **Revisjonsspesifikk tjeneste** | `prowler aws --services s3,iam` |
| **Revisjon enkelt region** | `prowler aws --region us-east-1` |
| **CIS Benchmark Audit** | `prowler aws --compliance cis_3.0_aws` |
| **Full Azure Audit** | `prowler azure --subscription-ids <SUB_ID>` |
| **Full GCP-revisjon** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Eksporter HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Liste tilgjengelige sjekker**| `prowler aws --list-checks` |

---

## 🛡️ Defensive vurderinger og utbedring

1. **Automatisk utbedring:** Se gjennom Prowlers utdataanbefalinger som gir den nøyaktige AWS CLI-kommandoen eller Terraform-parameteren som kreves for å fikse hver feilkonfigurasjon.
2. **AWS Security Hub-integrasjon:** Mate Prowler JSON-ASFF-funn direkte inn i AWS Security Hub for sentralisert flerkontosårbarhetsvarsling.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Prowler is an audit and posture assessment tool. Sørg for at du har riktig autorisasjon fra skyleietaker og abonnementseiere før du utfører samsvarsskanninger på tvers av produksjonsmiljøer.