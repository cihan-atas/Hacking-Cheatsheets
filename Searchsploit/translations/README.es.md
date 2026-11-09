<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Hoja de referencia de Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) es la utilidad de línea de comandos oficial para [Exploit-Database](https://www.exploit-db.com/). Permite a los profesionales de la seguridad, evaluadores de penetración e investigadores de vulnerabilidades realizar búsquedas fuera de línea en decenas de miles de exploits, códigos shell y documentos técnicos publicados sin enviar tráfico de red a motores de búsqueda de terceros.

---

## Tabla de contenidos

- [Descripción general y arquitectura] (#descripción general--arquitectura)
- [Instalación y actualizaciones de la base de datos local] (#instalación--actualizaciones-de-la-base-de-datos-local)
- [Búsqueda de exploits básica y avanzada] (#basic--búsqueda-de-exploit-avanzada)
- [Examinar y copiar exploits (`-m` / `-p` / `-x`)](#examinando--copiando-exploits--m---p---x)
- [Filtrado por plataforma y tipo](#filtrado-por-plataforma--tipo)
- [Integración de escaneo Nmap XML/Grepable](#nmap-xml--grepable-scanning-integration)
- [Salida JSON y automatización de scripts](#json-output--script-automation)
- [Mejores prácticas en pruebas de penetración](#mejores-practicas-en-pruebas-de-penetración)

---

## Descripción general y arquitectura

Searchsploit consulta un clon git local de Exploit-DB almacenado en `/usr/share/exploitdb/`.
- **Zero Network Fingerprint:** Las búsquedas se ejecutan en estructuras de directorios y CSV locales.
- **Confiabilidad sin conexión:** Totalmente funcional en entornos objetivo aislados y redes de participación.
- **Duplicación integrada:** Copia los archivos exploit directamente al directorio de trabajo actual para su revisión y modificación inmediatas.

---

## Instalación y actualizaciones de bases de datos locales

### Debian/Ubuntu/Kali Linux/Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Instalación del repositorio Git (cualquier distribución de Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Búsqueda de exploits básica y avanzada

### 1. Búsqueda sencilla por palabra clave
Busque servicios, demonios o software específicos:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Coincidencia exacta del título (`-t`)
Restrinja las consultas de búsqueda estrictamente para explotar títulos y eliminar coincidencias de descripciones ruidosas:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Coincidencia estricta de términos y casos (`-e`)
Encuentre coincidencias exactas de versiones y patrones:
```bash
searchsploit -e "Apache Struts"
```
### 4. Búsqueda por identificador CVE
Consultar exploits asignados a números específicos de vulnerabilidades y exposiciones comunes:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Excluir resultados no deseados (`--exclude`)
Filtre los scripts auxiliares o de denegación de servicio (DoS) cuando busque la ejecución remota de código (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Examinar y copiar exploits (`-m` / `-p` / `-x`)

Nunca edite ni ejecute exploits directamente en el directorio `/usr/share/exploitdb/`. Utilice las funciones de copiar e inspeccionar de Searchsploit.

### 1. Copiar (duplicar) el exploit al directorio actual (`-m`)
Copie un archivo PoC de exploit (Python, C, Ruby, bash) directamente a su directorio de trabajo local:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Inspeccionar los metadatos del exploit y la ruta completa (`-p`)
Muestre la ruta absoluta del sistema de archivos y la URL de Exploit-DB para un EDB-ID:
```bash
searchsploit -p 50383
```
Producción:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Leer el código de explotación en el buscapersonas de terminal (`-x`)
Abra el código de explotación directamente en `less` para una inspección rápida:
```bash
searchsploit -x 50383
```
---

## Filtrado por plataforma y tipo

Searchsploit indexa exploits, shellcode y artículos de investigación en múltiples arquitecturas:

### 1. Filtrar por sistema operativo de destino
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Filtrar solo para códigos Shell sin formato (`-s`)
Busque cargas útiles de shellcode específicas de la arquitectura (por ejemplo, execve `/bin/sh` sin bytes nulos):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filtro para documentos técnicos y documentos de seguridad (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Integración de escaneo Nmap XML/Grepable

Searchsploit puede analizar automáticamente los resultados del escaneo XML de Nmap y comparar las versiones de software descubiertas con exploits conocidos:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit inspecciona cada banner de puerto abierto y cadena de versión, consultando exploits locales para una clasificación inmediata.*

---

## Salida JSON y automatización de scripts

Para la automatización de tuberías y herramientas de reconocimiento personalizadas, los resultados de salida en JSON estructurado:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtre las rutas de los archivos de explotación utilizando `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Mejores prácticas en pruebas de penetración

1. **Verificar antes de ejecutar:** Inspeccione siempre el código fuente del exploit (`searchsploit -x <id>`) antes de la compilación o ejecución. Verifique el código shell codificado, las direcciones IP de destino y asegúrese de que no haya puertas traseras ni etapas de carga útil maliciosas.
2. **Compilar con indicadores correctos:** Muchos exploits de Exploit-DB C requieren indicadores GCC específicos (por ejemplo, `-m32`, `-lpthread` o `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Mantenga la base de datos actualizada:** Ejecute `searchsploit -u` periódicamente para garantizar que los días cero y las pruebas de concepto recién publicados se indexen localmente.