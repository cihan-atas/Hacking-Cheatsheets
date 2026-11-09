<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p align="centro">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="centro">
  <b>🌲 Enumerar y volcar zonas y registros DNS integrados de Active Directory (ADIDNS) a través de LDAP sin transferencias de zonas DNS (AXFR)</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y antecedentes técnicos] (#-descripción general--antecedentes técnicos)
- [Instalación](#-instalación)
- [Métodos de autenticación](#-métodos-de-autenticación)
  - [Autenticación de contraseña](#autenticación de contraseña)
  - [Pasar-el-hash (NTLM)](#pasar-el-hash-ntlm)
  - [Boleto Kerberos (ccache)](#kerberos-ticket-ccache)
- [Comandos de volcado de núcleo] (#-comandos-de-dumping-de-núcleo)
  - [Volcado de zona DNS estándar de Active Directory](#standard-active-directory-dns-zone-dump)
  - [Volcar registros desechados/eliminados] (#dumping-tombstoned--registros-eliminados)
  - [Vaciado de zonas DNS de dominio y DNS del bosque](#dumping-forest-dns--domain-dns-zones)
  - [Dirigido a zonas DNS específicas](#targeting-specific-dns-zones)
- [Formatos de exportación y posprocesamiento] (#-formatos-de-exportación--postprocesamiento)
  - [Salida CSV](#csv-salida)
  - [Integración con BloodHound y Nmap](#integración-con-bloodhound--nmap)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y antecedentes técnicos

En entornos estándar de Windows Active Directory, DNS suele estar integrado en Active Directory (**DNS integrado en Active Directory/ADIDNS**). En estas configuraciones, los registros DNS no se almacenan en archivos planos de texto simple, sino como objetos LDAP dentro de la partición del dominio (como `DC=DomainDnsZones` y `DC=ForestDnsZones`).

Las transferencias de zonas DNS tradicionales (`AXFR`) están casi universalmente deshabilitadas en los controladores de dominio. Sin embargo, **cualquier usuario de dominio autenticado** (incluso cuentas con pocos privilegios o cuentas de servicio comprometidas) tiene permisos de lectura sobre los nodos LDAP que almacenan registros ADIDNS de forma predeterminada.

Desarrollado por **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** se conecta al controlador de dominio a través de LDAP o LDAPS, consulta las particiones ADIDNS y extrae todos los registros (A, AAAA, CNAME, SRV, TXT, MX y registros desechados), creando un inventario preciso de cada computadora, estación de trabajo y servicio web interno en el dominio.

---

## 📥 Instalación
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Métodos de autenticación

### Autenticación de contraseña
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Pasar el hash (NTLM)
Si extrajiste hashes NTLM a través de Mimikatz o Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Ticket Kerberos (ccache)
Utilizando un ticket Kerberos TGT/TGS existente almacenado en `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Comandos de volcado de núcleos

### Volcado de zona DNS estándar de Active Directory
Vuelca todos los registros DNS activos de la partición DomainDnsZones predeterminada:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Esto genera un archivo llamado `records.csv` en su directorio de trabajo actual.

### Vaciado de registros desechados/eliminados
El DNS de Windows marca los registros eliminados como "desechados" durante varios días antes de la eliminación física. Estos registros suelen revelar sistemas fuera de servicio, servidores heredados o entornos de prueba:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Vaciado de zonas DNS de dominio y DNS de bosque
Extraiga registros de particiones DNS a nivel de bosque:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Dirigido a zonas DNS específicas
Si el objetivo opera múltiples zonas DNS o subdominios de cerebro dividido:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Comunicación cifrada LDAPS segura
Evite que las credenciales atraviesen la red en texto sin formato:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Formatos de exportación y posprocesamiento

### Salida CSV
De forma predeterminada, `adidnsdump` genera `records.csv`. Los campos incluyen:
- `zone`: La zona DNS a la que pertenece el registro.
- `name`: Grabar nombre de host o etiqueta.
- `type`: Tipo de registro (A, AAAA, CNAME, SRV, etc.).
- `value`: dirección IP de destino, nombre de host o cadena.

### Integración con BloodHound y Nmap
Extraiga todas las direcciones IP únicas de los registros volcados para realizar un escaneo automatizado:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Volcado ADIDNS estándar** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pasar el hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Autenticación de ticket Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Volcar registros desechados**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Zonas forestales de vertedero** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Especificar zona objetivo** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Aplicar LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Monitoreo de consultas LDAP:** Habilite la política de auditoría avanzada para el acceso al servicio de directorio (ID de evento 4662) para alertar sobre consultas de gran volumen contra `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Restringir las ACL de ADIDNS:** Restrinja los permisos de lectura en el contenedor `MicrosoftDNS` a administradores de dominio o roles administrativos específicos si los no administradores no requieren introspección DNS interna.
3. **Honeytokens:** Plante registros DNS atractivos (por ejemplo, `backup-vault.corp.local` o `admin-portal.corp.local`) que apunten a direcciones IP de honeypot.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> adidnsdump está destinado estrictamente a evaluaciones de seguridad autorizadas, auditorías de Active Directory y pruebas de penetración dentro de los alcances permitidos. Consultar servicios de directorio en redes no autorizadas es ilegal.