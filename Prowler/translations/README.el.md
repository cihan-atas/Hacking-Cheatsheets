<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
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
  <b>🌩️ Εργαλείο ασφαλείας ανοιχτού κώδικα βιομηχανικού προτύπου για AWS, Azure, Google Cloud (GCP) και Kubernetes αξιολόγηση στάσης, έλεγχος εσφαλμένης διαμόρφωσης και επαλήθευση συμμόρφωσης</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση] (#-εγκατάσταση)
- [AWS Security Auditing](#-aws-security-auditing)
  - [Προεπιλεγμένος έλεγχος λογαριασμού](#default-account-audit)
  - [Targeting Specific Services (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  - [Targeting Specific AWS Regions](#targeting-specific-aws-regions)
  - [Σάρωση πολλαπλών λογαριασμών με οργανισμούς AWS](#multi-account-scanning-with-aws-organizations)
- [Έλεγχος Azure & Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Microsoft Azure Assessment](#microsoft-azure-assessment)
  - [Αξιολόγηση Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Πλαίσια Συμμόρφωσης (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Μορφές αναφοράς και εξόδου (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Remediation](#-defensive-considerations--remediation)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Αρχικά δημιουργήθηκε από τον **Toni de la Fuente (@toniblyx)**, το **Prowler** είναι ένα απαραίτητο εργαλείο αξιολόγησης ασφάλειας πολλαπλών νέφους. Εκτελεί αυτοματοποιημένες αξιολογήσεις σε υπηρεσίες υποδομής ως υπηρεσία (IaaS) και πλατφόρμας για τον εντοπισμό:
- Κάδοι S3 και λογαριασμοί αποθήκευσης με δυνατότητα ανάγνωσης/εγγραφής στο κοινό.
- Στους λογαριασμούς ρίζας λείπει έλεγχος ταυτότητας πολλαπλών παραγόντων (MFA).
- Υπερ-προνομιακές πολιτικές IAM με δικαιώματα μπαλαντέρ `*`.
- Μη κρυπτογραφημένες βάσεις δεδομένων, δίσκοι και αντίγραφα ασφαλείας.
- Ανοίξτε τις ομάδες ασφαλείας που εκθέτουν τις θύρες διαχείρισης (SSH 22, RDP 3389) στο `0.0.0.0/0`.
- Μη συμμόρφωση με δείκτες αναφοράς CIS, ISO 27001, HIPAA, PCI-DSS και GDPR.

---

## 📥 Εγκατάσταση
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

## ☁️ Έλεγχος ασφαλείας AWS

Βεβαιωθείτε ότι το περιβάλλον σας έχει διαμορφωμένα έγκυρα διαπιστευτήρια AWS (`aws configure`, μεταβλητές περιβάλλοντος ή προφίλ παρουσίας IAM):

### Προεπιλεγμένος έλεγχος λογαριασμού
Εκτελεί όλους τους ενσωματωμένους ελέγχους σε όλες τις ενεργοποιημένες υπηρεσίες και περιοχές:
```bash
prowler aws
```
### Ειδικές υπηρεσίες στόχευσης (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Στόχευση συγκεκριμένων περιοχών AWS
Αποφύγετε τη σάρωση κενών αχρησιμοποίητων περιοχών για να αυξήσετε δραματικά την ταχύτητα σάρωσης:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Σάρωση πολλών λογαριασμών με οργανισμούς AWS
Ελέγξτε όλους τους λογαριασμούς σε έναν οργανισμό AWS αναλαμβάνοντας έναν κεντρικό ρόλο διαχείρισης:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Έλεγχος Azure & Google Cloud (GCP).

### Microsoft Azure Assessment
Έλεγχος ταυτότητας μέσω Azure CLI (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Αξιολόγηση Google Cloud Platform (GCP).
Έλεγχος ταυτότητας μέσω του Google Cloud SDK (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Πλαίσια Συμμόρφωσης (CIS, ISO27001, PCI-DSS)

Το Prowler κατηγοριοποιεί τους ελέγχους σε επίσημα πρότυπα συμμόρφωσης:
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

## 📊 Μορφές αναφοράς και εξόδου (HTML, JSON, CSV)

Από προεπιλογή, το Prowler εξάγει αναφορές σε έναν κατάλογο `output/`:
- **Διαδραστικό HTML:** Οπτικός πίνακας εργαλείων που δείχνει αποτυχημένες, επιτυχείς και σίγαση ελέγχους.
- **CSV:** Εύκολη εισαγωγή σε υπολογιστικά φύλλα ή συστήματα έκδοσης εισιτηρίων.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub και τηλεμετρία συμβατή με SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Πλήρης Έλεγχος AWS** | `prowler aws` |
| **Ειδική Υπηρεσία Ελέγχου** | `prowler aws --services s3,iam` |
| **Έλεγχος Ενιαίας Περιφέρειας** | `prowler aws --region us-east-1` |
| **Έλεγχος αναφοράς CIS** | `prowler aws --compliance cis_3.0_aws` |
| **Πλήρης Έλεγχος Azure** | `prowler azure --subscription-ids <SUB_ID>` |
| **Πλήρης έλεγχος GCP** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Εξαγωγή HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Λίστα διαθέσιμων επιταγών**| `prowler aws --list-checks` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Αποκατάσταση

1. **Αυτόματη αποκατάσταση:** Εξετάστε τις προτάσεις εξόδου του Prowler που παρέχουν την ακριβή εντολή AWS CLI ή την παράμετρο Terraform που απαιτείται για τη διόρθωση κάθε εσφαλμένης διαμόρφωσης.
2. **Ενσωμάτωση AWS Security Hub:** Feed Prowler JSON-ASFF ευρήματα απευθείας στο AWS Security Hub για κεντρική ειδοποίηση ευπάθειας πολλών λογαριασμών.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Prowler είναι ένα εργαλείο αξιολόγησης ελέγχου και στάσης. Βεβαιωθείτε ότι έχετε την κατάλληλη εξουσιοδότηση από τον μισθωτή του cloud και τους κατόχους συνδρομών προτού εκτελέσετε σαρώσεις συμμόρφωσης σε περιβάλλοντα παραγωγής.