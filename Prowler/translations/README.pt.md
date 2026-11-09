<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌩️ Prowler - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗    ██╗██╗     ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔═══██╗██║    ██║██║     ██╔════╝██╔══██╗
  ██████╔╝██████╔╝██║   ██║██║ █╗ ██║██║     █████╗  ██████╔╝
  ██╔═══╝ ██╔══██╗██║   ██║██║███╗██║██║     ██╔══╝  ██╔══██╗
  ██║     ██║  ██║╚██████╔╝╚███╔███╔╝███████╗███████╗██║  ██║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝  ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝  ╚═╝
       Multi-Cloud Security Posture Assessment & CIS Benchmark Auditor
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Prowler-Cloud_Security-blue?style=for-the-badge" alt="Prowler">
  <img src="https://img.shields.io/badge/Author-Toni_de_la_Fuente-orange?style=for-the-badge" alt="tonablyx">
  <img src="https://img.shields.io/badge/Clouds-AWS_%7C_Azure_%7C_GCP_%7C_K8s-gold?style=for-the-badge" alt="Multinuvem">
  <img src="https://img.shields.io/badge/Compliance-CIS_Benchmark-green?style=for-the-badge" alt="CIS">
</p>

<p alinhar="centro">
  <b>🌩️ Ferramenta de segurança de código aberto padrão do setor para AWS, Azure, Google Cloud (GCP) e avaliação de postura do Kubernetes, auditoria de configuração incorreta e verificação de conformidade</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação](#-instalação)
- [Auditoria de segurança da AWS](#-aws-security-auditing)
  - [Auditoria de conta padrão](#default-account-audit)
  - [Segmentação de serviços específicos (S3, IAM, EC2)](#targeting-specific-services-s3-iam-ec2)
  - [Segmentando regiões específicas da AWS](#targeting-specific-aws-regions)
  - [Verificação de várias contas com organizações AWS](#scanning-multi-account-with-aws-organizations)
- [Auditoria do Azure e do Google Cloud (GCP)](#-azure--google-cloud-gcp-auditing)
  - [Avaliação do Microsoft Azure](#microsoft-azure-assessment)
  - [Avaliação do Google Cloud Platform (GCP)](#google-cloud-platform-gcp-assessment)
- [Estruturas de conformidade (CIS, ISO27001, PCI-DSS)](#-compliance-frameworks-cis-iso27001-pci-dss)
- [Relatórios e formatos de saída (HTML, JSON, CSV)](#-reporting--output-formats-html-json-csv)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Remediação](#-considerações-defensivas--remediação)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Originalmente criado por **Toni de la Fuente (@tonablyx)**, o **Prowler** é uma ferramenta essencial de avaliação de segurança multinuvem. Ele realiza avaliações automatizadas em infraestrutura como serviço (IaaS) e serviços de plataforma para detectar:
- Buckets S3 e contas de armazenamento publicamente legíveis/graváveis.
- Contas raiz sem autenticação multifator (MFA).
- Políticas IAM com privilégios excessivos com permissões curinga `*`.
- Bancos de dados, discos e backups não criptografados.
- Abra grupos de segurança expondo portas de gerenciamento (SSH 22, RDP 3389) para `0.0.0.0/0`.
- Não conformidade com Benchmarks CIS, ISO 27001, HIPAA, PCI-DSS e GDPR.

---

## 📥 Instalação
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

## ☁️ Auditoria de segurança AWS

Certifique-se de que seu ambiente tenha credenciais válidas da AWS configuradas (`aws configure`, variáveis de ambiente ou perfil de instância do IAM):

### Auditoria de conta padrão
Executa todas as verificações integradas em todos os serviços e regiões habilitados:
```bash
prowler aws
```
### Direcionando serviços específicos (S3, IAM, EC2)
```bash
# Run only S3 bucket checks
prowler aws --services s3

# Run IAM and EC2 checks together
prowler aws --services iam,ec2
```
### Visando regiões específicas da AWS
Evite digitalizar regiões vazias não utilizadas para aumentar drasticamente a velocidade de digitalização:
```bash
prowler aws --region us-east-1,eu-west-1
```
### Verificação de várias contas com organizações AWS
Audite todas as contas de uma organização AWS assumindo uma função de gerenciamento centralizado:
```bash
prowler aws --sp-role-arn arn:aws:iam::111122223333:role/SecurityAuditRole
```
---

## 🔷 Auditoria Azure e Google Cloud (GCP)

### Avaliação do Microsoft Azure
Autenticar via CLI do Azure (`az login`):
```bash
# Audit entire Azure subscription
prowler azure --subscription-ids "00000000-0000-0000-0000-000000000000"

# Target specific services (Storage, Entra ID / IAM)
prowler azure --services storage,iam
```
### Avaliação do Google Cloud Platform (GCP)
Autenticar por meio do SDK do Google Cloud (`gcloud auth application-default login`):
```bash
# Audit specific GCP Project
prowler gcp --project-ids my-production-project

# Target Google Cloud Storage
prowler gcp --services storage
```
---

## 📜 Estruturas de conformidade (CIS, ISO27001, PCI-DSS)

O Prowler categoriza as verificações em padrões oficiais de conformidade:
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

## 📊 Relatórios e formatos de saída (HTML, JSON, CSV)

Por padrão, o Prowler gera relatórios para um diretório `output/`:
- **HTML interativo:** Painel visual mostrando verificações com falha, aprovadas e silenciadas.
- **CSV:** Fácil de importar para planilhas ou sistemas de tickets.
- **JSON-ASFF/JSON-OCSF:** AWS Security Hub e telemetria compatível com SIEM.
```bash
# Generate HTML and JSON reports in a custom folder
prowler aws -M html,json,csv -o /path/to/audit-reports/
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Auditoria completa da AWS** | `prowler aws` |
| **Serviço Específico de Auditoria** | `prowler aws --services s3,iam` |
| **Auditoria de região única** | `prowler aws --region us-east-1` |
| **Auditoria de referência CIS** | `prowler aws --compliance cis_3.0_aws` |
| **Auditoria completa do Azure** | `prowler azure --subscription-ids <SUB_ID>` |
| **Auditoria GCP completa** | `prowler gcp --project-ids <PROJECT_ID>` |
| **Exportar HTML/CSV/JSON** | `prowler aws -M html,csv,json -o ./reports` |
| **Listar cheques disponíveis**| `prowler aws --list-checks` |

---

## 🛡️ Considerações defensivas e remediação

1. **Correção automatizada:** Revise as recomendações de saída do Prowler que fornecem o comando exato da AWS CLI ou o parâmetro do Terraform necessário para corrigir cada configuração incorreta.
2. **Integração do AWS Security Hub:** Alimente as descobertas do Prowler JSON-ASFF diretamente no AWS Security Hub para alertas centralizados de vulnerabilidade de várias contas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Prowler é uma ferramenta de auditoria e avaliação de postura. Certifique-se de ter a autorização adequada do locatário da nuvem e dos proprietários da assinatura antes de executar verificações de conformidade em ambientes de produção.