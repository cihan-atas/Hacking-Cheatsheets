<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hoja de referencia enum4linux-ng

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) es una reescritura de próxima generación de la herramienta heredada `enum4linux` escrita en Python 3. Realiza una recopilación completa de información de Windows y Samba a través de SMB (bloque de mensajes del servidor), NetBIOS, RPC (llamada a procedimiento remoto) y LDAP. Extrae cuentas de usuario, membresías de grupos, carpetas compartidas, políticas de contraseñas, información del sistema operativo y ciclos RID.

---

## Tabla de contenidos

- [Mejoras clave sobre enum4linux heredado](#key-improvements-over-legacy-enum4linux)
- [Instalación y requisitos](#instalación--requisitos)
- [Enumeración básica de dominios y SMB](#basic-smb--enumeración-de-dominios)
- [Modos de autenticación (sesión nula frente a usuario válido)](#authentication-modes-null-session-vs-valid-user)
- [Módulos de enumeración específicos](#módulos-de-enumeración-objetivos)
- [Ciclismo RID y recolección de cuentas de usuario] (#rid-cycling--user-account-harvesting)
- [Exportación de informes (JSON, YAML, texto)](#exporting-reports-json-yaml-text)
- [Refuerzo defensivo para SMB y RPC](#refuerzo-defensivo-para-smb--rpc)

---

## Mejoras clave sobre enum4linux heredado

- **Motor Python 3 moderno:** Ejecución de subprocesos múltiples basada en `impacket`, que elimina fallas de scripts Perl antiguos.
- **Detección mejorada del sistema operativo:** Detección precisa de Windows Server (2012-2025), Windows 10/11 y versiones modernas de Samba.
- **Informes estructurados:** Exportación nativa a JSON y YAML para una integración perfecta de secuencias de comandos o SIEM.
- **Ciclismo RID más inteligente:** Ajustes de rango flexibles y detección automática de RID de administrador/invitado.
- **Salida CLI coloreada:** Indicadores visuales claros para configuraciones erróneas de seguridad y sesiones nulas.

---

## Instalación y requisitos
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## Enumeración básica de dominios y PYMES

### 1. Ejecución de enumeración predeterminada (`-A`)
Ejecute todas las comprobaciones de enumeración no intrusivas contra el objetivo:
```bash
enum4linux-ng -A 192.168.1.50
```
Este módulo consulta:
- Nombres NetBIOS y nombres de grupos de trabajo/dominio.
- Versión y arquitectura del sistema operativo.
- Directorios compartidos y permisos de acceso.
- Política de contraseñas (umbral de bloqueo, longitud mínima, historial).
- Usuarios y grupos locales vía RPC/SAMR.

---

## Modos de autenticación (sesión nula frente a usuario válido)

### 1. Sesión anónima/nula (`-u '' -p ''`)
Pruebe si el objetivo permite conexiones IPC$ no autenticadas:
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Dominio autenticado/Cuenta local
Proporcione credenciales válidas con pocos privilegios para ampliar drásticamente los datos recopilados:
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Proporcione un nombre de dominio específico (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Módulos de enumeración específicos

Ejecute módulos específicos para minimizar las huellas de la red y evitar alertas de detección de intrusiones:

### 1. Carpetas compartidas y permisos de acceso (`-S`)
Enumere los recursos compartidos, los comentarios y verifique el acceso de lectura/escritura:
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Extracción de política de contraseñas (`-P`)
Identifique los umbrales de bloqueo de cuentas para planificar de forma segura la difusión de contraseñas:
```bash
enum4linux-ng -P 192.168.1.50
```
Resultados clave a observar:
- **Longitud mínima de la contraseña:** por ejemplo, 8 caracteres.
- **Umbral de bloqueo de cuenta:** por ejemplo, 5 intentos (¡no rocíe más rápido que la ventana de bloqueo!).
- **Restablecer contador de bloqueo:** por ejemplo, 30 minutos.

### 3. Solo usuarios y grupos (`-U` y `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Sólo información del sistema operativo y NetBIOS (`-O` y `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## Ciclismo RID y recolección de cuentas de usuario

Cuando la enumeración de usuarios mediante consultas RPC estándar está restringida, realice un ciclo de identificador relativo (RID) mediante llamadas de consulta SAMR:

### 1. Ciclo RID predeterminado (rango 500 - 1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Rango RID personalizado (por ejemplo, 500 a 5000 para organizaciones grandes)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Extraer la lista de palabras de nombres de usuario limpios
Filtre los nombres de usuario RID descubiertos en un archivo de texto para rociar contraseñas:
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Exportación de informes (JSON, YAML, texto)

Exportar resultados de enumeración para documentación o análisis programático:

### 1. Exportar en todos los formatos simultáneamente (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Genera:
-`/tmp/target_recon.json`
-`/tmp/target_recon.yaml`
-`/tmp/target_recon.txt`

### 2. Exportar a un formato específico
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Endurecimiento defensivo para SMB y RPC

| Vector | Riesgo | Recomendación de endurecimiento |
|---|---|---|
| **Sesión nula (IPC$ anónimo)** | Reconocimiento de dominios no autenticados | Configure `RestrictNullSessAccess=1` en el Registro de Windows; configurar `RestrictAnonymous=2` en SAM. |
| **SMBv1 activo** | EternalBlue (MS17-010) y ataques MITM | Deshabilite completamente SMBv1 en todos los puntos finales y controladores de dominio. |
| **Permisos para compartir texto sin cifrar** | Archivos confidenciales y credenciales expuestos | Auditar permisos compartidos; hacer cumplir la enumeración basada en acceso (ABE). |
| **Enumeración de ciclos RID** | Extracción completa de la lista de cuentas de usuarios | Restrinja la enumeración de cuentas SAM anónimas mediante la Política de seguridad local. |