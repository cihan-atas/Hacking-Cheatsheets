<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de trucos de Sherlock

[Sherlock](https://github.com/sherlock-project/sherlock) es una herramienta de reconocimiento OSINT que busca cuentas de redes sociales por nombre de usuario en más de 400 plataformas sociales, foros, sitios de alojamiento de códigos y comunidades en línea simultáneamente.

---

## Tabla de contenidos

- [Capacidades clave](#key-capabilities)
- [Instalación](#instalación)
- [Búsqueda de nombre de usuario único y múltiple] (#único--búsqueda-de-nombre-de-usuario-múltiple)
- [Filtrado de sitios y plataformas](#filtrado-sitios--plataformas)
- [Proxy y enrutamiento Tor](#proxy--tor-routing)
- [Tiempo de espera y ajuste de subprocesos] (#timeout--threading-tuning)
- [Formatos de exportación (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automatización e integración de canalización OSINT] (#automatización--osint-pipeline-integration)

---

## Capacidades clave

- **Más de 400 plataformas:** Consultas GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin y cientos más.
- **Simultaneidad asíncrona:** Escaneo paralelo de alta velocidad mediante solicitudes asincrónicas de Python.
- **Detección precisa:** Detecta códigos de estado HTTP, mensajes de error y redireccionamientos para evitar falsos positivos.
- **Soporte Tor & SOCKS5:** Protege el anonimato de los investigadores y evita los límites de velocidad.

---

## Instalación
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Búsqueda de nombre de usuario único y múltiple

### 1. Búsqueda básica de nombre de usuario
Busque un identificador de destino único en los más de 400 sitios:
```bash
sherlock target_user
```
### 2. Buscar varios nombres de usuario simultáneamente
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Imprimir sólo cuentas encontradas (suprimir no encontradas/errores)
```bash
sherlock --print-found target_user
```
---

## Filtrado de sitios y plataformas

Limite las búsquedas a plataformas específicas para ahorrar tiempo o centrarse en categorías de inteligencia específicas:

### 1. Consultar únicamente sitios específicos
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Búsqueda sin plataformas específicas (ignore los sitios pesados ​​con falsos positivos)
```bash
sherlock target_user --site-list targets.txt
```
---

## Enrutamiento proxy y Tor

Para evitar prohibiciones de IP, límites de velocidad y mantener la seguridad operativa:

### 1. Ruta a través de la red Tor
Enrutar solicitudes automáticamente a través del proxy local Tor SOCKS (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Proxy HTTP / SOCKS5 personalizado
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Ajuste de tiempo de espera y subprocesos

Controle las velocidades de solicitud y las tolerancias de latencia:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Formatos de exportación (CSV, XLSX, JSON)

Guarde los descubrimientos para investigaciones, conjuntos de pruebas o informes:

### 1. Guardar directamente en un archivo CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Guardar la salida en un directorio específico
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Genera `/tmp/recon_data/target_user.txt` que contiene URL completas de perfiles confirmados.

### 3. Guardar como hoja de cálculo de Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automatización e integración de canalización OSINT

Encadena a Sherlock en un script de investigador automatizado:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```