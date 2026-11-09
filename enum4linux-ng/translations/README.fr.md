<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire enum4linux-ng

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) est une réécriture de nouvelle génération de l'ancien outil `enum4linux` écrit en Python 3. Il effectue une collecte complète d'informations Windows et Samba sur SMB (Server Message Block), NetBIOS, RPC (Remote Procedure Call) et LDAP. Il extrait les comptes d'utilisateurs, les appartenances à des groupes, les dossiers partagés, les politiques de mot de passe, les informations sur le système d'exploitation et les cycles RID.

---

## Table des matières

-[Améliorations clés par rapport à l'ancien enum4linux](#key-improvements-over-legacy-enum4linux)
- [Installation et exigences](#installation--exigences)
- [SMB de base et énumération de domaines](#basic-smb--domain-enumeration)
- [Modes d'authentification (session nulle vs utilisateur valide)] (#authentication-modes-null-session-vs-valid-user)
- [Modules d'énumération ciblée](#targeted-enumeration-modules)
-[RID Cycling & User Account Harvesting](#rid-cycling--user-account-harvesting)
- [Exportation de rapports (JSON, YAML, Texte)](#exporting-reports-json-yaml-text)
-[Renforcement défensif pour SMB et RPC](#defensive-hardening-for-smb--rpc)

---

## Améliorations clés par rapport à l'ancien enum4linux

- **Moteur Python 3 moderne :** Exécution multithread basée sur `impacket`, éliminant les anciens plantages de scripts Perl.
- **Détection améliorée du système d'exploitation :** Détection précise de Windows Server (2012-2025), Windows 10/11 et des versions modernes de Samba.
- **Rapports structurés :** Exportation native vers JSON et YAML pour une intégration transparente SIEM ou scripts.
- **Cyclisme RID plus intelligent :** Ajustements de plage flexibles et détection automatique du RID administrateur/invité.
- **Sortie CLI colorisée :** Indicateurs visuels clairs pour les mauvaises configurations de sécurité et les sessions nulles.

---

## Installation et exigences
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

## Énumération de base des PME et des domaines

### 1. Exécution d'énumération par défaut (`-A`)
Exécutez toutes les vérifications d'énumération non intrusives sur la cible :
```bash
enum4linux-ng -A 192.168.1.50
```
Ce module interroge :
- Noms NetBIOS et noms de groupes de travail/domaines.
- Version et architecture du système d'exploitation.
- Répertoires partagés et autorisations d'accès.
- Politique de mot de passe (seuil de verrouillage, longueur minimale, historique).
- Utilisateurs et groupes locaux via RPC/SAMR.

---

## Modes d'authentification (session nulle ou utilisateur valide)

### 1. Session anonyme/nulle (`-u '' -p ''`)
Testez si la cible autorise les connexions IPC$ non authentifiées :
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Domaine authentifié/compte local
Fournissez des informations d'identification valides à faible privilège pour augmenter considérablement les données récoltées :
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Fournir un nom de domaine spécifique (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Modules d'énumération ciblés

Exécutez des modules spécifiques pour minimiser l'empreinte réseau et éviter les alertes de détection d'intrusion :

### 1. Dossiers partagés et autorisations d'accès (`-S`)
Énumérez les partages, les commentaires et vérifiez l'accès en lecture/écriture :
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Extraction de la politique de mot de passe (`-P`)
Identifiez les seuils de verrouillage de compte pour planifier en toute sécurité la pulvérisation de mots de passe :
```bash
enum4linux-ng -P 192.168.1.50
```
Résultats clés à observer :
- **Longueur minimale du mot de passe :** par exemple, 8 caractères.
- **Seuil de verrouillage du compte :** par exemple, 5 tentatives (ne pulvérisez pas plus vite que la fenêtre de verrouillage !).
- **Réinitialiser le compteur de verrouillage :** par exemple, 30 minutes.

### 3. Utilisateurs et groupes uniquement (`-U` et `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Informations sur le système d'exploitation et NetBIOS uniquement (`-O` et `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## Cyclisme RID et récolte de comptes utilisateur

Lorsque l'énumération des utilisateurs via des requêtes RPC standard est restreinte, effectuez un cycle d'identification relative (RID) via des appels de requête SAMR :

### 1. Cyclage RID par défaut (plage 500 - 1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Plage RID personnalisée (par exemple 500 à 5 000 pour les grandes organisations)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Extraire la liste de mots de nom d'utilisateur propre
Filtrez les noms d'utilisateur RID découverts dans un fichier texte pour pulvériser les mots de passe :
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Exportation de rapports (JSON, YAML, texte)

Exportez les résultats de l’énumération pour la documentation ou l’analyse programmatique :

### 1. Exporter dans tous les formats simultanément (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Génère :
-`/tmp/target_recon.json`
-`/tmp/target_recon.yaml`
-`/tmp/target_recon.txt`

### 2. Exporter vers un format spécifique
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Renforcement défensif pour les PME et RPC

| Vecteur | Risque | Recommandation de durcissement |
|---|---|---|
| **Session nulle (IPC$ anonyme)** | Reconnaissance de domaine non authentifiée | Définissez `RestrictNullSessAccess=1` dans le registre Windows ; configurez `RestrictAnonymous=2` dans SAM. |
| **SMBv1 actif** | EternalBlue (MS17-010) et attaques MITM | Désactivez complètement SMBv1 sur tous les points de terminaison et contrôleurs de domaine. |
| **Autorisations de partage en texte clair** | Fichiers sensibles et informations d'identification exposés | Auditer les autorisations de partage ; appliquer l’énumération basée sur l’accès (ABE). |
| **Énumération des cycles RID** | Extraction complète de la liste des comptes d'utilisateurs | Restreindre l'énumération des comptes SAM anonymes via la politique de sécurité locale. |