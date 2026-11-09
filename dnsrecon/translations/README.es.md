<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de dnsrecon

[dnsrecon](https://github.com/darkoperator/dnsrecon) es una herramienta integral de enumeración y reconocimiento de DNS escrita en Python. Admite transferencias de zona (AXFR), búsquedas inversas, fuerza bruta de dominio, descubrimiento de registros SRV, detección de comodines, espionaje de caché y expansión de TLD.

---

## Tabla de contenidos

- [Capacidades básicas] (#capacidades básicas)
- [Instalación](#instalación)
- [Enumeración estándar y transferencias de zona (`-d -t std / axfr`)](#enumeración-estándar--transferencias-de-zona--d--t-std--axfr)
- [Fuerza bruta del subdominio DNS (`-t brt`)](#dns-subdominio-fuerza-bruta--t-brt)
- [Escaneo de rango de búsqueda inversa (`-t rvl`)](#escaneo-de-rango-de-búsqueda-inversa--t-rvl)
- [Exploración de caché de DNS (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Dirigido a servidores de nombres específicos (`-n`)](#targeting-specific-nameservers--n)
- [Formatos de salida (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Refuerzo defensivo para servidores DNS](#refuerzo-defensivo-para-servidores-dns)

---

## Capacidades principales

- **Prueba de transferencia de zona (AXFR):** Prueba automáticamente todos los servidores de nombres autorizados para detectar transferencias de zona no autenticadas.
- **Enumeración de registros:** Recupera registros SOA, NS, A, AAAA, MX, TXT, SPF y SRV.
- **Fuerza bruta de subdominio:** Búsquedas de diccionarios multiproceso con filtrado de comodines.
- **Escaneo de rango de IP inverso:** Extracción de registros PTR a través de bloques CIDR arbitrarios.
- **Snooping de caché:** Prueba si los servidores de nombres recursivos tienen dominios específicos almacenados en caché en la memoria.
- **Expansión de TLD:** Descubre dominios coincidentes en dominios de nivel superior genéricos y de código de país.

---

## Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Enumeración estándar y transferencias de zona (`-d -t std / axfr`)

### 1. Enumeración de registros DNS estándar
Recopila registros SOA, NS, A, AAAA, MX y TXT e intenta automáticamente transferencias de zona contra cada NS autorizado:
```bash
dnsrecon -d example.com -t std
```
### 2. Sólo prueba de transferencia de zona dedicada (AXFR)
Pruebe solo la vulnerabilidad AXFR contra todos los servidores de nombres autorizados:
```bash
dnsrecon -d example.com -t axfr
```
*Si tiene éxito, toda la base de datos DNS del objetivo se vuelca sin fuerza bruta.*

---

## Fuerza bruta de subdominio DNS (`-t brt`)

Subdominios de fuerza bruta utilizando listas de palabras personalizadas o predeterminadas:

### 1. Fuerza bruta de subdominio con lista de palabras personalizada
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Dominio de destino.
- `-t brt`: Modo de fuerza bruta.
- `-D <file>`: archivo de diccionario de subdominio.

### 2. Fuerza bruta con filtrado de comodines
Evita que las configuraciones DNS comodín (`*.example.com` se resuelva en una única IP general) contaminen los resultados:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: ignora los resultados de resolución comodín.

---

## Escaneo de rango de búsqueda inversa (`-t rvl`)

Enumere los registros PTR de un bloque de IP para identificar hosts internos, puertas de enlace de correo e instancias provisionales:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: rango de IP en notación CIDR (p. ej. `192.168.1.0/24`) o notación inicio-fin (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Modo de búsqueda inversa.

---

## Espionaje de caché de DNS (`-t snoop`)

Determine si los clientes en una red de destino han resuelto recientemente dominios específicos a través de su solucionador interno:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: servidor DNS de destino para consultar.
- `-D <domains_file>`: Lista de dominios a probar (por ejemplo, competidores, C2 maliciosos, portales internos).

---

## Dirigido a servidores de nombres específicos (`-n`)

Consulta servidores DNS internos o específicos de destino directamente en lugar de solucionadores públicos ascendentes:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Formatos de salida (CSV, JSON, SQLite, XML)

Guarde los resultados para la integración con emisión de tickets, SIEM o fases de ataque posteriores:

### 1. Exportar a JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Exportar a la base de datos SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Exportar a CSV y XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Refuerzo defensivo para servidores DNS

| Vulnerabilidad | Riesgo de explotación | Defensa recomendada |
|---|---|---|
| **AXFR sin restricciones** | Mapeo completo de infraestructura interna | Restrinja las transferencias de zona únicamente a las IP de servidores de nombres secundarios autorizados (`allow-transfer { 10.0.0.2; };` en BIND). |
| **Abrir resolución recursiva** | Amplificación DNS DDoS y Snooping | Deshabilite la recursividad externa (`recursion no;` en BIND). |
| **Nombres de host predecibles** | Fuerza bruta de subdominio | Utilice subdominios aleatorios para entornos de preparación sensibles. |
| **Adquisición de subdominio** | CNAME apunta a servicios AWS/GitHub/Azure no reclamados | Audite los registros CNAME de DNS pendientes y elimine las asignaciones no utilizadas de inmediato. |