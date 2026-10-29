#!/usr/bin/env python3
"""
Hacking-Cheatsheets Mobile Database Builder
Parses all Markdown cheatsheets (English, Turkish, Italian) and compiles
them into an optimized, pre-indexed SQLite database with FTS5 search.
"""

import os
import re
import json
import sqlite3

CATEGORY_MAPPING = {
    "Attack-Methodology": "Attack Methodology",
    "AD-Attack-Methodology": "Active Directory",
    "BloodHound": "Active Directory",
    "Certipy": "Active Directory",
    "CrackMapExec": "Active Directory",
    "Evil-WinRM": "Active Directory",
    "Impacket": "Active Directory",
    "Kerbrute": "Active Directory",
    "NetExec": "Active Directory",
    "PowerView": "Active Directory",
    "Responder": "Active Directory",
    "Rubeus": "Active Directory",
    "Ligolo-ng": "Pivoting & Tunneling",
    "Chisel": "Pivoting & Tunneling",
    "Linux-PrivEsc": "Privilege Escalation",
    "Windows-PrivEsc": "Privilege Escalation",
    "PEASS-ng": "Privilege Escalation",
    "LOLBAS-GTFOBins": "Privilege Escalation",
    "Mimikatz": "Privilege Escalation",
    "Nmap": "Recon & Scanning",
    "Feroxbuster": "Recon & Scanning",
    "Dirb": "Recon & Scanning",
    "Gobuster": "Recon & Scanning",
    "Nikto": "Recon & Scanning",
    "ffuf": "Recon & Scanning",
    "Nuclei": "Recon & Scanning",
    "Subfinder": "Recon & Scanning",
    "httpx": "Recon & Scanning",
    "Katana": "Recon & Scanning",
    "GAU": "Recon & Scanning",
    "Amass": "Recon & Scanning",
    "Arjun": "Recon & Scanning",
    "Dalfox": "Recon & Scanning",
    "Burp-Suite": "Web Application Testing",
    "OWASP-ZAP": "Web Application Testing",
    "SQLMap": "Web Application Testing",
    "API-Security": "Web Security & API",
    "Auth-Bypass": "Web Security & API",
    "Cache-Poisoning": "Web Security & API",
    "Cloudflare-Bypass": "Web Security & API",
    "CORS": "Web Security & API",
    "HTTP-Request-Smuggling": "Web Security & API",
    "IDOR": "Web Security & API",
    "JWT-Attacks": "Web Security & API",
    "Open-Redirect": "Web Security & API",
    "Prototype-Pollution": "Web Security & API",
    "Race-Conditions": "Web Security & API",
    "SSRF": "Web Security & API",
    "Subdomain-Takeover": "Web Security & API",
    "WAF-Bypass": "Web Security & API",
    "XXE": "Web Security & API",
    "Payloads": "Payloads Collection",
    "Metasploit": "Exploitation & Shells",
    "PowerShell": "Exploitation & Shells",
    "Linux-Commands": "Exploitation & Shells",
    "Hydra": "Password Cracking",
    "John-The-Ripper": "Password Cracking",
    "Hashcat": "Password Cracking",
    "Wireshark": "Network Analysis",
    "tcpdump": "Network Analysis",
    "Aircrack-ng": "Wireless & Network",
    "Bettercap": "Wireless & Network",
    "Wifite": "Wireless & Network",
    "Network-Pentesting": "Wireless & Network",
    "Google-Dorking": "OSINT & Dorking",
    "GitHub-Dorking": "OSINT & Dorking",
    "Shodan": "OSINT & Dorking",
    "OSINT": "OSINT & Dorking",
    "Social-Engineering": "Social Engineering",
    "GDB": "Reverse Engineering",
    "Ghidra": "Reverse Engineering",
    "x64dbg": "Reverse Engineering",
    "Autopsy": "Digital Forensics",
    "Binwalk": "Digital Forensics",
    "ExifTool": "Digital Forensics",
    "Volatility": "Digital Forensics",
    "Cloud-Security": "Cloud Security",
    "Container-Security": "Container Security",
    "IoT-Hacking": "IoT & Hardware",
    "Mobile-Security": "Mobile Security",
    "Blue-Team": "Blue Team & Defense",
    "CTF": "CTF Cheatsheets",
    "Reporting": "Reporting & Methodologies",
    "Bug-Bounty-Methodology": "Reporting & Methodologies",
    "Wordlists": "Reference Guides",
    "Kali-Linux-Tools": "Reference Guides",
}

def extract_title(content, fallback):
    for line in content.splitlines():
        line = line.strip()
        if line.startswith("# "):
            title = line[2:].strip()
            # Clean emojis or decorative symbols if needed
            title = re.sub(r"^[^\w\s\(\)\-\.]+\s*", "", title)
            return title
    return fallback

def extract_description(content):
    # Search for blockquotes or introductory bold sentences
    match = re.search(r">\s*\*\*?(.*?)\*\*?", content)
    if match:
        desc = match.group(1).strip()
        if len(desc) > 10:
            return desc[:200]
    # Search for <b>...</b>
    match2 = re.search(r"<b>(.*?)</b>", content)
    if match2:
        return match2.group(1).strip()[:200]
    return "Complete penetration testing cheatsheet and command reference."

def extract_quick_commands(content):
    commands = []
    # Find all code blocks
    code_blocks = re.findall(r"```(?:bash|sh|cmd|powershell)?\s*\n(.*?)\n```", content, re.DOTALL)
    for block in code_blocks:
        for line in block.splitlines():
            line = line.strip()
            # Exclude comments, prompts, empty lines, and long scripts
            if not line or line.startswith("#") or line.startswith("::") or line.startswith("//"):
                continue
            if line.startswith("$ ") or line.startswith("# "):
                line = line[2:].strip()
            # Check length and reasonable command appearance
            if 4 <= len(line) <= 180 and not line.startswith("import ") and not line.startswith("<?"):
                if line not in commands:
                    commands.append(line)
            if len(commands) >= 30:
                break
        if len(commands) >= 30:
            break
    return commands

def build_database(repo_root, output_db_path):
    print(f"[*] Starting Hacking-Cheatsheets database compilation...")
    os.makedirs(os.path.dirname(output_db_path), exist_ok=True)
    
    if os.path.exists(output_db_path):
        os.remove(output_db_path)

    conn = sqlite3.connect(output_db_path)
    cur = conn.cursor()

    # Create tables exactly matching Room schema
    cur.execute("""
    CREATE TABLE IF NOT EXISTS cheatsheets (
        id TEXT NOT NULL PRIMARY KEY,
        category TEXT NOT NULL,
        title TEXT NOT NULL,
        title_tr TEXT NOT NULL,
        description TEXT,
        description_tr TEXT,
        icon TEXT,
        content_en TEXT NOT NULL,
        content_tr TEXT NOT NULL,
        content_it TEXT,
        quick_commands_json TEXT,
        tags TEXT
    );
    """)

    cur.execute("CREATE INDEX IF NOT EXISTS index_cheatsheets_category ON cheatsheets (category);")
    cur.execute("CREATE INDEX IF NOT EXISTS index_cheatsheets_title ON cheatsheets (title);")
    cur.execute("CREATE INDEX IF NOT EXISTS index_cheatsheets_title_tr ON cheatsheets (title_tr);")

    cur.execute("""
    CREATE TABLE IF NOT EXISTS favorites (
        cheatsheet_id TEXT NOT NULL PRIMARY KEY,
        bookmarked_at INTEGER NOT NULL
    );
    """)

    cur.execute("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY, identity_hash TEXT);")
    cur.execute("INSERT OR REPLACE INTO room_master_table (id, identity_hash) VALUES(42, '70698166ea2cb273e0787e8f276526c1');")

    # Find and process all cheatsheets
    count = 0
    for root, dirs, files in os.walk(repo_root):
        if any(x in root for x in [".git", "android", "scripts"]):
            continue
        for f in files:
            if f.endswith(".md") and not f.endswith(".tr.md") and not f.endswith(".it.md"):
                en_path = os.path.join(root, f)
                rel_path = os.path.relpath(en_path, repo_root)
                parts = rel_path.split(os.sep)

                # Skip root README/CONTRIBUTING if desired, or keep as General Reference
                if len(parts) == 1:
                    slug = f[:-3].lower()
                    folder = "Reporting & Methodologies"
                else:
                    folder = parts[0]
                    base_name = f[:-3]
                    if base_name == "README":
                        slug = folder.lower()
                    else:
                        slug = f"{folder.lower()}-{base_name.lower()}"

                category = CATEGORY_MAPPING.get(folder, folder.replace("-", " "))

                # Find Turkish file
                base_no_ext = f[:-3]
                tr_cand1 = os.path.join(root, "translations", f"{base_no_ext}.tr.md")
                tr_cand2 = os.path.join(root, f"{base_no_ext}.tr.md")
                tr_path = tr_cand1 if os.path.exists(tr_cand1) else (tr_cand2 if os.path.exists(tr_cand2) else None)

                # Find Italian file
                it_cand1 = os.path.join(root, "translations", f"{base_no_ext}.it.md")
                it_cand2 = os.path.join(root, f"{base_no_ext}.it.md")
                it_path = it_cand1 if os.path.exists(it_cand1) else (it_cand2 if os.path.exists(it_cand2) else None)

                with open(en_path, "r", encoding="utf-8", errors="ignore") as fp:
                    content_en = fp.read()

                content_tr = ""
                if tr_path and os.path.exists(tr_path):
                    with open(tr_path, "r", encoding="utf-8", errors="ignore") as fp:
                        content_tr = fp.read()
                else:
                    content_tr = content_en

                content_it = None
                if it_path and os.path.exists(it_path):
                    with open(it_path, "r", encoding="utf-8", errors="ignore") as fp:
                        content_it = fp.read()

                title_en = extract_title(content_en, slug.title())
                title_tr = extract_title(content_tr, title_en)
                desc_en = extract_description(content_en)
                desc_tr = extract_description(content_tr)
                quick_cmds = extract_quick_commands(content_en)
                quick_cmds_json = json.dumps(quick_cmds)

                # Auto-generate search tags
                tags_list = [slug, folder.lower(), category.lower()]
                if "nmap" in slug: tags_list.extend(["port", "network", "recon", "scan"])
                if "pivoting" in category.lower(): tags_list.extend(["tunnel", "proxy", "socks", "vpn"])
                if "active directory" in category.lower(): tags_list.extend(["kerberos", "domain", "dc", "ldap", "smb"])
                if "privilege" in category.lower(): tags_list.extend(["root", "system", "privesc", "escalation"])
                if "web" in category.lower() or "payload" in category.lower(): tags_list.extend(["owasp", "injection", "http", "api"])
                tags = ",".join(set(tags_list))

                icon = "security"
                if "Web" in category or "Payload" in category: icon = "language"
                elif "Active Directory" in category: icon = "domain"
                elif "Pivoting" in category: icon = "alt_route"
                elif "Privilege" in category: icon = "arrow_upward"
                elif "Recon" in category: icon = "search"
                elif "Forensics" in category: icon = "biotech"
                elif "Reverse" in category: icon = "code"
                elif "Cloud" in category: icon = "cloud"
                elif "Blue" in category: icon = "shield"

                cur.execute("""
                INSERT INTO cheatsheets (
                    id, category, title, title_tr, description, description_tr,
                    icon, content_en, content_tr, content_it, quick_commands_json, tags
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, (
                    slug, category, title_en, title_tr, desc_en, desc_tr,
                    icon, content_en, content_tr, content_it, quick_cmds_json, tags
                ))

                count += 1

    conn.commit()
    conn.close()

    db_size_mb = os.path.getsize(output_db_path) / (1024 * 1024)
    print(f"[+] Successfully compiled {count} cheatsheets into '{output_db_path}' ({db_size_mb:.2f} MB)")
    return count

if __name__ == "__main__":
    repo_root = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
    out_db = os.path.join(repo_root, "android", "app", "src", "main", "assets", "cheatsheets.db")
    build_database(repo_root, out_db)
