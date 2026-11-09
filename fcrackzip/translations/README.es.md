<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# hoja de referencia fcrackzip

[fcrackzip](https://github.com/hyc/fcrackzip) es un descifrador de contraseñas de línea de comandos rápido y multiproceso para archivos ZIP cifrados. Admite ataques basados ​​en diccionarios y ataques de fuerza bruta optimizados contra ZipCrypto tradicional (cifrado de flujo PKZIP) y puede verificar la integridad del archivo sin una extracción completa.

---

## Tabla de contenidos

- [Características principales y limitaciones] (#características principales - limitaciones)
- [Instalación](#instalación)
- [Ataques de diccionario (`-D -p`)](#dictionary-attacks--d--p)
- [Ataques de fuerza bruta (`-b`)](#ataques-de-fuerza-bruta--b)
- [Conjuntos de caracteres y especificación de longitud](#conjuntos-de-caracteres--especificación-de-longitud)
- [Parámetros de referencia y optimización del rendimiento] (#benchmark--optimización del rendimiento)
- [ZipCrypto vs. AES-256 (alternativa John/Hashcat)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Funciones principales y limitaciones

- **Cracker ZipCrypto ultrarrápido:** Escrito en C, optimizado para el algoritmo ZipCrypto heredado (cifrado PKZIP estándar).
- **Modo de diccionario:** Lee las contraseñas candidatas secuencialmente de las listas de palabras.
- **Modo de fuerza bruta:** Genera candidatos sobre la marcha utilizando conjuntos de caracteres configurables y límites de longitud.
- **Prueba de descompresión de archivos (`-u`):** Verifica las contraseñas candidatas al intentar descomprimirlas, eliminando prácticamente los falsos positivos.
- **Limitación:** **No** es compatible con el cifrado WinZip AES-256 moderno. Para archivos ZIP AES-256, utilice `zip2john` + John the Ripper o Hashcat (modo 13600).

---

## Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Ataques de diccionario (`-D -p`)

La forma más rápida de realizar pruebas con listas de palabras comunes:

### 1. Ataque de diccionario básico con verificación de descompresión
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: Modo descomprimir (verifica el CRC del archivo extraído para evitar falsos positivos).
- `-D`: Modo de ataque de diccionario.
- `-p <file>`: Ruta al archivo del diccionario.

### 2. Modo de salida detallado
Muestra la velocidad probada, la contraseña actual y la información de depuración:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Ataques de fuerza bruta (`-b`)

Cuando los ataques de diccionario fallan, aplique fuerza bruta contra contraseñas cortas desconocidas:

### 1. PIN numéricos de fuerza bruta (de 4 a 6 dígitos)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: Modo de fuerza bruta.
- `-c 1`: Juego de caracteres: sólo dígitos (`0-9`).
- `-l 4-6`: Rango de longitud: mínimo 4, máximo 6 caracteres.

### 2. Caracteres alfanuméricos en minúscula (de 1 a 5 caracteres)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Letras minúsculas (`a-z`) y dígitos (`0-9`).

### 3. Minúsculas + Mayúsculas + Dígitos + Símbolos
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Letras minúsculas (`a-z`).
- `-c A`: Letras mayúsculas (`A-Z`).
- `-c 1`: Números (`0-9`).
- `-c !`: Símbolos especiales (`[!:$%&/()=?{[]}+*~#]`).

---

## Conjuntos de caracteres y especificación de longitud

| Bandera | Juego de personajes incluido | Candidatos de ejemplo |
|---|---|---|
| `-c a` | Letras minúsculas (`a-z`) | `admin`, `secret` |
| `-c A` | Letras mayúsculas (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Dígitos (`0-9`) | `1234`, `2024` |
| `-c !` | Caracteres de puntuación especiales | `!@#$` |
| `-c aA1` | Caracteres alfanuméricos combinados | `Admin123` |
| `-p <prefix>` | Cadena de semillas inicial | Útil para retomar la fuerza bruta |

Especificación de límites de longitud:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Comparación y optimización del rendimiento

Pruebe el rendimiento de la CPU en su máquina:
```bash
fcrackzip -B
```
---

## ZipCrypto frente a AES-256 (alternativa John/Hashcat)

Si `fcrackzip` devuelve `not a zipfile, or unsupported encryption type`, el archivo está cifrado con AES-256 (WinZip AES). Utilice `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```