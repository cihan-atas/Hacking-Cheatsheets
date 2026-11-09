<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p align="centro">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Listas de palabras">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p align="centro">
  <b>🔍 Escáner de contenido web clásico que lanza ataques basados en diccionarios contra servidores web</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [DIRB frente a alternativas modernas](#-dirb-vs-alternativas-modernas)
- [Instalación](#-instalación)
- [Listas de palabras predeterminadas] (#-listas de palabras predeterminadas)
- [Sintaxis básica](#-sintaxis-básica)
- [Escenarios de escaneo comunes](#-escenarios-de-escaneo-comunes)
- [Opciones avanzadas y ajuste](#-opciones-avanzadas--ajuste)
- [Búsqueda de vulnerabilidades con listas integradas](#-búsqueda-de-vulnerabilidades-con-listas-integradas)
- [Flujos de trabajo del mundo real](#-flujos de trabajo-del-mundo-real)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**DIRB** (Directory Buster) es un escáner de contenido web clásico, liviano, basado en URL, escrito en C. Lanza ataques basados en diccionarios contra servidores web, buscando objetos web existentes y ocultos (archivos, directorios, scripts y puntos finales).

DIRB funciona generando solicitudes HTTP para cada palabra en una lista de palabras, analizando los códigos de respuesta del servidor (200, 301, 302, 403, etc.) y determinando si existe un recurso.

### Características clave

- ✅ **Escaneo recursivo automático:** Escanea los subdirectorios descubiertos automáticamente de forma predeterminada.
- ✅ **Listas de palabras especializadas integradas:** Se envía con listas de palabras específicas para servidores web, CMS y vulnerabilidades comunes.
- ✅ **Baja huella de recursos:** Binario liviano que se ejecuta sin problemas incluso en hardware de baja especificación o sistemas más antiguos.
- ✅ **Soporte de proxy y encabezado personalizado:** Fácil integración con Burp Suite y servidores proxy HTTP ascendentes.

---

## ⚖️ DIRB frente a alternativas modernas

| Característica | DIRB | Gobuster | ffuf | Feroxbuster | búsqueda de directorios |
|---------|------|----------|------|-------------|-----------|
| **Idioma** | C | Ir | Ir | Óxido | Pitón |
| **Velocidad** | ⭐⭐ (Hilo bajo) | ⭐⭐⭐⭐⭐ (Rápido) | ⭐⭐⭐⭐⭐ (Extremadamente rápido) | ⭐⭐⭐⭐⭐ (óxido asíncrono) | ⭐⭐⭐⭐ (Rápido) |
| **Recursión predeterminada** | ✅ Sí (Automático) | ❌ No | ❌ No (Opcional `-recursion`) | ✅ Sí (Automático) | ✅ Sí (`-r`) |
| **Listas de palabras personalizadas incluidas** | ✅ Sí (`/usr/share/dirb/`) | ❌ No | ❌ No | ❌ No | ✅ Sí (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ No | ✅ Sí | ✅ Sí | ❌ No | ✅ Sí (`--subdirs`) |
| **POST / Fuzzing de datos** | ❌ No | ❌ No | ✅ Sí | ❌ No | ✅ Sí (`-d`) |
| **Uso de memoria/CPU** | Muy bajo | Bajo | Bajo | Muy bajo | Moderado |

---

## 📥 Instalación

### Kali Linux y sistema operativo Parrot

DIRB viene preinstalado en Kali Linux y Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
###Debian/Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Compilando desde la fuente
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Listas de palabras predeterminadas

Cuando se instala en Kali Linux, DIRB incluye listas de palabras dedicadas ubicadas en `/usr/share/dirb/wordlists/`:

| Ruta de la lista de palabras | Entradas | Propósito |
|------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Lista predeterminada utilizada cuando no se especifica ninguna lista de palabras |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Descubrimiento rápido de caminos de alta probabilidad |
| `/usr/share/dirb/wordlists/big.txt` | ~20,469 | Búsqueda exhaustiva de directorios y archivos |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Lista de palabras en catalán |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Lista de palabras en español |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Variaciones comunes de prefijos/sufijos |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Extensiones web estándar (`.php`, `.asp`, `.html`, etc.) |

### Listas de palabras de vulnerabilidad integradas (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt`: archivos específicos de Apache, páginas de estado y directorios conocidos
- `iis.txt` - Archivos del servidor web Microsoft IIS (`web.config`, `aspnet_client`, etc.)
- `tomcat.txt` - Rutas de administración y administrador de Apache Tomcat
- `cgis.txt` - Scripts y puntos finales de interfaz de puerta de enlace común (CGI)
- `fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Sintaxis básica
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Ejemplos esenciales
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Escenarios de escaneo comunes

### 1. Escaneo con extensiones de archivo (`-X` y `-x`)

Busque extensiones de archivo específicas adjuntas a cada palabra en el diccionario:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Control de la recursividad (`-r` y `-R`)

De forma predeterminada, DIRB rastrea recursivamente todos los directorios descubiertos. Puedes controlar este comportamiento:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Escaneo que no distingue entre mayúsculas y minúsculas (`-i`)

Útil cuando se dirige a servidores Windows/IIS donde las rutas de archivos no distinguen entre mayúsculas y minúsculas:
```bash
dirb http://target.local -i
```
### 4. Ignorar códigos de estado HTTP específicos (`-N`)

Filtre códigos de respuesta no deseados (por ejemplo, bucles de redireccionamiento o rutas prohibidas):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Opciones avanzadas y ajuste

### Autenticación (`-u`)

DIRB admite autenticación HTTP básica y implícita:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Cookies personalizadas y tokens de sesión (`-c`)

Pruebe rutas de aplicaciones web autenticadas proporcionando cookies de sesión:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Encabezados HTTP y agente de usuario personalizados (`-a` y `-H`)

Omita las reglas WAF básicas o simule navegadores legítimos:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Integración de enrutamiento de proxy y Burp Suite (`-p` y `-P`)

Envíe tráfico DIRB a través de un proxy HTTP ascendente como Burp Suite u OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Limitación y evasión de velocidad (`-z`)

Evite activar alertas IDS/IPS o DoSing en servicios web frágiles agregando un retraso entre solicitudes (en milisegundos):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Registro de salida (`-o` y `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Búsqueda de vulnerabilidades con listas integradas

DIRB incluye listas específicas en `/usr/share/dirb/wordlists/vulns/` para detectar servidores sin parches y puntos finales peligrosos:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Flujos de trabajo del mundo real

### Flujo de trabajo 1: Reconocimiento inicial rápido

Cuando encuentra por primera vez un servidor web en el puerto 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Flujo de trabajo 2: Evaluación exhaustiva de aplicaciones web

Realice un análisis profundo en busca de copias de seguridad de configuración, código fuente y puntos finales de API:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Flujo de trabajo 3: rastreo del portal autenticado a través de Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Tabla de referencia rápida

| Bandera | Argumento | Descripción | Ejemplo |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Especifique una cadena de agente de usuario personalizada | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Establecer encabezado de cookie HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Agregar encabezado HTTP personalizado | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Ninguno | Búsqueda que no distingue entre mayúsculas y minúsculas | `dirb http://target -i` |
| `-N` | `<codes>` | Ignorar respuestas que coincidan con el código de estado | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Guardar los resultados del análisis en un archivo | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Usar proxy HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Credenciales de autenticación de proxy | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Ninguno | No busques recursivamente | `dirb http://target -r` |
| `-R` | Ninguno | Búsqueda recursiva interactiva | `dirb http://target -R` |
| `-u` | `<user:pass>` | Autenticación HTTP básica/digest | `dirb http://target -u "admin:1234"` |
| `-w` | Ninguno | No detenga el análisis cuando aparezcan mensajes de ADVERTENCIA | `dirb http://target -w` |
| `-X` | `<ext_list>` | Agregar extensiones separadas por comas | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Agregar extensiones del archivo | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Agregar retraso entre solicitudes | `dirb http://target -z 250` |

---

## 💡 Consejos y mejores prácticas

1. **Cuidado con las respuestas comodín 200/302:**
   - Algunas aplicaciones web devuelven HTTP 200 o 302 para cada solicitud (controlador 404 personalizado). DIRB informará cada entrada como encontrada. Utilice `-N 302` o cambie a `ffuf` / `feroxbuster` para un filtrado avanzado de tamaño/palabra.
2. **Combine con `-r` para diccionarios grandes:**
   - Si usa `directory-list-2.3-medium.txt` (220k palabras), desactive siempre la recursividad con `-r`. De lo contrario, DIRB puede tardar días en rastrear directorios profundamente anidados.
3. **Verifique siempre las extensiones:**
   - Los servidores modernos exponen con frecuencia archivos confidenciales como `.env`, `.git`, `.bak`, `.conf` o `.sql`. Pruebe siempre las extensiones comunes con `-X`.
4. **Utilice `-w` para escaneos desatendidos:**
   - De forma predeterminada, DIRB puede pausarse si encuentra advertencias de red o SSL. Pasar `-w` garantiza que las pruebas se completen sin interacción manual.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** DIRB está diseñado para auditorías de seguridad, pruebas de penetración e investigaciones éticas en sistemas de su propiedad o que tiene permiso explícito y documentado para realizar pruebas. Los ataques de diccionario no autorizados contra servidores web pueden provocar interrupciones en el servicio y son ilegales según la legislación sobre delitos informáticos en todo el mundo.

---

## 📚 Recursos

- [Proyecto DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [Documentación del paquete DIRB de Kali Linux](https://www.kali.org/tools/dirb/)
- [SecLists - Listas de palabras de descubrimiento de contenido web](https://github.com/danielmiessler/SecLists)