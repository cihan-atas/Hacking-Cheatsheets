<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="center">
  <b>🌲 Énumérer et vider les zones et enregistrements Active Directory Integrated DNS (ADIDNS) via LDAP sans transferts de zone DNS (AXFR)</b>
</p>

---

## 📋 Table des matières

-[Aperçu et contexte technique](#-overview--technical-background)
- [Installation](#-installation)
- [Méthodes d'authentification](#-authentication-methods)
  - [Authentification par mot de passe](#password-authentication)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Ticket Kerberos (ccache)](#kerberos-ticket-ccache)
- [Commandes de dumping de base](#-core-dumping-commandes)
  - [Dump de zone DNS Active Directory standard](#standard-active-directory-dns-zone-dump)
  - [Dumping Tombstoned / Supprimé Records](#dumping-tombstoned--deleted-records)
  - [Dumping des zones DNS forestières et DNS de domaine](#dumping-forest-dns--domain-dns-zones)
  - [Ciblage de zones DNS spécifiques](#targeting-special-dns-zones)
- [Formats d'exportation et post-traitement](#-export-formats--post-traitement)
  - [Sortie CSV](#csv-output)
  - [Intégration avec BloodHound & Nmap](#integrating-with-bloodhound--nmap)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et contexte technique

Dans les environnements Windows Active Directory standard, DNS est généralement intégré à Active Directory (**Active Directory-Integrated DNS / ADIDNS**). Dans ces configurations, les enregistrements DNS ne sont pas stockés dans de simples fichiers plats de texte, mais sous forme d'objets LDAP au sein de la partition de domaine (tels que `DC=DomainDnsZones` et `DC=ForestDnsZones`).

Les transferts de zone DNS traditionnels (`AXFR`) sont presque universellement désactivés sur les contrôleurs de domaine. Cependant, **tout utilisateur de domaine authentifié** (même les comptes à faible privilèges ou les comptes de service compromis) dispose par défaut d'autorisations de lecture sur les nœuds LDAP stockant les enregistrements ADIDNS.

Développé par **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** se connecte au contrôleur de domaine via LDAP ou LDAPS, interroge les partitions ADIDNS et extrait tous les enregistrements (A, AAAA, CNAME, SRV, TXT, MX et enregistrements tombstoned), créant ainsi un inventaire précis de chaque ordinateur, poste de travail et service Web interne du domaine.

---

## 📥Installation
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Méthodes d'authentification

### Authentification par mot de passe
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Passer le hachage (NTLM)
Si vous avez extrait les hachages NTLM via Mimikatz ou Secretsdump :
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Ticket Kerberos (ccache)
Utilisation d'un ticket Kerberos TGT/TGS existant stocké dans `KRB5CCNAME` :
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Commandes de vidage de base

### Dump de zone DNS Active Directory standard
Vide tous les enregistrements DNS actifs de la partition DomainDnsZones par défaut :
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Cela génère un fichier nommé `records.csv` dans votre répertoire de travail actuel.

### Dumpstoned / Enregistrements supprimés
Le DNS Windows marque les enregistrements supprimés comme étant « désactivés » pendant plusieurs jours avant la suppression physique. Ces enregistrements révèlent souvent des systèmes mis hors service, des serveurs existants ou des environnements de test :
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping des zones DNS forestières et DNS de domaine
Extrayez les enregistrements des partitions DNS au niveau de la forêt :
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Ciblage de zones DNS spécifiques
Si la cible exploite plusieurs zones ou sous-domaines DNS split-brain :
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Communication cryptée LDAPS sécurisée
Empêchez les informations d'identification de traverser le réseau en texte brut :
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Formats d'exportation et post-traitement

### Sortie CSV
Par défaut, `adidnsdump` génère `records.csv`. Les champs comprennent :
- `zone` : La zone DNS à laquelle appartient l'enregistrement.
- `name` : enregistrez le nom d'hôte ou l'étiquette.
- `type` : Type d'enregistrement (A, AAAA, CNAME, SRV, etc.).
- `value` : adresse IP cible, nom d'hôte ou chaîne.

### Intégration avec BloodHound et Nmap
Extrayez toutes les adresses IP uniques des enregistrements sauvegardés pour une analyse automatisée :
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Décharge ADIDNS standard** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Passez le hachage (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Authentification des tickets Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Vider les enregistrements tombés en panne**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Zones forestières déversées** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Spécifiez la zone cible** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Appliquer LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Considérations défensives et détection

1. **Surveillance des requêtes LDAP :** Activez la stratégie d'audit avancée pour l'accès au service d'annuaire (ID d'événement 4662) pour alerter sur les requêtes à volume élevé sur `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Restreindre les ACL ADIDNS :** Restreindre les autorisations de lecture sur le conteneur `MicrosoftDNS` aux administrateurs de domaine ou à des rôles administratifs spécifiques si les non-administrateurs ne nécessitent pas d'introspection DNS interne.
3. **Honeytokens :** Créez des enregistrements DNS attrayants (par exemple, `backup-vault.corp.local` ou `admin-portal.corp.local`) pointant vers des adresses IP de pots de miel.

---

## ⚠️ Mentions légales

> [!ATTENTION]
> adidnsdump est strictement destiné aux évaluations de sécurité autorisées, aux audits Active Directory et aux tests d'intrusion dans les limites autorisées. L'interrogation des services d'annuaire sur des réseaux non autorisés est illégale.