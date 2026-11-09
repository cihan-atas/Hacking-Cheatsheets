<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# enum4linux-ng Cheatsheet

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) è una riscrittura di nuova generazione dello strumento legacy `enum4linux` scritto in Python 3. Esegue la raccolta completa di informazioni Windows e Samba su SMB (Server Message Block), NetBIOS, RPC (Remote Procedure Call) e LDAP. Estrae account utente, appartenenze a gruppi, cartelle condivise, criteri password, informazioni sul sistema operativo e cicli RID.

---

## Sommario

- [Miglioramenti chiave rispetto a Legacy enum4linux](#miglioramenti-chiave-over-legacy-enum4linux)
- [Installazione e requisiti](#installazione--requisiti)
- [Enumerazione SMB e domini di base](#basic-smb--enumerazione-dominio)
- [Modalità di autenticazione (sessione nulla e utente valido)](#modalità-autenticazione-sessione-null-vs-utente-valido)
- [Moduli di enumerazione mirata](#moduli di enumerazione mirata)
- [Rid Cycling e raccolta account utente](#rid-cycling--user-account-harvesting)
- [Esportazione di report (JSON, YAML, testo)](#exporting-reports-json-yaml-text)
- [Indurimento difensivo per PMI e RPC](#defensive-hardening-per-smb--rpc)

---

## Miglioramenti chiave rispetto alla versione precedente di enum4linux

- **Moderno motore Python 3:** Esecuzione multi-thread basata su `impacket`, eliminando i crash dei vecchi script Perl.
- **Rilevamento del sistema operativo avanzato:** Rilevamento preciso di Windows Server (2012–2025), Windows 10/11 e versioni moderne di Samba.
- **Report strutturati:** Esportazione nativa in JSON e YAML per una perfetta integrazione SIEM o scripting.
- **Riciclaggio RID più intelligente:** regolazioni flessibili dell'intervallo e rilevamento automatico dei RID amministratore/ospite.
- **Output CLI colorato:** Chiari indicatori visivi per configurazioni errate di sicurezza e sessioni nulle.

---

## Installazione e requisiti
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## Enumerazione base di PMI e domini

### 1. Esecuzione dell'enumerazione predefinita (`-A`)
Esegui tutti i controlli di enumerazione non intrusivi sulla destinazione:
```bash
enum4linux-ng -A 192.168.1.50
```
Questo modulo interroga:
- Nomi NetBIOS e nomi di gruppi di lavoro/domini.
- Versione e architettura del sistema operativo.
- Directory condivise e autorizzazioni di accesso.
- Politica della password (soglia di blocco, lunghezza minima, cronologia).
- Utenti e gruppi locali tramite RPC/SAMR.

---

## Modalità di autenticazione (sessione nulla e utente valido)

### 1. Sessione anonima/nulla (`-u '' -p ''`)
Verifica se la destinazione consente connessioni IPC$ non autenticate:
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Dominio autenticato/Account locale
Fornisci credenziali valide con privilegi limitati per espandere notevolmente i dati raccolti:
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Fornire un nome di dominio specifico (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Moduli di enumerazione mirati

Esegui moduli specifici per ridurre al minimo l'ingombro della rete ed evitare avvisi di rilevamento delle intrusioni:

### 1. Cartelle condivise e autorizzazioni di accesso (`-S`)
Enumerare condivisioni, commenti e controllare l'accesso in lettura/scrittura:
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Estrazione della policy password (`-P`)
Identificare le soglie di blocco dell'account per pianificare in modo sicuro la diffusione delle password:
```bash
enum4linux-ng -P 192.168.1.50
```
Risultati chiave da osservare:
- **Lunghezza minima della password:** ad esempio 8 caratteri.
- **Soglia di blocco dell'account:** ad esempio, 5 tentativi (non spruzzare più velocemente della finestra di blocco!).
- **Reimposta contatore di blocco:** ad esempio, 30 minuti.

### 3. Solo utenti e gruppi (`-U` e `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Solo informazioni sul sistema operativo e NetBIOS (`-O` e `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## Ciclo RID e raccolta degli account utente

Quando l'enumerazione degli utenti tramite query RPC standard è limitata, eseguire il ciclo RID (Relative Identifier) tramite chiamate di query SAMR:

### 1. Ciclo RID predefinito (intervallo 500 - 1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Intervallo RID personalizzato (ad esempio da 500 a 5000 per le grandi organizzazioni)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Estrai la lista di parole pulita del nome utente
Filtra i nomi utente RID rilevati in un file di testo per la spruzzatura della password:
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Esportazione di report (JSON, YAML, testo)

Esporta i risultati dell'enumerazione per la documentazione o l'analisi programmatica:

### 1. Esporta in tutti i formati contemporaneamente (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Genera:
-`/tmp/target_recon.json`
-`/tmp/target_recon.yaml`
-`/tmp/target_recon.txt`

### 2. Esporta in un formato specifico
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Rafforzamento difensivo per PMI e RPC

| Vettore | Rischio | Raccomandazione di rafforzamento |
|---|---|---|
| **Sessione nulla (IPC$ anonimo)** | Ricognizione domini non autenticati | Imposta `RestrictNullSessAccess=1` nel registro di Windows; configurare `RestrictAnonymous=2` in SAM. |
| **SMBv1 attivo** | Attacchi EternalBlue (MS17-010) e MITM | Disabilita completamente SMBv1 su tutti gli endpoint e i controller di dominio. |
| **Autorizzazioni per la condivisione di testo in chiaro** | File sensibili e credenziali esposte | Autorizzazioni di condivisione del controllo; applicare l'enumerazione basata sull'accesso (ABE). |
| **Enumerazione ciclica RID** | Estrazione completa dell'elenco degli account utente | Limita l'enumerazione degli account SAM anonimi tramite la policy di sicurezza locale. |