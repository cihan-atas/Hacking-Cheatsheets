<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire CUPP (profileur de mots de passe utilisateur commun)

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) est un générateur de dictionnaire de mots de passe spécialisé conçu pour le profilage humain ciblé. Plutôt que de forcer brutalement avec des dictionnaires génériques, CUPP génère des permutations basées sur des détails personnels (nom de la cible, partenaire, animaux de compagnie, dates de naissance, entreprise, loisirs, variations de langage libre et combinaisons de caractères spéciaux).

---

## Table des matières

- [Présentation et cas d'utilisation](#overview--use-cases)
- [Installation](#installation)
- [Assistant de profilage interactif (`-i`)](#interactive-profiling-wizard--i)
-[Configuration et règles Leet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Téléchargement et fusion de dictionnaires (`-l` et `-w`)](#downloading--merging-dictionaries--l---w)
- [Sortie de tuyauterie vers Hashcat et Hydra](#piping-output-to-hashcat--hydra)
- [Conseils défensifs et politiques de mot de passe](#defensive-insights--password-policy-guidelines)

---

## Présentation et cas d'utilisation

Les attaques par dictionnaire traditionnelles échouent souvent contre des mots de passe complexes qui suivent des modèles prévisibles par l'homme (par exemple, `John1984!`, `Sarah_Fluffy2022#`). Le CUPP résout ce problème en :
- Collecte de renseignements personnels spécifiques à une cible.
- Combiner les noms, les années de naissance, les dates importantes et les noms d'animaux.
- Application de mutations basées sur des règles (majuscules, remplacement de la parole, caractères spéciaux, ajout de nombres aléatoires).
- Produire une liste de mots optimisée et ciblée considérablement plus petite que `rockyou.txt` mais avec une pertinence cible plus élevée.

---

##Installation
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## Assistant de profilage interactif (`-i`)

Lancez l'invite du questionnaire pour créer une liste de mots personnalisée :
```bash
python3 cupp.py -i
```
Exemple de procédure pas à pas :
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
Le fichier de sortie `alice.csv` (ou le fichier texte spécifié) est généré avec des milliers de mutations personnalisées (par exemple, `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Configuration et règles Leet (`cupp.cfg`)

La configuration CUPP est contrôlée via `cupp.cfg`. Les sections clés comprennent :
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
Modifiez `cupp.cfg` pour l'ajuster aux paramètres régionaux et à la langue cibles.

---

## Téléchargement et fusion de dictionnaires (`-l` et `-w`)

### 1. Téléchargez des listes de mots précompilées à partir de référentiels
Téléchargez les listes de mots linguistiques par défaut :
```bash
python3 cupp.py -l
```
### 2. Améliorez une liste de mots existante avec Leet / Règles spéciales
Appliquez les règles de mutation CUPP à une liste de mots existants :
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Sortie de tuyauterie vers Hashcat et Hydra

Introduisez le dictionnaire personnalisé généré directement dans les outils de force brute :

### Cracker les hachages cibles avec Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Authentification en ligne Brute-Force avec Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Informations défensives et directives en matière de politique de mot de passe

- **Bannir les attributs personnels :** Interdire les mots de passe des utilisateurs contenant les prénoms, noms, noms d'utilisateur, noms de marque de l'entreprise ou dates de naissance des employés.
- **Imposer la longueur plutôt que la complexité :** Une phrase secrète de plus de 16 caractères (par exemple, quatre mots aléatoires du dictionnaire) est nettement plus résistante aux outils de profilage qu'un modèle `NameYear!` de 8 caractères.
- **Authentification multifacteur (MFA) :** Appliquez les jetons matériels FIDO2 ou TOTP afin que la génération de dictionnaires personnalisés ne puisse à elle seule compromettre les comptes.