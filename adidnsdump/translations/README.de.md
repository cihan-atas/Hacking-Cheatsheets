<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump – Vollständiger Cheatsheet

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
  <b>🌲 Zählen und sichern Sie ADIDNS-Zonen (Active Directory Integrated DNS) und Einträge über LDAP ohne DNS-Zonenübertragungen (AXFR)</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und technischer Hintergrund](#-overview--technical-background)
- [Installation](#-Installation)
- [Authentifizierungsmethoden](#-authentication-methods)
  - [Passwortauthentifizierung](#password-authentication)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Kerberos-Ticket (ccache)](#kerberos-ticket-ccache)
- [Core-Dumping-Befehle](#-core-dumping-commands)
  - [Standard Active Directory DNS Zone Dump](#standard-active-directory-dns-zone-dump)
  - [Tombstoned/Deleted Records löschen](#dumping-tombstoned--deleted-records)
  - [Dumping von Forest-DNS- und Domain-DNS-Zonen](#dumping-forest-dns--domain-dns-zones)
  - [Targeting auf bestimmte DNS-Zonen](#targeting-special-dns-zones)
- [Exportformate und Nachbearbeitung](#-export-formats--post-processing)
  - [CSV-Ausgabe](#csv-output)
  - [Integration mit BloodHound und Nmap](#integrating-with-bloodhound--nmap)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und technischer Hintergrund

In Standard-Windows-Active-Directory-Umgebungen ist DNS normalerweise in Active Directory integriert (**Active Directory-Integrated DNS / ADIDNS**). In diesen Konfigurationen werden DNS-Einträge nicht in einfachen Text-Flatfiles gespeichert, sondern als LDAP-Objekte innerhalb der Domänenpartition (z. B. `DC=DomainDnsZones` und `DC=ForestDnsZones`).

Herkömmliche DNS-Zonenübertragungen (`AXFR`) sind auf Domänencontrollern fast überall deaktiviert. Allerdings verfügt **jeder authentifizierte Domänenbenutzer** (auch Konten mit geringen Berechtigungen oder kompromittierte Dienstkonten) standardmäßig über Leseberechtigungen für die LDAP-Knoten, auf denen ADIDNS-Datensätze gespeichert sind.

Entwickelt von **Dirk-jan Mollema (@dirkjanm)** stellt **`adidnsdump`** über LDAP oder LDAPS eine Verbindung zum Domänencontroller her, fragt die ADIDNS-Partitionen ab und extrahiert alle Datensätze (A, AAAA, CNAME, SRV, TXT, MX und Tombstoned-Datensätze), wodurch eine genaue Bestandsaufnahme aller Computer, Workstations und internen Webdienste in der Domäne erstellt wird.

---

## 📥 Installation
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Authentifizierungsmethoden

### Passwortauthentifizierung
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Pass-the-Hash (NTLM)
Wenn Sie NTLM-Hashes über Mimikatz oder Secretsdump extrahiert haben:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Kerberos-Ticket (ccache)
Verwendung eines vorhandenen Kerberos TGT/TGS-Tickets, das in `KRB5CCNAME` gespeichert ist:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Core-Dumping-Befehle

### Standardmäßiger Active Directory-DNS-Zonen-Dump
Speichert alle aktiven DNS-Einträge von der Standard-DomainDnsZones-Partition:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Dadurch wird eine Datei mit dem Namen `records.csv` in Ihrem aktuellen Arbeitsverzeichnis generiert.

### Tombstoned/Gelöschte Datensätze werden gelöscht
Windows DNS markiert gelöschte Datensätze mehrere Tage lang als „tombstoned“, bevor sie physisch gelöscht werden. Diese Aufzeichnungen offenbaren oft stillgelegte Systeme, Legacy-Server oder Testumgebungen:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping von Gesamt-DNS- und Domänen-DNS-Zonen
Extrahieren Sie Datensätze aus DNS-Partitionen auf Gesamtstrukturebene:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Ausrichtung auf bestimmte DNS-Zonen
Wenn das Ziel mehrere Split-Brain-DNS-Zonen oder Subdomains betreibt:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Sichere LDAPS-verschlüsselte Kommunikation
Verhindern Sie, dass Anmeldeinformationen das Netzwerk im Klartext passieren:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Exportformate und Nachbearbeitung

### CSV-Ausgabe
Standardmäßig gibt `adidnsdump` `records.csv` aus. Zu den Feldern gehören:
- `zone`: Die DNS-Zone, zu der der Eintrag gehört.
- `name`: Hostnamen oder Label aufzeichnen.
- `type`: Datensatztyp (A, AAAA, CNAME, SRV usw.).
- `value`: Ziel-IP-Adresse, Hostname oder Zeichenfolge.

### Integration mit BloodHound und Nmap
Extrahieren Sie alle eindeutigen IP-Adressen aus den gespeicherten Datensätzen zum automatischen Scannen:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Standard-ADIDNS-Dump** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pass-the-Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos-Ticket-Authentifizierung** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Dump Tombstoned Records**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Dump Forest Zones** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Zielzone angeben** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **LDAPS (SSL) erzwingen** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **LDAP-Abfrageüberwachung:** Aktivieren Sie die erweiterte Überwachungsrichtlinie für den Verzeichnisdienstzugriff (Ereignis-ID 4662), um bei umfangreichen Abfragen gegen `CN=MicrosoftDNS,DC=DomainDnsZones` zu warnen.
2. **ADIDNS-ACLs einschränken:** Beschränken Sie die Leseberechtigungen für den `MicrosoftDNS`-Container auf Domänenadministratoren oder bestimmte Administratorrollen, wenn Nicht-Administratoren keine interne DNS-Selbstprüfung benötigen.
3. **Honeytokens:** Erstellen Sie attraktive DNS-Einträge (z. B. `backup-vault.corp.local` oder `admin-portal.corp.local`), die auf Honeypot-IP-Adressen verweisen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> adidnsdump ist ausschließlich für autorisierte Sicherheitsbewertungen, Active Directory-Prüfungen und Penetrationstests innerhalb zulässiger Bereiche gedacht. Das Abfragen von Verzeichnisdiensten in nicht autorisierten Netzwerken ist illegal.