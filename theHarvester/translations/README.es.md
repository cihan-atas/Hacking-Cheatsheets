<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de la cosechadora

[theHarvester](https://github.com/laramies/theHarvester) es una de las herramientas de inteligencia de código abierto (OSINT) más utilizadas para recopilar correos electrónicos, subdominios, nombres de empleados, hosts virtuales, puertos abiertos y claves PGP de empleados de diversas fuentes públicas (motores de búsqueda, servidores de claves PGP, Shodan, Censys, Certspotter, etc.).

---

## Tabla de contenidos

- [Fuentes de datos admitidas](#fuentes-de-datos-compatibles)
- [Instalación y configuración de clave API] (#instalación--api-key-configuration)
- [Reconocimiento de dominio básico](#reconocimiento-de-dominio-basico)
- [Cosecha pasiva de múltiples fuentes] (# recolección pasiva de múltiples fuentes)
- [Verificación DNS activa y fuerza bruta] (#verificación-dns activa--fuerza bruta)
- [Formatos de salida (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Integración del flujo de trabajo Pentest](#pentest-workflow-integration)

---

## Fuentes de datos admitidas

theHarvester agrega inteligencia de docenas de fuentes activas y pasivas:
- **Motores de búsqueda (Gratis / Sin clave):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Transparencia del certificado:** CRT.sh, Certspotter.
- **Repositorios de códigos y amenazas:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **API de reconocimiento y OSINT:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Red/DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Instalación y configuración de clave API

### 1. Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. Configurar claves API (`api-keys.yaml`)
Muchas fuentes (Hunter, Shodan, Censys, Intelx, GitHub) requieren claves API gratuitas o de pago.
Cree o edite `api-keys.yaml` en la ruta de configuración:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Reconocimiento de dominio básico

### 1. Coseche correos electrónicos y subdominios utilizando motores gratuitos
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Dominio de destino a reconocer.
- `-l <limit>`: Número de resultados a consultar por motor.
- `-b <sources>`: Lista de fuentes separadas por comas (o `all`).

### 2. Consultar todas las fuentes configuradas
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Cosecha pasiva de múltiples fuentes

Consulta bases de datos de transparencia de certificados e inteligencia sobre amenazas específicas sin enviar tráfico al objetivo:

### 1. Transparencia de certificados y amenazas Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Dirigirse a grupos de fuentes específicos
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Verificación de DNS activa y fuerza bruta

### 1. Resolver subdominios descubiertos (`-r`)
Realice búsquedas de DNS activas para verificar si los subdominios recopilados están activos actualmente:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. Fuerza bruta DNS (`-c`)
Realice fuerza bruta de subdominios DNS basada en listas de palabras directamente dentro de theHarvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Verificación del host virtual (`-v`)
Verifique los hosts virtuales mediante solicitudes HTTP:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Formatos de salida (HTML, XML, JSON)

Exporte resultados para informes automatizados o reconocimiento posterior:

### 1. Exportar a HTML y XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Crea `/tmp/theharvester_results.html` y `/tmp/theharvester_results.xml`.

### 2. Analizar subdominios descubiertos con `jq`
Si exporta o analiza JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Integración del flujo de trabajo Pentest

Encadene los resultados de theHarvester directamente con escáneres de vulnerabilidad activos:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```