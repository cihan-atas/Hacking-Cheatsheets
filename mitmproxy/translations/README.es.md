<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p align="centro">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Análisis de red">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p align="centro">
  <b>🕶️ Un proxy HTTP interactivo con capacidad SSL/TLS para evaluadores de penetración, investigadores de seguridad e ingenieros de software con una rica interfaz de consola y API de secuencias de comandos Python</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y los tres binarios](#-descripción general--los-tres-binarios)
- [Instalación y configuración del certificado](#-instalación--certificado-configuración)
  - [Instalación en Linux/macOS](#instalación-en-linux--macos)
  - [Instalación del certificado CA para interceptación SSL/TLS](#installing-ca-certificate-for-ssltls-interception)
- [Modos de funcionamiento: normal, transparente y proxy inverso](#-modos-de-operación-regular-transparente--proxy-inverso)
- [Navegación TUI interactiva (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Atajos de teclado] (#atajos de teclado)
  - [Filtrado e interceptación de flujos](#filtrado--interceptación-de-flujos)
- [Automatización no interactiva (`mitmdump`)](#-automatización-no interactiva-mitmdump)
  - [Grabación y reproducción de tráfico] (#recording--replaying-traffic)
- [Secuencias de comandos y complementos de Python] (#-python-scripting--addons)
  - [Secuencia de comandos de manipulación automatizada de encabezados] (#script-de-manipulación-de-encabezados-automatizada)
  - [Manipulación del cuerpo de respuesta en vivo] (#manipulación-del-cuerpo-de-respuesta en vivo)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y los tres binarios

A diferencia de los voluminosos proxies gráficos de escritorio, **mitmproxy** ofrece una intercepción de HTTP/1, HTTP/2 y WebSockets ultrarrápida, programable y de total fidelidad directamente dentro de su terminal o canal de automatización.

Se envía con tres binarios de punto de entrada:
1. **`mitmproxy`:** La interfaz de usuario de terminal (TUI) interactiva basada en ncurses.
2. **`mitmdump`:** La versión de línea de comandos no interactiva (el equivalente `tcpdump` para HTTP).
3. **`mitmweb`:** Una interfaz web gráfica liviana basada en navegador.

---

## 📥 Instalación y configuración del certificado

### Instalación en Linux/macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Instalación del certificado CA para la interceptación SSL/TLS
Para interceptar el tráfico HTTPS, los objetivos deben confiar en la autoridad de certificación dinámica de mitmproxy:
1. Inicie mitmproxy una vez: `mitmproxy` (crea certificados en `~/.mitmproxy/`).
2. En el dispositivo/navegador cliente, configure el proxy `127.0.0.1:8080`.
3. Busque `http://mitm.it` y descargue/instale el certificado para Android, iOS, Windows o Linux.

---

## ⚙️ Modos de funcionamiento: normal, transparente y proxy inverso

### Modo proxy normal (predeterminado)
Escucha de proxy directo estándar en el puerto 8080:
```bash
mitmproxy -p 8080
```
### Modo de proxy inverso
Retransmite el tráfico entrante a un servidor de destino específico (ideal para pruebas de API):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Modo transparente
Intercepta el tráfico enrutado a través de iptables sin configurar los ajustes del proxy del cliente:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Navegación TUI interactiva (`mitmproxy`)

### Atajos de teclado
- `?`: Abrir superposición de ayuda.
- `Enter`: Inspecciona el flujo HTTP seleccionado (Solicitud/Respuesta/Detalle).
- `q`: Regresar/Salir.
- `d`: Eliminar el flujo actual.
- `r`: reproduce la solicitud seleccionada al servidor.
- `e`: Editar encabezados, ruta o cuerpo de solicitud/respuesta.
- `a`: Reanuda el flujo pausado (al interceptar).
- `A`: Reanuda todos los flujos pausados.

### Filtrado e interceptación de flujos
Presione `i` para ingresar una expresión de filtro de interceptación:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Automatización no interactiva (`mitmdump`)

### Grabación y reproducción de tráfico
Capture el tráfico en vivo directamente en un archivo de volcado binario:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Secuencias de comandos y complementos de Python

mitmproxy presenta una API nativa de Python para modificar el tráfico mediante programación:

### Script de manipulación automatizada de encabezados (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Ejecutar con:
```bash
mitmproxy -s add_headers.py
```
### Manipulación del cuerpo de respuesta en vivo (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Ejecutar con:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Iniciar TUI interactiva** | `mitmproxy -p 8080` |
| **Iniciar GUI web** | `mitmweb -p 8080` |
| **Registrador CLI silencioso** | `mitmdump -p 8080` |
| **Objetivo de proxy inverso** | `mitmproxy --mode reverse:https://target.com` |
| **Registrar tráfico al archivo** | `mitmdump -w capture.mitm` |
| **Cargar secuencia de comandos adicional de Python**| `mitmproxy -s script.py` |
| **Ignorar hosts de destino** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Fijación de certificado:** Las aplicaciones móviles que aplican la fijación SSL rechazan el certificado CA de mitmproxy. Combínelo con Frida / Objeción para evitar los controles del lado del cliente.
2. **Detección de proxy en TLS Handshake:** Los proxy introducen conjuntos de cifrado TLS personalizados y huellas digitales de negociación ALPN que los WAF y los administradores de bots pueden detectar.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> mitmproxy intercepta y descifra comunicaciones de red confidenciales. Debe implementarse estrictamente en sistemas y redes donde se haya obtenido una autorización explícita. Interceptar comunicaciones sin consentimiento es ilegal.