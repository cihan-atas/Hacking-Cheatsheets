<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de Name-That-Hash (enésimo)

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) es un motor de identificación de hash moderno y ultrarrápido que identifica con precisión hashes desconocidos, hashes incrustados en texto o archivos y genera modos directos Hashcat (`-m`) y John the Ripper (`--format`). Decodifica automáticamente codificaciones anidadas como Base64 y Hex.

---

## Tabla de contenidos

- [Características clave] (#características clave)
- [Instalación](#instalación)
- [Identificación CLI Hash] (#cli-hash-identificación)
- [Procesamiento de archivos y multihash] (#multi-hash--procesamiento de archivos)
- [Salida en formato Hashcat y John the Ripper] (#hashcat--john-the-ripper-format-output)
- [Filtros de popularidad y decodificación automática] (#auto-decodificación--filtros de popularidad)
- [Integración con Tuberías de Cracking](#integración-con-tuberías-de-craqueo)

---

## Características clave

- **Más de 300 tipos de hash:** Cubre algoritmos modernos, formatos de Active Directory, hashes de bases de datos, firmas CMS y cifrados esotéricos.
- **Asignación de banderas de Hashcat y John:** Muestra inmediatamente los valores `-m <mode>` y `--format=<format>`.
- **Decodificación automática:** Elimina la codificación Base64, hexadecimal y URL antes de calcular el tipo de hash.
- **Búsqueda de subcadena/texto:** Encuentra hashes enterrados dentro de registros, volcados de memoria o archivos de configuración.
- **Clasificación de popularidad:** Clasifica los resultados según la probabilidad de uso en el mundo real.

---

## Instalación
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## Identificación de hash CLI

### 1. Identificar un único hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identificar los modos de formato Hashcat y John directamente
Solo muestra los tipos de hash que han admitido los modos de craqueo Hashcat o John:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Mostrar información detallada (descripción, velocidad, ejemplos)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Procesamiento de archivos y hash múltiples

### 1. Identificar hashes dentro de un archivo (línea por línea)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtrar resultados solo para los hashes más populares
Reduzca los falsos positivos de tipos de hash poco conocidos filtrando por popularidad:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Buscar y extraer hashes incrustados en texto no estructurado
Escanee cadenas de memoria sin procesar, archivos de registro o código fuente en busca de hashes:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Salida en formato Hashcat y John the Ripper

`nth` hace que sea trivial introducir hashes en crackers sin memorizar los números de modo:

| Formato hash de muestra | Algoritmo detectado | Modo Hashcat (`-m`) | Formato Juan (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | Tostado AS-REP | `18200` | `krb5asrep` |

---

## Filtros de popularidad y decodificación automática

Muchas aplicaciones almacenan hashes en Base64 o Hex antes de la inserción de la base de datos. `nth` puede revertir esto automáticamente:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integración con Cracking Pipelines

Canalice rápidamente un hash desconocido a Hashcat mediante identificación automática:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```