#!/usr/bin/env python3
"""
Update all 10 non-English multilingual homepages (README.<lang>.md)
to include the 71 expanded security tools under their proper categories.
"""

import os
import re

TARGET_LANGS = ["de", "es", "fr", "it", "pt", "ru", "ar", "el", "zh-CN", "nb"]

# Mapping of tools to their category anchors
TOOL_CATEGORIES = {
    # Active Directory -> category-26
    "category-26": [
        ("Certipy", "Certipy"),
        ("NetExec", "NetExec"),
        ("Snaffler", "Snaffler"),
        ("Coercer", "Coercer"),
        ("PKINITtools", "PKINITtools"),
        ("adidnsdump", "adidnsdump"),
        ("enum4linux-ng", "enum4linux-ng")
    ],
    # Network Pentesting / Pivoting & Tunneling -> category-09
    "category-09": [
        ("Chisel", "Chisel"),
        ("Ligolo-ng", "Ligolo-ng"),
        ("Proxychains", "Proxychains"),
        ("sshuttle", "sshuttle"),
        ("SSH-Tunneling", "SSH Tunneling")
    ],
    # Privilege Escalation -> category-22
    "category-22": [
        ("PEASS-ng", "PEASS-ng (LinPEAS / WinPEAS)"),
        ("LOLBAS-GTFOBins", "LOLBAS & GTFOBins"),
        ("LaZagne", "LaZagne"),
        ("pypykatz", "pypykatz")
    ],
    # Reconnaissance and Scanning -> category-13
    "category-13": [
        ("RustScan", "RustScan"),
        ("Masscan", "Masscan"),
        ("dnsx", "dnsx"),
        ("dnsrecon", "dnsrecon"),
        ("Feroxbuster", "Feroxbuster"),
        ("Dirb", "Dirb"),
        ("dirsearch", "dirsearch"),
        ("Naabu", "Naabu"),
        ("WhatWeb", "WhatWeb")
    ],
    # Web Application Testing -> category-14
    "category-14": [
        ("WPScan", "WPScan"),
        ("Kiterunner", "Kiterunner"),
        ("Wfuzz", "Wfuzz"),
        ("Commix", "Commix"),
        ("Tplmap", "Tplmap"),
        ("jwt_tool", "jwt_tool"),
        ("smuggler", "smuggler"),
        ("CORSy", "CORSy"),
        ("InQL", "InQL"),
        ("wafw00f", "wafw00f"),
        ("mitmproxy", "mitmproxy")
    ],
    # Web Vulnerabilities -> category-19
    "category-19": [
        ("JWT-Attacks", "JWT Attacks")
    ],
    # Exploitation Frameworks -> category-12
    "category-12": [
        ("Msfvenom", "Msfvenom"),
        ("Sliver", "Sliver C2"),
        ("Searchsploit", "Searchsploit"),
        ("Netcat", "Netcat (nc)"),
        ("Socat", "Socat"),
        ("rlwrap", "rlwrap")
    ],
    # Password Cracking -> category-15
    "category-15": [
        ("CeWL", "CeWL"),
        ("Crunch", "Crunch"),
        ("name-that-hash", "name-that-hash"),
        ("CUPP", "CUPP"),
        ("fcrackzip", "fcrackzip")
    ],
    # Reverse Engineering -> category-24
    "category-24": [
        ("pwntools", "pwntools"),
        ("pwndbg-GEF", "pwndbg & GEF"),
        ("Radare2", "Radare2 (r2)"),
        ("Ropper", "Ropper (ROP Gadgets)"),
        ("checksec", "checksec"),
        ("strace-ltrace", "strace & ltrace")
    ],
    # Network Analysis -> category-16
    "category-16": [
        ("tshark", "tshark"),
        ("Scapy", "Scapy"),
        ("Ettercap", "Ettercap"),
        ("Yersinia", "Yersinia")
    ],
    # Wireless Security -> category-25
    "category-25": [
        ("hcxdumptool", "hcxdumptool"),
        ("Reaver", "Reaver & PixieWPS")
    ],
    # OSINT -> category-08
    "category-08": [
        ("theHarvester", "theHarvester"),
        ("Sherlock", "Sherlock"),
        ("TruffleHog", "TruffleHog"),
        ("Gitleaks", "Gitleaks"),
        ("GitTools", "GitTools")
    ],
    # Cloud Security -> category-03
    "category-03": [
        ("Pacu", "Pacu AWS"),
        ("Prowler", "Prowler")
    ],
    # Container Security -> category-05
    "category-05": [
        ("Trivy", "Trivy"),
        ("kube-bench", "kube-bench")
    ],
    # Mobile Security -> category-04
    "category-04": [
        ("Frida", "Frida"),
        ("Jadx", "Jadx (Jadx-GUI)")
    ]
}

def update_homepage(lang):
    filename = f"README.{lang}.md"
    if not os.path.isfile(filename):
        print(f"[!] {filename} not found!")
        return

    with open(filename, "r", encoding="utf-8") as f:
        content = f.read()

    modified = False

    for cat_id, tools in TOOL_CATEGORIES.items():
        pattern = rf'(<a id="{cat_id}"></a>\s*\n##[^\n]+\n)([\s\S]*?)(?=\n<a id="|\n## [^\n]+|\Z)'
        match = re.search(pattern, content)
        if not match:
            continue

        header = match.group(1)
        body = match.group(2)

        new_items = []
        for tool_folder, display_name in tools:
            # Check if link already exists in body
            if f"{tool_folder}/" not in body:
                link = f"- [🛠️ {display_name}]({tool_folder}/translations/README.{lang}.md)"
                new_items.append(link)

        if new_items:
            updated_body = body.rstrip() + "\n" + "\n".join(new_items) + "\n"
            content = content[:match.start(2)] + updated_body + content[match.end(2):]
            modified = True

    if modified:
        with open(filename, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"[+] Updated {filename} with expanded tools.")
    else:
        print(f"[-] No updates needed for {filename}.")

GREEK_SECTIONS = {
    "section-28": [ # Active Directory
        ("Certipy", "Εργαλείο ελέγχου AD CS", "Certipy"),
        ("NetExec", "Σύγχρονο πλαίσιο αυτοματισμού AD", "NetExec"),
        ("Snaffler", "Εντοπισμός ευαίσθητων αρχείων σε κοινόχρηστα", "Snaffler"),
        ("Coercer", "Αναγκαστικός έλεγχος ταυτότητας SMB/RPC", "Coercer"),
        ("PKINITtools", "Εργαλεία PKINIT και Kerberos", "PKINITtools"),
        ("adidnsdump", "Εξαγωγή εγγραφών DNS Active Directory", "adidnsdump"),
        ("enum4linux-ng", "Καταγραφή πληροφοριών SMB και AD", "enum4linux-ng")
    ],
    "section-24": [ # Privilege Escalation
        ("PEASS-ng", "Αυτοματοποιημένος έλεγχος LinPEAS / WinPEAS", "PEASS-ng"),
        ("LOLBAS-GTFOBins", "Εκμετάλλευση δυαδικών αρχείων συστήματος", "LOLBAS & GTFOBins"),
        ("LaZagne", "Ανάκτηση αποθηκευμένων κωδικών πρόσβασης", "LaZagne"),
        ("pypykatz", "Υλοποίηση Mimikatz σε Python", "pypykatz")
    ],
    "section-26": [ # Reverse Engineering
        ("pwntools", "Πλαίσιο CTF και δυαδικής εκμετάλλευσης", "pwntools"),
        ("pwndbg-GEF", "Επεκτάσεις GDB για δυαδική ανάλυση", "pwndbg & GEF"),
        ("Radare2", "Πλαίσιο ανάλυσης δυαδικών και αποσυναρμολόγησης", "Radare2"),
        ("Ropper", "Εύρεση gadgets ROP και ανάλυση αρχείων", "Ropper"),
        ("checksec", "Έλεγχος μηχανισμών προστασίας δυαδικών αρχείων", "checksec"),
        ("strace-ltrace", "Παρακολούθηση κλήσεων συστήματος και βιβλιοθηκών", "strace & ltrace")
    ],
    "section-15": [ # Recon & Scanning
        ("RustScan", "Εξαιρετικά γρήγορος σαρωτής θυρών", "RustScan"),
        ("Masscan", "Μαζικός σαρωτής δικτύου", "Masscan"),
        ("dnsx", "Γρήγορος σαρωτής DNS πολλαπλών χρήσεων", "dnsx"),
        ("dnsrecon", "Αναγνώριση και απαρίθμηση DNS", "dnsrecon"),
        ("Feroxbuster", "Γρήγορη εύρεση περιεχομένου ιστού σε Rust", "Feroxbuster"),
        ("Dirb", "Σαρωτής καταλόγων ιστού", "Dirb"),
        ("dirsearch", "Προηγμένος σαρωτής διαδρομών ιστού", "dirsearch"),
        ("Naabu", "Ταχύς σαρωτής θυρών TCP/UDP", "Naabu"),
        ("WhatWeb", "Αναγνώριση τεχνολογιών ιστότοπων", "WhatWeb")
    ],
    "section-16": [ # Web Application Testing
        ("WPScan", "Σαρωτής ασφάλειας WordPress", "WPScan"),
        ("Kiterunner", "Σάρωση και ανίχνευση σημείων API", "Kiterunner"),
        ("Wfuzz", "Εργαλείο δοκιμών ασάφειας εφαρμογών ιστού", "Wfuzz"),
        ("Commix", "Αυτοματοποιημένη εκμετάλλευση Command Injection", "Commix"),
        ("Tplmap", "Εκμετάλλευση Server-Side Template Injection (SSTI)", "Tplmap"),
        ("jwt_tool", "Ανάλυση και εκμετάλλευση JSON Web Tokens", "jwt_tool"),
        ("smuggler", "Ανίχνευση HTTP Request Smuggling", "smuggler"),
        ("CORSy", "Έλεγχος εσφαλμένων ρυθμίσεων CORS", "CORSy"),
        ("InQL", "Ανάλυση ασφάλειας GraphQL", "InQL"),
        ("wafw00f", "Αναγνώριση Web Application Firewall (WAF)", "wafw00f"),
        ("mitmproxy", "Διαδραστικός διαμεσολαβητής HTTP/HTTPS", "mitmproxy")
    ]
}

def update_greek_homepage():
    filename = "README.el.md"
    if not os.path.isfile(filename):
        return
    with open(filename, "r", encoding="utf-8") as f:
        content = f.read()

    modified = False
    for sec_id, tools in GREEK_SECTIONS.items():
        pattern = rf'(<a id="{sec_id}"></a>[\s\S]*?\n\|[^\n]+\n\|[-|\s]+\n)([\s\S]*?)(?=\n<a id=|\n##|\Z)'
        match = re.search(pattern, content)
        if not match:
            continue
        header = match.group(1)
        body = match.group(2)
        new_items = []
        for tool_folder, desc, display_name in tools:
            if f"{tool_folder}/" not in body:
                row = f"| **{display_name}** | {desc} | [📄 Προβολή](./{tool_folder}/translations/README.el.md) |"
                new_items.append(row)
        if new_items:
            updated_body = body.rstrip() + "\n" + "\n".join(new_items) + "\n"
            content = content[:match.start(2)] + updated_body + content[match.end(2):]
            modified = True

    if modified:
        with open(filename, "w", encoding="utf-8") as f:
            f.write(content)
        print(f"[+] Updated {filename} with expanded tools.")

def main():
    for lang in TARGET_LANGS:
        update_homepage(lang)
    update_greek_homepage()

if __name__ == "__main__":
    main()
