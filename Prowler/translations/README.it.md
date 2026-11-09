<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="toniblyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Cloud">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<palign="centro">
  <b>🌩️ Strumento di sicurezza open source standard di settore per AWS, Azure, Google Cloud (GCP) e valutazione del comportamento di Kubernetes, controllo degli errori di configurazione e verifica della conformità</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione](#-installazione)
- [Auditing di sicurezza AWS](#-aws-security-auditing)
  - [Controllo account predefinito](#default-account-audit)
  - [Destinazione di servizi specifici (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  - [Destinazione a regioni AWS specifiche](#targeting-specific-aws-regions)
  - [Scansione multi-account con AWS Organizations](#scansione-multi-account-con-aws-organizations)
- [Controllo di Azure e Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Valutazione di Microsoft Azure](#microsoft-azure-valutazione)
  - [Valutazione di Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Quadro di conformità (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Formati di output e report (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e riparazione](#-considerazioni-difensive--riparazione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Creato originariamente da **Toni de la Fuente (@toniblyx)**, **Prowler** è uno strumento essenziale di valutazione della sicurezza multi-cloud. Esegue valutazioni automatizzate sull'infrastruttura come servizio (IaaS) e sui servizi della piattaforma per rilevare:
- Account di archiviazione e bucket S3 leggibili/scrivibili pubblicamente.
- Account root privi di autenticazione a più fattori (MFA).
- Policy IAM con privilegi eccessivi con autorizzazioni `*` con caratteri jolly.
- Database, dischi e backup non crittografati.
- Gruppi di sicurezza aperti che espongono le porte di gestione (SSH 22, RDP 3389) a `0.0.0.0/0`.
- Non conformità con i benchmark CIS, ISO 27001, HIPAA, PCI-DSS e GDPR.

---

## 📥Installazione
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

## ☁️ Controllo della sicurezza AWS

Assicurati che nel tuo ambiente siano configurate credenziali AWS valide (`aws configure`, variabili di ambiente o profilo dell'istanza IAM):

### Controllo account predefinito
Esegue tutti i controlli integrati in tutti i servizi e le regioni abilitati:
```bash
prowler aws
```
### Targeting di servizi specifici (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Targeting per regioni AWS specifiche
Evita di scansionare regioni vuote inutilizzate per aumentare notevolmente la velocità di scansione:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Scansione di più account con AWS Organizations
Controlla tutti gli account di un'organizzazione AWS assumendo un ruolo di gestione centralizzata:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Controllo di Azure e Google Cloud (GCP).

### Valutazione di Microsoft Azure
Autenticazione tramite l'interfaccia della riga di comando di Azure (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Valutazione di Google Cloud Platform (GCP).
Autenticazione tramite Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Quadri di conformità (CIS, ISO27001, PCI-DSS)

Prowler classifica i controlli in standard di conformità ufficiali:
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

## 📊 Reporting e formati di output (HTML, JSON, CSV)

Per impostazione predefinita, Prowler invia i report a una directory `output/`:
- **HTML interattivo:** Dashboard visiva che mostra i controlli non riusciti, superati e disattivati.
- **CSV:** Facile da importare in fogli di calcolo o sistemi di biglietteria.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub e telemetria compatibile con SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Audit AWS completo** | `prowler aws` |
| **Servizio specifico di audit** | `prowler aws --services s3,iam` |
| **Audit Singola Regione** | `prowler aws --region us-east-1` |
| **Audit benchmark CIS** | `prowler aws --compliance cis_3.0_aws` |
| **Controllo completo di Azure** | `prowler azure --subscription-ids <SUB_ID>` |
| **Verifica completa GCP** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Esporta HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Elenco assegni disponibili**| `prowler aws --list-checks` |

---

## 🛡️ Considerazioni difensive e rimedi

1. **Rimedio automatizzato:** esamina i consigli di output di Prowler che forniscono l'esatto comando AWS CLI o il parametro Terraform richiesto per correggere ogni errore di configurazione.
2. **Integrazione AWS Security Hub:** alimenta i risultati JSON-ASFF di Prowler direttamente in AWS Security Hub per avvisi centralizzati di vulnerabilità multi-account.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Prowler è uno strumento di audit e valutazione della postura. Assicurati di disporre dell'autorizzazione adeguata da parte del tenant cloud e dei proprietari dell'abbonamento prima di eseguire scansioni di conformità negli ambienti di produzione.