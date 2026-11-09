<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools: foglio informativo completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<palign="centro">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Sicurezza Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<palign="centro">
  <b>📂 Toolkit essenziale per scoprire cartelle <code>/.git/</code> esposte sui server Web, scaricare archivi di oggetti Git non elaborati e ricostruire completamente il codice sorgente originale e la cronologia dei commit</b>
</p>

---

## 📋 Sommario

- [Panoramica e rischio .git esposto](#-panoramica--the-exposed-git-hazard)
- [Installazione](#-installazione)
- [I tre moduli GitTools](#-the-tre-gittools-modules)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Estrattore (`extractor.sh`)](#2-estrattore-estrattoresh)
  - [3. Finder (`finder.py`)](#3-finder-finderpy)
- [Dumper Python alternativo: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Segreto post-ricostruzione e ricerca di credenziali](#-post-ricostruzione-segreto--ricerca-di-credenziali)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rafforzamento del server Web](#-considerazioni-difensive--web-server-hardening)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e rischio .git esposto

Quando gli sviluppatori distribuiscono applicazioni web tramite `git clone` o script automatizzati senza limitare adeguatamente l'accesso alle directory nascoste, la cartella **`/.git/`** diventa accessibile tramite HTTP/HTTPS:
`https://target.com/.git/`

Anche se l'elenco delle directory (indicizzazione) è disabilitato, la struttura deterministica di Git consente a un utente malintenzionato di scaricare `/.git/HEAD`, `/.git/config`, `/.git/index` e attraversare tutti gli oggetti sciolti Git (`.git/objects/xx/xxxx...`). Ciò consente la ricostruzione completa di:
- Codice sorgente completo dell'applicazione e API interne.
- Credenziali del database, token API e chiavi SSH private nei file di configurazione.
- Registri completi dei commit git, email degli autori e rami di sviluppo.

---

## 📥Installazione
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 I tre moduli GitTools

GitTools è organizzato in tre utilità specializzate:

### 1. Dumper (`gitdumper.sh`)
Scarica quanta più directory `.git` possibile dal server web:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Estrattore (`extractor.sh`)
Ricostruisce file originali, alberi di commit e rami dagli oggetti sciolti `.git` scaricati:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
All'interno di `/tmp/extracted_source/`, ogni commit viene ripristinato in una directory isolata contenente l'intero albero dei file in quel momento.

### 3. Trova (`finder.py`)
Esegue la scansione di elenchi di domini o blocchi CIDR per rilevare directory `.git` esposte:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Dumper Python alternativo: `git-dumper`

`git-dumper` è un'alternativa Python multi-thread che ricostruisce automaticamente un repository git attivo e funzionante in un solo passaggio:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Una volta completato, puoi utilizzare i normali comandi `git` all'interno della cartella:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Caccia ai segreti e alle credenziali post-ricostruzione

Una volta ripristinato il repository, cerca credenziali e file nascosti:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Dump con git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Dump con GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Estrai commit nell'origine** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Scansiona elenco host per .git** | `python3 Finder/finder.py -i domains.txt` |
| **Visualizza cronologia commit** | `git log -p --color` |
| **Caccia ai segreti codificati** | `gitleaks detect -v` |

---

## 🛡️ Considerazioni difensive e rafforzamento del server Web

1. **Indurimento Nginx:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Indurimento di Apache (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Best practice CI/CD:** Non distribuire mai il codice clonando git direttamente nella root web (`/var/www/html/`). Utilizza bundle di artefatti puliti o contenitori Docker.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> GitTools e git-dumper sono progettati per valutazioni di vulnerabilità autorizzate e audit dell'infrastruttura difensiva. Scaricare codice sorgente proprietario da applicazioni web non autorizzate senza autorizzazione è illegale.