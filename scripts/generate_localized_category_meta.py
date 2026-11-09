#!/usr/bin/env python3
"""
Generates CategoryMeta.kt with comprehensive 12-language support for all 22 categories.
"""

CATEGORY_DATA = {
    "Active Directory": {
        "icon": "Icons.Default.Lan",
        "color": "AccentPurple",
        "titles": {
            "TR": "Active Directory",
            "EN": "Active Directory",
            "DE": "Active Directory",
            "ES": "Active Directory",
            "FR": "Active Directory",
            "IT": "Active Directory",
            "PT": "Active Directory",
            "RU": "Active Directory",
            "ZH_CN": "Active Directory 域安全",
            "AR": "أمان Active Directory",
            "EL": "Active Directory",
            "NB": "Active Directory"
        },
        "descs": {
            "TR": "Kerberos, BloodHound, NetExec, Impacket ve etki alanı saldırıları",
            "EN": "Kerberos, BloodHound, NetExec, Impacket & domain attacks",
            "DE": "Kerberos, BloodHound, NetExec, Impacket & Domain-Angriffe",
            "ES": "Kerberos, BloodHound, NetExec, Impacket y ataques de dominio",
            "FR": "Kerberos, BloodHound, NetExec, Impacket & attaques de domaine",
            "IT": "Kerberos, BloodHound, NetExec, Impacket e attacchi al dominio",
            "PT": "Kerberos, BloodHound, NetExec, Impacket e ataques de domínio",
            "RU": "Kerberos, BloodHound, NetExec, Impacket и атаки на домен",
            "ZH_CN": "Kerberos、BloodHound、NetExec、Impacket 与域渗透攻击",
            "AR": "هجمات Kerberos و BloodHound و NetExec و Impacket وهجمات النطاق",
            "EL": "Kerberos, BloodHound, NetExec, Impacket και επιθέσεις τομέα",
            "NB": "Kerberos, BloodHound, NetExec, Impacket og domeneangrep"
        }
    },
    "Web Security & API": {
        "icon": "Icons.Default.Public",
        "color": "CyberGreen",
        "titles": {
            "TR": "Web Güvenliği & API",
            "EN": "Web Security & API",
            "DE": "Web-Sicherheit & API",
            "ES": "Seguridad Web y API",
            "FR": "Sécurité Web & API",
            "IT": "Sicurezza Web & API",
            "PT": "Segurança Web e API",
            "RU": "Веб-безопасность и API",
            "ZH_CN": "Web 安全与 API",
            "AR": "أمان الويب وواجهات البرمجة",
            "EL": "Ασφάλεια Web & API",
            "NB": "Nettsikkerhet og API"
        },
        "descs": {
            "TR": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            "EN": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            "DE": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            "ES": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF y OWASP Top 10",
            "FR": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            "IT": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF e OWASP Top 10",
            "PT": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF e OWASP Top 10",
            "RU": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF и OWASP Top 10",
            "ZH_CN": "Burp、SQLMap、SSRF、JWT、IDOR、XSS、WAF 与 OWASP Top 10",
            "AR": "Burp و SQLMap و SSRF و JWT و IDOR و XSS و WAF و OWASP Top 10",
            "EL": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            "NB": "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF og OWASP Top 10"
        }
    },
    "Recon & Scanning": {
        "icon": "Icons.Default.Radar",
        "color": "TerminalCyan",
        "titles": {
            "TR": "Keşif & Tarama",
            "EN": "Recon & Scanning",
            "DE": "Aufklärung & Scanning",
            "ES": "Reconocimiento y Escaneo",
            "FR": "Reconnaissance & Scan",
            "IT": "Ricognizione & Scansione",
            "PT": "Reconhecimento e Escaneamento",
            "RU": "Разведка и сканирование",
            "ZH_CN": "信息收集与端口扫描",
            "AR": "الاستطلاع والمسح",
            "EL": "Αναγνώριση & Σάρωση",
            "NB": "Rekognosering og skanning"
        },
        "descs": {
            "TR": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei ve hedef tespiti",
            "EN": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & asset discovery",
            "DE": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & Asset-Erkennung",
            "ES": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei y descubrimiento de activos",
            "FR": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & découverte d'actifs",
            "IT": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei e asset discovery",
            "PT": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei e descoberta de ativos",
            "RU": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei и обнаружение активов",
            "ZH_CN": "Nmap、ffuf、Feroxbuster、RustScan、Nuclei 与资产探测",
            "AR": "Nmap و ffuf و Feroxbuster و RustScan و Nuclei واكتشاف الأصول",
            "EL": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & ανακάλυψη στόχων",
            "NB": "Nmap, ffuf, Feroxbuster, RustScan, Nuclei og målidentifisering"
        }
    },
    "Privilege Escalation": {
        "icon": "Icons.AutoMirrored.Filled.TrendingUp",
        "color": "WarningOrange",
        "titles": {
            "TR": "Yetki Yükseltme",
            "EN": "Privilege Escalation",
            "DE": "Privilege Escalation",
            "ES": "Escalada de Privilegios",
            "FR": "Élévation de Privilèges",
            "IT": "Escalation dei Privilegi",
            "PT": "Escalação de Privilégios",
            "RU": "Повышение привилегий",
            "ZH_CN": "权限提升 (PrivEsc)",
            "AR": "تصعيد الصلاحيات",
            "EL": "Κλιμάκωση Προνομίων",
            "NB": "Privilegieeskalering"
        },
        "descs": {
            "TR": "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
            "EN": "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
            "DE": "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
            "ES": "PrivEsc en Linux y Windows, PEASS-ng, LOLBAS, GTFOBins",
            "FR": "PrivEsc Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
            "IT": "PrivEsc Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
            "PT": "PrivEsc Linux e Windows, PEASS-ng, LOLBAS, GTFOBins",
            "RU": "Повышение привилегий Linux и Windows, PEASS-ng, LOLBAS, GTFOBins",
            "ZH_CN": "Linux 与 Windows 提权技术、PEASS-ng、LOLBAS、GTFOBins",
            "AR": "تصعيد الصلاحيات في Linux و Windows و PEASS-ng و LOLBAS و GTFOBins",
            "EL": "PrivEsc για Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
            "NB": "Linux og Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins"
        }
    },
    "Exploitation & Shells": {
        "icon": "Icons.Default.Terminal",
        "color": "ExploitRed",
        "titles": {
            "TR": "Sömürü & Kabuklar",
            "EN": "Exploitation & Shells",
            "DE": "Exploitation & Shells",
            "ES": "Explotación y Shells",
            "FR": "Exploitation & Shells",
            "IT": "Exploitation & Shell",
            "PT": "Exploração e Shells",
            "RU": "Эксплуатация и шеллы",
            "ZH_CN": "漏洞利用与反弹 Shell",
            "AR": "الاستغلال وشبكات الاتصال",
            "EL": "Εκμετάλλευση & Shells",
            "NB": "Utnyttelse og shells"
        },
        "descs": {
            "TR": "Metasploit, Searchsploit, Msfvenom, Ters kabuklar & Socat",
            "EN": "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
            "DE": "Metasploit, Searchsploit, Msfvenom, Reverse Shells & Socat",
            "ES": "Metasploit, Searchsploit, Msfvenom, Reverse Shells y Socat",
            "FR": "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
            "IT": "Metasploit, Searchsploit, Msfvenom, Reverse shell e Socat",
            "PT": "Metasploit, Searchsploit, Msfvenom, Reverse shells e Socat",
            "RU": "Metasploit, Searchsploit, Msfvenom, реверс-шеллы и Socat",
            "ZH_CN": "Metasploit、Searchsploit、Msfvenom、反向 Shell 与 Socat",
            "AR": "Metasploit و Searchsploit و Msfvenom وأصداف عكسية و Socat",
            "EL": "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
            "NB": "Metasploit, Searchsploit, Msfvenom, Reverse shells og Socat"
        }
    },
    "Reverse Engineering": {
        "icon": "Icons.Default.BugReport",
        "color": "AccentPink",
        "titles": {
            "TR": "Tersine Mühendislik",
            "EN": "Reverse Engineering",
            "DE": "Reverse Engineering",
            "ES": "Ingeniería Inversa",
            "FR": "Rétro-Ingénierie",
            "IT": "Ingegneria Inversa",
            "PT": "Engenharia Reversa",
            "RU": "Обратная разработка",
            "ZH_CN": "逆向工程与二进制分析",
            "AR": "الهندسة العكسية",
            "EL": "Αντίστροφη Μηχανική",
            "NB": "Omvendt utvikling"
        },
        "descs": {
            "TR": "Ghidra, GDB, Radare2, Pwntools, Ropper & ikili analiz",
            "EN": "Ghidra, GDB, Radare2, Pwntools, Ropper & binary exploitation",
            "DE": "Ghidra, GDB, Radare2, Pwntools, Ropper & Binär-Analyse",
            "ES": "Ghidra, GDB, Radare2, Pwntools, Ropper y análisis binario",
            "FR": "Ghidra, GDB, Radare2, Pwntools, Ropper & exploitation binaire",
            "IT": "Ghidra, GDB, Radare2, Pwntools, Ropper e analisi binaria",
            "PT": "Ghidra, GDB, Radare2, Pwntools, Ropper e exploração binária",
            "RU": "Ghidra, GDB, Radare2, Pwntools, Ropper и анализ бинарников",
            "ZH_CN": "Ghidra、GDB、Radare2、Pwntools、Ropper 与二进制利用",
            "AR": "Ghidra و GDB و Radare2 و Pwntools و Ropper وتحليل البرمجيات",
            "EL": "Ghidra, GDB, Radare2, Pwntools, Ropper & ανάλυση εκτελέσιμων",
            "NB": "Ghidra, GDB, Radare2, Pwntools, Ropper og binær analyse"
        }
    },
    "Password Cracking": {
        "icon": "Icons.Default.Key",
        "color": "AccentYellow",
        "titles": {
            "TR": "Parola Kırma",
            "EN": "Password Cracking",
            "DE": "Passwort-Cracking",
            "ES": "Descifrado de Contraseñas",
            "FR": "Cassage de Mots de Passe",
            "IT": "Cracking Password",
            "PT": "Quebra de Senhas",
            "RU": "Взлом паролей",
            "ZH_CN": "密码破解与哈希恢复",
            "AR": "كسر كلمات المرور",
            "EL": "Σπάσιμο Κωδικών",
            "NB": "Passordknekking"
        },
        "descs": {
            "TR": "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            "EN": "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            "DE": "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            "ES": "Hashcat, John the Ripper, Hydra, CeWL, Crunch y NTH",
            "FR": "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            "IT": "Hashcat, John the Ripper, Hydra, CeWL, Crunch e NTH",
            "PT": "Hashcat, John the Ripper, Hydra, CeWL, Crunch e NTH",
            "RU": "Hashcat, John the Ripper, Hydra, CeWL, Crunch и NTH",
            "ZH_CN": "Hashcat、John the Ripper、Hydra、CeWL、Crunch 与 NTH",
            "AR": "Hashcat و John the Ripper و Hydra و CeWL و Crunch و NTH",
            "EL": "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            "NB": "Hashcat, John the Ripper, Hydra, CeWL, Crunch og NTH"
        }
    },
    "Pivoting & Tunneling": {
        "icon": "Icons.Default.SwapHoriz",
        "color": "AccentBlue",
        "titles": {
            "TR": "Pivoting & Tünelleme",
            "EN": "Pivoting & Tunneling",
            "DE": "Pivoting & Tunneling",
            "ES": "Pivoting y Túneles",
            "FR": "Pivoting & Tunnels",
            "IT": "Pivoting & Tunneling",
            "PT": "Pivoting e Tunelamento",
            "RU": "Пивотинг и туннелирование",
            "ZH_CN": "网络穿透与流量代理 (Pivoting)",
            "AR": "التنقل التكتيكي والأنفاق",
            "EL": "Pivoting & Διοχέτευση",
            "NB": "Pivoting og tunnelering"
        },
        "descs": {
            "TR": "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
            "EN": "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
            "DE": "Ligolo-ng, Chisel, Proxychains, SSH-Tunnel & SOCKS5",
            "ES": "Ligolo-ng, Chisel, Proxychains, Túneles SSH y SOCKS5",
            "FR": "Ligolo-ng, Chisel, Proxychains, Tunnels SSH & SOCKS5",
            "IT": "Ligolo-ng, Chisel, Proxychains, Tunnel SSH e SOCKS5",
            "PT": "Ligolo-ng, Chisel, Proxychains, Túneis SSH e SOCKS5",
            "RU": "Ligolo-ng, Chisel, Proxychains, SSH-туннели и SOCKS5",
            "ZH_CN": "Ligolo-ng、Chisel、Proxychains、SSH 隧道与 SOCKS5",
            "AR": "Ligolo-ng و Chisel و Proxychains وأنفاق SSH و SOCKS5",
            "EL": "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
            "NB": "Ligolo-ng, Chisel, Proxychains, SSH-tunneler og SOCKS5"
        }
    },
    "OSINT & Dorking": {
        "icon": "Icons.Default.Visibility",
        "color": "AccentIndigo",
        "titles": {
            "TR": "Açık Kaynak İstihbaratı",
            "EN": "OSINT & Dorking",
            "DE": "OSINT & Dorking",
            "ES": "OSINT y Dorking",
            "FR": "OSINT & Dorking",
            "IT": "OSINT & Dorking",
            "PT": "OSINT e Dorking",
            "RU": "OSINT и доркинг",
            "ZH_CN": "开源情报 (OSINT) 与高级搜索",
            "AR": "استخبارات المصادر المفتوحة",
            "EL": "OSINT & Dorking",
            "NB": "OSINT og dorking"
        },
        "descs": {
            "TR": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "EN": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "DE": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "ES": "Shodan, Google Dorking, Sherlock, TruffleHog, theHarvester",
            "FR": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "IT": "Shodan, Google Dorking, Sherlock, TruffleHog, theHarvester",
            "PT": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "RU": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "ZH_CN": "Shodan、Google Dorking、Sherlock、TruffleHog 与 theHarvester",
            "AR": "Shodan و Google Dorking و Sherlock و TruffleHog و Harvester",
            "EL": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            "NB": "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester"
        }
    },
    "Payloads & Wordlists": {
        "icon": "Icons.Default.DataObject",
        "color": "AccentYellow",
        "titles": {
            "TR": "Payloadlar & Kelime Listeleri",
            "EN": "Payloads & Wordlists",
            "DE": "Payloads & Wordlists",
            "ES": "Payloads y Diccionarios",
            "FR": "Payloads & Listes de Mots",
            "IT": "Payload & Wordlist",
            "PT": "Payloads e Wordlists",
            "RU": "Полезные нагрузки и словари",
            "ZH_CN": "有效载荷 (Payloads) 与字典",
            "AR": "الحمولات وقوائم الكلمات",
            "EL": "Payloads & Λίστες Λέξεων",
            "NB": "Nyttelaster og ordlister"
        },
        "descs": {
            "TR": "Enjeksiyon yükleri, Seclists referansları & Kali araçları",
            "EN": "Injection payloads, Seclists references & Kali tools",
            "DE": "Injection-Payloads, SecLists-Referenzen & Kali-Tools",
            "ES": "Payloads de inyección, referencias de SecLists y herramientas Kali",
            "FR": "Payloads d'injection, références SecLists & outils Kali",
            "IT": "Payload di injection, riferimenti SecLists e strumenti Kali",
            "PT": "Payloads de injeção, referências do SecLists e ferramentas Kali",
            "RU": "Инъекционные нагрузки, ссылки SecLists и инструменты Kali",
            "ZH_CN": "注入载荷、SecLists 字典集与 Kali 工具指南",
            "AR": "حمولات الحقن، ومراجع SecLists وأدوات Kali",
            "EL": "Φορτία έγχυσης, αναφορές SecLists & εργαλεία Kali",
            "NB": "Injeksjonslaster, SecLists-referanser og Kali-verktøy"
        }
    },
    "Wireless & Network": {
        "icon": "Icons.Default.Wifi",
        "color": "AccentEmerald",
        "titles": {
            "TR": "Kablosuz Ağ Güvenliği",
            "EN": "Wireless & Network",
            "DE": "WLAN- & Netzwerksicherheit",
            "ES": "Seguridad Inalámbrica y Redes",
            "FR": "Sans-Fil & Réseau",
            "IT": "Sicurezza Wireless & Reti",
            "PT": "Segurança Sem Fio e Redes",
            "RU": "Беспроводные сети и Wi-Fi",
            "ZH_CN": "无线网络与 Wi-Fi 安全",
            "AR": "الشبكات اللاسلكية والواي فاي",
            "EL": "Ασύρματα Δίκτυα & Wi-Fi",
            "NB": "Trådløst og nettverk"
        },
        "descs": {
            "TR": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "EN": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "DE": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "ES": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "FR": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "IT": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "PT": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "RU": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "ZH_CN": "Aircrack-ng、Bettercap、Wifite、hcxdumptool、Reaver",
            "AR": "Aircrack-ng و Bettercap و Wifite و hcxdumptool و Reaver",
            "EL": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            "NB": "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver"
        }
    },
    "Network Analysis": {
        "icon": "Icons.Default.Hub",
        "color": "AccentBlue",
        "titles": {
            "TR": "Ağ Analizi & Sniffing",
            "EN": "Network Analysis",
            "DE": "Netzwerkanalyse & Sniffing",
            "ES": "Análisis de Red y Sniffing",
            "FR": "Analyse Réseau & Sniffing",
            "IT": "Analisi di Rete & Sniffing",
            "PT": "Análise de Rede e Sniffing",
            "RU": "Анализ сети и сниффинг",
            "ZH_CN": "网络分析与流量嗅探",
            "AR": "تحليل الشبكات واعتراض الحزم",
            "EL": "Ανάλυση Δικτύου & Υποκλοπή",
            "NB": "Nettverksanalyse og sniffing"
        },
        "descs": {
            "TR": "Wireshark, tshark, tcpdump, Scapy, Ettercap paket analizi",
            "EN": "Wireshark, tshark, tcpdump, Scapy, Ettercap packet capture",
            "DE": "Wireshark, tshark, tcpdump, Scapy, Ettercap Paketanalyse",
            "ES": "Wireshark, tshark, tcpdump, Scapy, Ettercap captura de paquetes",
            "FR": "Wireshark, tshark, tcpdump, Scapy, Ettercap capture de paquets",
            "IT": "Wireshark, tshark, tcpdump, Scapy, Ettercap analisi pacchetti",
            "PT": "Wireshark, tshark, tcpdump, Scapy, Ettercap captura de pacotes",
            "RU": "Wireshark, tshark, tcpdump, Scapy, Ettercap захват пакетов",
            "ZH_CN": "Wireshark、tshark、tcpdump、Scapy、Ettercap 数据包分析",
            "AR": "تحليل الحزم عبر Wireshark و tshark و tcpdump و Scapy و Ettercap",
            "EL": "Wireshark, tshark, tcpdump, Scapy, Ettercap σύλληψη πακέτων",
            "NB": "Wireshark, tshark, tcpdump, Scapy, Ettercap pakkeanalyse"
        }
    },
    "Cloud Security": {
        "icon": "Icons.Default.Cloud",
        "color": "AccentTeal",
        "titles": {
            "TR": "Bulut Güvenliği",
            "EN": "Cloud Security",
            "DE": "Cloud-Sicherheit",
            "ES": "Seguridad en la Nube",
            "FR": "Sécurité Cloud",
            "IT": "Sicurezza Cloud",
            "PT": "Segurança na Nuvem",
            "RU": "Облачная безопасность",
            "ZH_CN": "云安全 (Cloud Security)",
            "AR": "أمان السحابة",
            "EL": "Ασφάλεια Cloud",
            "NB": "Skysikkerhet"
        },
        "descs": {
            "TR": "AWS, Azure, GCP denetimi, Pacu ve Prowler araçları",
            "EN": "AWS, Azure, GCP audit, Pacu and Prowler tools",
            "DE": "AWS, Azure, GCP-Audits, Pacu & Prowler-Tools",
            "ES": "Auditorías de AWS, Azure, GCP, herramientas Pacu y Prowler",
            "FR": "Audit AWS, Azure, GCP, outils Pacu & Prowler",
            "IT": "Audit AWS, Azure, GCP, strumenti Pacu e Prowler",
            "PT": "Auditoria AWS, Azure, GCP, ferramentas Pacu e Prowler",
            "RU": "Аудит AWS, Azure, GCP, инструменты Pacu и Prowler",
            "ZH_CN": "AWS、Azure、GCP 安全审计、Pacu 与 Prowler 工具",
            "AR": "تدقيق AWS و Azure و GCP وأدوات Pacu و Prowler",
            "EL": "Έλεγχος AWS, Azure, GCP, εργαλεία Pacu & Prowler",
            "NB": "AWS-, Azure-, GCP-revisjon, Pacu- og Prowler-verktøy"
        }
    },
    "Container Security": {
        "icon": "Icons.Default.Inventory2",
        "color": "AccentTeal",
        "titles": {
            "TR": "Konteyner Güvenliği",
            "EN": "Container Security",
            "DE": "Container-Sicherheit",
            "ES": "Seguridad de Contenedores",
            "FR": "Sécurité des Conteneurs",
            "IT": "Sicurezza dei Container",
            "PT": "Segurança de Contêineres",
            "RU": "Безопасность контейнеров",
            "ZH_CN": "容器与云原生安全",
            "AR": "أمان الحاويات والـ Docker",
            "EL": "Ασφάλεια Containers",
            "NB": "Containersikkerhet"
        },
        "descs": {
            "TR": "Docker kaçışları, Kubernetes sızma testleri, Trivy",
            "EN": "Docker breakouts, Kubernetes pentesting, Trivy",
            "DE": "Docker-Ausbrüche, Kubernetes-Pentests, Trivy",
            "ES": "Escapes de Docker, pentesting en Kubernetes, Trivy",
            "FR": "Évasions Docker, pentest Kubernetes, Trivy",
            "IT": "Evasioni Docker, pentesting Kubernetes, Trivy",
            "PT": "Escapes de Docker, pentest em Kubernetes, Trivy",
            "RU": "Побеги из Docker, пентестинг Kubernetes, Trivy",
            "ZH_CN": "Docker 逃逸、Kubernetes 渗透测试与 Trivy 镜像漏洞扫描",
            "AR": "الهروب من Docker واختبار اختراق Kubernetes و Trivy",
            "EL": "Διαφυγές Docker, pentesting Kubernetes, Trivy",
            "NB": "Docker-unnslippelse, Kubernetes-pentesting, Trivy"
        }
    },
    "Mobile Security": {
        "icon": "Icons.Default.PhoneAndroid",
        "color": "AccentLime",
        "titles": {
            "TR": "Mobil Güvenlik",
            "EN": "Mobile Security",
            "DE": "Mobile Sicherheit",
            "ES": "Seguridad Móvil",
            "FR": "Sécurité Mobile",
            "IT": "Sicurezza Mobile",
            "PT": "Segurança Mobile",
            "RU": "Мобильная безопасность",
            "ZH_CN": "移动安全 (Android & iOS)",
            "AR": "أمان الأجهزة المحمولة",
            "EL": "Ασφάλεια Κινητών Συσκευών",
            "NB": "Mobilsikkerhet"
        },
        "descs": {
            "TR": "Android & iOS pentest, Frida, Jadx, APKTool enjeksiyonu",
            "EN": "Android & iOS pentesting, Frida, Jadx, APKTool hooks",
            "DE": "Android- & iOS-Pentesting, Frida, Jadx, APKTool-Hooks",
            "ES": "Pentesting en Android e iOS, Frida, Jadx, inyecciones APKTool",
            "FR": "Pentest Android & iOS, Frida, Jadx, injection APKTool",
            "IT": "Pentest Android & iOS, Frida, Jadx, iniezioni APKTool",
            "PT": "Pentesting em Android e iOS, Frida, Jadx, injeções APKTool",
            "RU": "Пентестинг Android и iOS, Frida, Jadx, хуки APKTool",
            "ZH_CN": "Android 与 iOS 渗透测试、Frida Hook、Jadx 反编译与 APKTool",
            "AR": "اختبار اختراق Android و iOS و Frida و Jadx و APKTool",
            "EL": "Pentesting Android & iOS, Frida, Jadx, hooks APKTool",
            "NB": "Android- og iOS-pentesting, Frida, Jadx, APKTool-hooks"
        }
    },
    "Digital Forensics": {
        "icon": "Icons.Default.Fingerprint",
        "color": "AccentRose",
        "titles": {
            "TR": "Adli Bilişim (Forensics)",
            "EN": "Digital Forensics",
            "DE": "Digitale Forensik",
            "ES": "Informática Forense",
            "FR": "Investigation Numérique",
            "IT": "Informatica Forense",
            "PT": "Computação Forense",
            "RU": "Цифровая криминалистика",
            "ZH_CN": "电子数据取证 (Forensics)",
            "AR": "الأدلة الجنائية الرقمية",
            "EL": "Ψηφιακή Εγκληματολογία",
            "NB": "Digital etterforskning"
        },
        "descs": {
            "TR": "Autopsy, Volatility RAM analizi, Binwalk, ExifTool",
            "EN": "Autopsy, Volatility RAM analysis, Binwalk, ExifTool",
            "DE": "Autopsy, Volatility RAM-Analyse, Binwalk, ExifTool",
            "ES": "Autopsy, análisis de RAM con Volatility, Binwalk, ExifTool",
            "FR": "Autopsy, analyse de RAM Volatility, Binwalk, ExifTool",
            "IT": "Autopsy, analisi RAM Volatility, Binwalk, ExifTool",
            "PT": "Autopsy, análise de RAM com Volatility, Binwalk, ExifTool",
            "RU": "Autopsy, анализ RAM с Volatility, Binwalk, ExifTool",
            "ZH_CN": "Autopsy、Volatility 内存取证、Binwalk 固件分析、ExifTool",
            "AR": "Autopsy وتحليل الذاكرة عبر Volatility و Binwalk و ExifTool",
            "EL": "Autopsy, ανάλυση RAM με Volatility, Binwalk, ExifTool",
            "NB": "Autopsy, Volatility RAM-analyse, Binwalk, ExifTool"
        }
    },
    "Blue Team & Defense": {
        "icon": "Icons.Default.Security",
        "color": "TerminalCyan",
        "titles": {
            "TR": "Mavi Takım & Savunma",
            "EN": "Blue Team & Defense",
            "DE": "Blue Team & Verteidigung",
            "ES": "Blue Team y Defensa",
            "FR": "Blue Team & Défense",
            "IT": "Blue Team & Difesa",
            "PT": "Blue Team e Defesa",
            "RU": "Blue Team и защита",
            "ZH_CN": "蓝队防守与威胁检测",
            "AR": "الفريق الأزرق والدفاع",
            "EL": "Blue Team & Άμυνα",
            "NB": "Blue Team og forsvar"
        },
        "descs": {
            "TR": "Sigma kuralları, YARA, Sysinternals, log ve tehdit avı",
            "EN": "Sigma rules, YARA, Sysinternals, logs & threat hunting",
            "DE": "Sigma-Regeln, YARA, Sysinternals, Logs & Threat Hunting",
            "ES": "Reglas Sigma, YARA, Sysinternals, logs y caza de amenazas",
            "FR": "Règles Sigma, YARA, Sysinternals, journaux & traque des menaces",
            "IT": "Regole Sigma, YARA, Sysinternals, log e threat hunting",
            "PT": "Regras Sigma, YARA, Sysinternals, logs e caça a ameaças",
            "RU": "Правила Sigma, YARA, Sysinternals, журналы и поиск угроз",
            "ZH_CN": "Sigma 规则、YARA、Sysinternals、日志分析与威胁狩猎",
            "AR": "قواعد Sigma و YARA و Sysinternals والسجلات وصيد التهديدات",
            "EL": "Κανόνες Sigma, YARA, Sysinternals, αρχεία καταγραφής & κυνήγι απειλών",
            "NB": "Sigma-regler, YARA, Sysinternals, logger og trusselsjakt"
        }
    },
    "Attack Methodology": {
        "icon": "Icons.Default.TrackChanges",
        "color": "ExploitRed",
        "titles": {
            "TR": "Saldırı Metodolojisi",
            "EN": "Attack Methodology",
            "DE": "Angriffsmethodik",
            "ES": "Metodología de Ataque",
            "FR": "Méthodologie d'Attaque",
            "IT": "Metodologia di Attacco",
            "PT": "Metodologia de Ataque",
            "RU": "Методология атак",
            "ZH_CN": "渗透测试方法论 (MITRE ATT&CK)",
            "AR": "منهجية الهجوم والتسلل",
            "EL": "Μεθοδολογία Επιθέσεων",
            "NB": "Angrepsmetodikk"
        },
        "descs": {
            "TR": "MITRE ATT&CK Kill Chain: 01 Erişimden 07 Hedefe kadar",
            "EN": "MITRE ATT&CK Kill Chain: 01 Initial Access to 07 Objectives",
            "DE": "MITRE ATT&CK Kill Chain: 01 Initial Access bis 07 Objectives",
            "ES": "MITRE ATT&CK Kill Chain: 01 Acceso Inicial a 07 Objetivos",
            "FR": "MITRE ATT&CK Kill Chain : 01 Accès Initial à 07 Objectifs",
            "IT": "MITRE ATT&CK Kill Chain: Da 01 Accesso Iniziale a 07 Obiettivi",
            "PT": "MITRE ATT&CK Kill Chain: 01 Acesso Inicial até 07 Objetivos",
            "RU": "MITRE ATT&CK Kill Chain: от 01 Начального доступа до 07 Целей",
            "ZH_CN": "MITRE ATT&CK 攻击链：从 01 初始访问至 07 行动目标",
            "AR": "سلسلة القتل MITRE ATT&CK: من 01 الوصول الأولي إلى 07 الأهداف",
            "EL": "MITRE ATT&CK Kill Chain: 01 Αρχική Πρόσβαση έως 07 Στόχοι",
            "NB": "MITRE ATT&CK Kill Chain: 01 Førstegangstilgang til 07 Mål"
        }
    },
    "CTF Cheatsheets": {
        "icon": "Icons.Default.EmojiEvents",
        "color": "AccentYellow",
        "titles": {
            "TR": "CTF İpuçları & Rehberler",
            "EN": "CTF Cheatsheets",
            "DE": "CTF Cheatsheets & Guides",
            "ES": "Guías y Cheatsheets de CTF",
            "FR": "Aide-mémoire & Guides CTF",
            "IT": "Guide e Cheatsheet CTF",
            "PT": "Guias e Cheatsheets CTF",
            "RU": "Шпаргалки и гайды CTF",
            "ZH_CN": "CTF 比赛速查与攻略",
            "AR": "أدلة وتلميحات مسابقات CTF",
            "EL": "Οδηγοί & Cheatsheets CTF",
            "NB": "CTF Cheatsheets og veiledninger"
        },
        "descs": {
            "TR": "Kriptografi, steganografi, web ve binary pwn ipuçları",
            "EN": "Cryptography, steganography, web & binary pwn tricks",
            "DE": "Kryptographie, Steganographie, Web & Binary Pwn Tricks",
            "ES": "Criptografía, esteganografía, trucos web y binary pwn",
            "FR": "Cryptographie, stéganographie, astuces web & binary pwn",
            "IT": "Crittografia, steganografia, trucchi web e binary pwn",
            "PT": "Criptografia, esteganografia, truques web e binary pwn",
            "RU": "Криптография, стеганография, трюки для веб и бинарного pwn",
            "ZH_CN": "密码学、隐写术、Web 渗透与二进制 Pwn 实战技巧",
            "AR": "التشفير وإخفاء البيانات وحيل الويب والـ pwn الثنائي",
            "EL": "Κρυπτογραφία, στεγανογραφία, κόλπα web & binary pwn",
            "NB": "Kryptografi, steganografi, web og binære pwn-triks"
        }
    },
    "IoT & Hardware": {
        "icon": "Icons.Default.Memory",
        "color": "AccentSlate",
        "titles": {
            "TR": "IoT & Donanım Güvenliği",
            "EN": "IoT & Hardware",
            "DE": "IoT & Hardware-Sicherheit",
            "ES": "IoT y Seguridad de Hardware",
            "FR": "Sécurité IoT & Matérielle",
            "IT": "Sicurezza IoT & Hardware",
            "PT": "Segurança de IoT e Hardware",
            "RU": "IoT и аппаратная безопасность",
            "ZH_CN": "物联网与硬件安全",
            "AR": "أمان إنترنت الأشياء والعتاد",
            "EL": "Ασφάλεια IoT & Υλικού",
            "NB": "IoT og maskinvaresikkerhet"
        },
        "descs": {
            "TR": "Gömülü sistemler, UART, JTAG, firmware analizi",
            "EN": "Embedded systems, UART, JTAG, firmware extraction",
            "DE": "Eingebettete Systeme, UART, JTAG, Firmware-Extraktion",
            "ES": "Sistemas embebidos, UART, JTAG, extracción de firmware",
            "FR": "Systèmes embarqués, UART, JTAG, extraction de firmware",
            "IT": "Sistemi embedded, UART, JTAG, estrazione di firmware",
            "PT": "Sistemas embarcados, UART, JTAG, extração de firmware",
            "RU": "Встраиваемые системы, UART, JTAG, извлечение прошивок",
            "ZH_CN": "嵌入式系统、UART、JTAG 硬件接口与固件提取分析",
            "AR": "الأنظمة المدمجة و UART و JTAG واستخراج البرامج الثابتة",
            "EL": "Ενσωματωμένα συστήματα, UART, JTAG, εξαγωγή firmware",
            "NB": "Innebygde systemer, UART, JTAG, firmware-utvinning"
        }
    },
    "Social Engineering": {
        "icon": "Icons.Default.Psychology",
        "color": "AccentPink",
        "titles": {
            "TR": "Sosyal Mühendislik",
            "EN": "Social Engineering",
            "DE": "Social Engineering",
            "ES": "Ingeniería Social",
            "FR": "Ingénierie Sociale",
            "IT": "Ingegneria Sociale",
            "PT": "Engenharia Social",
            "RU": "Социальная инженерия",
            "ZH_CN": "社会工程学 (Social Engineering)",
            "AR": "الهندسة الاجتماعية",
            "EL": "Κοινωνική Μηχανική",
            "NB": "Sosial manipulering"
        },
        "descs": {
            "TR": "Oltalama (Phishing), OSINT insan faktörü ve senaryolar",
            "EN": "Phishing, pretexting, human OSINT & assessments",
            "DE": "Phishing, Pretexting, Human-OSINT & Szenarien",
            "ES": "Phishing, pretexting, OSINT del factor humano y escenarios",
            "FR": "Phishing, prétextage, OSINT humain & évaluations",
            "IT": "Phishing, pretexting, OSINT umano e valutazioni",
            "PT": "Phishing, pretexting, OSINT humano e avaliações",
            "RU": "Фишинг, претекстинг, человеческий фактор OSINT и оценки",
            "ZH_CN": "网络钓鱼 (Phishing)、话术设套、人肉情报与对抗评估",
            "AR": "التصيد الاحتيالي والانتحال و OSINT البشري والتقييمات",
            "EL": "Phishing, pretexting, ανθρώπινο OSINT & αξιολογήσεις",
            "NB": "Phishing, pretexting, menneskelig OSINT og vurderinger"
        }
    },
    "Reporting & Guides": {
        "icon": "Icons.Default.Description",
        "color": "AccentSlate",
        "titles": {
            "TR": "Raporlama & Şablonlar",
            "EN": "Reporting & Guides",
            "DE": "Berichte & Vorlagen",
            "ES": "Informes y Guías",
            "FR": "Rapports & Guides",
            "IT": "Reportistica & Guide",
            "PT": "Relatórios e Guias",
            "RU": "Отчеты и руководства",
            "ZH_CN": "渗透测试报告与评估指南",
            "AR": "التقارير والقوالب الإرشادية",
            "EL": "Αναφορές & Οδηγοί",
            "NB": "Rapportering og veiledninger"
        },
        "descs": {
            "TR": "Pentest rapor taslakları, yönetici özeti, Bug Bounty",
            "EN": "Pentest report templates, executive summary, Bug Bounty",
            "DE": "Pentest-Berichtsvorlagen, Management Summary, Bug Bounty",
            "ES": "Plantillas de informes de pentest, resumen ejecutivo, Bug Bounty",
            "FR": "Modèles de rapports de pentest, résumé exécutif, Bug Bounty",
            "IT": "Modelli di report pentest, executive summary, Bug Bounty",
            "PT": "Modelos de relatórios de pentest, resumo executivo, Bug Bounty",
            "RU": "Шаблоны отчетов пентеста, резюме для руководства, Bug Bounty",
            "ZH_CN": "渗透测试报告模板、管理层执行摘要与漏洞赏金指南",
            "AR": "نماذج تقارير اختبار الاختراق، الملخص التنفيذي ومكافآت الثغرات",
            "EL": "Πρότυπα αναφορών pentest, περίληψη στελεχών, Bug Bounty",
            "NB": "Pentest-rapportmaler, ledelsessammendrag, Bug Bounty"
        }
    }
}

def generate_kotlin():
    out = []
    out.append("""package com.hackingcheatsheets.app.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lan
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.hackingcheatsheets.app.data.AppLanguage

data class CategoryMeta(
    val categoryName: String,
    val icon: ImageVector,
    val color: Color,
    val titleTr: String,
    val descTr: String,
    val descEn: String,
    val titlesByLang: Map<AppLanguage, String> = emptyMap(),
    val descsByLang: Map<AppLanguage, String> = emptyMap()
) {
    fun getTitle(lang: AppLanguage): String {
        return titlesByLang[lang] ?: when (lang) {
            AppLanguage.TR -> titleTr
            else -> categoryName
        }
    }

    fun getDesc(lang: AppLanguage): String {
        return descsByLang[lang] ?: when (lang) {
            AppLanguage.TR -> descTr
            else -> descEn
        }
    }
}

fun getCategoryMeta(categoryName: String): CategoryMeta {
    return when (categoryName.trim()) {""")

    for cat_name, data in CATEGORY_DATA.items():
        icon = data["icon"]
        color = data["color"]
        title_tr = data["titles"]["TR"].replace('"', '\\"')
        desc_tr = data["descs"]["TR"].replace('"', '\\"')
        desc_en = data["descs"]["EN"].replace('"', '\\"')

        titles_map = ",\n                ".join(
            f'AppLanguage.{lang} to "{text.replace(chr(34), chr(92) + chr(34))}"'
            for lang, text in data["titles"].items()
        )
        descs_map = ",\n                ".join(
            f'AppLanguage.{lang} to "{text.replace(chr(34), chr(92) + chr(34))}"'
            for lang, text in data["descs"].items()
        )

        out.append(f"""        "{cat_name}" -> CategoryMeta(
            categoryName = "{cat_name}",
            icon = {icon},
            color = {color},
            titleTr = "{title_tr}",
            descTr = "{desc_tr}",
            descEn = "{desc_en}",
            titlesByLang = mapOf(
                {titles_map}
            ),
            descsByLang = mapOf(
                {descs_map}
            )
        )""")

    out.append("""        else -> CategoryMeta(
            categoryName = categoryName,
            icon = Icons.Default.Folder,
            color = TerminalCyan,
            titleTr = categoryName,
            descTr = "Güvenlik araçları ve kılavuzlar",
            descEn = "Security tools and cheatsheets",
            titlesByLang = mapOf(
                AppLanguage.TR to categoryName,
                AppLanguage.EN to categoryName,
                AppLanguage.DE to categoryName,
                AppLanguage.ES to categoryName,
                AppLanguage.FR to categoryName,
                AppLanguage.IT to categoryName,
                AppLanguage.PT to categoryName,
                AppLanguage.RU to categoryName,
                AppLanguage.ZH_CN to categoryName,
                AppLanguage.AR to categoryName,
                AppLanguage.EL to categoryName,
                AppLanguage.NB to categoryName
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Güvenlik araçları ve kılavuzlar",
                AppLanguage.EN to "Security tools and cheatsheets",
                AppLanguage.DE to "Sicherheitswerkzeuge und Cheatsheets",
                AppLanguage.ES to "Herramientas de seguridad y guías",
                AppLanguage.FR to "Outils de sécurité et aide-mémoire",
                AppLanguage.IT to "Strumenti di sicurezza e cheatsheet",
                AppLanguage.PT to "Ferramentas de segurança e cheatsheets",
                AppLanguage.RU to "Инструменты безопасности и руководства",
                AppLanguage.ZH_CN to "安全工具与命令速查指南",
                AppLanguage.AR to "أدوات الأمان وأوراق الغش",
                AppLanguage.EL to "Εργαλεία ασφαλείας και οδηγοί",
                AppLanguage.NB to "Sikkerhetsverktøy og cheatsheets"
            )
        )
    }
}
""")
    return "\n".join(out)

if __name__ == "__main__":
    content = generate_kotlin()
    path = "android/app/src/main/java/com/hackingcheatsheets/app/ui/theme/CategoryMeta.kt"
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print("Successfully wrote localized CategoryMeta.kt")
