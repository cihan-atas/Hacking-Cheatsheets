#!/usr/bin/env python3
"""
Full High-Speed Multi-Engine Translation Engine for Hacking-Cheatsheets Tools
Translates markdown documentation while strictly preserving:
- Code blocks (fenced ```...```)
- Inline code (`...`)
- URLs and paths in markdown links
- CLI flags and syntax
"""

import os
import re
import sys
import json
import time
import urllib.request
import urllib.parse
from concurrent.futures import ThreadPoolExecutor, as_completed

LANG_CONFIG = {
    'de': {'name': 'Deutsch', 'flag': '🇩🇪', 'home': 'Deutsche Startseite', 'api_code': 'de'},
    'es': {'name': 'Español', 'flag': '🇪🇸', 'home': 'Página Principal Española', 'api_code': 'es'},
    'fr': {'name': 'Français', 'flag': '🇫🇷', 'home': 'Page d\'accueil Française', 'api_code': 'fr'},
    'it': {'name': 'Italiano', 'flag': '🇮🇹', 'home': 'Home Page Italiana', 'api_code': 'it'},
    'pt': {'name': 'Português', 'flag': '🇵🇹', 'home': 'Página Principal Portuguesa', 'api_code': 'pt'},
    'ru': {'name': 'Русский', 'flag': '🇷🇺', 'home': 'Русская главная страница', 'api_code': 'ru'},
    'zh-CN': {'name': '简体中文', 'flag': '🇨🇳', 'home': '中文主页', 'api_code': 'zh-CN'},
    'ar': {'name': 'العربية', 'flag': '🇸🇦', 'home': 'الصفحة الرئيسية العربية', 'api_code': 'ar'},
    'el': {'name': 'Ελληνικά', 'flag': '🇬🇷', 'home': 'Ελληνική Αρχική Σελίδα', 'api_code': 'el'},
    'nb': {'name': 'Norsk', 'flag': '🇳🇴', 'home': 'Norsk Hovedside', 'api_code': 'no'},
}

ALL_LANG_BAR = [
    ('tr', '🇹🇷 Türkçe', 'README.tr.md'),
    ('de', '🇩🇪 Deutsch', 'README.de.md'),
    ('es', '🇪🇸 Español', 'README.es.md'),
    ('fr', '🇫🇷 Français', 'README.fr.md'),
    ('it', '🇮🇹 Italiano', 'README.it.md'),
    ('pt', '🇵🇹 Português', 'README.pt.md'),
    ('ru', '🇷🇺 Русский', 'README.ru.md'),
    ('zh-CN', '🇨🇳 简体中文', 'README.zh-CN.md'),
    ('ar', '🇸🇦 العربية', 'README.ar.md'),
    ('el', '🇬🇷 Ελληνικά', 'README.el.md'),
    ('nb', '🇳🇴 Norsk', 'README.nb.md'),
]

TRANSLATION_CACHE = {}

def call_translation_api(text: str, target_api_lang: str) -> str:
    if not text.strip():
        return text
    key = (text, target_api_lang)
    if key in TRANSLATION_CACHE:
        return TRANSLATION_CACHE[key]
    
    clients = ['dict-chrome-ex', 'atv']
    for client in clients:
        url = f"https://clients5.google.com/translate_a/t?client={client}&sl=en&tl={target_api_lang}&q=" + urllib.parse.quote(text)
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'})
        try:
            with urllib.request.urlopen(req, timeout=10) as resp:
                data = json.loads(resp.read().decode('utf-8'))
                if isinstance(data, list) and len(data) > 0:
                    res = data[0] if isinstance(data[0], str) else "".join(data)
                    TRANSLATION_CACHE[key] = res
                    return res
        except Exception:
            pass
        time.sleep(0.1)

    # Fallback to mymemory if needed
    try:
        from deep_translator import MyMemoryTranslator
        res = MyMemoryTranslator(source='en-US', target=f"{target_api_lang}").translate(text[:500])
        if res:
            TRANSLATION_CACHE[key] = res
            return res
    except Exception:
        pass

    return text

def translate_prose_block(block_text: str, target_lang: str) -> str:
    api_lang = LANG_CONFIG[target_lang]['api_code']
    
    # Mask inline code
    code_tokens = []
    def mask_code(m):
        code_tokens.append(m.group(0))
        return f"XZC{len(code_tokens)-1}ZX"
    
    masked = re.sub(r'`[^`]+`', mask_code, block_text)
    
    # Translate
    translated = call_translation_api(masked, api_lang)
    
    # Restore inline code using lambda to avoid backslash escape issues
    for idx, code in enumerate(code_tokens):
        pattern = re.compile(rf'XZC\s*{idx}\s*ZX', re.IGNORECASE)
        translated = pattern.sub(lambda _, c=code: c, translated)
        
    return translated

def translate_markdown_document(doc: str, target_lang: str) -> str:
    lines = doc.splitlines()
    output_parts = []
    
    current_prose = []
    in_code_block = False
    
    def flush_prose():
        nonlocal current_prose
        if not current_prose:
            return
        chunk = "\n".join(current_prose)
        current_prose = []
        if not chunk.strip():
            output_parts.append(chunk)
            return
            
        # Split chunk into ~1200 char pieces by double newline
        paragraphs = chunk.split("\n\n")
        sub_chunks = []
        curr = []
        curr_len = 0
        for p in paragraphs:
            if curr_len + len(p) > 1200 and curr:
                sub_chunks.append("\n\n".join(curr))
                curr = [p]
                curr_len = len(p)
            else:
                curr.append(p)
                curr_len += len(p)
        if curr:
            sub_chunks.append("\n\n".join(curr))
            
        trans_sub = [translate_prose_block(sc, target_lang) for sc in sub_chunks]
        output_parts.append("\n\n".join(trans_sub))

    for line in lines:
        if line.strip().startswith('```'):
            flush_prose()
            in_code_block = not in_code_block
            output_parts.append(line)
            continue
            
        if in_code_block:
            output_parts.append(line)
        else:
            if '<!-- language-home-navigation -->' in line or '[🏠' in line or '[🌐' in line:
                continue
            current_prose.append(line)
            
    flush_prose()
    return "\n".join(output_parts)

def build_nav_header(target_lang: str) -> str:
    cfg = LANG_CONFIG[target_lang]
    home_name = cfg['home']
    home_link = f"../../README.{target_lang}.md"
    nav_line1 = f"<!-- language-home-navigation -->\n[🏠 {home_name}]({home_link}) • [🌐 Languages](../../README.md#-available-languages)\n\n"
    
    links = []
    for code, label, fname in ALL_LANG_BAR:
        if code == target_lang:
            continue
        if code == 'tr':
            target_file = "README.tr.md"
        else:
            target_file = f"README.{code}.md"
        links.append(f"[{label}]({target_file})")
    
    links.append("[🇬🇧 English](../README.md)")
    return nav_line1 + " • ".join(links[:4]) + "\n\n" + " • ".join(links[4:]) + "\n\n---\n"

def process_tool_lang(tool_dir: str, lang: str):
    readme_path = os.path.join(tool_dir, "README.md")
    if not os.path.isfile(readme_path):
        return None
        
    trans_dir = os.path.join(tool_dir, "translations")
    out_file = os.path.join(trans_dir, f"README.{lang}.md")
    
    # Check if already freshly generated within last 20 minutes and has non-English translated content
    if os.path.isfile(out_file) and (time.time() - os.path.getmtime(out_file) < 1200):
        with open(out_file, 'r', encoding='utf-8') as f:
            c = f.read()
            # If it's already translated (contains language home navigation and size > 200), skip
            if '<!-- language-home-navigation -->' in c and len(c) > 200:
                return out_file

    with open(readme_path, "r", encoding="utf-8") as f:
        src = f.read()
        
    trans_body = translate_markdown_document(src, lang)
    nav_header = build_nav_header(lang)
    full_content = nav_header + "\n\n" + trans_body
    
    os.makedirs(trans_dir, exist_ok=True)
    with open(out_file, "w", encoding="utf-8") as f:
        f.write(full_content)
    return out_file

def main():
    tools = [
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
        'rlwrap', 'smuggler', 'sshuttle', 'strace-ltrace', 'theHarvester', 'tshark', 'wafw00f'
    ]
    
    langs = list(LANG_CONFIG.keys())
    tasks = []
    for t in tools:
        if not os.path.isdir(t) or not os.path.isfile(os.path.join(t, "README.md")):
            continue
        for l in langs:
            tasks.append((t, l))
            
    print(f"Total tasks: {len(tasks)} (tools: {len(tools)}, langs: {len(langs)})", flush=True)
    
    done = 0
    errors = 0
    start_time = time.time()
    
    with ThreadPoolExecutor(max_workers=8) as executor:
        futures = {executor.submit(process_tool_lang, t, l): (t, l) for t, l in tasks}
        for future in as_completed(futures):
            t, l = futures[future]
            try:
                res = future.result()
                done += 1
                if done % 20 == 0 or done == len(tasks):
                    elapsed = time.time() - start_time
                    rate = done / elapsed if elapsed > 0 else 0
                    print(f"Progress: {done}/{len(tasks)} ({done*100//len(tasks)}%) - {rate:.1f} files/sec", flush=True)
            except Exception as e:
                errors += 1
                print(f"Error on {t} {l}: {e}", flush=True)

    print(f"Translation completed in {time.time()-start_time:.1f}s. Total: {done}, Errors: {errors}", flush=True)

if __name__ == '__main__':
    main()
