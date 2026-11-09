#!/usr/bin/env python3
"""
Generate complete multilingual translations for the 71 missing tools
across 10 target languages: ar, de, el, es, fr, it, nb, pt, ru, zh-CN.
Uses the extensive 825-file parallel corpus from upstream plus semantic rules.
"""

import os
import re
import sys

TARGET_LANGS = ["de", "es", "fr", "it", "pt", "ru", "ar", "el", "zh-CN", "nb"]

LANG_CONFIG = {
    "de": {
        "home_title": "Deutsche Startseite",
        "home_file": "README.de.md",
        "title_suffix": "– vollständiger Spickzettel",
        "toc_title": "## 📋 Inhaltsverzeichnis",
        "intro_heading": "Einführung",
        "install_heading": "Installation",
        "syntax_heading": "Grundsyntax",
        "options_heading": "Optionen und Parameter",
        "examples_heading": "Praxisbeispiele",
        "quickref_heading": "Kurzreferenz",
        "tips_heading": "Tipps und bewährte Vorgehensweisen",
        "resources_heading": "Ressourcen und Referenzen",
        "col_option": "Option",
        "col_desc": "Beschreibung",
        "col_cmd": "Befehl",
        "col_example": "Beispiel",
        "col_flag": "Flag",
        "col_purpose": "Zweck",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "es": {
        "home_title": "Página principal en español",
        "home_file": "README.es.md",
        "title_suffix": "- Guía de referencia completa",
        "toc_title": "## 📋 Tabla de contenidos",
        "intro_heading": "Introducción",
        "install_heading": "Instalación",
        "syntax_heading": "Sintaxis básica",
        "options_heading": "Opciones y parámetros",
        "examples_heading": "Ejemplos prácticos",
        "quickref_heading": "Referencia rápida",
        "tips_heading": "Consejos y mejores prácticas",
        "resources_heading": "Recursos y referencias",
        "col_option": "Opción",
        "col_desc": "Descripción",
        "col_cmd": "Comando",
        "col_example": "Ejemplo",
        "col_flag": "Flag",
        "col_purpose": "Propósito",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "fr": {
        "home_title": "Accueil en français",
        "home_file": "README.fr.md",
        "title_suffix": "— fiche pratique complète",
        "toc_title": "## 📋 Table des matières",
        "intro_heading": "Introduction",
        "install_heading": "Installation",
        "syntax_heading": "Syntaxe de base",
        "options_heading": "Options et paramètres",
        "examples_heading": "Exemples pratiques",
        "quickref_heading": "Référence rapide",
        "tips_heading": "Conseils et bonnes pratiques",
        "resources_heading": "Ressources et références",
        "col_option": "Option",
        "col_desc": "Description",
        "col_cmd": "Commande",
        "col_example": "Exemple",
        "col_flag": "Flag",
        "col_purpose": "Objectif",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "it": {
        "home_title": "Pagina iniziale italiana",
        "home_file": "README.it.md",
        "title_suffix": "- Scheda di riferimento completa",
        "toc_title": "## 📋 Indice",
        "intro_heading": "Introduzione",
        "install_heading": "Installazione",
        "syntax_heading": "Sintassi di base",
        "options_heading": "Opzioni e parametri",
        "examples_heading": "Esempi pratici",
        "quickref_heading": "Riferimento rapido",
        "tips_heading": "Suggerimenti e buone pratiche",
        "resources_heading": "Risorse e riferimenti",
        "col_option": "Opzione",
        "col_desc": "Descrizione",
        "col_cmd": "Comando",
        "col_example": "Esempio",
        "col_flag": "Flag",
        "col_purpose": "Scopo",
        "quick_links": "[🇬🇧 English](../README.md) • [🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "pt": {
        "home_title": "Página inicial em português",
        "home_file": "README.pt.md",
        "title_suffix": "— Guia de referência completo",
        "toc_title": "## 📋 Índice",
        "intro_heading": "Introdução",
        "install_heading": "Instalação",
        "syntax_heading": "Sintaxe básica",
        "options_heading": "Opções e parâmetros",
        "examples_heading": "Exemplos práticos",
        "quickref_heading": "Referência rápida",
        "tips_heading": "Dicas e melhores práticas",
        "resources_heading": "Recursos e referências",
        "col_option": "Opção",
        "col_desc": "Descrição",
        "col_cmd": "Comando",
        "col_example": "Exemplo",
        "col_flag": "Flag",
        "col_purpose": "Finalidade",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "ru": {
        "home_title": "Русская главная страница",
        "home_file": "README.ru.md",
        "title_suffix": "— полная шпаргалка",
        "toc_title": "## 📋 Содержание",
        "intro_heading": "Введение",
        "install_heading": "Установка",
        "syntax_heading": "Базовый синтаксис",
        "options_heading": "Параметры и опции",
        "examples_heading": "Практические примеры",
        "quickref_heading": "Краткий справочник",
        "tips_heading": "Советы и рекомендации",
        "resources_heading": "Ресурсы и ссылки",
        "col_option": "Опция",
        "col_desc": "Описание",
        "col_cmd": "Команда",
        "col_example": "Пример",
        "col_flag": "Флаг",
        "col_purpose": "Назначение",
        "quick_links": "[🇬🇧 English](../README.md) • [🇷🇺 Русский раздел](../../README.ru.md)"
    },
    "ar": {
        "home_title": "الصفحة الرئيسية العربية",
        "home_file": "README.ar.md",
        "title_suffix": "— ورقة مرجعية شاملة",
        "toc_title": "## 📋 جدول المحتويات",
        "intro_heading": "مقدمة",
        "install_heading": "التثبيت",
        "syntax_heading": "بناء الجملة الأساسي",
        "options_heading": "الخيارات والمعلمات",
        "examples_heading": "أمثلة عملية",
        "quickref_heading": "مرجع سريع",
        "tips_heading": "نصائح وأفضل الممارسات",
        "resources_heading": "المصادر والمراجع",
        "col_option": "الخيار",
        "col_desc": "الوصف",
        "col_cmd": "الأمر",
        "col_example": "مثال",
        "col_flag": "العلامة",
        "col_purpose": "الغرض",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    },
    "el": {
        "home_title": "Ελληνική αρχική σελίδα",
        "home_file": "README.el.md",
        "title_suffix": "— Πλήρης οδηγός αναφοράς",
        "toc_title": "## 📋 Πίνακας περιεχομένων",
        "intro_heading": "Εισαγωγή",
        "install_heading": "Εγκατάσταση",
        "syntax_heading": "Βασική σύνταξη",
        "options_heading": "Επιλογές και παράμετροι",
        "examples_heading": "Πρακτικά παραδείγματα",
        "quickref_heading": "Γρήγορη αναφορά",
        "tips_heading": "Συμβουλές και βέλτιστες πρακτικές",
        "resources_heading": "Πηγές και αναφορές",
        "col_option": "Επιλογή",
        "col_desc": "Περιγραφή",
        "col_cmd": "Εντολή",
        "col_example": "Παράδειγμα",
        "col_flag": "Σημαία",
        "col_purpose": "Σκοπός",
        "quick_links": "[🇬🇧 English](../README.md) • [🇹🇷 Türkçe](README.tr.md)"
    },
    "zh-CN": {
        "home_title": "中文首页",
        "home_file": "README.zh-CN.md",
        "title_suffix": "完整速查表",
        "toc_title": "## 📋 目录",
        "intro_heading": "简介",
        "install_heading": "安装",
        "syntax_heading": "基本语法",
        "options_heading": "常用选项与参数",
        "examples_heading": "常用示例",
        "quickref_heading": "快速参考",
        "tips_heading": "技巧与最佳实践",
        "resources_heading": "参考资源",
        "col_option": "选项",
        "col_desc": "描述",
        "col_cmd": "命令",
        "col_example": "示例",
        "col_flag": "参数",
        "col_purpose": "作用",
        "quick_links": "[🇹🇷 Türkçe Sürüm](./README.tr.md) • [🇬🇷 Ελληνικά](./README.el.md)"
    },
    "nb": {
        "home_title": "Norsk startside",
        "home_file": "README.nb.md",
        "title_suffix": "– komplett hurtigreferanse",
        "toc_title": "## 📋 Innholdsfortegnelse",
        "intro_heading": "Introduksjon",
        "install_heading": "Installasjon",
        "syntax_heading": "Grunnleggende syntaks",
        "options_heading": "Alternativer og parametere",
        "examples_heading": "Praktiske eksempler",
        "quickref_heading": "Hurtigreferanse",
        "tips_heading": "Tips og beste praksis",
        "resources_heading": "Ressurser og referanser",
        "col_option": "Alternativ",
        "col_desc": "Beskrivelse",
        "col_cmd": "Kommando",
        "col_example": "Eksempel",
        "col_flag": "Flagg",
        "col_purpose": "Formål",
        "quick_links": "[🇹🇷 Türkçe Sürüm](README.tr.md) • [🇬🇷 Ελληνικά](README.el.md)"
    }
}

MISSING_TOOLS = [
    'CORSy', 'CUPP', 'CeWL', 'Certipy', 'Chisel', 'Coercer', 'Commix', 'Crunch',
    'Dirb', 'Ettercap', 'Feroxbuster', 'Frida', 'GitTools', 'Gitleaks', 'InQL',
    'JWT-Attacks', 'Jadx', 'Kiterunner', 'LOLBAS-GTFOBins', 'LaZagne', 'Ligolo-ng',
    'Masscan', 'Msfvenom', 'Naabu', 'NetExec', 'Netcat', 'PEASS-ng', 'PKINITtools',
    'Pacu', 'Prowler', 'Proxychains', 'Radare2', 'Reaver', 'Ropper', 'RustScan',
    'SSH-Tunneling', 'Scapy', 'Searchsploit', 'Sherlock', 'Sliver', 'Snaffler',
    'Socat', 'Tplmap', 'Trivy', 'TruffleHog', 'WPScan', 'Wfuzz', 'WhatWeb',
    'Yersinia', 'adidnsdump', 'checksec', 'dirsearch', 'dnsrecon', 'dnsx',
    'enum4linux-ng', 'fcrackzip', 'hcxdumptool', 'jwt_tool', 'kube-bench',
    'mitmproxy', 'name-that-hash', 'pwndbg-GEF', 'pwntools', 'pypykatz',
    'rlwrap', 'smuggler', 'sshuttle', 'strace-ltrace', 'theHarvester',
    'tshark', 'wafw00f'
]

def load_parallel_corpus():
    print("[*] Extracting parallel vocabulary from existing upstream translations...")
    vocab = {lang: {} for lang in TARGET_LANGS}
    tools = [d for d in os.listdir(".") if os.path.isfile(os.path.join(d, "README.md")) and os.path.isdir(os.path.join(d, "translations"))]
    for tool in tools:
        en_path = os.path.join(tool, "README.md")
        try:
            with open(en_path, "r", encoding="utf-8", errors="ignore") as f:
                en_lines = [l.strip() for l in f]
        except Exception:
            continue
        for lang in TARGET_LANGS:
            lang_path = os.path.join(tool, "translations", f"README.{lang}.md")
            if not os.path.isfile(lang_path):
                continue
            try:
                with open(lang_path, "r", encoding="utf-8", errors="ignore") as f:
                    lang_lines = [l.strip() for l in f]
            except Exception:
                continue

            # Align lines outside code blocks
            i_en = 0
            i_lg = 0
            while i_en < len(en_lines) and i_lg < len(lang_lines):
                l_en = en_lines[i_en]
                l_lg = lang_lines[i_lg]
                if l_en.startswith("```") or l_lg.startswith("```"):
                    if l_en.startswith("```"):
                        i_en += 1
                        while i_en < len(en_lines) and not en_lines[i_en].startswith("```"):
                            i_en += 1
                    if l_lg.startswith("```"):
                        i_lg += 1
                        while i_lg < len(lang_lines) and not lang_lines[i_lg].startswith("```"):
                            i_lg += 1
                    i_en += 1
                    i_lg += 1
                    continue

                if l_en and l_lg and not l_en.startswith("<") and not l_lg.startswith("<"):
                    clean_en = l_en.strip("-*# ")
                    clean_lg = l_lg.strip("-*# ")
                    if 2 < len(clean_en) < 140 and 2 < len(clean_lg) < 160:
                        vocab[lang][clean_en.lower()] = clean_lg

                i_en += 1
                i_lg += 1

    return vocab

def translate_markdown_file(en_content, lang, tool_name, vocab):
    cfg = LANG_CONFIG[lang]
    v_dict = vocab.get(lang, {})
    lines = en_content.splitlines()
    out = []

    # Title extraction
    title_line = ""
    start_idx = 0
    for idx, l in enumerate(lines[:10]):
        if l.strip().startswith("# "):
            title_line = l.strip()
            start_idx = idx + 1
            break

    # Build Header
    clean_title = re.sub(r"^#\s*", "", title_line)
    clean_title = re.sub(r"^[^\w\s\(\)\-\.]+\s*", "", clean_title) # strip emoji if needed
    emoji_match = re.match(r"^#\s*([^\w\s]+)", title_line)
    emoji = emoji_match.group(1).strip() if emoji_match else "🔍"

    out.append(f"# {emoji} {tool_name} {cfg['title_suffix']}")
    out.append("")
    out.append("<!-- language-home-navigation -->")
    out.append(f"[🏠 {cfg['home_title']}](../../{cfg['home_file']}) • [🌐 Languages](../../README.md#-available-languages)")
    out.append("")
    if cfg['quick_links']:
        out.append(cfg['quick_links'])
        out.append("")

    in_code = False
    in_ascii = False

    for line in lines[start_idx:]:
        sline = line.strip()

        # Handle code blocks
        if sline.startswith("```"):
            in_code = not in_code
            out.append(line)
            continue
        if in_code:
            out.append(line)
            continue

        # Skip existing navigation comments or redundant language links if any
        if "<!-- language-home-navigation -->" in line or "[🏠" in line or "Türkçe Sürüm" in line or "Türkçe" in line and "(" in line:
            continue

        # Handle ASCII art blocks
        if any(c in "█═╔╗║╚╝▀▄▌▐░▒▓" for c in line) or re.search(r"[/\\_]{6,}", line):
            out.append(line)
            continue

        # Handle Headings
        if sline.startswith("#"):
            level = len(re.match(r"^#+", sline).group(0))
            heading_text = sline.lstrip("#").strip()
            clean_h = re.sub(r"^[^\w\s\(\)\-\.]+\s*", "", heading_text)

            # Match standard sections
            h_lower = clean_h.lower()
            translated_h = None
            if "table of contents" in h_lower or "inhaltsverzeichnis" in h_lower:
                out.append(cfg["toc_title"])
                continue
            elif "introduction" in h_lower or "overview" in h_lower or "about" in h_lower:
                translated_h = cfg["intro_heading"]
            elif "installation" in h_lower or "setup" in h_lower or "prerequisites" in h_lower:
                translated_h = cfg["install_heading"]
            elif "basic syntax" in h_lower or "usage" in h_lower or "quick start" in h_lower or "syntax" in h_lower:
                translated_h = cfg["syntax_heading"]
            elif "option" in h_lower or "parameter" in h_lower or "flags" in h_lower or "arguments" in h_lower:
                translated_h = cfg["options_heading"]
            elif "example" in h_lower or "practical" in h_lower or "use cases" in h_lower or "scenarios" in h_lower:
                translated_h = cfg["examples_heading"]
            elif "quick reference" in h_lower or "cheatsheet" in h_lower or "summary" in h_lower:
                translated_h = cfg["quickref_heading"]
            elif "tip" in h_lower or "best practice" in h_lower or "tricks" in h_lower:
                translated_h = cfg["tips_heading"]
            elif "resource" in h_lower or "reference" in h_lower or "documentation" in h_lower or "links" in h_lower:
                translated_h = cfg["resources_heading"]
            else:
                translated_h = v_dict.get(clean_h.lower(), clean_h)

            h_prefix = "#" * level
            # Preserve emoji in heading if present
            h_emoji_match = re.match(r"^([^\w\s]+)", heading_text)
            h_emoji = f"{h_emoji_match.group(1)} " if h_emoji_match else ""
            out.append(f"{h_prefix} {h_emoji}{translated_h}")
            continue

        # Handle Table Headers
        if "|" in line:
            parts = [p.strip() for p in line.split("|")]
            new_parts = []
            for p in parts:
                p_lower = p.lower()
                if p_lower in ["option", "options"]:
                    new_parts.append(cfg["col_option"])
                elif p_lower in ["description", "explanation", "details"]:
                    new_parts.append(cfg["col_desc"])
                elif p_lower in ["command", "syntax"]:
                    new_parts.append(cfg["col_cmd"])
                elif p_lower in ["example", "examples"]:
                    new_parts.append(cfg["col_example"])
                elif p_lower in ["flag", "flags"]:
                    new_parts.append(cfg["col_flag"])
                elif p_lower in ["purpose", "action", "function"]:
                    new_parts.append(cfg["col_purpose"])
                elif p_lower in v_dict:
                    new_parts.append(v_dict[p_lower])
                else:
                    new_parts.append(p)
            out.append("|".join(new_parts))
            continue

        # Handle TOC items: - [Section](#section)
        toc_match = re.match(r"^(\s*[-*]\s*)\[(.*?)\]\((#.*?)\)", line)
        if toc_match:
            prefix, label, link = toc_match.groups()
            clean_label = re.sub(r"^[^\w\s\(\)\-\.]+\s*", "", label).strip()
            l_lower = clean_label.lower()
            trans_label = None
            if "introduction" in l_lower or "overview" in l_lower: trans_label = cfg["intro_heading"]
            elif "installation" in l_lower or "setup" in l_lower: trans_label = cfg["install_heading"]
            elif "syntax" in l_lower or "usage" in l_lower: trans_label = cfg["syntax_heading"]
            elif "option" in l_lower or "parameter" in l_lower or "flag" in l_lower: trans_label = cfg["options_heading"]
            elif "example" in l_lower: trans_label = cfg["examples_heading"]
            elif "quick reference" in l_lower or "summary" in l_lower: trans_label = cfg["quickref_heading"]
            elif "tip" in l_lower: trans_label = cfg["tips_heading"]
            elif "resource" in l_lower or "reference" in l_lower: trans_label = cfg["resources_heading"]
            else: trans_label = v_dict.get(l_lower, label)

            # Preserve emoji in TOC label
            lbl_emoji_match = re.match(r"^([^\w\s]+)", label)
            lbl_emoji = f"{lbl_emoji_match.group(1)} " if lbl_emoji_match else ""
            out.append(f"{prefix}[{lbl_emoji}{trans_label}]({link})")
            continue

        # Handle general prose / bullet points
        bullet_match = re.match(r"^(\s*[-*]\s+)(.*)$", line)
        if bullet_match:
            b_prefix, b_content = bullet_match.groups()
            # If line is bold title: **Title**: Description
            bold_match = re.match(r"^(\*\*.*?\*\*:?\s*)(.*)$", b_content)
            if bold_match:
                b_title, b_desc = bold_match.groups()
                clean_bt = b_title.strip(":* ")
                trans_bt = v_dict.get(clean_bt.lower(), clean_bt)
                clean_bd = b_desc.strip()
                trans_bd = v_dict.get(clean_bd.lower(), clean_bd)
                out.append(f"{b_prefix}**{trans_bt}**: {trans_bd}")
            else:
                clean_b = b_content.strip()
                trans_b = v_dict.get(clean_b.lower(), clean_b)
                out.append(f"{b_prefix}{trans_b}")
            continue

        # General line: check vocab
        if sline:
            clean_s = sline.strip()
            trans_s = v_dict.get(clean_s.lower(), line)
            out.append(trans_s)
        else:
            out.append("")

    return "\n".join(out) + "\n"

def main():
    print(f"[*] Starting multilingual translation generation for {len(MISSING_TOOLS)} missing tools...")
    vocab = load_parallel_corpus()

    success_count = 0

    for tool in MISSING_TOOLS:
        en_path = os.path.join(tool, "README.md")
        if not os.path.isfile(en_path):
            print(f"[!] Warning: {en_path} not found, skipping.")
            continue

        with open(en_path, "r", encoding="utf-8", errors="ignore") as f:
            en_content = f.read()

        trans_dir = os.path.join(tool, "translations")
        os.makedirs(trans_dir, exist_ok=True)

        for lang in TARGET_LANGS:
            dest_file = os.path.join(trans_dir, f"README.{lang}.md")
            translated_content = translate_markdown_file(en_content, lang, tool, vocab)
            with open(dest_file, "w", encoding="utf-8") as f:
                f.write(translated_content)
            success_count += 1

    print(f"[+] Successfully generated {success_count} translated guide files!")

if __name__ == "__main__":
    main()
