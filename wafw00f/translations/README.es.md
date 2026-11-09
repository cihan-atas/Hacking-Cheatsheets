<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Hoja de referencia

[wafw00f](https://github.com/EnableSecurity/wafw00f) es una herramienta de detección y toma de huellas digitales de firewall de aplicaciones web (WAF) de línea de comandos de código abierto. Identifica y toma huellas dactilares de más de 150 soluciones de seguridad WAF comerciales y de código abierto que protegen los sitios web de destino mediante el envío de solicitudes HTTP normales y provocativas y el análisis de respuestas de comportamiento, encabezados y cookies.

---

## Tabla de contenidos

- [¿Por qué los WAF de huellas dactilares?](#why-fingerprint-wafs)
- [Instalación](#instalación)
- [Escaneo básico y de objetivos] (#basic--escaneo de objetivos)
- [Detección exhaustiva de Multi-WAF (`-a`)](#detección-multi-waf exhaustiva--a)
- [Escaneo de destino masivo desde archivo (`-i`)](#escaneo-de-objetivo-masivo-desde-archivo--i)
- [Proxy y enrutamiento Tor (`-p`)](#proxy--tor-routing--p)
- [Formatos de exportación (JSON, CSV, texto)](#export-formats-json-csv-text)
- [Lista de firmas WAF admitidas (`-l`)](#list-supported-waf-signatures--l)
- [Integración de pruebas de penetración y recompensas de errores] (#bug-bounty--penetration-testing-integration)

---

## ¿Por qué WAF de huellas dactilares?

El lanzamiento de ataques web activos (inyección SQL, XSS, SSRF) contra un objetivo protegido sin un reconocimiento previo a menudo resulta en prohibiciones instantáneas de IP o resultados de vulnerabilidad falsos negativos. Identificar el WAF exacto permite:
- **Evasión personalizada:** Selección de técnicas de omisión específicas del proveedor (por ejemplo, normalización Unicode para ModSecurity, codificación de transferencia fragmentada para Cloudflare).
- **Búsqueda de IP de origen:** Si se detecta Cloudflare o Akamai, gire para encontrar la IP de origen del backend antes de realizar la prueba.
- **Conocimiento del umbral de regla:** Ajuste de la simultaneidad del escáner y los límites de velocidad por debajo de los umbrales de bloqueo.

---

## Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Escaneo básico y de objetivos

### 1. Escanear un único dominio
```bash
wafw00f https://example.com
```
*wafw00f envía solicitudes HTTP de referencia seguidas de patrones maliciosos personalizados para activar filtros de seguridad, identificando firmas en encabezados, códigos de estado y páginas de error.*

### 2. Modo de salida detallado (`-v` / `-vv`)
Inspeccione encabezados coincidentes específicos, códigos de estado HTTP y heurísticas:
```bash
wafw00f https://example.com -v
```
### 3. Deshabilitar seguimiento de redirección (`-r`)
Evite que wafw00f siga redireccionamientos 301/302 para separar los servidores proxy inversos de borde de las aplicaciones backend:
```bash
wafw00f https://example.com -r
```
---

## Detección exhaustiva de múltiples WAF (`-a`)

De forma predeterminada, wafw00f detiene el escaneo tan pronto como se confirma la primera coincidencia WAF. Utilice `-a` para comprobar todas las firmas de proveedores conocidos (esencial cuando una aplicación se encuentra detrás de una defensa de varios niveles como Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Escaneo masivo de destino desde archivo (`-i`)

Escanee una lista completa de subdominios o activos de destino recopilados de `subfinder` o `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Enrutamiento de proxy y Tor (`-p`)

Enrute el tráfico de sondeo a través de servidores proxy para evitar la inclusión en listas negras de IP o inspeccionar consultas en Burp Suite:

### 1. Ruta a través del proxy Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Ruta a través del proxy local Tor SOCKS5
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Formatos de exportación (JSON, CSV, texto)

Exporte resultados para informes automatizados o herramientas de reconocimiento posteriores:

### 1. Exportar a formato JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Exportar a hoja de cálculo CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Lista de firmas WAF admitidas (`-l`)

Ver los más de 150 productos WAF detectables:
```bash
wafw00f -l
```
Los productos clave compatibles incluyen:
- **Proveedores de nube:** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Dispositivos empresariales:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Código abierto/Software:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Integración de pruebas de penetración y recompensas de errores

Integre wafw00f en un proceso de ataque web automatizado:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```