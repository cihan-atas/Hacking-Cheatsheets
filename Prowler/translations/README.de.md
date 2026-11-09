<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="tonablyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="center">
  <b>🌩️ Branchenstandardisiertes Open-Source-Sicherheitstool für AWS, Azure, Google Cloud (GCP) und Kubernetes-Statusbewertung, Fehlkonfigurationsprüfung und Compliance-Überprüfung</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation](#-Installation)
- [AWS-Sicherheitsprüfung](#-aws-security-auditing)
  - [Standardkontoprüfung](#default-account-audit)
  - [Targeting spezifischer Dienste (S3, IAM, EC2)](#targeting-special-services-s3-iam-ec2)
  - [Targeting auf bestimmte AWS-Regionen](#targeting-special-aws-regions)
  - [Multi-Account-Scanning mit AWS Organizations](#multi-account-scanning-with-aws-organizations)
- [Azure- und Google Cloud (GCP)-Prüfung](#-azure--google-cloud-gcp-auditing)
  - [Microsoft Azure-Bewertung](#microsoft-azure-assessment)
  - [Bewertung der Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Compliance Frameworks (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Berichts- und Ausgabeformate (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Abhilfemaßnahmen](#-defensive-überlegungen--remediation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**Prowler** wurde ursprünglich von **Toni de la Fuente (@tonablyx)** entwickelt und ist ein unverzichtbares Multi-Cloud-Sicherheitsbewertungstool. Es führt automatisierte Auswertungen über Infrastructure-as-a-Service (IaaS) und Plattformdienste durch, um Folgendes zu erkennen:
– Öffentlich lesbare/beschreibbare S3-Buckets und Speicherkonten.
– Bei Root-Konten fehlt die Multi-Faktor-Authentifizierung (MFA).
– Überprivilegierte IAM-Richtlinien mit Platzhalter-`*`-Berechtigungen.
- Unverschlüsselte Datenbanken, Festplatten und Backups.
– Öffnen Sie Sicherheitsgruppen, die Verwaltungsports (SSH 22, RDP 3389) für `0.0.0.0/0` verfügbar machen.
- Nichteinhaltung der CIS-Benchmarks, ISO 27001, HIPAA, PCI-DSS und DSGVO.

---

## 📥 Installation
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

## ☁️ AWS-Sicherheitsüberwachung

Stellen Sie sicher, dass in Ihrer Umgebung gültige AWS-Anmeldeinformationen konfiguriert sind (`aws configure`, Umgebungsvariablen oder IAM-Instanzprofil):

### Standard-Kontoprüfung
Führt alle integrierten Prüfungen für alle aktivierten Dienste und Regionen durch:
```bash
prowler aws
```
### Ausrichtung auf bestimmte Dienste (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Ausrichtung auf bestimmte AWS-Regionen
Vermeiden Sie das Scannen leerer ungenutzter Bereiche, um die Scangeschwindigkeit drastisch zu erhöhen:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Scannen mehrerer Konten mit AWS Organizations
Überprüfen Sie alle Konten einer AWS-Organisation, indem Sie eine zentrale Verwaltungsrolle übernehmen:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Azure- und Google Cloud (GCP)-Prüfung

### Microsoft Azure-Bewertung
Authentifizieren Sie sich über Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Bewertung der Google Cloud Platform (GCP).
Authentifizieren Sie sich über das Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Compliance Frameworks (CIS, ISO27001, PCI-DSS)

Prowler kategorisiert Kontrollen in offizielle Compliance-Standards:
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

## 📊 Berichts- und Ausgabeformate (HTML, JSON, CSV)

Standardmäßig gibt Prowler Berichte in ein `output/`-Verzeichnis aus:
- **Interaktives HTML:** Visuelles Dashboard mit fehlgeschlagenen, bestandenen und stummgeschalteten Prüfungen.
- **CSV:** Einfach in Tabellenkalkulationen oder Ticketsysteme zu importieren.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub und SIEM-kompatible Telemetrie.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Vollständiges AWS-Audit** | `prowler aws` |
| **Prüfungsspezifischer Service** | `prowler aws --services s3,iam` |
| **Einzelne Region prüfen** | `prowler aws --region us-east-1` |
| **CIS-Benchmark-Audit** | `prowler aws --compliance cis_3.0_aws` |
| **Vollständiges Azure-Audit** | `prowler azure --subscription-ids <SUB_ID>` |
| **Vollständiges GCP-Audit** | `prowler gcp --project-ids <PROJECT_ID>` |
| **HTML/CSV/JSON exportieren** | `prowler aws -M html,csv,json -o ./reports` |
| **Verfügbare Schecks auflisten**| `prowler aws --list-checks` |

---

## 🛡️ Defensive Überlegungen und Abhilfemaßnahmen

1. **Automatisierte Behebung:** Sehen Sie sich die Ausgabeempfehlungen von Prowler an, die den genauen AWS CLI-Befehl oder Terraform-Parameter enthalten, der zur Behebung jeder Fehlkonfiguration erforderlich ist.
2. **AWS Security Hub-Integration:** Leiten Sie die JSON-ASFF-Ergebnisse von Prowler direkt in AWS Security Hub ein, um zentralisierte Schwachstellenwarnungen für mehrere Konten zu erhalten.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Prowler ist ein Audit- und Haltungsbewertungstool. Stellen Sie sicher, dass Sie über die entsprechende Autorisierung durch den Cloud-Mandanten und die Abonnementbesitzer verfügen, bevor Sie Compliance-Scans in Produktionsumgebungen durchführen.