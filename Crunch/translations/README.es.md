<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p align="centro">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Idioma C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Descifrado de contraseñas">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Generador de listas de palabras que genera todas las combinaciones posibles de conjuntos de caracteres utilizando longitudes, patrones y símbolos personalizados</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y motor de permutación](#-descripción general--el-motor-de-permutación)
- [Instalación](#-instalación)
- [Sintaxis básica y conjuntos de caracteres](#-sintaxis-básica--conjuntos-de-caracteres)
- [Coincidencia de patrones y marcadores de posición (`-t`)](#-pattern-matching--placeholders--t)
- [Uso de conjuntos de caracteres del sistema (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Limitación de caracteres duplicados (`-d`)](#-limitación-de-caracteres-duplicados--d)
- [Dividir listas de palabras en fragmentos (`-b` y `-c`)](#-dividir listas de palabras en fragmentos--b---c)
- [Conectar directamente a las herramientas de ataque (espacio en disco cero)] (#-conectar-directamente-a-las-herramientas-de-ataque-espacio-en-disco-cero)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y advertencias de cálculo](#-consideraciones-defensivas--advertencias-de-cálculo)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y motor de permutación

Al atacar patrones de contraseñas conocidos (por ejemplo, PIN corporativos, números de teléfono o contraseñas que se ajustan a reglas de complejidad estándar como `Word2026!`), los diccionarios estáticos estándar como `rockyou.txt` no dan en el blanco o son ineficientes.

Escrito en C por **bofheaded**, **Crunch** es un generador de listas de palabras increíblemente rápido. Calcula permutaciones matemáticamente, lo que le permite definir posiciones exactas de caracteres, longitudes, grupos de caracteres y restricciones de duplicación.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Sintaxis básica y conjuntos de caracteres

La sintaxis principal requiere una longitud mínima, una longitud máxima y, opcionalmente, un alfabeto:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Coincidencia de patrones y marcadores de posición (`-t`)

La bandera `-t` permite corregir caracteres conocidos mientras borra las ranuras desconocidas usando marcadores de posición especiales:

| Marcador de posición | Tipo de personaje |
|-------------|----------------|
| `@` | Letras minúsculas (`a-z`) |
| `,` | Letras mayúsculas (`A-Z`) |
| `%` | Números (`0-9`) |
| `^` | Símbolos especiales/puntuación (`!@#$%^&*...`) |

### Ejemplos de patrones prácticos:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Uso de conjuntos de caracteres del sistema (`charset.lst`)

Crunch agrupa matrices de caracteres preconfiguradas en `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Limitación de caracteres duplicados (`-d`)

Aplique límites a los caracteres duplicados consecutivos para eliminar contraseñas poco probables:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 División de listas de palabras en fragmentos (`-b` y `-c`)

Evite que los archivos de varios terabytes agoten el espacio en disco dividiéndolos en volúmenes manejables:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Tubería directa a herramientas de ataque (espacio en disco cero)

Nunca escriba listas de palabras gigantescas en el disco cuando pueda transmitir permutaciones directamente a herramientas de craqueo:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **PIN numéricos (4 dígitos)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Patrón: Capital + Palabra + Números**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Patrón: Prefijo específico** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Usar archivo de juego de caracteres** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Límite de idénticos consecutivos**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Dividido en archivos de 1 GB** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Tubería a Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Tubería a Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Consideraciones defensivas y advertencias de cálculo

> [!ADVERTENCIA]
> **Explosiones de permutación:** Siempre verifique el tamaño de salida proyectado de Crunch antes de confirmar la generación del disco. ¡Una lista de palabras alfanumérica de 8 caracteres sin restricciones produce **más de 200 Terabytes** de datos de texto! Salida por tubería siempre que sea posible.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Crunch está destinado únicamente a la recuperación de contraseñas autorizadas, pruebas de penetración y auditorías de seguridad. La fuerza bruta de contraseñas en sistemas no autorizados es estrictamente ilegal.