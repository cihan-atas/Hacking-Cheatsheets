#!/usr/bin/env python3
"""
Translates all Command Builder options, flags, and labels from CommandBuilderLocalization.kt
into all 10 non-English target languages, preserving CLI flags and syntax pristine.
Saves the resulting multi-language dictionary to android/app/src/main/assets/builder_i18n.json.
"""

import os
import re
import json
import time
import urllib.request
import urllib.parse
from concurrent.futures import ThreadPoolExecutor

LANG_MAP = {
    "de": "de",
    "es": "es",
    "fr": "fr",
    "it": "it",
    "pt": "pt",
    "ru": "ru",
    "zh-CN": "zh-CN",
    "ar": "ar",
    "el": "el",
    "nb": "no"  # Google Translate uses "no" for Norwegian
}

def extract_pairs():
    with open("android/app/src/main/java/com/hackingcheatsheets/app/ui/components/CommandBuilderLocalization.kt", "r", encoding="utf-8") as f:
        content = f.read()
    raw_pairs = re.findall(r"\"([^\"]+)\"\s*to\s*\"([^\"]+)\"", content)
    # Deduplicate while preserving order
    seen = set()
    pairs = []
    for k, v in raw_pairs:
        if k not in seen:
            seen.add(k)
            pairs.append((k, v))
    return pairs

def split_prefix_desc(text):
    # Match CLI flags like "--open ", "-sV ", "/all ", "↳ --version-intensity 9 ", etc.
    m = re.match(r"^((?:↳\s*)?(?:--?|/)[a-zA-Z0-9_\-\.\=\:]+\s*(?:[0-9]+\s*)?)(.*)$", text)
    if m:
        prefix = m.group(1)
        desc = m.group(2).strip()
        if desc:
            return prefix, desc
    return "", text

def translate_batch(batch, target_lang):
    payload = "\n".join(batch)
    url = f"https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=en&tl={target_lang}&q={urllib.parse.quote(payload)}"
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"})
    for attempt in range(4):
        try:
            with urllib.request.urlopen(req, timeout=15) as resp:
                res = json.loads(resp.read().decode("utf-8"))
                if isinstance(res, list) and len(res) > 0:
                    lines = res[0].split("\n")
                    if len(lines) == len(batch):
                        return lines
                    elif len(lines) > 0:
                        # Fallback if line count mismatch
                        return lines + batch[len(lines):]
            return batch
        except Exception as e:
            time.sleep(1 + attempt * 2)
    return batch

def process_language(lang_code, api_lang, items, batch_size=40):
    print(f"[*] Starting translation for {lang_code} (API: {api_lang}), items: {len(items)}...")
    results = {}
    
    # items is a list of (key, prefix, en_desc)
    batches = []
    for i in range(0, len(items), batch_size):
        batches.append(items[i:i + batch_size])
    
    for b_idx, batch in enumerate(batches):
        descs = [b[2] for b in batch]
        translated = translate_batch(descs, api_lang)
        for idx, (key, prefix, _) in enumerate(batch):
            trans_desc = translated[idx] if idx < len(translated) else descs[idx]
            if prefix:
                results[key] = f"{prefix}{trans_desc.strip()}"
            else:
                results[key] = trans_desc.strip()
        time.sleep(0.15)
        if (b_idx + 1) % 10 == 0:
            print(f"    [{lang_code}] Completed batch {b_idx + 1}/{len(batches)}")
            
    print(f"[+] Completed {lang_code}: {len(results)} items")
    return lang_code, results

def main():
    pairs = extract_pairs()
    print(f"Total unique builder pairs to translate: {len(pairs)}")

    items = []
    en_dict = {}
    for k, v in pairs:
        en_dict[k] = v
        prefix, desc = split_prefix_desc(v)
        items.append((k, prefix, desc))

    final_translations = {
        "en": en_dict
    }

    # Parallelize across languages
    with ThreadPoolExecutor(max_workers=5) as executor:
        futures = [
            executor.submit(process_language, lang_code, api_lang, items)
            for lang_code, api_lang in LANG_MAP.items()
        ]
        for f in futures:
            lang_code, lang_results = f.result()
            final_translations[lang_code] = lang_results

    out_dir = "android/app/src/main/assets"
    os.makedirs(out_dir, exist_ok=True)
    out_path = os.path.join(out_dir, "builder_i18n.json")
    with open(out_path, "w", encoding="utf-8") as f:
        json.dump(final_translations, f, ensure_ascii=False, indent=2)

    stat = os.stat(out_path)
    print(f"\n[SUCCESS] Wrote {out_path} ({stat.st_size / 1024:.2f} KB)")
    for lang in final_translations:
        print(f"  - {lang}: {len(final_translations[lang])} keys")

if __name__ == "__main__":
    main()
