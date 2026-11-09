#!/usr/bin/env python3
"""
Extracts the 56 UI strings from UtilitiesScreen.kt and translates them into all 10 languages,
then appends them into android/app/src/main/assets/builder_i18n.json.
"""

import re
import json
import time
import urllib.request
import urllib.parse

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
    "nb": "no"
}

def translate_batch(batch, target_lang):
    payload = "\n".join(batch)
    url = f"https://clients5.google.com/translate_a/t?client=dict-chrome-ex&sl=en&tl={target_lang}&q={urllib.parse.quote(payload)}"
    req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
    for attempt in range(3):
        try:
            with urllib.request.urlopen(req, timeout=10) as resp:
                res = json.loads(resp.read().decode("utf-8"))
                if isinstance(res, list) and len(res) > 0:
                    lines = res[0].split("\n")
                    if len(lines) == len(batch):
                        return lines
                    return lines + batch[len(lines):]
            return batch
        except Exception:
            time.sleep(1)
    return batch

def main():
    with open("android/app/src/main/java/com/hackingcheatsheets/app/ui/screens/UtilitiesScreen.kt", "r", encoding="utf-8") as f:
        text = f.read()

    matches = re.findall(r"if\s*\(language\s*==\s*AppLanguage\.TR\)\s*\"([^\"]+)\"\s*else\s*\"([^\"]+)\"", text)
    print(f"Found {len(matches)} pairs in UtilitiesScreen.kt")

    # Load existing builder_i18n.json
    json_path = "android/app/src/main/assets/builder_i18n.json"
    with open(json_path, "r", encoding="utf-8") as f:
        db = json.load(f)

    # English additions
    for tr_str, en_str in matches:
        db["en"][tr_str] = en_str

    en_list = [en_str for tr_str, en_str in matches]

    for lang_code, api_lang in LANG_MAP.items():
        print(f"Translating {len(en_list)} strings to {lang_code}...")
        translated = translate_batch(en_list, api_lang)
        for idx, (tr_str, _) in enumerate(matches):
            t_val = translated[idx] if idx < len(translated) else en_list[idx]
            db[lang_code][tr_str] = t_val.strip()
        time.sleep(0.2)

    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(db, f, ensure_ascii=False, indent=2)

    print(f"[SUCCESS] Updated {json_path} with UtilitiesScreen strings.")

if __name__ == "__main__":
    main()
