<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p align="centro">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Descifrado de contraseñas">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Generador de diccionario de contraseñas específico para objetivos que rastrea sitios web para recopilar terminología corporativa especializada, nombres de marcas y correos electrónicos de empleados</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y filosofía de la contraseña específica](#-overview--the-password-philosophy)
- [Instalación](#-instalación)
- [Rastreo de sitios web básico](#-rastreo-de-sitio-web-basico)
- [Controles de profundidad de araña y longitud de palabra] (#-profundidad de araña--controles de longitud de palabra)
- [Recolección de direcciones de correo electrónico (`-e`)](#-harvesting-email-addresses--e)
- [Incluyendo números y minúsculas](#-incluidos-números--minúsculas)
- [Extracción de metadatos de documentos (`-a`)](#-extracción-de-metadatos-de-documentos--a)
- [Recuento de palabras y perfil de frecuencia (`-c`)](#-recuento de palabras--perfil-de-frecuencia--c)
- [Encadenamiento de CeWL con Hashcat y John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y filosofía de contraseña específica

Las listas de palabras genéricas como `rockyou.txt` son efectivas para las contraseñas de los consumidores, pero **frecuentemente fallan en las políticas de contraseñas corporativas** que requieren más de 12 caracteres, símbolos especiales y rotaciones estacionales.

Los empleados basan habitualmente sus contraseñas empresariales en:
- Nombres de proyectos de la empresa, nombres en clave internos y lemas (`Pegasus2026!`).
- Terminología específica de la industria, jerga técnica o marcas de productos.
- Nombres de ejecutivos, puntos de referencia de ubicación y direcciones de oficinas.

Creado por **Robin Wood (DigiNinja)**, **CeWL** (generador de listas de palabras personalizadas) es una araña Ruby que rastrea una URL de destino específica hasta una profundidad definida, extrae palabras únicas que coinciden con los requisitos de longitud mínima y crea un diccionario de contraseñas personalizado.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Rastreo básico de sitios web
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Controles de profundidad de araña y longitud de palabras

Controle con qué profundidad CeWL sigue los enlaces y qué tamaños de palabras captura:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Recopilación de direcciones de correo electrónico (`-e`)

Extraiga direcciones de correo electrónico corporativas para posteriores simulaciones de phishing o pulverización de credenciales:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Incluyendo números y minúsculas
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Extracción de metadatos de documentos (`-a`)

Extraiga los nombres de usuario de autor y creador de documentos PDF y Microsoft Office descargados:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Perfil de frecuencia y recuento de palabras (`-c`)

Ordene las palabras por frecuencia para identificar las palabras de moda corporativas más generalizadas:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Encadenamiento de CeWL con Hashcat y John Rules

Una lista de palabras sin formato de CeWL contiene raíces de diccionario base. Para generar contraseñas viables, expanda la lista de palabras usando **reglas de mutación de Hashcat/John the Ripper**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Rastreo básico hasta archivo** | `cewl <URL> -w wordlist.txt` |
| **Establecer longitud y profundidad mínimas** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Extraer correos electrónicos corporativos**| `cewl <URL> -e --email_file emails.txt` |
| **Extraer metadatos del documento**| `cewl <URL> -a --meta_file authors.txt` |
| **Incluir palabras alfanuméricas**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Salida en minúsculas** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Contar frecuencias de palabras** | `cewl <URL> -c -w frequencies.txt` |
| **Autenticar detrás del inicio de sesión**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Registros de acceso al servidor web:** CeWL genera cientos de solicitudes HTTP GET rápidas, lo que produce registros de acceso de araña identificables. Utilice cadenas de agente de usuario personalizadas (`-u "Mozilla/5.0..."`) para integrarse.
2. **Trampas de palabras de miel:** Los equipos azules defensivos pueden incrustar enlaces HTML invisibles u ocultos que conduzcan a credenciales canary para señalar arañas automatizadas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> CeWL está diseñado únicamente para evaluaciones de seguridad autorizadas, pruebas de penetración e investigaciones de auditoría de contraseñas. Está estrictamente prohibido raspar o arañar sistemas no autorizados.