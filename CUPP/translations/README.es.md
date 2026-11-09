<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de CUPP (Perfilador de contraseñas de usuarios comunes)

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) es un generador de diccionario de contraseñas especializado diseñado para la elaboración de perfiles humanos específicos. En lugar de utilizar diccionarios genéricos por fuerza bruta, CUPP genera permutaciones basadas en datos personales (nombre del objetivo, pareja, mascotas, fechas de nacimiento, empresa, pasatiempos, variaciones de leet-speak y combinaciones de caracteres especiales).

---

## Tabla de contenidos

- [Resumen y casos de uso](#overview--use-cases)
- [Instalación](#instalación)
- [Asistente de creación de perfiles interactivo (`-i`)](#interactive-profiling-wizard--i)
- [Configuración y reglas Leet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Descarga y combinación de diccionarios (`-l` y `-w`)](#downloading--merging-dictionaries--l---w)
- [Salida de tubería a Hashcat e Hydra](#piping-output-to-hashcat--hydra)
- [Información defensiva y directrices sobre política de contraseñas](#información-defensiva--directrices-política-de-contraseñas)

---

## Descripción general y casos de uso

Los ataques de diccionario tradicionales a menudo fallan contra contraseñas complejas que siguen patrones predecibles por humanos (por ejemplo, `John1984!`, `Sarah_Fluffy2022#`). CUPP aborda esto mediante:
- Recopilar inteligencia personal específica del objetivo.
- Combinar nombres, años de nacimiento, fechas importantes y nombres de mascotas.
- Aplicación de mutaciones basadas en reglas (mayúsculas, reemplazo de palabras, caracteres especiales, adición de números aleatorios).
- Producir una lista de palabras optimizada y dirigida dramáticamente más pequeña que `rockyou.txt` pero con mayor relevancia para el objetivo.

---

## Instalación
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## Asistente de creación de perfiles interactivo (`-i`)

Inicie el mensaje del cuestionario para crear una lista de palabras personalizada:
```bash
python3 cupp.py -i
```
Tutorial de ejemplo:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
El archivo de salida `alice.csv` (o el archivo de texto especificado) se genera con miles de mutaciones personalizadas (por ejemplo, `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Configuración y reglas Leet (`cupp.cfg`)

La configuración de CUPP se controla a través de `cupp.cfg`. Las secciones clave incluyen:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
Edite `cupp.cfg` para ajustarlo a las características específicas del idioma y la configuración regional de destino.

---

## Descarga y combinación de diccionarios (`-l` y `-w`)

### 1. Descargue listas de palabras precompiladas desde repositorios
Descargue listas de palabras lingüísticas predeterminadas:
```bash
python3 cupp.py -l
```
### 2. Mejorar una lista de palabras existente con Leet/reglas especiales
Aplique las reglas de mutación CUPP a una lista de palabras existentes:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Salida de tubería a Hashcat e Hydra

Introduzca el diccionario personalizado generado directamente en herramientas de fuerza bruta:

### Descifrando hashes objetivo con Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Autenticación en línea Fuerza bruta con Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Información defensiva y directrices sobre políticas de contraseñas

- **Prohibir atributos personales:** Prohibir contraseñas de usuario que contengan nombres, apellidos, nombres de usuario, marcas de la empresa o fechas de nacimiento de los empleados.
- **Hacer cumplir la longitud sobre la complejidad:** Una frase de contraseña de más de 16 caracteres (por ejemplo, cuatro palabras aleatorias del diccionario) es significativamente más resistente a las herramientas de creación de perfiles que un patrón `NameYear!` de 8 caracteres.
- **Autenticación multifactor (MFA):** Aplique tokens FIDO2 o TOTP de hardware para que la generación de diccionarios personalizados por sí sola no comprometa las cuentas.