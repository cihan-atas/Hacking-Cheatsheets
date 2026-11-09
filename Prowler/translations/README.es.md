<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p align="centro">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="tonivelyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multi-Nube">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p align="centro">
  <b>🌩️ Herramienta de seguridad de código abierto estándar del sector para evaluación de postura, auditoría de configuración incorrecta y verificación de cumplimiento de AWS, Azure, Google Cloud (GCP) y Kubernetes</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación](#-instalación)
- [Auditoría de seguridad de AWS](#-aws-security-auditing)
  - [Auditoría de cuenta predeterminada](#auditoría-de-cuenta-predeterminada)
  - [Dirigida a servicios específicos (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  - [Dirección a regiones de AWS específicas](#targeting-specific-aws-regions)
  - [Escaneo de múltiples cuentas con organizaciones de AWS](#escaneo-de-multicuentas-con-organizaciones-aws)
- [Auditoría de Azure y Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Evaluación de Microsoft Azure](#microsoft-azure-evaluación)
  - [Evaluación de Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Marcos de cumplimiento (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Formatos de informes y salida (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y remediación](#-consideraciones-defensivas--remediación)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Creado originalmente por **Toni de la Fuente (@tonivelyx)**, **Prowler** es una herramienta esencial de evaluación de seguridad multinube. Realiza evaluaciones automatizadas en infraestructura como servicio (IaaS) y servicios de plataforma para detectar:
- Cuentas de almacenamiento y depósitos S3 de lectura/escripción pública.
- A las cuentas raíz les falta la autenticación multifactor (MFA).
- Políticas de IAM con privilegios excesivos y permisos comodín `*`.
- Bases de datos, discos y copias de seguridad no cifrados.
- Grupos de seguridad abiertos que exponen los puertos de administración (SSH 22, RDP 3389) a `0.0.0.0/0`.
- Incumplimiento de los Benchmarks CIS, ISO 27001, HIPAA, PCI-DSS y GDPR.

---

## 📥 Instalación
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

## ☁️ Auditoría de seguridad de AWS

Asegúrese de que su entorno tenga configuradas credenciales de AWS válidas (`aws configure`, variables de entorno o perfil de instancia de IAM):

### Auditoría de cuenta predeterminada
Ejecuta todas las comprobaciones integradas en todos los servicios y regiones habilitados:
```bash
prowler aws
```
### Orientación a servicios específicos (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Dirigido a regiones específicas de AWS
Evite escanear regiones vacías y no utilizadas para aumentar drásticamente la velocidad de escaneo:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Escaneo de múltiples cuentas con AWS Organizations
Audite todas las cuentas de una organización de AWS asumiendo una función de administración centralizada:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Auditoría de Azure y Google Cloud (GCP)

### Evaluación de Microsoft Azure
Autenticar a través de la CLI de Azure (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Evaluación de Google Cloud Platform (GCP)
Autenticar a través del SDK de Google Cloud (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Marcos de cumplimiento (CIS, ISO27001, PCI-DSS)

Prowler clasifica los controles en estándares de cumplimiento oficiales:
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

## 📊 Formatos de informes y salida (HTML, JSON, CSV)

De forma predeterminada, Prowler genera informes en un directorio `output/`:
- **HTML interactivo:** Panel visual que muestra comprobaciones fallidas, aprobadas y silenciadas.
- **CSV:** Fácil de importar a hojas de cálculo o sistemas de emisión de tickets.
- **JSON-ASFF / JSON-OCSF:** AWS Security Hub y telemetría compatible con SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Auditoría completa de AWS** | `prowler aws` |
| **Servicio específico de auditoría** | `prowler aws --services s3,iam` |
| **Auditoría de una sola región** | `prowler aws --region us-east-1` |
| **Auditoría de referencia del CIS** | `prowler aws --compliance cis_3.0_aws` |
| **Auditoría completa de Azure** | `prowler azure --subscription-ids <SUB_ID>` |
| **Auditoría completa de GCP** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Exportar HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Lista de cheques disponibles**| `prowler aws --list-checks` |

---

## 🛡️ Consideraciones defensivas y remediación

1. **Remediación automatizada:** Revise las recomendaciones de salida de Prowler que proporcionan el comando AWS CLI exacto o el parámetro de Terraform necesarios para corregir cada configuración incorrecta.
2. **Integración de AWS Security Hub:** Introduzca los hallazgos de Prowler JSON-ASFF directamente en AWS Security Hub para recibir alertas centralizadas de vulnerabilidades de múltiples cuentas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Prowler es una herramienta de auditoría y evaluación de postura. Asegúrese de tener la autorización adecuada del inquilino de la nube y de los propietarios de la suscripción antes de ejecutar análisis de cumplimiento en los entornos de producción.