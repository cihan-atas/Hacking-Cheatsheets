<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Aide-mémoire dnsrecon

[dnsrecon](https://github.com/darkooperator/dnsrecon) est un outil complet de reconnaissance et d'énumération DNS écrit en Python. Il prend en charge les transferts de zone (AXFR), les recherches inversées, le forçage de domaine, la découverte d'enregistrements SRV, la détection de caractères génériques, la surveillance du cache et l'expansion des TLD.

---

## Table des matières

- [Capacités de base](#core-capabilities)
- [Installation](#installation)
- [Énumération standard et transferts de zones (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [Forçage brut du sous-domaine DNS (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Analyse de plage de recherche inversée (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
-[DNS Cache Snooping (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Ciblage de serveurs de noms spécifiques (`-n`)](#ciblage de serveurs de noms spécifiques--n)
- [Formats de sortie (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Renforcement défensif pour les serveurs DNS](#defensive-hardening-for-dns-servers)

---

## Capacités de base

- **Zone Transfer Testing (AXFR) :** Teste automatiquement tous les serveurs de noms faisant autorité pour les transferts de zone non authentifiés.
- **Énumération des enregistrements :** Récupère les enregistrements SOA, NS, A, AAAA, MX, TXT, SPF et SRV.
- **Sous-domaine Brute-Force :** Recherches de dictionnaire multithread avec filtrage par caractères génériques.
- **Analyse inversée de la plage IP :** Extraction d'enregistrements PTR sur des blocs CIDR arbitraires.
- **Cache Snooping :** Teste si les serveurs de noms récursifs ont des domaines spécifiques mis en cache en mémoire.
- **Extension TLD :** découvre les domaines correspondants dans les domaines nationaux et génériques de premier niveau.

---

##Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Énumération standard et transferts de zones (`-d -t std / axfr`)

### 1. Énumération standard des enregistrements DNS
Collecte les enregistrements SOA, NS, A, AAAA, MX et TXT et tente automatiquement les transferts de zone contre chaque NS faisant autorité :
```bash
dnsrecon -d example.com -t std
```
### 2. Test de transfert de zone dédiée (AXFR) uniquement
Testez uniquement la vulnérabilité AXFR sur tous les serveurs de noms faisant autorité :
```bash
dnsrecon -d example.com -t axfr
```
*En cas de succès, l'intégralité de la base de données DNS de la cible est vidée sans force brute.*

---

## Forçage brut du sous-domaine DNS (`-t brt`)

Sous-domaines par force brute utilisant des listes de mots personnalisées ou par défaut :

### 1. Sous-domaine Brute-Force avec liste de mots personnalisée
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>` : Domaine cible.
- `-t brt` : mode force brute.
- `-D <file>` : Fichier dictionnaire de sous-domaine.

### 2. Brute-Force avec filtrage générique
Empêche les configurations DNS génériques (`*.example.com` se résolvant en une seule adresse IP fourre-tout) de polluer les résultats :
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw` : Ignorer les résultats de résolution générique.

---

## Analyse de plage de recherche inversée (`-t rvl`)

Énumérez les enregistrements PTR pour un bloc IP afin d'identifier les hôtes internes, les passerelles de messagerie et les instances intermédiaires :
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>` : plage IP en notation CIDR (par exemple `192.168.1.0/24`) ou notation début-fin (`192.168.1.1-192.168.1.254`).
- `-t rvl` : Mode de recherche inversée.

---

## Surveillance du cache DNS (`-t snoop`)

Déterminez si les clients d'un réseau cible ont récemment résolu des domaines spécifiques via leur résolveur interne :
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>` : serveur DNS cible à interroger.
- `-D <domains_file>` : Liste des domaines à tester (ex : concurrents, C2 malveillants, portails internes).

---

## Ciblage de serveurs de noms spécifiques (`-n`)

Interrogez directement les serveurs DNS internes ou spécifiques à la cible plutôt que les résolveurs publics en amont :
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Formats de sortie (CSV, JSON, SQLite, XML)

Enregistrez les résultats pour l'intégration avec la billetterie, le SIEM ou d'autres phases d'attaque :

### 1. Exporter vers JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Exporter vers la base de données SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Exporter vers CSV et XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Defensive Hardening for DNS Servers

| Vulnérabilité | Risque d'exploitation | Défense recommandée |
|---|---|---|
| **AXFR sans restriction** | Cartographie complète de l'infrastructure interne | Restreindre les transferts de zone aux adresses IP de serveurs de noms secondaires autorisées uniquement (`allow-transfer { 10.0.0.2; };` dans BIND). |
| **Open Recursive Resolver** | Amplification DNS DDoS et surveillance | Disable external recursion (`recursion no;` in BIND). |
| **Predictable Hostnames** | Subdomain brute-forcing | Utilisez des sous-domaines aléatoires pour les environnements de test sensibles. |
| **Reprise de sous-domaine** | CNAME pointant vers des services AWS/GitHub/Azure non réclamés | Auditez les enregistrements DNS CNAME en suspens et supprimez immédiatement les mappages inutilisés. |