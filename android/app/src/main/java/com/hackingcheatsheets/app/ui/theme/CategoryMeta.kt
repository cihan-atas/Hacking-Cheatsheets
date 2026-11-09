package com.hackingcheatsheets.app.ui.theme

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
    return when (categoryName.trim()) {
        "Active Directory" -> CategoryMeta(
            categoryName = "Active Directory",
            icon = Icons.Default.Lan,
            color = AccentPurple,
            titleTr = "Active Directory",
            descTr = "Kerberos, BloodHound, NetExec, Impacket ve etki alanı saldırıları",
            descEn = "Kerberos, BloodHound, NetExec, Impacket & domain attacks",
            titlesByLang = mapOf(
                AppLanguage.TR to "Active Directory",
                AppLanguage.EN to "Active Directory",
                AppLanguage.DE to "Active Directory",
                AppLanguage.ES to "Active Directory",
                AppLanguage.FR to "Active Directory",
                AppLanguage.IT to "Active Directory",
                AppLanguage.PT to "Active Directory",
                AppLanguage.RU to "Active Directory",
                AppLanguage.ZH_CN to "Active Directory 域安全",
                AppLanguage.AR to "أمان Active Directory",
                AppLanguage.EL to "Active Directory",
                AppLanguage.NB to "Active Directory"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Kerberos, BloodHound, NetExec, Impacket ve etki alanı saldırıları",
                AppLanguage.EN to "Kerberos, BloodHound, NetExec, Impacket & domain attacks",
                AppLanguage.DE to "Kerberos, BloodHound, NetExec, Impacket & Domain-Angriffe",
                AppLanguage.ES to "Kerberos, BloodHound, NetExec, Impacket y ataques de dominio",
                AppLanguage.FR to "Kerberos, BloodHound, NetExec, Impacket & attaques de domaine",
                AppLanguage.IT to "Kerberos, BloodHound, NetExec, Impacket e attacchi al dominio",
                AppLanguage.PT to "Kerberos, BloodHound, NetExec, Impacket e ataques de domínio",
                AppLanguage.RU to "Kerberos, BloodHound, NetExec, Impacket и атаки на домен",
                AppLanguage.ZH_CN to "Kerberos、BloodHound、NetExec、Impacket 与域渗透攻击",
                AppLanguage.AR to "هجمات Kerberos و BloodHound و NetExec و Impacket وهجمات النطاق",
                AppLanguage.EL to "Kerberos, BloodHound, NetExec, Impacket και επιθέσεις τομέα",
                AppLanguage.NB to "Kerberos, BloodHound, NetExec, Impacket og domeneangrep"
            )
        )
        "Web Security & API" -> CategoryMeta(
            categoryName = "Web Security & API",
            icon = Icons.Default.Public,
            color = CyberGreen,
            titleTr = "Web Güvenliği & API",
            descTr = "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            descEn = "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
            titlesByLang = mapOf(
                AppLanguage.TR to "Web Güvenliği & API",
                AppLanguage.EN to "Web Security & API",
                AppLanguage.DE to "Web-Sicherheit & API",
                AppLanguage.ES to "Seguridad Web y API",
                AppLanguage.FR to "Sécurité Web & API",
                AppLanguage.IT to "Sicurezza Web & API",
                AppLanguage.PT to "Segurança Web e API",
                AppLanguage.RU to "Веб-безопасность и API",
                AppLanguage.ZH_CN to "Web 安全与 API",
                AppLanguage.AR to "أمان الويب وواجهات البرمجة",
                AppLanguage.EL to "Ασφάλεια Web & API",
                AppLanguage.NB to "Nettsikkerhet og API"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
                AppLanguage.EN to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
                AppLanguage.DE to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
                AppLanguage.ES to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF y OWASP Top 10",
                AppLanguage.FR to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
                AppLanguage.IT to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF e OWASP Top 10",
                AppLanguage.PT to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF e OWASP Top 10",
                AppLanguage.RU to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF и OWASP Top 10",
                AppLanguage.ZH_CN to "Burp、SQLMap、SSRF、JWT、IDOR、XSS、WAF 与 OWASP Top 10",
                AppLanguage.AR to "Burp و SQLMap و SSRF و JWT و IDOR و XSS و WAF و OWASP Top 10",
                AppLanguage.EL to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF & OWASP Top 10",
                AppLanguage.NB to "Burp, SQLMap, SSRF, JWT, IDOR, XSS, WAF og OWASP Top 10"
            )
        )
        "Recon & Scanning" -> CategoryMeta(
            categoryName = "Recon & Scanning",
            icon = Icons.Default.Radar,
            color = TerminalCyan,
            titleTr = "Keşif & Tarama",
            descTr = "Nmap, ffuf, Feroxbuster, RustScan, Nuclei ve hedef tespiti",
            descEn = "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & asset discovery",
            titlesByLang = mapOf(
                AppLanguage.TR to "Keşif & Tarama",
                AppLanguage.EN to "Recon & Scanning",
                AppLanguage.DE to "Aufklärung & Scanning",
                AppLanguage.ES to "Reconocimiento y Escaneo",
                AppLanguage.FR to "Reconnaissance & Scan",
                AppLanguage.IT to "Ricognizione & Scansione",
                AppLanguage.PT to "Reconhecimento e Escaneamento",
                AppLanguage.RU to "Разведка и сканирование",
                AppLanguage.ZH_CN to "信息收集与端口扫描",
                AppLanguage.AR to "الاستطلاع والمسح",
                AppLanguage.EL to "Αναγνώριση & Σάρωση",
                AppLanguage.NB to "Rekognosering og skanning"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei ve hedef tespiti",
                AppLanguage.EN to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & asset discovery",
                AppLanguage.DE to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & Asset-Erkennung",
                AppLanguage.ES to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei y descubrimiento de activos",
                AppLanguage.FR to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & découverte d'actifs",
                AppLanguage.IT to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei e asset discovery",
                AppLanguage.PT to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei e descoberta de ativos",
                AppLanguage.RU to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei и обнаружение активов",
                AppLanguage.ZH_CN to "Nmap、ffuf、Feroxbuster、RustScan、Nuclei 与资产探测",
                AppLanguage.AR to "Nmap و ffuf و Feroxbuster و RustScan و Nuclei واكتشاف الأصول",
                AppLanguage.EL to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei & ανακάλυψη στόχων",
                AppLanguage.NB to "Nmap, ffuf, Feroxbuster, RustScan, Nuclei og målidentifisering"
            )
        )
        "Privilege Escalation" -> CategoryMeta(
            categoryName = "Privilege Escalation",
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            color = WarningOrange,
            titleTr = "Yetki Yükseltme",
            descTr = "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
            descEn = "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
            titlesByLang = mapOf(
                AppLanguage.TR to "Yetki Yükseltme",
                AppLanguage.EN to "Privilege Escalation",
                AppLanguage.DE to "Privilege Escalation",
                AppLanguage.ES to "Escalada de Privilegios",
                AppLanguage.FR to "Élévation de Privilèges",
                AppLanguage.IT to "Escalation dei Privilegi",
                AppLanguage.PT to "Escalação de Privilégios",
                AppLanguage.RU to "Повышение привилегий",
                AppLanguage.ZH_CN to "权限提升 (PrivEsc)",
                AppLanguage.AR to "تصعيد الصلاحيات",
                AppLanguage.EL to "Κλιμάκωση Προνομίων",
                AppLanguage.NB to "Privilegieeskalering"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.EN to "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.DE to "Linux & Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.ES to "PrivEsc en Linux y Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.FR to "PrivEsc Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.IT to "PrivEsc Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.PT to "PrivEsc Linux e Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.RU to "Повышение привилегий Linux и Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.ZH_CN to "Linux 与 Windows 提权技术、PEASS-ng、LOLBAS、GTFOBins",
                AppLanguage.AR to "تصعيد الصلاحيات في Linux و Windows و PEASS-ng و LOLBAS و GTFOBins",
                AppLanguage.EL to "PrivEsc για Linux & Windows, PEASS-ng, LOLBAS, GTFOBins",
                AppLanguage.NB to "Linux og Windows PrivEsc, PEASS-ng, LOLBAS, GTFOBins"
            )
        )
        "Exploitation & Shells" -> CategoryMeta(
            categoryName = "Exploitation & Shells",
            icon = Icons.Default.Terminal,
            color = ExploitRed,
            titleTr = "Sömürü & Kabuklar",
            descTr = "Metasploit, Searchsploit, Msfvenom, Ters kabuklar & Socat",
            descEn = "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
            titlesByLang = mapOf(
                AppLanguage.TR to "Sömürü & Kabuklar",
                AppLanguage.EN to "Exploitation & Shells",
                AppLanguage.DE to "Exploitation & Shells",
                AppLanguage.ES to "Explotación y Shells",
                AppLanguage.FR to "Exploitation & Shells",
                AppLanguage.IT to "Exploitation & Shell",
                AppLanguage.PT to "Exploração e Shells",
                AppLanguage.RU to "Эксплуатация и шеллы",
                AppLanguage.ZH_CN to "漏洞利用与反弹 Shell",
                AppLanguage.AR to "الاستغلال وشبكات الاتصال",
                AppLanguage.EL to "Εκμετάλλευση & Shells",
                AppLanguage.NB to "Utnyttelse og shells"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Metasploit, Searchsploit, Msfvenom, Ters kabuklar & Socat",
                AppLanguage.EN to "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
                AppLanguage.DE to "Metasploit, Searchsploit, Msfvenom, Reverse Shells & Socat",
                AppLanguage.ES to "Metasploit, Searchsploit, Msfvenom, Reverse Shells y Socat",
                AppLanguage.FR to "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
                AppLanguage.IT to "Metasploit, Searchsploit, Msfvenom, Reverse shell e Socat",
                AppLanguage.PT to "Metasploit, Searchsploit, Msfvenom, Reverse shells e Socat",
                AppLanguage.RU to "Metasploit, Searchsploit, Msfvenom, реверс-шеллы и Socat",
                AppLanguage.ZH_CN to "Metasploit、Searchsploit、Msfvenom、反向 Shell 与 Socat",
                AppLanguage.AR to "Metasploit و Searchsploit و Msfvenom وأصداف عكسية و Socat",
                AppLanguage.EL to "Metasploit, Searchsploit, Msfvenom, Reverse shells & Socat",
                AppLanguage.NB to "Metasploit, Searchsploit, Msfvenom, Reverse shells og Socat"
            )
        )
        "Reverse Engineering" -> CategoryMeta(
            categoryName = "Reverse Engineering",
            icon = Icons.Default.BugReport,
            color = AccentPink,
            titleTr = "Tersine Mühendislik",
            descTr = "Ghidra, GDB, Radare2, Pwntools, Ropper & ikili analiz",
            descEn = "Ghidra, GDB, Radare2, Pwntools, Ropper & binary exploitation",
            titlesByLang = mapOf(
                AppLanguage.TR to "Tersine Mühendislik",
                AppLanguage.EN to "Reverse Engineering",
                AppLanguage.DE to "Reverse Engineering",
                AppLanguage.ES to "Ingeniería Inversa",
                AppLanguage.FR to "Rétro-Ingénierie",
                AppLanguage.IT to "Ingegneria Inversa",
                AppLanguage.PT to "Engenharia Reversa",
                AppLanguage.RU to "Обратная разработка",
                AppLanguage.ZH_CN to "逆向工程与二进制分析",
                AppLanguage.AR to "الهندسة العكسية",
                AppLanguage.EL to "Αντίστροφη Μηχανική",
                AppLanguage.NB to "Omvendt utvikling"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Ghidra, GDB, Radare2, Pwntools, Ropper & ikili analiz",
                AppLanguage.EN to "Ghidra, GDB, Radare2, Pwntools, Ropper & binary exploitation",
                AppLanguage.DE to "Ghidra, GDB, Radare2, Pwntools, Ropper & Binär-Analyse",
                AppLanguage.ES to "Ghidra, GDB, Radare2, Pwntools, Ropper y análisis binario",
                AppLanguage.FR to "Ghidra, GDB, Radare2, Pwntools, Ropper & exploitation binaire",
                AppLanguage.IT to "Ghidra, GDB, Radare2, Pwntools, Ropper e analisi binaria",
                AppLanguage.PT to "Ghidra, GDB, Radare2, Pwntools, Ropper e exploração binária",
                AppLanguage.RU to "Ghidra, GDB, Radare2, Pwntools, Ropper и анализ бинарников",
                AppLanguage.ZH_CN to "Ghidra、GDB、Radare2、Pwntools、Ropper 与二进制利用",
                AppLanguage.AR to "Ghidra و GDB و Radare2 و Pwntools و Ropper وتحليل البرمجيات",
                AppLanguage.EL to "Ghidra, GDB, Radare2, Pwntools, Ropper & ανάλυση εκτελέσιμων",
                AppLanguage.NB to "Ghidra, GDB, Radare2, Pwntools, Ropper og binær analyse"
            )
        )
        "Password Cracking" -> CategoryMeta(
            categoryName = "Password Cracking",
            icon = Icons.Default.Key,
            color = AccentYellow,
            titleTr = "Parola Kırma",
            descTr = "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            descEn = "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
            titlesByLang = mapOf(
                AppLanguage.TR to "Parola Kırma",
                AppLanguage.EN to "Password Cracking",
                AppLanguage.DE to "Passwort-Cracking",
                AppLanguage.ES to "Descifrado de Contraseñas",
                AppLanguage.FR to "Cassage de Mots de Passe",
                AppLanguage.IT to "Cracking Password",
                AppLanguage.PT to "Quebra de Senhas",
                AppLanguage.RU to "Взлом паролей",
                AppLanguage.ZH_CN to "密码破解与哈希恢复",
                AppLanguage.AR to "كسر كلمات المرور",
                AppLanguage.EL to "Σπάσιμο Κωδικών",
                AppLanguage.NB to "Passordknekking"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
                AppLanguage.EN to "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
                AppLanguage.DE to "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
                AppLanguage.ES to "Hashcat, John the Ripper, Hydra, CeWL, Crunch y NTH",
                AppLanguage.FR to "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
                AppLanguage.IT to "Hashcat, John the Ripper, Hydra, CeWL, Crunch e NTH",
                AppLanguage.PT to "Hashcat, John the Ripper, Hydra, CeWL, Crunch e NTH",
                AppLanguage.RU to "Hashcat, John the Ripper, Hydra, CeWL, Crunch и NTH",
                AppLanguage.ZH_CN to "Hashcat、John the Ripper、Hydra、CeWL、Crunch 与 NTH",
                AppLanguage.AR to "Hashcat و John the Ripper و Hydra و CeWL و Crunch و NTH",
                AppLanguage.EL to "Hashcat, John the Ripper, Hydra, CeWL, Crunch & NTH",
                AppLanguage.NB to "Hashcat, John the Ripper, Hydra, CeWL, Crunch og NTH"
            )
        )
        "Pivoting & Tunneling" -> CategoryMeta(
            categoryName = "Pivoting & Tunneling",
            icon = Icons.Default.SwapHoriz,
            color = AccentBlue,
            titleTr = "Pivoting & Tünelleme",
            descTr = "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
            descEn = "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
            titlesByLang = mapOf(
                AppLanguage.TR to "Pivoting & Tünelleme",
                AppLanguage.EN to "Pivoting & Tunneling",
                AppLanguage.DE to "Pivoting & Tunneling",
                AppLanguage.ES to "Pivoting y Túneles",
                AppLanguage.FR to "Pivoting & Tunnels",
                AppLanguage.IT to "Pivoting & Tunneling",
                AppLanguage.PT to "Pivoting e Tunelamento",
                AppLanguage.RU to "Пивотинг и туннелирование",
                AppLanguage.ZH_CN to "网络穿透与流量代理 (Pivoting)",
                AppLanguage.AR to "التنقل التكتيكي والأنفاق",
                AppLanguage.EL to "Pivoting & Διοχέτευση",
                AppLanguage.NB to "Pivoting og tunnelering"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
                AppLanguage.EN to "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
                AppLanguage.DE to "Ligolo-ng, Chisel, Proxychains, SSH-Tunnel & SOCKS5",
                AppLanguage.ES to "Ligolo-ng, Chisel, Proxychains, Túneles SSH y SOCKS5",
                AppLanguage.FR to "Ligolo-ng, Chisel, Proxychains, Tunnels SSH & SOCKS5",
                AppLanguage.IT to "Ligolo-ng, Chisel, Proxychains, Tunnel SSH e SOCKS5",
                AppLanguage.PT to "Ligolo-ng, Chisel, Proxychains, Túneis SSH e SOCKS5",
                AppLanguage.RU to "Ligolo-ng, Chisel, Proxychains, SSH-туннели и SOCKS5",
                AppLanguage.ZH_CN to "Ligolo-ng、Chisel、Proxychains、SSH 隧道与 SOCKS5",
                AppLanguage.AR to "Ligolo-ng و Chisel و Proxychains وأنفاق SSH و SOCKS5",
                AppLanguage.EL to "Ligolo-ng, Chisel, Proxychains, SSH Tunnels & SOCKS5",
                AppLanguage.NB to "Ligolo-ng, Chisel, Proxychains, SSH-tunneler og SOCKS5"
            )
        )
        "OSINT & Dorking" -> CategoryMeta(
            categoryName = "OSINT & Dorking",
            icon = Icons.Default.Visibility,
            color = AccentIndigo,
            titleTr = "Açık Kaynak İstihbaratı",
            descTr = "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            descEn = "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
            titlesByLang = mapOf(
                AppLanguage.TR to "Açık Kaynak İstihbaratı",
                AppLanguage.EN to "OSINT & Dorking",
                AppLanguage.DE to "OSINT & Dorking",
                AppLanguage.ES to "OSINT y Dorking",
                AppLanguage.FR to "OSINT & Dorking",
                AppLanguage.IT to "OSINT & Dorking",
                AppLanguage.PT to "OSINT e Dorking",
                AppLanguage.RU to "OSINT и доркинг",
                AppLanguage.ZH_CN to "开源情报 (OSINT) 与高级搜索",
                AppLanguage.AR to "استخبارات المصادر المفتوحة",
                AppLanguage.EL to "OSINT & Dorking",
                AppLanguage.NB to "OSINT og dorking"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.EN to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.DE to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.ES to "Shodan, Google Dorking, Sherlock, TruffleHog, theHarvester",
                AppLanguage.FR to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.IT to "Shodan, Google Dorking, Sherlock, TruffleHog, theHarvester",
                AppLanguage.PT to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.RU to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.ZH_CN to "Shodan、Google Dorking、Sherlock、TruffleHog 与 theHarvester",
                AppLanguage.AR to "Shodan و Google Dorking و Sherlock و TruffleHog و Harvester",
                AppLanguage.EL to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester",
                AppLanguage.NB to "Shodan, Google Dorking, Sherlock, TruffleHog, Harvester"
            )
        )
        "Payloads & Wordlists" -> CategoryMeta(
            categoryName = "Payloads & Wordlists",
            icon = Icons.Default.DataObject,
            color = AccentYellow,
            titleTr = "Payloadlar & Kelime Listeleri",
            descTr = "Enjeksiyon yükleri, Seclists referansları & Kali araçları",
            descEn = "Injection payloads, Seclists references & Kali tools",
            titlesByLang = mapOf(
                AppLanguage.TR to "Payloadlar & Kelime Listeleri",
                AppLanguage.EN to "Payloads & Wordlists",
                AppLanguage.DE to "Payloads & Wordlists",
                AppLanguage.ES to "Payloads y Diccionarios",
                AppLanguage.FR to "Payloads & Listes de Mots",
                AppLanguage.IT to "Payload & Wordlist",
                AppLanguage.PT to "Payloads e Wordlists",
                AppLanguage.RU to "Полезные нагрузки и словари",
                AppLanguage.ZH_CN to "有效载荷 (Payloads) 与字典",
                AppLanguage.AR to "الحمولات وقوائم الكلمات",
                AppLanguage.EL to "Payloads & Λίστες Λέξεων",
                AppLanguage.NB to "Nyttelaster og ordlister"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Enjeksiyon yükleri, Seclists referansları & Kali araçları",
                AppLanguage.EN to "Injection payloads, Seclists references & Kali tools",
                AppLanguage.DE to "Injection-Payloads, SecLists-Referenzen & Kali-Tools",
                AppLanguage.ES to "Payloads de inyección, referencias de SecLists y herramientas Kali",
                AppLanguage.FR to "Payloads d'injection, références SecLists & outils Kali",
                AppLanguage.IT to "Payload di injection, riferimenti SecLists e strumenti Kali",
                AppLanguage.PT to "Payloads de injeção, referências do SecLists e ferramentas Kali",
                AppLanguage.RU to "Инъекционные нагрузки, ссылки SecLists и инструменты Kali",
                AppLanguage.ZH_CN to "注入载荷、SecLists 字典集与 Kali 工具指南",
                AppLanguage.AR to "حمولات الحقن، ومراجع SecLists وأدوات Kali",
                AppLanguage.EL to "Φορτία έγχυσης, αναφορές SecLists & εργαλεία Kali",
                AppLanguage.NB to "Injeksjonslaster, SecLists-referanser og Kali-verktøy"
            )
        )
        "Wireless & Network" -> CategoryMeta(
            categoryName = "Wireless & Network",
            icon = Icons.Default.Wifi,
            color = AccentEmerald,
            titleTr = "Kablosuz Ağ Güvenliği",
            descTr = "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            descEn = "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
            titlesByLang = mapOf(
                AppLanguage.TR to "Kablosuz Ağ Güvenliği",
                AppLanguage.EN to "Wireless & Network",
                AppLanguage.DE to "WLAN- & Netzwerksicherheit",
                AppLanguage.ES to "Seguridad Inalámbrica y Redes",
                AppLanguage.FR to "Sans-Fil & Réseau",
                AppLanguage.IT to "Sicurezza Wireless & Reti",
                AppLanguage.PT to "Segurança Sem Fio e Redes",
                AppLanguage.RU to "Беспроводные сети и Wi-Fi",
                AppLanguage.ZH_CN to "无线网络与 Wi-Fi 安全",
                AppLanguage.AR to "الشبكات اللاسلكية والواي فاي",
                AppLanguage.EL to "Ασύρματα Δίκτυα & Wi-Fi",
                AppLanguage.NB to "Trådløst og nettverk"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.EN to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.DE to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.ES to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.FR to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.IT to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.PT to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.RU to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.ZH_CN to "Aircrack-ng、Bettercap、Wifite、hcxdumptool、Reaver",
                AppLanguage.AR to "Aircrack-ng و Bettercap و Wifite و hcxdumptool و Reaver",
                AppLanguage.EL to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver",
                AppLanguage.NB to "Aircrack-ng, Bettercap, Wifite, hcxdumptool, Reaver"
            )
        )
        "Network Analysis" -> CategoryMeta(
            categoryName = "Network Analysis",
            icon = Icons.Default.Hub,
            color = AccentBlue,
            titleTr = "Ağ Analizi & Sniffing",
            descTr = "Wireshark, tshark, tcpdump, Scapy, Ettercap paket analizi",
            descEn = "Wireshark, tshark, tcpdump, Scapy, Ettercap packet capture",
            titlesByLang = mapOf(
                AppLanguage.TR to "Ağ Analizi & Sniffing",
                AppLanguage.EN to "Network Analysis",
                AppLanguage.DE to "Netzwerkanalyse & Sniffing",
                AppLanguage.ES to "Análisis de Red y Sniffing",
                AppLanguage.FR to "Analyse Réseau & Sniffing",
                AppLanguage.IT to "Analisi di Rete & Sniffing",
                AppLanguage.PT to "Análise de Rede e Sniffing",
                AppLanguage.RU to "Анализ сети и сниффинг",
                AppLanguage.ZH_CN to "网络分析与流量嗅探",
                AppLanguage.AR to "تحليل الشبكات واعتراض الحزم",
                AppLanguage.EL to "Ανάλυση Δικτύου & Υποκλοπή",
                AppLanguage.NB to "Nettverksanalyse og sniffing"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Wireshark, tshark, tcpdump, Scapy, Ettercap paket analizi",
                AppLanguage.EN to "Wireshark, tshark, tcpdump, Scapy, Ettercap packet capture",
                AppLanguage.DE to "Wireshark, tshark, tcpdump, Scapy, Ettercap Paketanalyse",
                AppLanguage.ES to "Wireshark, tshark, tcpdump, Scapy, Ettercap captura de paquetes",
                AppLanguage.FR to "Wireshark, tshark, tcpdump, Scapy, Ettercap capture de paquets",
                AppLanguage.IT to "Wireshark, tshark, tcpdump, Scapy, Ettercap analisi pacchetti",
                AppLanguage.PT to "Wireshark, tshark, tcpdump, Scapy, Ettercap captura de pacotes",
                AppLanguage.RU to "Wireshark, tshark, tcpdump, Scapy, Ettercap захват пакетов",
                AppLanguage.ZH_CN to "Wireshark、tshark、tcpdump、Scapy、Ettercap 数据包分析",
                AppLanguage.AR to "تحليل الحزم عبر Wireshark و tshark و tcpdump و Scapy و Ettercap",
                AppLanguage.EL to "Wireshark, tshark, tcpdump, Scapy, Ettercap σύλληψη πακέτων",
                AppLanguage.NB to "Wireshark, tshark, tcpdump, Scapy, Ettercap pakkeanalyse"
            )
        )
        "Cloud Security" -> CategoryMeta(
            categoryName = "Cloud Security",
            icon = Icons.Default.Cloud,
            color = AccentTeal,
            titleTr = "Bulut Güvenliği",
            descTr = "AWS, Azure, GCP denetimi, Pacu ve Prowler araçları",
            descEn = "AWS, Azure, GCP audit, Pacu and Prowler tools",
            titlesByLang = mapOf(
                AppLanguage.TR to "Bulut Güvenliği",
                AppLanguage.EN to "Cloud Security",
                AppLanguage.DE to "Cloud-Sicherheit",
                AppLanguage.ES to "Seguridad en la Nube",
                AppLanguage.FR to "Sécurité Cloud",
                AppLanguage.IT to "Sicurezza Cloud",
                AppLanguage.PT to "Segurança na Nuvem",
                AppLanguage.RU to "Облачная безопасность",
                AppLanguage.ZH_CN to "云安全 (Cloud Security)",
                AppLanguage.AR to "أمان السحابة",
                AppLanguage.EL to "Ασφάλεια Cloud",
                AppLanguage.NB to "Skysikkerhet"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "AWS, Azure, GCP denetimi, Pacu ve Prowler araçları",
                AppLanguage.EN to "AWS, Azure, GCP audit, Pacu and Prowler tools",
                AppLanguage.DE to "AWS, Azure, GCP-Audits, Pacu & Prowler-Tools",
                AppLanguage.ES to "Auditorías de AWS, Azure, GCP, herramientas Pacu y Prowler",
                AppLanguage.FR to "Audit AWS, Azure, GCP, outils Pacu & Prowler",
                AppLanguage.IT to "Audit AWS, Azure, GCP, strumenti Pacu e Prowler",
                AppLanguage.PT to "Auditoria AWS, Azure, GCP, ferramentas Pacu e Prowler",
                AppLanguage.RU to "Аудит AWS, Azure, GCP, инструменты Pacu и Prowler",
                AppLanguage.ZH_CN to "AWS、Azure、GCP 安全审计、Pacu 与 Prowler 工具",
                AppLanguage.AR to "تدقيق AWS و Azure و GCP وأدوات Pacu و Prowler",
                AppLanguage.EL to "Έλεγχος AWS, Azure, GCP, εργαλεία Pacu & Prowler",
                AppLanguage.NB to "AWS-, Azure-, GCP-revisjon, Pacu- og Prowler-verktøy"
            )
        )
        "Container Security" -> CategoryMeta(
            categoryName = "Container Security",
            icon = Icons.Default.Inventory2,
            color = AccentTeal,
            titleTr = "Konteyner Güvenliği",
            descTr = "Docker kaçışları, Kubernetes sızma testleri, Trivy",
            descEn = "Docker breakouts, Kubernetes pentesting, Trivy",
            titlesByLang = mapOf(
                AppLanguage.TR to "Konteyner Güvenliği",
                AppLanguage.EN to "Container Security",
                AppLanguage.DE to "Container-Sicherheit",
                AppLanguage.ES to "Seguridad de Contenedores",
                AppLanguage.FR to "Sécurité des Conteneurs",
                AppLanguage.IT to "Sicurezza dei Container",
                AppLanguage.PT to "Segurança de Contêineres",
                AppLanguage.RU to "Безопасность контейнеров",
                AppLanguage.ZH_CN to "容器与云原生安全",
                AppLanguage.AR to "أمان الحاويات والـ Docker",
                AppLanguage.EL to "Ασφάλεια Containers",
                AppLanguage.NB to "Containersikkerhet"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Docker kaçışları, Kubernetes sızma testleri, Trivy",
                AppLanguage.EN to "Docker breakouts, Kubernetes pentesting, Trivy",
                AppLanguage.DE to "Docker-Ausbrüche, Kubernetes-Pentests, Trivy",
                AppLanguage.ES to "Escapes de Docker, pentesting en Kubernetes, Trivy",
                AppLanguage.FR to "Évasions Docker, pentest Kubernetes, Trivy",
                AppLanguage.IT to "Evasioni Docker, pentesting Kubernetes, Trivy",
                AppLanguage.PT to "Escapes de Docker, pentest em Kubernetes, Trivy",
                AppLanguage.RU to "Побеги из Docker, пентестинг Kubernetes, Trivy",
                AppLanguage.ZH_CN to "Docker 逃逸、Kubernetes 渗透测试与 Trivy 镜像漏洞扫描",
                AppLanguage.AR to "الهروب من Docker واختبار اختراق Kubernetes و Trivy",
                AppLanguage.EL to "Διαφυγές Docker, pentesting Kubernetes, Trivy",
                AppLanguage.NB to "Docker-unnslippelse, Kubernetes-pentesting, Trivy"
            )
        )
        "Mobile Security" -> CategoryMeta(
            categoryName = "Mobile Security",
            icon = Icons.Default.PhoneAndroid,
            color = AccentLime,
            titleTr = "Mobil Güvenlik",
            descTr = "Android & iOS pentest, Frida, Jadx, APKTool enjeksiyonu",
            descEn = "Android & iOS pentesting, Frida, Jadx, APKTool hooks",
            titlesByLang = mapOf(
                AppLanguage.TR to "Mobil Güvenlik",
                AppLanguage.EN to "Mobile Security",
                AppLanguage.DE to "Mobile Sicherheit",
                AppLanguage.ES to "Seguridad Móvil",
                AppLanguage.FR to "Sécurité Mobile",
                AppLanguage.IT to "Sicurezza Mobile",
                AppLanguage.PT to "Segurança Mobile",
                AppLanguage.RU to "Мобильная безопасность",
                AppLanguage.ZH_CN to "移动安全 (Android & iOS)",
                AppLanguage.AR to "أمان الأجهزة المحمولة",
                AppLanguage.EL to "Ασφάλεια Κινητών Συσκευών",
                AppLanguage.NB to "Mobilsikkerhet"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Android & iOS pentest, Frida, Jadx, APKTool enjeksiyonu",
                AppLanguage.EN to "Android & iOS pentesting, Frida, Jadx, APKTool hooks",
                AppLanguage.DE to "Android- & iOS-Pentesting, Frida, Jadx, APKTool-Hooks",
                AppLanguage.ES to "Pentesting en Android e iOS, Frida, Jadx, inyecciones APKTool",
                AppLanguage.FR to "Pentest Android & iOS, Frida, Jadx, injection APKTool",
                AppLanguage.IT to "Pentest Android & iOS, Frida, Jadx, iniezioni APKTool",
                AppLanguage.PT to "Pentesting em Android e iOS, Frida, Jadx, injeções APKTool",
                AppLanguage.RU to "Пентестинг Android и iOS, Frida, Jadx, хуки APKTool",
                AppLanguage.ZH_CN to "Android 与 iOS 渗透测试、Frida Hook、Jadx 反编译与 APKTool",
                AppLanguage.AR to "اختبار اختراق Android و iOS و Frida و Jadx و APKTool",
                AppLanguage.EL to "Pentesting Android & iOS, Frida, Jadx, hooks APKTool",
                AppLanguage.NB to "Android- og iOS-pentesting, Frida, Jadx, APKTool-hooks"
            )
        )
        "Digital Forensics" -> CategoryMeta(
            categoryName = "Digital Forensics",
            icon = Icons.Default.Fingerprint,
            color = AccentRose,
            titleTr = "Adli Bilişim (Forensics)",
            descTr = "Autopsy, Volatility RAM analizi, Binwalk, ExifTool",
            descEn = "Autopsy, Volatility RAM analysis, Binwalk, ExifTool",
            titlesByLang = mapOf(
                AppLanguage.TR to "Adli Bilişim (Forensics)",
                AppLanguage.EN to "Digital Forensics",
                AppLanguage.DE to "Digitale Forensik",
                AppLanguage.ES to "Informática Forense",
                AppLanguage.FR to "Investigation Numérique",
                AppLanguage.IT to "Informatica Forense",
                AppLanguage.PT to "Computação Forense",
                AppLanguage.RU to "Цифровая криминалистика",
                AppLanguage.ZH_CN to "电子数据取证 (Forensics)",
                AppLanguage.AR to "الأدلة الجنائية الرقمية",
                AppLanguage.EL to "Ψηφιακή Εγκληματολογία",
                AppLanguage.NB to "Digital etterforskning"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Autopsy, Volatility RAM analizi, Binwalk, ExifTool",
                AppLanguage.EN to "Autopsy, Volatility RAM analysis, Binwalk, ExifTool",
                AppLanguage.DE to "Autopsy, Volatility RAM-Analyse, Binwalk, ExifTool",
                AppLanguage.ES to "Autopsy, análisis de RAM con Volatility, Binwalk, ExifTool",
                AppLanguage.FR to "Autopsy, analyse de RAM Volatility, Binwalk, ExifTool",
                AppLanguage.IT to "Autopsy, analisi RAM Volatility, Binwalk, ExifTool",
                AppLanguage.PT to "Autopsy, análise de RAM com Volatility, Binwalk, ExifTool",
                AppLanguage.RU to "Autopsy, анализ RAM с Volatility, Binwalk, ExifTool",
                AppLanguage.ZH_CN to "Autopsy、Volatility 内存取证、Binwalk 固件分析、ExifTool",
                AppLanguage.AR to "Autopsy وتحليل الذاكرة عبر Volatility و Binwalk و ExifTool",
                AppLanguage.EL to "Autopsy, ανάλυση RAM με Volatility, Binwalk, ExifTool",
                AppLanguage.NB to "Autopsy, Volatility RAM-analyse, Binwalk, ExifTool"
            )
        )
        "Blue Team & Defense" -> CategoryMeta(
            categoryName = "Blue Team & Defense",
            icon = Icons.Default.Security,
            color = TerminalCyan,
            titleTr = "Mavi Takım & Savunma",
            descTr = "Sigma kuralları, YARA, Sysinternals, log ve tehdit avı",
            descEn = "Sigma rules, YARA, Sysinternals, logs & threat hunting",
            titlesByLang = mapOf(
                AppLanguage.TR to "Mavi Takım & Savunma",
                AppLanguage.EN to "Blue Team & Defense",
                AppLanguage.DE to "Blue Team & Verteidigung",
                AppLanguage.ES to "Blue Team y Defensa",
                AppLanguage.FR to "Blue Team & Défense",
                AppLanguage.IT to "Blue Team & Difesa",
                AppLanguage.PT to "Blue Team e Defesa",
                AppLanguage.RU to "Blue Team и защита",
                AppLanguage.ZH_CN to "蓝队防守与威胁检测",
                AppLanguage.AR to "الفريق الأزرق والدفاع",
                AppLanguage.EL to "Blue Team & Άμυνα",
                AppLanguage.NB to "Blue Team og forsvar"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Sigma kuralları, YARA, Sysinternals, log ve tehdit avı",
                AppLanguage.EN to "Sigma rules, YARA, Sysinternals, logs & threat hunting",
                AppLanguage.DE to "Sigma-Regeln, YARA, Sysinternals, Logs & Threat Hunting",
                AppLanguage.ES to "Reglas Sigma, YARA, Sysinternals, logs y caza de amenazas",
                AppLanguage.FR to "Règles Sigma, YARA, Sysinternals, journaux & traque des menaces",
                AppLanguage.IT to "Regole Sigma, YARA, Sysinternals, log e threat hunting",
                AppLanguage.PT to "Regras Sigma, YARA, Sysinternals, logs e caça a ameaças",
                AppLanguage.RU to "Правила Sigma, YARA, Sysinternals, журналы и поиск угроз",
                AppLanguage.ZH_CN to "Sigma 规则、YARA、Sysinternals、日志分析与威胁狩猎",
                AppLanguage.AR to "قواعد Sigma و YARA و Sysinternals والسجلات وصيد التهديدات",
                AppLanguage.EL to "Κανόνες Sigma, YARA, Sysinternals, αρχεία καταγραφής & κυνήγι απειλών",
                AppLanguage.NB to "Sigma-regler, YARA, Sysinternals, logger og trusselsjakt"
            )
        )
        "Attack Methodology" -> CategoryMeta(
            categoryName = "Attack Methodology",
            icon = Icons.Default.TrackChanges,
            color = ExploitRed,
            titleTr = "Saldırı Metodolojisi",
            descTr = "MITRE ATT&CK Kill Chain: 01 Erişimden 07 Hedefe kadar",
            descEn = "MITRE ATT&CK Kill Chain: 01 Initial Access to 07 Objectives",
            titlesByLang = mapOf(
                AppLanguage.TR to "Saldırı Metodolojisi",
                AppLanguage.EN to "Attack Methodology",
                AppLanguage.DE to "Angriffsmethodik",
                AppLanguage.ES to "Metodología de Ataque",
                AppLanguage.FR to "Méthodologie d'Attaque",
                AppLanguage.IT to "Metodologia di Attacco",
                AppLanguage.PT to "Metodologia de Ataque",
                AppLanguage.RU to "Методология атак",
                AppLanguage.ZH_CN to "渗透测试方法论 (MITRE ATT&CK)",
                AppLanguage.AR to "منهجية الهجوم والتسلل",
                AppLanguage.EL to "Μεθοδολογία Επιθέσεων",
                AppLanguage.NB to "Angrepsmetodikk"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "MITRE ATT&CK Kill Chain: 01 Erişimden 07 Hedefe kadar",
                AppLanguage.EN to "MITRE ATT&CK Kill Chain: 01 Initial Access to 07 Objectives",
                AppLanguage.DE to "MITRE ATT&CK Kill Chain: 01 Initial Access bis 07 Objectives",
                AppLanguage.ES to "MITRE ATT&CK Kill Chain: 01 Acceso Inicial a 07 Objetivos",
                AppLanguage.FR to "MITRE ATT&CK Kill Chain : 01 Accès Initial à 07 Objectifs",
                AppLanguage.IT to "MITRE ATT&CK Kill Chain: Da 01 Accesso Iniziale a 07 Obiettivi",
                AppLanguage.PT to "MITRE ATT&CK Kill Chain: 01 Acesso Inicial até 07 Objetivos",
                AppLanguage.RU to "MITRE ATT&CK Kill Chain: от 01 Начального доступа до 07 Целей",
                AppLanguage.ZH_CN to "MITRE ATT&CK 攻击链：从 01 初始访问至 07 行动目标",
                AppLanguage.AR to "سلسلة القتل MITRE ATT&CK: من 01 الوصول الأولي إلى 07 الأهداف",
                AppLanguage.EL to "MITRE ATT&CK Kill Chain: 01 Αρχική Πρόσβαση έως 07 Στόχοι",
                AppLanguage.NB to "MITRE ATT&CK Kill Chain: 01 Førstegangstilgang til 07 Mål"
            )
        )
        "CTF Cheatsheets" -> CategoryMeta(
            categoryName = "CTF Cheatsheets",
            icon = Icons.Default.EmojiEvents,
            color = AccentYellow,
            titleTr = "CTF İpuçları & Rehberler",
            descTr = "Kriptografi, steganografi, web ve binary pwn ipuçları",
            descEn = "Cryptography, steganography, web & binary pwn tricks",
            titlesByLang = mapOf(
                AppLanguage.TR to "CTF İpuçları & Rehberler",
                AppLanguage.EN to "CTF Cheatsheets",
                AppLanguage.DE to "CTF Cheatsheets & Guides",
                AppLanguage.ES to "Guías y Cheatsheets de CTF",
                AppLanguage.FR to "Aide-mémoire & Guides CTF",
                AppLanguage.IT to "Guide e Cheatsheet CTF",
                AppLanguage.PT to "Guias e Cheatsheets CTF",
                AppLanguage.RU to "Шпаргалки и гайды CTF",
                AppLanguage.ZH_CN to "CTF 比赛速查与攻略",
                AppLanguage.AR to "أدلة وتلميحات مسابقات CTF",
                AppLanguage.EL to "Οδηγοί & Cheatsheets CTF",
                AppLanguage.NB to "CTF Cheatsheets og veiledninger"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Kriptografi, steganografi, web ve binary pwn ipuçları",
                AppLanguage.EN to "Cryptography, steganography, web & binary pwn tricks",
                AppLanguage.DE to "Kryptographie, Steganographie, Web & Binary Pwn Tricks",
                AppLanguage.ES to "Criptografía, esteganografía, trucos web y binary pwn",
                AppLanguage.FR to "Cryptographie, stéganographie, astuces web & binary pwn",
                AppLanguage.IT to "Crittografia, steganografia, trucchi web e binary pwn",
                AppLanguage.PT to "Criptografia, esteganografia, truques web e binary pwn",
                AppLanguage.RU to "Криптография, стеганография, трюки для веб и бинарного pwn",
                AppLanguage.ZH_CN to "密码学、隐写术、Web 渗透与二进制 Pwn 实战技巧",
                AppLanguage.AR to "التشفير وإخفاء البيانات وحيل الويب والـ pwn الثنائي",
                AppLanguage.EL to "Κρυπτογραφία, στεγανογραφία, κόλπα web & binary pwn",
                AppLanguage.NB to "Kryptografi, steganografi, web og binære pwn-triks"
            )
        )
        "IoT & Hardware" -> CategoryMeta(
            categoryName = "IoT & Hardware",
            icon = Icons.Default.Memory,
            color = AccentSlate,
            titleTr = "IoT & Donanım Güvenliği",
            descTr = "Gömülü sistemler, UART, JTAG, firmware analizi",
            descEn = "Embedded systems, UART, JTAG, firmware extraction",
            titlesByLang = mapOf(
                AppLanguage.TR to "IoT & Donanım Güvenliği",
                AppLanguage.EN to "IoT & Hardware",
                AppLanguage.DE to "IoT & Hardware-Sicherheit",
                AppLanguage.ES to "IoT y Seguridad de Hardware",
                AppLanguage.FR to "Sécurité IoT & Matérielle",
                AppLanguage.IT to "Sicurezza IoT & Hardware",
                AppLanguage.PT to "Segurança de IoT e Hardware",
                AppLanguage.RU to "IoT и аппаратная безопасность",
                AppLanguage.ZH_CN to "物联网与硬件安全",
                AppLanguage.AR to "أمان إنترنت الأشياء والعتاد",
                AppLanguage.EL to "Ασφάλεια IoT & Υλικού",
                AppLanguage.NB to "IoT og maskinvaresikkerhet"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Gömülü sistemler, UART, JTAG, firmware analizi",
                AppLanguage.EN to "Embedded systems, UART, JTAG, firmware extraction",
                AppLanguage.DE to "Eingebettete Systeme, UART, JTAG, Firmware-Extraktion",
                AppLanguage.ES to "Sistemas embebidos, UART, JTAG, extracción de firmware",
                AppLanguage.FR to "Systèmes embarqués, UART, JTAG, extraction de firmware",
                AppLanguage.IT to "Sistemi embedded, UART, JTAG, estrazione di firmware",
                AppLanguage.PT to "Sistemas embarcados, UART, JTAG, extração de firmware",
                AppLanguage.RU to "Встраиваемые системы, UART, JTAG, извлечение прошивок",
                AppLanguage.ZH_CN to "嵌入式系统、UART、JTAG 硬件接口与固件提取分析",
                AppLanguage.AR to "الأنظمة المدمجة و UART و JTAG واستخراج البرامج الثابتة",
                AppLanguage.EL to "Ενσωματωμένα συστήματα, UART, JTAG, εξαγωγή firmware",
                AppLanguage.NB to "Innebygde systemer, UART, JTAG, firmware-utvinning"
            )
        )
        "Social Engineering" -> CategoryMeta(
            categoryName = "Social Engineering",
            icon = Icons.Default.Psychology,
            color = AccentPink,
            titleTr = "Sosyal Mühendislik",
            descTr = "Oltalama (Phishing), OSINT insan faktörü ve senaryolar",
            descEn = "Phishing, pretexting, human OSINT & assessments",
            titlesByLang = mapOf(
                AppLanguage.TR to "Sosyal Mühendislik",
                AppLanguage.EN to "Social Engineering",
                AppLanguage.DE to "Social Engineering",
                AppLanguage.ES to "Ingeniería Social",
                AppLanguage.FR to "Ingénierie Sociale",
                AppLanguage.IT to "Ingegneria Sociale",
                AppLanguage.PT to "Engenharia Social",
                AppLanguage.RU to "Социальная инженерия",
                AppLanguage.ZH_CN to "社会工程学 (Social Engineering)",
                AppLanguage.AR to "الهندسة الاجتماعية",
                AppLanguage.EL to "Κοινωνική Μηχανική",
                AppLanguage.NB to "Sosial manipulering"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Oltalama (Phishing), OSINT insan faktörü ve senaryolar",
                AppLanguage.EN to "Phishing, pretexting, human OSINT & assessments",
                AppLanguage.DE to "Phishing, Pretexting, Human-OSINT & Szenarien",
                AppLanguage.ES to "Phishing, pretexting, OSINT del factor humano y escenarios",
                AppLanguage.FR to "Phishing, prétextage, OSINT humain & évaluations",
                AppLanguage.IT to "Phishing, pretexting, OSINT umano e valutazioni",
                AppLanguage.PT to "Phishing, pretexting, OSINT humano e avaliações",
                AppLanguage.RU to "Фишинг, претекстинг, человеческий фактор OSINT и оценки",
                AppLanguage.ZH_CN to "网络钓鱼 (Phishing)、话术设套、人肉情报与对抗评估",
                AppLanguage.AR to "التصيد الاحتيالي والانتحال و OSINT البشري والتقييمات",
                AppLanguage.EL to "Phishing, pretexting, ανθρώπινο OSINT & αξιολογήσεις",
                AppLanguage.NB to "Phishing, pretexting, menneskelig OSINT og vurderinger"
            )
        )
        "Reporting & Guides" -> CategoryMeta(
            categoryName = "Reporting & Guides",
            icon = Icons.Default.Description,
            color = AccentSlate,
            titleTr = "Raporlama & Şablonlar",
            descTr = "Pentest rapor taslakları, yönetici özeti, Bug Bounty",
            descEn = "Pentest report templates, executive summary, Bug Bounty",
            titlesByLang = mapOf(
                AppLanguage.TR to "Raporlama & Şablonlar",
                AppLanguage.EN to "Reporting & Guides",
                AppLanguage.DE to "Berichte & Vorlagen",
                AppLanguage.ES to "Informes y Guías",
                AppLanguage.FR to "Rapports & Guides",
                AppLanguage.IT to "Reportistica & Guide",
                AppLanguage.PT to "Relatórios e Guias",
                AppLanguage.RU to "Отчеты и руководства",
                AppLanguage.ZH_CN to "渗透测试报告与评估指南",
                AppLanguage.AR to "التقارير والقوالب الإرشادية",
                AppLanguage.EL to "Αναφορές & Οδηγοί",
                AppLanguage.NB to "Rapportering og veiledninger"
            ),
            descsByLang = mapOf(
                AppLanguage.TR to "Pentest rapor taslakları, yönetici özeti, Bug Bounty",
                AppLanguage.EN to "Pentest report templates, executive summary, Bug Bounty",
                AppLanguage.DE to "Pentest-Berichtsvorlagen, Management Summary, Bug Bounty",
                AppLanguage.ES to "Plantillas de informes de pentest, resumen ejecutivo, Bug Bounty",
                AppLanguage.FR to "Modèles de rapports de pentest, résumé exécutif, Bug Bounty",
                AppLanguage.IT to "Modelli di report pentest, executive summary, Bug Bounty",
                AppLanguage.PT to "Modelos de relatórios de pentest, resumo executivo, Bug Bounty",
                AppLanguage.RU to "Шаблоны отчетов пентеста, резюме для руководства, Bug Bounty",
                AppLanguage.ZH_CN to "渗透测试报告模板、管理层执行摘要与漏洞赏金指南",
                AppLanguage.AR to "نماذج تقارير اختبار الاختراق، الملخص التنفيذي ومكافآت الثغرات",
                AppLanguage.EL to "Πρότυπα αναφορών pentest, περίληψη στελεχών, Bug Bounty",
                AppLanguage.NB to "Pentest-rapportmaler, ledelsessammendrag, Bug Bounty"
            )
        )
        else -> CategoryMeta(
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
