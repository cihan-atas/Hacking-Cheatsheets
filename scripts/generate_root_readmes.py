#!/usr/bin/env python3
"""
High-Speed Root README Multi-Language Generator
Synchronizes and translates the canonical README.md into all 10 target languages:
de, es, fr, it, pt, ru, zh-CN, ar, el, nb
ensuring:
- Complete tool listings (all 146+ tools)
- Complete categories and attack kill chain tables
- Android app showcases & download badges
- Upstream author & repository credits
- Accurate relative links to each tool's localized translation file
"""

import os
import re
import sys
import json
import time
import urllib.request
import urllib.parse
from concurrent.futures import ThreadPoolExecutor, as_completed

LANG_META = {
    'de': {'name': 'Deutsch', 'flag': '🇩🇪', 'view': 'Anzeigen', 'api': 'de', 'sub': 'Umfassende Referenzhandbücher für Penetrationstests für Sicherheitsexperten'},
    'es': {'name': 'Español', 'flag': '🇪🇸', 'view': 'Ver', 'api': 'es', 'sub': 'Guías de referencia completas de pruebas de penetración para profesionales de seguridad'},
    'fr': {'name': 'Français', 'flag': '🇫🇷', 'view': 'Voir', 'api': 'fr', 'sub': 'Guides de référence complets sur les tests d\'intrusion pour les professionnels de la sécurité'},
    'it': {'name': 'Italiano', 'flag': '🇮🇹', 'view': 'Visualizza', 'api': 'it', 'sub': 'Guide di riferimento complete sui test di penetrazione per professionisti della sicurezza'},
    'pt': {'name': 'Português', 'flag': '🇵🇹', 'view': 'Ver', 'api': 'pt', 'sub': 'Guias de referência abrangentes de testes de penetração para profissionais de segurança'},
    'ru': {'name': 'Русский', 'flag': '🇷🇺', 'view': 'Посмотреть', 'api': 'ru', 'sub': 'Комплексные справочные руководства по тестированию на проникновение для специалистов по безопасности'},
    'zh-CN': {'name': '简体中文', 'flag': '🇨🇳', 'view': '查看', 'api': 'zh-CN', 'sub': '面向网络安全从业者与渗透测试人员的完整实战速查备忘单'},
    'ar': {'name': 'العربية', 'flag': '🇸🇦', 'view': 'عرض', 'api': 'ar', 'sub': 'أدلة مرجعية شاملة لاختبار الاختراق لمتخصصي الأمن السيبراني'},
    'el': {'name': 'Ελληνικά', 'flag': '🇬🇷', 'view': 'Προβολή', 'api': 'el', 'sub': 'Ολοκληρωμένοι οδηγοί αναφοράς δοκιμών διείσδυσης για επαγγελματίες ασφαλείας'},
    'nb': {'name': 'Norsk', 'flag': '🇳🇴', 'view': 'Vis', 'api': 'no', 'sub': 'Omfattende referanseveiledninger for penetrasjonstesting for sikkerhetseksperter'},
}

GLOBAL_CACHE = {}

def translate_phrase(text: str, api_lang: str) -> str:
    cleaned = text.strip()
    if not cleaned or len(cleaned) < 2:
        return text
    key = (cleaned, api_lang)
    if key in GLOBAL_CACHE:
        return GLOBAL_CACHE[key]
        
    url = f"https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=en&tl={api_lang}&q=" + urllib.parse.quote(cleaned)
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'})
    for attempt in range(3):
        try:
            with urllib.request.urlopen(req, timeout=8) as r:
                res = json.loads(r.read().decode('utf-8'))
                val = res[0] if isinstance(res, list) and res else cleaned
                GLOBAL_CACHE[key] = val
                return val
        except Exception:
            time.sleep(0.1)
    return cleaned

def build_root_readme(target_lang: str):
    with open("README.md", "r", encoding="utf-8") as f:
        src = f.read()

    meta = LANG_META[target_lang]
    api_lang = meta['api']
    view_text = meta['view']

    # 1. Transform link targets to localized versions if present
    def link_replacer(match):
        link_path = match.group(2).strip()
        rel = link_path[2:] if link_path.startswith("./") else link_path
        parts = rel.split("/")
        if len(parts) == 2 and parts[1].endswith(".md"):
            folder, fname = parts
            base = fname[:-3]
            cand1 = os.path.join(folder, "translations", f"{base}.{target_lang}.md")
            cand2 = os.path.join(folder, f"{base}.{target_lang}.md")
            if os.path.isfile(cand1):
                return f"[📄 {view_text}](./{folder}/translations/{base}.{target_lang}.md)"
            elif os.path.isfile(cand2):
                return f"[📄 {view_text}](./{folder}/{base}.{target_lang}.md)"
        return f"[📄 {view_text}]({link_path})"

    updated = re.sub(r'(\[📄\s*View\]\()([^\)]+)(\))', link_replacer, src)

    # 2. Collect all unique descriptions in table rows
    row_pattern = re.compile(r'^(\|\s*\*\*[^*]+\*\*\s*\|)([^|]+)(\|\s*\[📄[^|]+\|\s*)$', re.MULTILINE)
    descriptions = set()
    for m in row_pattern.finditer(updated):
        desc = m.group(2).strip()
        if desc:
            descriptions.add(desc)

    # Collect overview headings e.g. Full Cloud Security Overview
    overview_pattern = re.compile(r'\[(Full [^\]]+ Overview)\]\(([^\)]+)\)')
    overviews = set()
    for m in overview_pattern.finditer(updated):
        overviews.add(m.group(1).strip())

    # Pre-translate all descriptions & overviews concurrently
    all_phrases = list(descriptions | overviews)
    with ThreadPoolExecutor(max_workers=10) as ex:
        futures = {ex.submit(translate_phrase, p, api_lang): p for p in all_phrases}
        for fut in as_completed(futures):
            fut.result()

    # 3. Replace in document
    def row_sub(m):
        prefix = m.group(1)
        desc = m.group(2).strip()
        suffix = m.group(3)
        trans_desc = GLOBAL_CACHE.get((desc, api_lang), desc)
        return f"{prefix} {trans_desc} {suffix}"

    updated = row_pattern.sub(row_sub, updated)

    def overview_sub(m):
        label = m.group(1).strip()
        path = m.group(2).strip()
        rel = path[2:] if path.startswith("./") else path
        parts = rel.split("/")
        if len(parts) == 2 and parts[1].endswith(".md"):
            folder, fname = parts
            base = fname[:-3]
            cand = os.path.join(folder, "translations", f"{base}.{target_lang}.md")
            if os.path.isfile(cand):
                path = f"./{folder}/translations/{base}.{target_lang}.md"
        trans_label = GLOBAL_CACHE.get((label, api_lang), label)
        return f"[{trans_label}]({path})"

    updated = overview_pattern.sub(overview_sub, updated)

    # 4. Update top subtitle and note
    sub_en = "📚 Comprehensive penetration testing reference guides for security professionals"
    updated = updated.replace(sub_en, f"📚 {meta['sub']}")

    out_file = f"README.{target_lang}.md"
    with open(out_file, "w", encoding="utf-8") as f:
        f.write(updated)
    return out_file

def main():
    print("Generating all 10 localized root READMEs concurrently...", flush=True)
    t0 = time.time()
    with ThreadPoolExecutor(max_workers=5) as ex:
        futures = {ex.submit(build_root_readme, l): l for l in LANG_META.keys()}
        for fut in as_completed(futures):
            l = futures[fut]
            try:
                res = fut.result()
                size = os.path.getsize(res)
                print(f"[{l}] Generated {res} ({size:,} bytes)", flush=True)
            except Exception as e:
                print(f"[{l}] Error: {e}", flush=True)

    print(f"All root README files generated in {time.time()-t0:.1f}s!", flush=True)

if __name__ == '__main__':
    main()
