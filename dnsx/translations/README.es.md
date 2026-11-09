<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="centro">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconocimiento y escaneo">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licencia MIT">
</p>

<p align="centro">
  <b>🌐 Resolución DNS de alto rendimiento, filtrado de comodines, búsquedas DNS inversas y motor de extracción multiconsulta</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [¿Por qué dnsx? (Manejo y velocidad de comodines)](#-why-dnsx-wildcard-handling--speed)
- [Instalación](#-instalación)
- [Sondeo y resolución de subdominio básico](#-sondeo-de-subdominio-básico--resolución)
- [Filtrado DNS comodín (`-wd`)](#-filtrado-dns-comodín---wd)
- [Consulta de tipos de registros DNS específicos](#-consulta-tipos-de-registros-dns-específicos)
- [Búsquedas DNS inversas (`-ptr`)](#-búsquedas-dns-inversas---ptr)
- [Uso de solucionadores personalizados y limitación de velocidad](#-using-custom-resolvers--rate-limiting)
- [Automatización de tuberías (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Diseñado en Go por **ProjectDiscovery**, **dnsx** es un conjunto de herramientas DNS rápido y multipropósito diseñado para reconocimiento, búsqueda de errores y descubrimiento de redes.

Mientras que herramientas como `dig`, `nslookup` y `host` resuelven registros uno por uno, `dnsx` procesa miles de consultas de forma asincrónica, reintenta automáticamente los solucionadores fallidos, extrae direcciones IP de respuesta limpias y elimina el ruido de comodines que contamina la enumeración de subdominios.

---

## ⚡ ¿Por qué dnsx? (Manejo y velocidad comodín)

Durante el reconocimiento de recompensas por errores, las herramientas pasivas (`subfinder`, `amass`) a menudo devuelven miles de subdominios muertos o comodín. Probar puntos finales HTTP en entradas DNS inactivas desperdicia un ancho de banda enorme.

`dnsx` resuelve esto mediante:
1. **Filtrado de comodines (`-wd`):** Detecta si `*.domain.com` se resuelve en una IP general y filtra los subdominios fantasmas.
2. **Velocidad y simultaneidad:** Resuelve más de 10 000 dominios por minuto en grupos de resolución públicos y privados.
3. **Limpiar tubería de salida:** Se alimenta directamente a `httpx` o `nmap`.

---

## 📥 Instalación
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Sondeo y resolución básicos de subdominios
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Filtrado DNS comodín (`-wd`)

Los comodines devuelven una IP válida para subdominios inexistentes (`random123xyz.target.com`), lo que envenena los conjuntos de datos de reconocimiento.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Consulta de tipos de registros DNS específicos

Consulta tipos de registros específicos en miles de dominios:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Búsquedas inversas de DNS (`-ptr`)

Descubra infraestructura activa y hosts virtuales realizando búsquedas PTR en un rango de IP:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Uso de solucionadores personalizados y limitación de velocidad

Evite que su ISP o Google DNS limiten la velocidad utilizando un grupo de resolución seleccionado de alta velocidad:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Automatización de canalizaciones (Subfinder + dnsx + httpx)

El flujo de trabajo de reconocimiento definitivo para cazarrecompensas de errores y evaluadores de penetración:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Resolver subdominios** | `dnsx -l subdomains.txt` |
| **Extraer direcciones IP únicamente** | `dnsx -l subdomains.txt -resp-only` |
| **Filtrar dominios comodín** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Extraer CNAME (adquisición)** | `dnsx -l subdomains.txt -cname -resp` |
| **Consultar todos los registros (Recon)** | `dnsx -l domains.txt -recon` |
| **Búsqueda DNS inversa (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Resolvedores y subprocesos personalizados**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Salida JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Inundación de DNS:** Activar decenas de miles de consultas DNS a través de servidores DNS internos corporativos aumentará las cargas de CPU y activará alertas SIEM para túneles DNS/DDoS.
2. **Envenenamiento de solucionadores:** Utilice siempre listas de solucionadores verificados y confiables para evitar que solucionadores maliciosos intercepten consultas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia es únicamente para pruebas de penetración autorizadas y compromisos de recompensas por errores. Está estrictamente prohibido realizar sondeos de DNS de gran volumen contra infraestructura no autorizada.